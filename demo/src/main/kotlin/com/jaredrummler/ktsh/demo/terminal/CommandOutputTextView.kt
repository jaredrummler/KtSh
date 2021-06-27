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
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.blacksquircle.ui.editorkit.model.ColorScheme
import kotlin.math.roundToInt

internal class CommandOutputTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @get:ColorInt
    private val stdErrTextColor by lazy {
        ContextCompat.getColor(context, android.R.color.holo_red_light)
    }

    init {
        setPadding(8.dp, 0, 8.dp, 0)
        layoutParams = createFillWidthParams()
        typeface = Typeface.MONOSPACE
    }

    fun init(scheme: ColorScheme) = apply {
        setBackgroundColor(scheme.backgroundColor)
        setTextColor(scheme.textColor)
    }

    fun appendStdOut(line: String) {
        append("\n") { !text.isNullOrEmpty() }
        append(line)
    }

    fun appendStdErr(line: String) {
        append("\n") { !text.isNullOrEmpty() }
        append(
            SpannableString(line).apply {
                setSpan(
                    ForegroundColorSpan(stdErrTextColor),
                    0,
                    line.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        )
    }

    private val Int.dp: Int get() = (this * context.resources.displayMetrics.density).roundToInt()

    companion object {
        private fun TextView.append(str: String, condition: () -> Boolean) {
            if (condition()) append(str)
        }
    }
}
