// Generated code from Butter Knife. Do not modify!
package com.example.exercise.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PersonProfileLayout$$ViewInjector<T extends com.example.exercise.ui.PersonProfileLayout> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427518, "field 'profileLayout'");
    target.profileLayout = view;
    view = finder.findRequiredView(source, 2131427520, "field 'nameView'");
    target.nameView = finder.castView(view, 2131427520, "field 'nameView'");
    view = finder.findRequiredView(source, 2131427519, "field 'avatarView'");
    target.avatarView = finder.castView(view, 2131427519, "field 'avatarView'");
    view = finder.findRequiredView(source, 2131427521, "field 'editHint'");
    target.editHint = view;
  }

  @Override public void reset(T target) {
    target.profileLayout = null;
    target.nameView = null;
    target.avatarView = null;
    target.editHint = null;
  }
}
