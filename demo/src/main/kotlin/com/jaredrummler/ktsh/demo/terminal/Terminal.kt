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

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.res.use
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import com.blacksquircle.ui.editorkit.model.ColorScheme
import com.blacksquircle.ui.editorkit.theme.EditorTheme
import com.jaredrummler.ktsh.Shell
import com.jaredrummler.ktsh.demo.R

class Terminal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private val history = History()
    private lateinit var output: CommandOutputTextView
    private lateinit var executor: CommandExecutor
    private lateinit var console: Console
    private lateinit var shell: Shell
    private var command: String? = null

    var theme: Theme = Theme.DEFAULT
        set(value) {
            field = value
            setBackgroundColor(value.scheme.backgroundColor)
            if (::console.isInitialized) {
                console.children.onEach { child ->
                    child.setBackgroundColor(value.scheme.backgroundColor)
                    if (child is PromptStatementTwo) {
                        child.editor.colorScheme = value.scheme
                        child.editor.colorize()
                    }
                }
            }
        }

    init {
        layoutParams = createFillWidthParams()
        context.obtainStyledAttributes(attrs, intArrayOf(R.attr.syntaxColorTheme)).use { ta ->
            this.theme = Theme.values()[ta.getInt(0, Theme.DEFAULT.ordinal)]
            this.setBackgroundColor(theme.scheme.backgroundColor)
        }
    }

    /**
     * Attach the shell and an executor to run commands in the shell to the [Terminal] view.
     */
    fun attach(shell: Shell, executor: CommandExecutor) {
        this.executor = executor
        this.shell = shell

        this.console = Console(context)
        this.addView(console)

        this.shell.addOnStdoutLineListener(
            object : Shell.OnLineListener {
                override fun onLine(line: String) {
                    handler.post {
                        output.visibility = VISIBLE
                        output.appendStdOut(line)
                        scrollToBottom()
                    }
                }
            }
        )

        this.shell.addOnStderrLineListener(
            object : Shell.OnLineListener {
                override fun onLine(line: String) {
                    handler.post {
                        output.visibility = VISIBLE
                        output.appendStdErr(line)
                        scrollToBottom()
                    }
                }
            }
        )
    }

    /**
     * Navigate terminal history (up or down)
     *
     * @param direction Either [History.Direction.UP] or [History.Direction.DOWN]
     */
    fun navigate(direction: History.Direction): Boolean {
        val command = history[direction] ?: return false
        setInputText(command)
        return true
    }

    private fun onEnter(input: String) {
        when (this.command) {
            null -> this.command = input
            else -> {
                this.command += CONTROL_J
                this.command += input
            }
        }

        val command = requireNotNull(command)
        val constructs = ShellConstruct.constructs(command)
        if (constructs.isNotEmpty()) {
            addPromptStatementTwo()
            return
        }

        if (shell.isIdle()) {
            try {
                when (command) {
                    "clear" -> console.clear()
                    "exit" -> activity.finish()
                    else -> execute(command)
                }
            } finally {
                this.command = null
            }
        }
    }

    private fun execute(command: String) {
        history.add(command)
        setUpPromptForNewCommand()
        executor.execute(shell, command) { result -> onResult(result) }
    }

    private fun setUpPromptForNewCommand() {
        // Remove PS1
        console.removeChildrenOf<PromptStatementOne>()
        // Add the TextView for standard out/err streams.
        console.addView(
            CommandOutputTextView(context).also { view ->
                view.init(theme.scheme)
                view.visibility = GONE
            }.also { view ->
                output = view
            }
        )
        // Add the new PS2 view and disable all others.
        addPromptStatementTwo()
        // Set focus to the bottom of the Terminal
        scrollToBottom()
    }

    private fun addPromptStatementTwo() {
        console.addView(
            ps2().also { prompt ->
                command?.let { command ->
                    prompt.constructs = ShellConstruct.constructs(command)
                }
                prompt.doOnNextLayout {
                    (0 until console.childCount - 1).onEach { index ->
                        when (val child = console.getChildAt(index)) {
                            is PromptStatementTwo -> {
                                child.isEnabled = false
                            }
                        }
                    }
                }
            }
        )
        scrollToBottom()
    }

    private fun setInputText(text: String) {
        console.run {
            for (index in (childCount - 1) downTo 0) {
                when (val child = getChildAt(index)) {
                    is PromptStatementTwo -> {
                        child.text = text
                        break
                    }
                }
            }
        }
    }

    private fun onResult(result: Shell.Command.Result) = handler.post {
        console.addView(ps1(), console.childCount - 1)
        postDelayed({ scrollToBottom() }, 50L)
    }

    private fun scrollToBottom() {
        val lastChild = getChildAt(childCount - 1)
        val bottom = lastChild.bottom + paddingBottom
        val delta = bottom - (scrollY + height)
        smoothScrollBy(0, delta)
    }

    private fun ps1() = PromptStatementOne(context).init(shell, theme.scheme)

    private fun ps2() = PromptStatementTwo(context).init(shell, theme.scheme).onEnter(::onEnter)

    interface CommandExecutor {

        fun execute(
            shell: Shell,
            command: String,
            callback: (result: Shell.Command.Result) -> Unit
        )
    }

    /**
     * Terminal command history
     */
    class History {
        private val history = mutableListOf<String>()
        private var index = -1

        fun add(command: String) = history.add(command)

        operator fun get(direction: Direction): String? {
            when (direction) {
                Direction.UP -> if (--index <= -1) index = history.size - 1
                Direction.DOWN -> if (++index !in history.indices) index = 0
            }
            return if (index in history.indices) {
                val command = history[index]
                when {
                    command.contains(CONTROL_J) -> ShellFormatter().format(command)
                    else -> command
                }
            } else {
                null
            }
        }

        enum class Direction {
            UP, DOWN
        }
    }

    private inner class Console(context: Context) : LinearLayout(context) {

        init {
            layoutParams = createFillWidthParams()
            orientation = VERTICAL
            addPrompts()
        }

        fun clear() {
            removeAllViews()
            addPrompts()
        }

        private fun addPrompts() {
            addView(ps1())
            addView(ps2())
        }
    }

    companion object {
        private val CONTROL_J = Char(0x0A)
        private val TAB = Char(0x09)
    }

    enum class Theme(val scheme: ColorScheme) {
        Darcula(EditorTheme.DARCULA),
        Monokai(EditorTheme.MONOKAI),
        Obsidian(EditorTheme.OBSIDIAN),
        Ladies_Night(EditorTheme.LADIES_NIGHT),
        Tomorrow_Night(EditorTheme.TOMORROW_NIGHT),
        Visual_Studio_2013(EditorTheme.VISUAL_STUDIO_2013);

        companion object {
            val DEFAULT = Obsidian
        }
    }
}
