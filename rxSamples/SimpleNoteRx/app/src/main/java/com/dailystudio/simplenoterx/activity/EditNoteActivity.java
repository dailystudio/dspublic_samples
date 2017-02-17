package com.dailystudio.simplenoterx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.dailystudio.app.activity.ActionBarFragmentActivity;
import com.dailystudio.development.Logger;
import com.dailystudio.simplenoterx.Constants;
import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.databaseobject.NoteObject;
import com.dailystudio.simplenoterx.databaseobject.NoteObjectDatabaseModal;
import com.dailystudio.simplenoterx.fragment.EditNoteFragment;
import com.hwangjr.rxbus.RxBus;

/**
 * Created by nanye on 17/2/9.
 */

public class EditNoteActivity extends ActionBarFragmentActivity {

    private int mNoteId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_not);

        setupViews();
    }

    private void setupViews() {
        showBackButtonOnActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();

            return true;
        } else if (itemId == R.id.action_edit_done) {
            Fragment fragment = findFragment(R.id.fragment_edit);
            if (fragment instanceof EditNoteFragment == false) {
                return true;
            }

            EditNoteFragment editFragment = (EditNoteFragment)fragment;

            final String title = editFragment.getNoteTitle();
            final String content = editFragment.getNoteContent();

            NoteObject object = NoteObjectDatabaseModal.addOrUpdateNote(
                    getApplicationContext(),
                    title,
                    content,
                    mNoteId);
            Logger.debug("[%s]title = [%s], content = [%s], object = %s",
                    (mNoteId == 0 ? "new" : "update"),
                    title, content, object);

            if (object != null) {
                RxBus.get().post((mNoteId == 0 ? Constants.DbEvent.EVENT_NEW_NOTE
                                : Constants.DbEvent.EVENT_UPDATE_NOTE));
            }

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showBackButtonOnActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void bindIntent(Intent intent) {
        super.bindIntent(intent);

        if (intent == null) {
            return;
        }

        mNoteId = intent.getIntExtra(Constants.EXTRA_NOTE_ID, 0);
    }



}
