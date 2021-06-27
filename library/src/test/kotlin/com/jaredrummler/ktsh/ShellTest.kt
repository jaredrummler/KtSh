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

package com.jaredrummler.ktsh

import com.jaredrummler.ktsh.Shell.Command.Status.SUCCESS
import org.junit.Assert
import org.junit.Test

class ShellTest {

    @Test
    fun `should start process with provided environment`() {
        val variable: Variable = "NYAN"
        val value: Value = "CAT"

        fun Shell.assertEnvironment() = run("env").stdout.contains("$variable=$value")

        // Test each constructor
        Shell("sh", variable to value).assertEnvironment()
        Shell("sh", arrayOf("$variable=$value")).assertEnvironment()
        Shell("sh", mapOf(variable to value)).assertEnvironment()
    }

    @Test
    fun `should shell be alive until shutdown`() {
        val shell = Shell("sh")
        Assert.assertTrue(shell.isAlive())
        shell.shutdown()
        Assert.assertFalse(shell.isAlive())
    }

    @Test
    fun `should execute multi-line command`() {
        val result = Shell.SH.run(
            """
            for i in 1 2 3
            do
                echo ${'$'}i
            done
            """.trimIndent()
        )
        Assert.assertEquals(listOf("1", "2", "3"), result.stdout)
    }

    @Test
    fun `should add command result listener`() {
        var gotResult = false
        Shell.SH.addOnCommandResultListener(
            object : Shell.OnCommandResultListener {
                override fun onResult(result: Shell.Command.Result) {
                    Shell.SH.removeOnCommandResultListener(this)
                    gotResult = true
                }
            }
        )
        Shell.SH.run(TEST_COMMAND)
        Assert.assertTrue(gotResult)
    }

    @Test
    fun `should interrupt running command`() {
        val interruptTime = 100L
        val shell = Shell.SH

        Thread {
            Thread.sleep(interruptTime)
            Assert.assertTrue(shell.isRunning())
            shell.interrupt()
        }.start()

        fun Long.roundToNearestHundredth(): Long = when {
            this > interruptTime -> this - Math.floorMod(this, 100)
            this < interruptTime -> this - Math.floorMod(this, 100) + 100
            else -> this
        }

        val result = shell.run("sleep 1")

        val elapsedMillis = result.details.elapsed.roundToNearestHundredth()
        Assert.assertTrue(elapsedMillis == interruptTime)
    }

    @Test
    fun `should set exit status for next command`() {
        infix fun String.assert(status: Int) {
            val result = Shell.SH.run(this)
            val lastExitCode = Shell.SH.run("echo $?").stdout().toInt()
            Assert.assertEquals(status, result.exitCode)
            Assert.assertEquals(status, lastExitCode)
        }

        ERROR_COMMAND assert ERROR_STATUS
        TEST_COMMAND assert SUCCESS
    }

    @Test
    fun `should shell be idle when not running command`() {
        Assert.assertTrue(Shell.SH.isIdle())
        Shell.SH.run(TEST_COMMAND)
        Assert.assertTrue(Shell.SH.isIdle())
    }

    @Test
    fun `should redirect stderr to stdout`() {
        val result = Shell.SH.run(ERROR_COMMAND) { redirectErrorStream = true }
        Assert.assertTrue(result.stderr.isEmpty())
        Assert.assertTrue(result.stdout.isNotEmpty())
    }

    @Test
    fun `should include stdout`() {
        val result = Shell.SH.run(TEST_COMMAND)
        Assert.assertEquals(TEST_STDOUT, result.stdout.first())
    }

    @Test
    fun `should include stderr`() {
        val result = Shell.SH.run(ERROR_COMMAND)
        Assert.assertTrue(result.stderr.first().contains(ERROR_STDERR))
        Assert.assertTrue(!result.isSuccess)
        Assert.assertEquals(0, result.stdout.size)
    }

    @Test
    fun `should shutdown shell`() {
        val shell = Shell("sh")
        val result = shell.run(TEST_COMMAND)
        shell.shutdown()
        Assert.assertTrue(shell.isShutdown())
        Assert.assertTrue(result.stdout[0] == TEST_STDOUT)
    }

    @Test(expected = Shell.ClosedException::class)
    fun `should throw exception when running command when shutdown`() {
        val shell = Shell("sh")
        shell.shutdown()
        shell.run(TEST_COMMAND)
    }

    companion object {
        private const val TEST_COMMAND = "echo 'Hello, World!'"
        private const val TEST_STDOUT = "Hello, World!"
        private const val ERROR_COMMAND = "this-should-fail"
        private const val ERROR_STATUS = Shell.Command.Status.NOT_FOUND
        private const val ERROR_STDERR = "command not found"
    }
}
