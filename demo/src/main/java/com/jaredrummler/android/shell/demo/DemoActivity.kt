/*
 * Copyright (C) 2018 Jared Rummler
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

package com.jaredrummler.android.shell.demo

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jaredrummler.simplemvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.commandsEditText
import kotlinx.android.synthetic.main.activity_main.outputTextView
import kotlinx.android.synthetic.main.activity_main.rootCheckBox
import kotlinx.android.synthetic.main.activity_main.runButton

class DemoActivity : MvpAppCompatActivity<DemoPresenter>(), DemoView {

  private var dialog: ProgressDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    commandsEditText.addTextChangedListener(object : TextChangeListener() {
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        runButton.isEnabled = !TextUtils.isEmpty(s)
      }
    })
    presenter.checkIfRootIsAvailable()
  }

  fun onRun(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(commandsEditText.windowToken, 0)
    val command = commandsEditText.text.toString()
    val asRoot = rootCheckBox.isChecked
    presenter.execute(asRoot, command)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.add(0, Menu.FIRST, 0, R.string.github)
        .setIcon(R.drawable.ic_github)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      Menu.FIRST -> {
        try {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jaredrummler/AndroidShell")))
        } catch (ignored: ActivityNotFoundException) {
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onRootAvailable(available: Boolean) {
    rootCheckBox.isChecked = available
  }

  override fun showProgress() {
    dialog = ProgressDialog.show(this, "Running Command", "Please Wait...")
    dialog?.setCancelable(false)
  }

  override fun hideProgress() {
    dialog?.dismiss()
  }

  override fun showResult(result: CharSequence) {
    outputTextView.text = result
  }

  override fun createPresenter(): DemoPresenter = DemoPresenter()

  abstract class TextChangeListener : TextWatcher {
    override fun afterTextChanged(s: Editable) {}
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
  }

}