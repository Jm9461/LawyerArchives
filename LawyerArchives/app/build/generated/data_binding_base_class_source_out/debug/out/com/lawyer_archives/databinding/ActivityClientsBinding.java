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

public final class ActivityClientsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final FloatingActionButton fabAddClient;

  @NonNull
  public final RecyclerView recyclerViewClients;

  private ActivityClientsBinding(@NonNull RelativeLayout rootView,
      @NonNull FloatingActionButton fabAddClient, @NonNull RecyclerView recyclerViewClients) {
    this.rootView = rootView;
    this.fabAddClient = fabAddClient;
    this.recyclerViewClients = recyclerViewClients;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityClientsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityClientsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_clients, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityClientsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fabAddClient;
      FloatingActionButton fabAddClient = ViewBindings.findChildViewById(rootView, id);
      if (fabAddClient == null) {
        break missingId;
      }

      id = R.id.recyclerViewClients;
      RecyclerView recyclerViewClients = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewClients == null) {
        break missingId;
      }

      return new ActivityClientsBinding((RelativeLayout) rootView, fabAddClient,
          recyclerViewClients);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
