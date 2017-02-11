package com.dailystudio.simplenoterx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dailystudio.app.fragment.BaseIntentFragment;
import com.dailystudio.simplenoterx.R;

/**
 * Created by nanye on 17/2/9.
 */

public class EditNoteFragment extends BaseIntentFragment {

    private EditText mTitleEdit;
    private EditText mContentEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, null);

        setupViews(view);

        return view;
    }

    private void setupViews(View fragmentView) {
        if (fragmentView == null) {
            return;
        }

        mTitleEdit = (EditText) fragmentView.findViewById(R.id.note_title);
        mContentEdit = (EditText) fragmentView.findViewById(R.id.note_content);
    }

    public String getNoteTitle() {
        return getEditText(mTitleEdit);
    }

    public String getNoteContent() {
        return getEditText(mContentEdit);
    }

    public String getEditText(EditText editView) {
        if (editView == null) {
            return null;
        }

        Editable editable = editView.getText();
        if (editable == null) {
            return null;
        }

        return editable.toString();
    }

}
