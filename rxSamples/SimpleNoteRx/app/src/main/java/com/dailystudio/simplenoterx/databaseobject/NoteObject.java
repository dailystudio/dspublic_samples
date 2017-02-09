package com.dailystudio.simplenoterx.databaseobject;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;

/**
 * Created by nanye on 17/2/9.
 */

public class NoteObject extends TimeCapsule {

    public static final Column COLUMN_TITLE = new TextColumn("title", false);
    public static final Column COLUMN_CONTENT = new TextColumn("content");

    private final static Column[] sCloumns = {
            COLUMN_TITLE,
            COLUMN_CONTENT,
    };

    public NoteObject(Context context) {
        super(context);

        final Template templ = getTemplate();

        templ.addColumns(sCloumns);
    }

    public void setTitle(String title) {
        setValue(COLUMN_TITLE, title);
    }

    public String getTitle() {
        return getTextValue(COLUMN_TITLE);
    }

    public void setContent(String content) {
        setValue(COLUMN_CONTENT, content);
    }

    public String getContent() {
        return getTextValue(COLUMN_CONTENT);
    }

}
