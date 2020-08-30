// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.mvel.utils

import java.io.File
import java.nio.file.Files
import org.islandoftex.arara.api.AraraException
import org.islandoftex.arara.cli.Arara
import org.islandoftex.arara.core.files.FileHandling
import org.islandoftex.arara.core.session.Executor
import org.islandoftex.arara.core.ui.GUIDialogs

@Suppress("unused", "TooManyFunctions")
object KtConditionalMethods {
    // the GUI generator
    private val dialogs = GUIDialogs(Arara.userInterfaceOptions)

    /**
     * Checks if the file exists according to its extension.
     *
     * @param extension The extension.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun exists(extension: String): Boolean = Files.exists(FileHandling.changeExtension(
            Arara.currentFile.path, extension))

    /**
     * Checks if the file is missing according to its extension.
     *
     * @param extension The extension.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun missing(extension: String): Boolean = !exists(extension)

    /**
     * Checks if the file has changed, according to its extension.
     *
     * @param extension The extension.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun changed(extension: String): Boolean = changed(FileHandling.changeExtension(
            Arara.currentFile.path, extension).toFile())

    /**
     * Checks if the file is unchanged according to its extension.
     *
     * @param extension The extension.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun unchanged(extension: String): Boolean = !changed(extension)

    /**
     * Checks if the file exists.
     *
     * @param filename The file.
     * @return A boolean value.
     */
    @JvmStatic
    fun exists(filename: File): Boolean = filename.exists()

    /**
     * Checks if the file is missing.
     *
     * @param filename The file.
     * @return A boolean value.
     */
    @JvmStatic
    fun missing(filename: File): Boolean = !exists(filename)

    /**
     * Checks if the file has changed.
     *
     * @param filename The file.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun changed(filename: File): Boolean = FileHandling.hasChanged(
            filename.toPath(),
            Arara.currentProject.workingDirectory.resolve(
                    Executor.executionOptions.databaseName
            )
    )

    /**
     * Checks if the file is unchanged.
     *
     * @param filename The file.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun unchanged(filename: File): Boolean = !changed(filename)

    /**
     * Checks if the file contains the regex, based on its extension.
     *
     * @param extension The extension.
     * @param regex The regex.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun found(extension: String, regex: String): Boolean = found(
            FileHandling.changeExtension(Arara.currentFile.path, extension)
                    .toFile(), regex)

    /**
     * Checks if the file contains the provided regex.
     *
     * @param file The file.
     * @param regex The regex.
     * @return A boolean value.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @JvmStatic
    @Throws(AraraException::class)
    fun found(file: File, regex: String): Boolean = MethodUtils.checkRegex(file, regex)

    /**
     * Returns a file object based on the provided name.
     *
     * @param name The file name.
     * @return A file object.
     */
    @JvmStatic
    fun toFile(name: String): File = File(name)

    /**
     * Shows a message with options presented as an array of buttons.
     *
     * @param width Integer value, in pixels.
     * @param type Type of message.
     * @param title Title of the message.
     * @param text Text of the message.
     * @param buttons An array of objects to be presented as buttons.
     * @return The index of the selected button, starting from 1.
     */
    @JvmStatic
    @JvmOverloads
    fun showOptions(
        width: Int = dialogs.defaultWidth,
        type: Int,
        title: String,
        text: String,
        vararg buttons: Any
    ): Int = dialogs.showOptions(width, type, title, text, buttons)

    /**
     * Shows a message with a text input.
     *
     * @param width Integer value, in pixels.
     * @param type Type of message.
     * @param title Title of the message.
     * @param text Text of the message.
     * @return The string representing the input text.
     */
    @JvmStatic
    @JvmOverloads
    fun showInput(
        width: Int = dialogs.defaultWidth,
        type: Int,
        title: String,
        text: String
    ): String = dialogs.showInput(width, type, title, text)

    /**
     * Shows a message with options presented as a dropdown list of elements.
     *
     * @param width Integer value, in pixels.
     * @param type Type of message.
     * @param title Title of the message.
     * @param text Text of the message.
     * @param elements An array of objects representing the elements.
     * @return The index of the selected element, starting from 1.
     */
    @JvmStatic
    @JvmOverloads
    fun showDropdown(
        width: Int = dialogs.defaultWidth,
        type: Int,
        title: String,
        text: String,
        vararg elements: Any
    ): Int = dialogs.showDropdown(width, type, title, text, elements)

    /**
     * Gets the file reference for the current directive. It is important to
     * observe that version 4.0 of arara replicates the directive when 'files'
     * is detected amongst the parameters, so each instance will have a
     * different reference.
     *
     * @return A file reference for the current directive.
     */
    @JvmStatic
    fun currentFile(): File = Arara.currentFile.path.toFile()
}
