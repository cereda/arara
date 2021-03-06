// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.api.files

/**
 * Implements the file type model.
 */
public interface FileType {
    /**
     * This identifies the file's extension without dot.
     */
    public val extension: String

    /**
     * This identifies the search pattern for arara's directive comments.
     */
    public val pattern: String

    public companion object {
        /**
         * This constant identifies an invalid extension. As unices do not
         * allow a forward and Windows does not allow a backward slash, this
         * should suffice.
         */
        public const val INVALID_EXTENSION: String = "/\\"

        /**
         * This constant identifies an invalid pattern. This is an opening
         * character class which is invalid.
         */
        public const val INVALID_PATTERN: String = ""
    }

    /**
     * This indicates whether an instance of an implementation is invalid.
     */
    public val isUnknown: Boolean
        get() = extension == INVALID_EXTENSION || pattern == INVALID_PATTERN
}
