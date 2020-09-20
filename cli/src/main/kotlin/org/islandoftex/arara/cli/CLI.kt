// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.clikt.parameters.types.restrictTo
import java.nio.file.Paths
import java.time.LocalDate
import java.util.Locale
import kotlin.time.TimeSource
import kotlin.time.milliseconds
import org.islandoftex.arara.api.AraraAPI
import org.islandoftex.arara.api.AraraException
import org.islandoftex.arara.api.configuration.ExecutionMode
import org.islandoftex.arara.api.session.ExecutionStatus
import org.islandoftex.arara.cli.configuration.ConfigurationUtils
import org.islandoftex.arara.cli.ruleset.DirectiveUtils
import org.islandoftex.arara.cli.utils.DisplayUtils
import org.islandoftex.arara.cli.utils.LoggingUtils
import org.islandoftex.arara.core.configuration.ExecutionOptions
import org.islandoftex.arara.core.configuration.LoggingOptions
import org.islandoftex.arara.core.configuration.UserInterfaceOptions
import org.islandoftex.arara.core.files.FileHandling
import org.islandoftex.arara.core.files.FileSearching
import org.islandoftex.arara.core.files.Project
import org.islandoftex.arara.core.localization.LanguageController
import org.islandoftex.arara.core.rules.Directives
import org.islandoftex.arara.core.session.ExecutorHooks
import org.islandoftex.arara.core.session.LinearExecutor
import org.islandoftex.arara.core.session.Session
import org.islandoftex.arara.mvel.utils.MvelState

/**
 * arara's command line interface
 *
 * @author Island of TeX
 * @version 5.0
 * @since 5.0
 */
class CLI : CliktCommand(name = "arara", printHelpOnEmptyArgs = true) {
    private val log by option("-l", "--log",
            help = "Generate a log output")
            .flag()
    private val verbose by option("-v", "--verbose",
            help = "Print the command output")
            .flag("-s", "--silent")
    private val dryRun by option("-n", "--dry-run",
            help = "Go through all the motions of running a command, but " +
                    "with no actual calls")
            .flag()
    private val safeRun by option("-s", "--safe-run",
            help = "Run in safe mode and disable potentially harmful features." +
                    "Make sure your projects uses only allowed features.")
            .flag()
    private val onlyHeader by option("-H", "--header",
            help = "Extract directives only in the file header")
            .flag()
    private val preamble by option("-p", "--preamble",
            help = "Set the file preamble based on the configuration file")
    private val timeout by option("-t", "--timeout",
            help = "Set the execution timeout (in milliseconds)")
            .int().restrictTo(min = 1)
    private val language by option("-L", "--language",
            help = "Set the application language")
    private val maxLoops by option("-m", "--max-loops",
            help = "Set the maximum number of loops (> 0)")
            .int().restrictTo(min = 1)
    private val workingDirectory by option("-d", "--working-directory",
            help = "Set the working directory for all tools")
            .path(mustExist = true, canBeFile = false, mustBeReadable = true)
    private val parameters: Map<String, String> by option("-P", "--call-property",
            help = "Pass parameters to the application to be used within the " +
                    "session.")
            .associate()

    private val reference by argument("file",
            help = "The file(s) to evaluate and process")
            .multiple(required = true)

    /**
     * Update arara's configuration with the command line arguments.
     */
    private fun updateConfigurationFromCommandLine() {
        Session.userInterfaceOptions = UserInterfaceOptions(
                locale = language?.let { Locale.forLanguageTag(it) }
                        ?: Session.userInterfaceOptions.locale,
                swingLookAndFeel = Session.userInterfaceOptions.swingLookAndFeel
        )
        LanguageController.setLocale(Session.userInterfaceOptions.locale)

        LinearExecutor.executionOptions = ExecutionOptions
                .from(LinearExecutor.executionOptions)
                .copy(
                        maxLoops = maxLoops
                                ?: LinearExecutor.executionOptions.maxLoops,
                        timeoutValue = timeout?.milliseconds
                                ?: LinearExecutor.executionOptions.timeoutValue,
                        verbose = if (verbose)
                            true
                        else
                            LinearExecutor.executionOptions.verbose,
                        executionMode = when {
                            dryRun -> ExecutionMode.DRY_RUN
                            safeRun -> ExecutionMode.SAFE_RUN
                            else -> LinearExecutor.executionOptions.executionMode
                        },
                        parseOnlyHeader = if (onlyHeader)
                            true
                        else
                            LinearExecutor.executionOptions.parseOnlyHeader
                )

        Session.loggingOptions = LoggingOptions(
                enableLogging = if (log)
                    true
                else
                    Session.loggingOptions.enableLogging,
                appendLog = Session.loggingOptions.appendLog,
                logFile = Session.loggingOptions.logFile
        )
    }

    /**
     * The actual main method of arara (when run in command-line mode)
     */
    override fun run() {
        // initializing logging has to come first; init() actually disables
        // the logging, so early exceptions won't generate a lot of noise in
        // the terminal
        LoggingUtils.init()

        // start the internal stopwatch before any of arara's real working
        // starts
        val executionStart = TimeSource.Monotonic.markNow()

        // logging has to be initialized only once and for all because
        // context resets lead to missing output
        LoggingUtils.setupLogging(LoggingOptions(log))

        // resolve the working directory from the one that may be given
        // as command line parameter; otherwise resolve current directory
        val workingDir = FileHandling.normalize(workingDirectory ?: Paths.get(""))

        // add all command line call parameters to the session
        parameters.forEach { (key, value) -> Session.put("arg:$key", value) }

        try {
            val projects = listOf(Project(
                    workingDir.fileName.toString(),
                    workingDir,
                    reference.map { fileName ->
                        FileSearching.resolveFile(
                                fileName,
                                workingDir,
                                LinearExecutor.executionOptions
                        )
                    }.toSet()
            ))
            try {
                LinearExecutor.hooks = ExecutorHooks(
                        executeBeforeExecution = {
                            // directive processing has to be initialized, so that the core
                            // component respects our MVEL processing
                            DirectiveUtils.initializeDirectiveCore()
                        },
                        executeBeforeProject = { project ->
                            ConfigurationUtils.configFileForProject(project)?.let {
                                DisplayUtils.configurationFileName = it.toString()
                                ConfigurationUtils.load(it)
                            }
                        },
                        executeBeforeFile = {
                            // TODO: do we have to reset some more file-specific config?
                            updateConfigurationFromCommandLine()
                            DisplayUtils.printFileInformation(it)
                        },
                        executeAfterFile = {
                            // add an empty line between file executions
                            println()
                        },
                        processDirectives = { directives ->
                            // if we have a preamble, prepend it,
                            // otherwise use the preprocessed directives
                            // TODO: shall we throw AraraException(
                            //  LanguageController.messages.ERROR_PARSER_INVALID_PREAMBLE.format(it))
                            val allDirectives = preamble
                                    ?.takeIf { MvelState.preambles.containsKey(it) }
                                    ?.let {
                                        Directives.extractDirectives(
                                                MvelState.preambles[preamble]!!
                                                        .lines()
                                                        .filterNot { it.isEmpty() },
                                                true,
                                                LinearExecutor.currentFile!!.fileType
                                        ).plus(directives)
                                    } ?: directives
                            if (allDirectives.isEmpty())
                                throw AraraException(LanguageController
                                        .messages.ERROR_VALIDATE_NO_DIRECTIVES_FOUND)
                            DirectiveUtils.process(allDirectives)
                        }
                )
                LinearExecutor.executionStatus = if (LinearExecutor.execute(projects).exitCode != 0)
                    ExecutionStatus.EXTERNAL_CALL_FAILED
                else
                    ExecutionStatus.PROCESSING
            } catch (exception: AraraException) {
                // catch a propagated exception to replace intentionally left
                // out local treatment
                DisplayUtils.printException(exception)
            }

            // print the execution time if the command line parsing does not
            // return false as result (it makes no sense to print the execution
            // time for a help message)
            DisplayUtils.printTime(executionStart.elapsedNow().inSeconds)
        } catch (ex: AraraException) {
            DisplayUtils.printException(ex)
            LinearExecutor.executionStatus = ExecutionStatus.CAUGHT_EXCEPTION
        }

        throw ProgramResult(LinearExecutor.executionStatus.exitCode)
    }
}

/**
 * Main method. This is the application entry point.
 * @param args A string array containing all command line arguments.
 */
fun main(args: Array<String>) {
    // print the arara logo in the terminal; I just
    // hope people use this tool in a good terminal with
    // fixed-width fonts, otherwise the logo will be messed
    DisplayUtils.printLogo()

    CLI().versionOption(AraraAPI.version, names = setOf("-V", "--version"),
            message = {
                "arara ${AraraAPI.version}\n" +
                        "Copyright (c) ${LocalDate.now().year}, Island of TeX\n" +
                        LanguageController.messages.INFO_PARSER_NOTES + "\n\n" +
                        "New features in version ${AraraAPI.version}:\n" +
                        CLI::class.java
                                .getResource("/org/islandoftex/arara/cli/configuration/release-notes")
                                .readText()
            })
            .main(args)
}
