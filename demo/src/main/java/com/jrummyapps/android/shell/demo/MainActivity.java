/*
 * Copyright (C) 2016 JRummy Apps Inc.
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

package com.jrummyapps.android.shell.demo;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
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

import com.jrummyapps.android.shell.CommandResult;
import com.jrummyapps.android.shell.Shell;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  EditText commandEditText;
  CheckBox rootCheckBox;
  Button runButton;
  TextView outputTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    commandEditText = (EditText) findViewById(R.id.commandsEditText);
    rootCheckBox = (CheckBox) findViewById(R.id.rootCheckBox);
    runButton = (Button) findViewById(R.id.runButton);
    outputTextView = (TextView) findViewById(R.id.outputTextView);

    commandEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        runButton.setEnabled(!TextUtils.isEmpty(s));
      }
    });

    runButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commandEditText.getWindowToken(), 0);
        String command = commandEditText.getText().toString();
        boolean asRoot = rootCheckBox.isChecked();
        new RunCommandTask(asRoot).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, command);
      }
    });

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
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jrummyapps/android-shell")));
        } catch (ActivityNotFoundException ignored) {
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (isFinishing()) {
      Shell.SU.closeConsole();
    }
  }

  // Ignore the bad AsyncTask usage.
  final class RunCommandTask extends AsyncTask<String, Void, CommandResult> {

    private final boolean asRoot;
    private ProgressDialog dialog;

    RunCommandTask(boolean asRoot) {
      this.asRoot = asRoot;
    }

    @Override protected void onPreExecute() {
      dialog = ProgressDialog.show(MainActivity.this, "Running Command", "Please Wait...");
      dialog.setCancelable(false);
    }

    @Override protected CommandResult doInBackground(String... commands) {
      if (asRoot) {
        return Shell.SU.run(commands);
      } else {
        return Shell.SH.run(commands);
      }
    }

    @Override protected void onPostExecute(CommandResult result) {
      if (!isFinishing()) {
        dialog.dismiss();
        outputTextView.setText(resultToHtml(result));
      }
    }

    private Spanned resultToHtml(CommandResult result) {
      StringBuilder html = new StringBuilder();
      // exit status
      html.append("<p><strong>Edit Code:</strong> ");
      if (result.isSuccessful()) {
        html.append("<font color='green'>").append(result.exitCode).append("</font>");
      } else {
        html.append("<font color='red'>").append(result.exitCode).append("</font>");
      }
      html.append("</p>");
      // stdout
      if (result.stdout.size() > 0) {
        html.append("<p><strong>STDOUT:</strong></p><p>")
            .append(result.getStdout().replaceAll("\n", "<br>"))
            .append("</p>");
      }
      // stderr
      if (result.stderr.size() > 0) {
        html.append("<p><strong>STDERR:</strong></p><p><font color='red'>")
            .append(result.getStderr().replaceAll("\n", "<br>"))
            .append("</font></p>");
      }
      return Html.fromHtml(html.toString());
    }

  }

}
