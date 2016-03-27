// Generated code from Butter Knife. Do not modify!
package com.example.exercise.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PersonActivity$$ViewInjector<T extends com.example.exercise.ui.PersonActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427515, "field 'personProfileLayout'");
    target.personProfileLayout = finder.castView(view, 2131427515, "field 'personProfileLayout'");
    view = finder.findRequiredView(source, 2131427474, "field 'statusList'");
    target.statusList = finder.castView(view, 2131427474, "field 'statusList'");
    view = finder.findRequiredView(source, 2131427514, "field 'followActionBtn' and method 'followAction'");
    target.followActionBtn = finder.castView(view, 2131427514, "field 'followActionBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.followAction();
        }
      });
    view = finder.findRequiredView(source, 2131427513, "field 'followStatusView'");
    target.followStatusView = finder.castView(view, 2131427513, "field 'followStatusView'");
    view = finder.findRequiredView(source, 2131427512, "field 'followLayout'");
    target.followLayout = view;
  }

  @Override public void reset(T target) {
    target.personProfileLayout = null;
    target.statusList = null;
    target.followActionBtn = null;
    target.followStatusView = null;
    target.followLayout = null;
  }
}
