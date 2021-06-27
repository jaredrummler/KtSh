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
import com.blacksquircle.ui.editorkit.model.ColorScheme
import com.jaredrummler.ktsh.Shell

internal abstract class PromptStatement @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        this.setPadding(0.dp, 2.dp, 0.dp, 2.dp)
        this.layoutParams = createFillWidthParams()
        this.orientation = HORIZONTAL
    }

    abstract fun init(shell: Shell, scheme: ColorScheme): PromptStatement

    protected val Int.dp: Int get() = (this * context.resources.displayMetrics.density).toInt()
}
