package com.dailystudio.simplenoterx;

/**
 * Created by nanye on 17/2/17.
 */

public class Constants {

    public enum DbEvent {
        EVENT_NONE,
        EVENT_NEW_NOTE,
        EVENT_UPDATE_NOTE,
    };

    public enum EditModeEvent {
        EVENT_ENTER,
        EVENT_LEAVE,
    }

    public final static String ACTION_EDIT_NOTE = "simplenoterx.intent.ACTION_EDIT_NOTE";

    public final static String EXTRA_NOTE_ID = "simplenoterx.intent.EXTRA_NOTE_ID";

}
