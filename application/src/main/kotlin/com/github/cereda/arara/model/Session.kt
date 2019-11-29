/*
 * Arara, the cool TeX automation tool
 * Copyright (c) 2012 -- 2019, Paulo Roberto Massa Cereda
 * All rights reserved.
 *
 * Redistribution and  use in source  and binary forms, with  or without
 * modification, are  permitted provided  that the  following conditions
 * are met:
 *
 * 1. Redistributions  of source  code must  retain the  above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form  must reproduce the above copyright
 * notice, this list  of conditions and the following  disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither  the name  of the  project's author nor  the names  of its
 * contributors may be used to  endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE COPYRIGHT  HOLDERS AND CONTRIBUTORS
 * "AS IS"  AND ANY  EXPRESS OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED  TO, THE  IMPLIED WARRANTIES  OF MERCHANTABILITY  AND FITNESS
 * FOR  A PARTICULAR  PURPOSE  ARE  DISCLAIMED. IN  NO  EVENT SHALL  THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE  LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY,  OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT  NOT LIMITED  TO, PROCUREMENT  OF SUBSTITUTE  GOODS OR  SERVICES;
 * LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION)  HOWEVER
 * CAUSED AND  ON ANY THEORY  OF LIABILITY, WHETHER IN  CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY  OUT  OF  THE USE  OF  THIS  SOFTWARE,  EVEN  IF ADVISED  OF  THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.cereda.arara.model

import com.github.cereda.arara.localization.LanguageController
import com.github.cereda.arara.localization.Messages

/**
 * Implements the session.
 *
 * This class wraps a map that holds the execution session, that is, a dirty
 * maneuver to exchange pretty much any data between commands and even rules.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 4.0
 * @since 4.0
 */
object Session {
    // the application messages obtained from the
    // language controller
    private val messages = LanguageController

    // the session map which holds the execution session;
    // the idea here is to provide wrappers to the map
    // methods, so it could be easily manipulated
    private val map = mutableMapOf<String, Any>()

    /**
     * Gets the object indexed by the provided key from the session. This method
     * holds the map method of the very same name.
     *
     * @param key The provided key.
     * @return The object indexed by the provided key.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @Throws(AraraException::class)
    operator fun get(key: String): Any {
        return if (contains(key)) {
            map.getValue(key)
        } else {
            throw AraraException(
                    messages.getMessage(
                            Messages.ERROR_SESSION_OBTAIN_UNKNOWN_KEY,
                            key
                    )
            )
        }
    }

    /**
     * Inserts (or overwrites) the object indexed by the provided key into the
     * session. This method holds the map method of the very same name.
     *
     * @param key The provided key.
     * @param value The value to be inserted.
     */
    fun put(key: String, value: Any) {
        map[key] = value
    }

    /**
     * Removes the entry indexed by the provided key from the session. This method
     * holds the map method of the same name.
     *
     * @param key The provided key.
     * @throws AraraException Something wrong happened, to be caught in the
     * higher levels.
     */
    @Throws(AraraException::class)
    fun remove(key: String) {
        if (contains(key)) {
            map.remove(key)
        } else {
            throw AraraException(
                    messages.getMessage(
                            Messages.ERROR_SESSION_REMOVE_UNKNOWN_KEY,
                            key
                    )
            )
        }
    }

    /**
     * Checks if the provided key exists in the session.
     *
     * @param key The provided key.
     * @return A boolean value indicating if the provided key exists in the
     * session.
     */
    operator fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    /**
     * Clears the session (map). This method, as usual, holds the map method of
     * the same name.
     */
    fun clear() {
        map.clear()
    }
}
