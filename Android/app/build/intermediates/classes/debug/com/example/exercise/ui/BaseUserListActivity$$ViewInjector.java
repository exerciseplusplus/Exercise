// Generated code from Butter Knife. Do not modify!
package com.example.exercise.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class BaseUserListActivity$$ViewInjector<T extends com.example.exercise.ui.BaseUserListActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427523, "field 'userList'");
    target.userList = finder.castView(view, 2131427523, "field 'userList'");
  }

  @Override public void reset(T target) {
    target.userList = null;
  }
}
