// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.api.files

import java.nio.file.Path

/**
 * The database model represents arara's way to track file changes. arara
 * tracks changes within a project.
 */
public interface Database {
    /**
     * Check whether the database contains a file.
     *
     * @param path The path to a file. It should be implemented in a way that
     *   non-canonical paths are dealt with.
     * @return Whether the object represented by the path is in the database.
     */
    public operator fun contains(path: Path): Boolean

    /**
     * Get the hash value associated with a file.
     *
     * @param path The path to a file. Should use the same mechanics as
     *   [contains].
     * @return The hash value associated with the file if any and `null` if
     *   the element is not in the database.
     */
    public operator fun get(path: Path): Long?

    /**
     * Set the hash value for a given file.
     *
     * @param path The path of a file. Try to resolve it to an absolute path.
     * @param hash The hash value of the file.
     */
    public operator fun set(path: Path, hash: Long)

    /**
     * Remove the entry associated with the key (file).
     *
     * Implementation may throw [NoSuchElementException] exception if the
     * [path] is not contained within the database.
     *
     * @param path The file acting as key in the database.
     */
    public fun remove(path: Path)

    /**
     * Save the database.
     *
     * @param path Where to save it.
     */
    public fun save(path: Path)
}
