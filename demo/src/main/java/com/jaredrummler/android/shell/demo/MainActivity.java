/*
 * Copyright (C) 2017 Jared Rummler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jaredrummler.android.shell.demo;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.jaredrummler.simplemvp.MvpAppCompatActivity;

public class MainActivity extends MvpAppCompatActivity<MainPresenter> implements MainView {

  private EditText commandEditText;
  private CheckBox rootCheckBox;
  private Button runButton;
  private TextView outputTextView;
  private ProgressDialog dialog;

  private final TextWatcher runButtonEnabledWatcher = new TextWatcher() {

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      runButton.setEnabled(!TextUtils.isEmpty(s));
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    commandEditText = findViewById(R.id.commandsEditText);
    rootCheckBox = findViewById(R.id.rootCheckBox);
    runButton = findViewById(R.id.runButton);
    outputTextView = findViewById(R.id.outputTextView);

    commandEditText.addTextChangedListener(runButtonEnabledWatcher);
  }

  public void onRun(View v) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(commandEditText.getWindowToken(), 0);
    String command = commandEditText.getText().toString();
    boolean asRoot = rootCheckBox.isChecked();
    getPresenter().execute(asRoot, command);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, Menu.FIRST, 0, R.string.github)
        .setIcon(R.drawable.ic_github)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case Menu.FIRST:
        try {
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jaredrummler/AndroidShell")));
        } catch (ActivityNotFoundException ignored) {
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void showProgress() {
    dialog = ProgressDialog.show(MainActivity.this, "Running Command", "Please Wait...");
    dialog.setCancelable(false);
  }

  @Override public void hideProgress() {
    dialog.dismiss();
  }

  @Override public void showResult(CharSequence result) {
    outputTextView.setText(result);
  }

  @NonNull @Override public MainPresenter createPresenter() {
    return new MainPresenter();
  }

}
