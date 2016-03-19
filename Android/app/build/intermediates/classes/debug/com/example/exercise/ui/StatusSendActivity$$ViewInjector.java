// Generated code from Butter Knife. Do not modify!
package com.example.exercise.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class StatusSendActivity$$ViewInjector<T extends com.example.exercise.ui.StatusSendActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427487, "field 'editText'");
    target.editText = finder.castView(view, 2131427487, "field 'editText'");
    view = finder.findRequiredView(source, 2131427366, "field 'imageView'");
    target.imageView = finder.castView(view, 2131427366, "field 'imageView'");
    view = finder.findRequiredView(source, 2131427488, "field 'imageAction' and method 'imageAction'");
    target.imageAction = finder.castView(view, 2131427488, "field 'imageAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.imageAction();
        }
      });
    view = finder.findRequiredView(source, 2131427476, "method 'send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.send();
        }
      });
  }

  @Override public void reset(T target) {
    target.editText = null;
    target.imageView = null;
    target.imageAction = null;
  }
}
