// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.dsl.session

import org.islandoftex.arara.api.files.MPPPath
import org.islandoftex.arara.api.session.Command

data class Command(
    override val elements: List<String>,
    override val workingDirectory: MPPPath?
) : Command
