// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.ruleset

import kotlinx.serialization.Serializable

/**
 * Implements the rule command model.
 *
 * @author Island of TeX
 * @version 5.0
 * @since 4.0
 */
@Serializable
class RuleCommand {
    /**
     * The command name
     */
    var name: String? = null

    /**
     * The command instruction
     */
    // TODO: why is trimming needed?
    var command: String? = null
        get() = field?.trim()

    /**
     * The exit status expression
     */
    var exit: String? = null
}
