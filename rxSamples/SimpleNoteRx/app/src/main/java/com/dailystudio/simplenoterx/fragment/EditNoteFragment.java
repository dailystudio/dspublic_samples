package com.dailystudio.simplenoterx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dailystudio.app.fragment.BaseIntentFragment;
import com.dailystudio.simplenoterx.Constants;
import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.databaseobject.NoteObject;
import com.dailystudio.simplenoterx.databaseobject.NoteObjectDatabaseModal;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    protected void bindNote(NoteObject noteObject) {
        if (noteObject == null) {
            return;
        }

        if (mTitleEdit != null) {
            mTitleEdit.setText(noteObject.getTitle());
        }

        if (mContentEdit != null) {
            mContentEdit.setText(noteObject.getContent());
        }

    }

    @Override
    public void bindIntent(Intent intent) {
        super.bindIntent(intent);

        if (intent == null) {
            return;
        }

        final int nodeId = intent.getIntExtra(
                Constants.EXTRA_NOTE_ID, 0);
        if (nodeId == 0) {
            return;
        }

        Observable.create(new Observable.OnSubscribe<NoteObject>() {

            @Override
            public void call(Subscriber<? super NoteObject> subscriber) {
                subscriber.onNext(NoteObjectDatabaseModal.findNote(getContext(), nodeId));
            }

        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NoteObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NoteObject noteObject) {
                bindNote(noteObject);
            }

        });
    }
}
