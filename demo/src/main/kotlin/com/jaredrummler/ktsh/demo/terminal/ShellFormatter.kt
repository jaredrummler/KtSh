/*
 * Copyright (C) 2021 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.ktsh.demo.terminal

import com.jaredrummler.ktsh.demo.terminal.ShellFormatter.IndentStyle
import java.util.regex.Pattern

/**
 * A simple formatter to indent shell scripts line by line based on keywords.
 *
 * @param style Determines how to indent each line.
 *   Either [IndentStyle.TAB] or [IndentStyle.Spaces]. Defaults to spaces, obviously.
 * @author Jared Rummler
 */
class ShellFormatter(
    private val style: IndentStyle = IndentStyle.Spaces()
) {

    /**
     * Format the code
     *
     * @param input The code to format
     * @return The formatted input
     */
    fun format(input: String) = buildString {
        var numIndents = 0
        var addNewLine = false
        for (line in input.lines()) {
            numIndents -= line.countMatches(IndentPatterns.end)
            append(CONTROL_J) onlyIf@{ addNewLine }
            append(style.indent, numIndents)
            append(line.trimStart())
            numIndents += line.countMatches(IndentPatterns.add)
            addNewLine = true
        }
    }

    private fun String.countMatches(pattern: Pattern): Int = pattern.matcher(this).let { m ->
        var count = 0
        while (m.find()) count++
        return count
    }

    private fun StringBuilder.append(c: Char, condition: () -> Boolean) = apply {
        if (condition()) append(c)
    }

    private fun StringBuilder.append(str: String, times: Int) = apply {
        (0 until times).onEach { append(str) }
    }

    sealed class IndentStyle(val indent: String) {

        object /*Evil*/ Tab : IndentStyle(TAB)

        class Spaces(size: Int = DEFAULT_SPACES) : IndentStyle(spaces(size))

        override fun toString(): String = indent

        companion object {
            const val TAB = "\t"
            const val DEFAULT_SPACES = 4
            private fun spaces(size: Int) = String(size.downTo(1).map { ' ' }.toCharArray())
        }
    }

    companion object {
        private const val END_INDENT_REGEX = "\\b(elif|else|fi|done|esac)|^\\}"
        private const val ADD_INDENT_REGEX = "\\b(do|then|else)\\b|\\s\\{|\\bcase\\b\\s*in\\s*\\S+"

        object IndentPatterns {
            val add = ADD_INDENT_REGEX.toPattern()
            val end = END_INDENT_REGEX.toPattern()
        }

        private val CONTROL_J = Char(0x0A)
    }
}
