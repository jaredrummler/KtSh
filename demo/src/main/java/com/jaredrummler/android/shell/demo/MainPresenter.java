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

package com.jaredrummler.android.shell.demo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.jaredrummler.simplemvp.Presenter;

public class MainPresenter extends Presenter<MainView> {

  @Override public void destroy() {
    super.destroy();
    Shell.SU.closeConsole();
  }

  @SuppressLint("StaticFieldLeak")
  public void execute(final boolean asRoot, final String... commands) {
    new AsyncTask<String, Void, CommandResult>() {

      @Override protected void onPreExecute() {
        if (isAttached()) {
          getView().showProgress();
        }
      }

      @Override protected CommandResult doInBackground(String... commands) {
        if (asRoot) {
          return Shell.SU.run(commands);
        } else {
          return Shell.SH.run(commands);
        }
      }

      @Override protected void onPostExecute(CommandResult commandResult) {
        if (isAttached()) {
          Spanned result = resultToHtml(commandResult);
          getView().hideProgress();
          getView().showResult(result);
        }
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, commands);
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
