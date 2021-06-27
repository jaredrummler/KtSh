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

import org.junit.Assert.assertEquals
import org.junit.Test

class ShellFormatterTest {

    @Test
    fun `should format code using spaces`() {
        val formatted = ShellFormatter().format(TEST_CODE_UNFORMATTED)
        assertEquals(TEST_CODE_FORMATTED_WITH_SPACES, formatted)
    }

    @Test
    fun `should format code using tabs`() {
        val formatted = ShellFormatter(ShellFormatter.IndentStyle.Tab).format(TEST_CODE_UNFORMATTED)
        assertEquals(TEST_CODE_FORMATTED_WITH_TABS, formatted)
    }

    companion object {
        private const val TEST_CODE_UNFORMATTED = """
for i in `seq 1 100`; do
if [ ${'$'}i%15 -eq 15 ]; then
echo "FizzBuzz"
elif [ ${'$'}i%5 -eq 0 ]; then
echo "Buzz"
elif [ ${'$'}i%3 -eq 0 ]; then
echo "Fizz"
else
echo ${'$'}i
fi
done
"""

        private const val TEST_CODE_FORMATTED_WITH_SPACES = """
for i in `seq 1 100`; do
    if [ ${'$'}i%15 -eq 15 ]; then
        echo "FizzBuzz"
    elif [ ${'$'}i%5 -eq 0 ]; then
        echo "Buzz"
    elif [ ${'$'}i%3 -eq 0 ]; then
        echo "Fizz"
    else
        echo ${'$'}i
    fi
done
"""

        private val TEST_CODE_FORMATTED_WITH_TABS =
            TEST_CODE_FORMATTED_WITH_SPACES.replace("    ", "\t")
    }
}