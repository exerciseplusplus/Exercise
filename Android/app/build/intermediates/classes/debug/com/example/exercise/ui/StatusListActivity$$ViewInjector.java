// Generated code from Butter Knife. Do not modify!
package com.example.exercise.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class StatusListActivity$$ViewInjector<T extends com.example.exercise.ui.StatusListActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427474, "field 'statusList'");
    target.statusList = finder.castView(view, 2131427474, "field 'statusList'");
    view = finder.findRequiredView(source, 2131427509, "method 'goFollowers'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goFollowers();
        }
      });
    view = finder.findRequiredView(source, 2131427510, "method 'goFollowing'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goFollowing();
        }
      });
    view = finder.findRequiredView(source, 2131427508, "method 'goSend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goSend();
        }
      });
  }

  @Override public void reset(T target) {
    target.statusList = null;
  }
}
