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

enum class ShellConstruct(
    val open: Keyword,
    val close: Keyword,
    val prompt: String = open
) {
    Backslash("\\", "", ""),
    Curly("{", "}", "") {
        override val begin: Regex = "\\{(\\s+)?\$".toRegex()
        override val end: Regex = "\\{".toRegex()
    },
    Select("select", "done"),
    While("while", "done"),
    Until("until", "done"),
    Case("case", "esac"),
    For("for", "done"),
    Else("else", "fi"),
    Elif("elif", "fi"),
    If("if", "fi");

    open val begin: Regex by lazy { "\\b$open\\b".toRegex() }

    open val end: Regex by lazy { "\\b$close\\b".toRegex() }

    companion object {

        fun constructs(input: String): List<ShellConstruct> {
            val result = mutableListOf<ShellConstruct>()
            for (line in input.lines()) {
                if (result.lastOrNull() == Backslash) {
                    // Remove the last backlash from the last processed line.
                    result.removeLast()
                }

                if (line.trimEnd().endsWith(Backslash.open)) {
                    result.add(Backslash)
                }

                var statement = line
                for (construct in values()) {
                    // remove any closed constructs from the result
                    val iterator = result.asReversed().iterator()
                    iterator.forEachRemaining { shellConstruct ->
                        when (shellConstruct) {
                            Backslash -> return@forEachRemaining
                            else -> {
                                val index = statement.indexOf(shellConstruct.end)
                                if (index == -1) return@forEachRemaining
                                statement = statement.removeRange(
                                    index,
                                    index + shellConstruct.close.length
                                )
                                iterator.remove()
                            }
                        }
                    }

                    if (construct == Backslash) continue
                    val beginIndex = statement.indexOf(construct.begin)
                    val endIndex = statement.indexOf(construct.end)
                    if (beginIndex != -1 && endIndex < beginIndex) {
                        if (construct == Elif || construct == Else) {
                            if (result.lastOrNull() == If || result.lastOrNull() == Elif) result.removeLast()
                        }
                        result.add(construct)
                    }
                }
            }

            return result
        }

        private fun String.indexOf(regex: Regex): Int {
            return regex.toPattern().matcher(this).let { m ->
                if (m.find()) {
                    m.start()
                } else {
                    -1
                }
            }
        }
    }
}
