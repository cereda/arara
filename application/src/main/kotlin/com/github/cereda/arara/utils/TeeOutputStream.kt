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
package com.github.cereda.arara.utils

import java.io.IOException
import java.io.OutputStream

/**
 * Implements a stream splitter.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 4.0
 * @since 4.0
 */
class TeeOutputStream
/**
 * Constructor.
 *
 * @param outputStreams An array of output streams.
 */
(vararg outputStreams: OutputStream) : OutputStream() {

    // an array of streams in which
    // an object of this class will
    // split data
    private val streams: List<OutputStream> = outputStreams.toList()

    /**
     * Writes the provided integer to each stream.
     *
     * @param b The provided integer
     * @throws IOException An IO exception.
     */
    @Throws(IOException::class)
    override fun write(b: Int) {
        for (ostream in streams) {
            ostream.write(b)
        }
    }

    /**
     * Writes the provided byte array to each stream, with the provided offset
     * and length.
     *
     * @param b      The byte array.
     * @param offset The offset.
     * @param length The length.
     * @throws IOException An IO exception.
     */
    @Throws(IOException::class)
    override fun write(b: ByteArray, offset: Int, length: Int) {
        for (ostream in streams) {
            ostream.write(b, offset, length)
        }
    }

    /**
     * Flushes every stream.
     *
     * @throws IOException An IO exception.
     */
    @Throws(IOException::class)
    override fun flush() {
        for (ostream in streams) {
            ostream.flush()
        }
    }

    /**
     * Closes every stream silently.
     */
    override fun close() {
        for (ostream in streams) {
            try {
                ostream.close()
            } catch (ignored: IOException) {
                // do nothing on purpose
            }

        }
    }

}
