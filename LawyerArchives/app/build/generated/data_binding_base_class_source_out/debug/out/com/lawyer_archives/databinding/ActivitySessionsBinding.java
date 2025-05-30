// Generated by view binder compiler. Do not edit!
package com.lawyer_archives.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lawyer_archives.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySessionsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final FloatingActionButton fabAddSession;

  @NonNull
  public final RecyclerView recyclerViewSessions;

  private ActivitySessionsBinding(@NonNull RelativeLayout rootView,
      @NonNull FloatingActionButton fabAddSession, @NonNull RecyclerView recyclerViewSessions) {
    this.rootView = rootView;
    this.fabAddSession = fabAddSession;
    this.recyclerViewSessions = recyclerViewSessions;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySessionsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySessionsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sessions, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySessionsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fabAddSession;
      FloatingActionButton fabAddSession = ViewBindings.findChildViewById(rootView, id);
      if (fabAddSession == null) {
        break missingId;
      }

      id = R.id.recyclerViewSessions;
      RecyclerView recyclerViewSessions = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewSessions == null) {
        break missingId;
      }

      return new ActivitySessionsBinding((RelativeLayout) rootView, fabAddSession,
          recyclerViewSessions);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
