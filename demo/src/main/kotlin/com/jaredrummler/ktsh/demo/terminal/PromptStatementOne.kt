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
import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.blacksquircle.ui.editorkit.model.ColorScheme
import com.jaredrummler.ktsh.Shell
import com.jaredrummler.ktsh.Shell.Command.Config.Companion.silent
import com.jaredrummler.ktsh.demo.R

internal class PromptStatementOne @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PromptStatement(context, attrs, defStyleAttr) {

    override fun init(shell: Shell, scheme: ColorScheme): PromptStatementOne = apply {
        addView(
            ImageView(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.layoutParams = createFillHeightParams()
                this.setPadding(6.dp, 2.dp, 6.dp, 2.dp)
                this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_android_green_16dp))
            }
        )
        addView(
            TextView(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.layoutParams = createFillHeightParams()
                this.typeface = Typeface.MONOSPACE
                this.text = Build.DEVICE
            }
        )
        addView(
            ImageView(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.layoutParams = createFillHeightParams()
                this.setPadding(6.dp, 2.dp, 6.dp, 2.dp)
                this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder_white_16dp))
            }
        )
        addView(
            TextView(context).apply {
                super.setBackgroundColor(scheme.backgroundColor)
                this.layoutParams = createWrapParams()
                this.typeface = Typeface.MONOSPACE
                this.ellipsize = TextUtils.TruncateAt.MIDDLE
                this.isSingleLine = true
                this.maxLines = 1
                this.text = shell.run(CMD_PWD, silent()).stdout()
            }
        )
    }

    override fun setBackgroundColor(color: Int) {
        children.onEach { child -> child.setBackgroundColor(color) }
        super.setBackgroundColor(color)
    }

    companion object {
        private const val CMD_PWD = "echo \${PWD:-?}"
    }
}
