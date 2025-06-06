// Generated by view binder compiler. Do not edit!
package com.lawyer_archives.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.lawyer_archives.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddMeetingBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText editClientName;

  @NonNull
  public final EditText editDuration;

  @NonNull
  public final EditText editLocation;

  @NonNull
  public final EditText editMeetingDate;

  @NonNull
  public final EditText editTopic;

  @NonNull
  public final Button saveMeetingButton;

  private ActivityAddMeetingBinding(@NonNull LinearLayout rootView,
      @NonNull EditText editClientName, @NonNull EditText editDuration,
      @NonNull EditText editLocation, @NonNull EditText editMeetingDate,
      @NonNull EditText editTopic, @NonNull Button saveMeetingButton) {
    this.rootView = rootView;
    this.editClientName = editClientName;
    this.editDuration = editDuration;
    this.editLocation = editLocation;
    this.editMeetingDate = editMeetingDate;
    this.editTopic = editTopic;
    this.saveMeetingButton = saveMeetingButton;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddMeetingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddMeetingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_meeting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddMeetingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.editClientName;
      EditText editClientName = ViewBindings.findChildViewById(rootView, id);
      if (editClientName == null) {
        break missingId;
      }

      id = R.id.editDuration;
      EditText editDuration = ViewBindings.findChildViewById(rootView, id);
      if (editDuration == null) {
        break missingId;
      }

      id = R.id.editLocation;
      EditText editLocation = ViewBindings.findChildViewById(rootView, id);
      if (editLocation == null) {
        break missingId;
      }

      id = R.id.editMeetingDate;
      EditText editMeetingDate = ViewBindings.findChildViewById(rootView, id);
      if (editMeetingDate == null) {
        break missingId;
      }

      id = R.id.editTopic;
      EditText editTopic = ViewBindings.findChildViewById(rootView, id);
      if (editTopic == null) {
        break missingId;
      }

      id = R.id.saveMeetingButton;
      Button saveMeetingButton = ViewBindings.findChildViewById(rootView, id);
      if (saveMeetingButton == null) {
        break missingId;
      }

      return new ActivityAddMeetingBinding((LinearLayout) rootView, editClientName, editDuration,
          editLocation, editMeetingDate, editTopic, saveMeetingButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
