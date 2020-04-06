// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.cli.model

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import org.islandoftex.arara.api.AraraException
import org.islandoftex.arara.api.rules.Directive
import org.islandoftex.arara.cli.localization.LanguageController
import org.islandoftex.arara.cli.localization.Messages
import org.islandoftex.arara.cli.ruleset.DirectiveUtils
import org.islandoftex.arara.cli.utils.CommonUtils

/**
 * Extractor for directives from the provided main file.
 *
 * @author Island of TeX
 * @version 5.0
 * @since 4.0
 */
object Extractor {
    // the application messages obtained from the
    // language controller
    private val messages = LanguageController

    /**
     * Extracts a list of directives from the provided main file, obtained from
     * the configuration controller.
     * @param file The file to extract the directives from.
     * @param charset The charset of the file.
     * @return A list of directives.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @Throws(AraraException::class)
    fun extract(file: File, charset: Charset = Charsets.UTF_8):
            List<Directive> {
        try {
            val content = CommonUtils.preambleContent.toMutableList()
            content.addAll(file.readLines(charset))
            return DirectiveUtils.extractDirectives(content)
        } catch (ioexception: IOException) {
            throw AraraException(
                messages.getMessage(
                    Messages.ERROR_EXTRACTOR_IO_ERROR
                ),
                ioexception
            )
        }
    }
}