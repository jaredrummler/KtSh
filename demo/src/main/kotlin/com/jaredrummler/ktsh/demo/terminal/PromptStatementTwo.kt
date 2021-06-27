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
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.core.widget.doOnTextChanged
import com.blacksquircle.ui.editorkit.model.ColorScheme
import com.blacksquircle.ui.editorkit.model.EditorConfig
import com.blacksquircle.ui.editorkit.widget.TextProcessor
import com.blacksquircle.ui.language.shell.ShellLanguage
import com.jaredrummler.ktsh.Shell

internal class PromptStatementTwo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PromptStatement(context, attrs, defStyleAttr) {

    lateinit var editor: TextProcessor
    private lateinit var callback: OnEnterCallback

    var text: CharSequence
        get() = editor.text.toString()
        set(value) {
            editor.setTextContent(value)
            editor.setSelection(value.length)
        }

    var constructs: List<ShellConstruct>
        get() = ShellConstruct.constructs(editor.text.toString())
        set(value) {
            (value.joinToString(" ") { it.prompt } + ">").also { text ->
                val textView = getChildAt(0) as TextView
                textView.text = text
            }
        }

    override fun init(shell: Shell, scheme: ColorScheme): PromptStatementTwo = apply {
        addView(
            TextView(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.setPadding(6.dp, 3.dp, 6.dp, 3.dp)
                this.minWidth = 28.dp
                this.layoutParams = createFillHeightParams()
                this.gravity = Gravity.CENTER
                this.text = ">"
                this.setTextColor(Color.GREEN)
            }
        )
        addView(
            TextProcessor(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.setPadding(16.dp, 5.dp, 16.dp, 5.dp)
                this.layoutParams = createFillParams()
                this.language = ShellLanguage()
                this.gravity = Gravity.CENTER xor Gravity.START
                this.isCursorVisible = true
                this.editorConfig = EditorConfig(
                    fontType = Typeface.MONOSPACE,
                    codeCompletion = true,
                    lineNumbers = false,
                    highlightCurrentLine = false,
                    highlightDelimiters = false
                )
                onNewLineListener { command ->
                    callback.onEnter(command)
                }
                doOnNextLayout { view ->
                    view.requestFocus()
                }
            }.also { editor = it }
        )
    }

    override fun setBackgroundColor(color: Int) {
        children.onEach { child -> child.setBackgroundColor(color) }
        super.setBackgroundColor(color)
    }

    fun onEnter(callback: (input: String) -> Unit) = apply {
        this.callback = object : OnEnterCallback {
            override fun onEnter(command: String) {
                callback(command)
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        editor.isEnabled = false
        editor.isFocusable = false
        super.setEnabled(enabled)
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return editor.requestFocus(direction, previouslyFocusedRect) ||
            super.requestFocus(direction, previouslyFocusedRect)
    }

    interface OnEnterCallback {

        fun onEnter(command: String)
    }

    companion object {
        private fun TextProcessor.onNewLineListener(callback: (input: String) -> Unit) {
            doOnTextChanged { text, _, _, _ ->
                if (text != null && text.endsWith('\n')) {
                    val input = text.substring(0, text.length - 1)
                    setTextContent(input)
                    callback(input)
                }
            }
        }
    }
}
