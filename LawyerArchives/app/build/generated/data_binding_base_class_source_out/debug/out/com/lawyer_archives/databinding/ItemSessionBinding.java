// Generated by view binder compiler. Do not edit!
package com.lawyer_archives.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.lawyer_archives.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemSessionBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final Button deleteButton;

  @NonNull
  public final Button editButton;

  @NonNull
  public final TextView location;

  @NonNull
  public final TextView sessionDate;

  @NonNull
  public final TextView sessionTitle;

  private ItemSessionBinding(@NonNull CardView rootView, @NonNull Button deleteButton,
      @NonNull Button editButton, @NonNull TextView location, @NonNull TextView sessionDate,
      @NonNull TextView sessionTitle) {
    this.rootView = rootView;
    this.deleteButton = deleteButton;
    this.editButton = editButton;
    this.location = location;
    this.sessionDate = sessionDate;
    this.sessionTitle = sessionTitle;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemSessionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemSessionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_session, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemSessionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteButton;
      Button deleteButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.editButton;
      Button editButton = ViewBindings.findChildViewById(rootView, id);
      if (editButton == null) {
        break missingId;
      }

      id = R.id.location;
      TextView location = ViewBindings.findChildViewById(rootView, id);
      if (location == null) {
        break missingId;
      }

      id = R.id.sessionDate;
      TextView sessionDate = ViewBindings.findChildViewById(rootView, id);
      if (sessionDate == null) {
        break missingId;
      }

      id = R.id.sessionTitle;
      TextView sessionTitle = ViewBindings.findChildViewById(rootView, id);
      if (sessionTitle == null) {
        break missingId;
      }

      return new ItemSessionBinding((CardView) rootView, deleteButton, editButton, location,
          sessionDate, sessionTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
