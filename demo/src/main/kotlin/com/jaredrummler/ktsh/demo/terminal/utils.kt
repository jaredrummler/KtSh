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

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

typealias Keyword = String

internal inline fun <reified T : ViewGroup.LayoutParams> createWrapParams(): T =
    createParams(WRAP_CONTENT, WRAP_CONTENT)

internal inline fun <reified T : ViewGroup.LayoutParams> createFillParams(): T =
    createParams(MATCH_PARENT, MATCH_PARENT)

internal inline fun <reified T : ViewGroup.LayoutParams> createFillWidthParams(): T =
    createParams(MATCH_PARENT, WRAP_CONTENT)

internal inline fun <reified T : ViewGroup.LayoutParams> createFillHeightParams(): T =
    createParams(WRAP_CONTENT, MATCH_PARENT)

internal inline fun <reified T : ViewGroup.LayoutParams> createParams(width: Int, height: Int): T =
    T::class.java.getDeclaredConstructor(Int::class.java, Int::class.java).newInstance(
        width,
        height
    )

internal val View.activity: Activity get() = context as Activity

internal inline fun <reified T : View> ViewGroup.removeChildrenOf() {
    for (index in 0 until childCount) {
        val child = getChildAt(index)
        if (child is T) {
            removeView(child)
        }
    }
}
