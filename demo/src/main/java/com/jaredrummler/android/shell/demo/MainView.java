/*
 * Copyright (C) 2018 JRummy Apps, Inc. - All Rights Reserved
 *
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited.
 */

package com.jaredrummler.android.shell.demo;

import com.jaredrummler.simplemvp.MvpView;

public interface MainView extends MvpView {

  void showProgress();
  void hideProgress();
  void showResult(CharSequence result);

}
