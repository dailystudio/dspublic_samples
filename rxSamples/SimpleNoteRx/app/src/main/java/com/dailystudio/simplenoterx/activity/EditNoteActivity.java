package com.dailystudio.simplenoterx.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dailystudio.app.activity.ActionBarFragmentActivity;
import com.dailystudio.simplenoterx.R;

/**
 * Created by nanye on 17/2/9.
 */

public class EditNoteActivity extends ActionBarFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_not);
    }

}
