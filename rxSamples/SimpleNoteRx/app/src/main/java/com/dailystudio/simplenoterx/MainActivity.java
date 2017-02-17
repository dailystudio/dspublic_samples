package com.dailystudio.simplenoterx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.development.Logger;
import com.dailystudio.simplenoterx.activity.EditNoteActivity;
import com.hwangjr.rxbus.*;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;

public class MainActivity extends AppCompatActivity {

    private final static long PROMPT_DELAY = 500;

    private boolean mSuppressNotify = false;
    private int mLastEventId = Constants.EVENT_NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent i = new Intent();
                i.setClass(getApplicationContext(), EditNoteActivity.class);

                ActivityLauncher.launchActivity(MainActivity.this, i);
            }
        });

        RxBus.get().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSuppressNotify = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSuppressNotify
                && mLastEventId != Constants.EVENT_NONE) {
            showPrompt(mLastEventId);

            mLastEventId = Constants.EVENT_NONE;
            mSuppressNotify = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);
    }

    private void showPrompt(final int event) {
        Logger.debug("show prompt for event = %d", event);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int resId = 0;
                switch (event) {
                    case Constants.EVENT_NEW_NOTE:
                        resId = R.string.prompt_new_note;
                        break;

                    case Constants.EVENT_UPDATE_NOTE:
                        resId = R.string.prompt_update_note;
                        break;

                }

                if (resId > 0) {
                    Snackbar.make(findViewById(R.id.root_view),
                            getString(resId),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        }, PROMPT_DELAY);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onDdEvent(final Integer event) {
        if (mSuppressNotify) {
            Logger.debug("suppress notify, do it after resume: %d",
                    event);
            mLastEventId = event;

            return;
        }

        showPrompt(event);
    }

    private Handler mHandler = new Handler();

}
