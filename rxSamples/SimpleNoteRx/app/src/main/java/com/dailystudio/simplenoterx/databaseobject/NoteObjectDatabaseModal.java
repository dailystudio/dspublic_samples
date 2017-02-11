package com.dailystudio.simplenoterx.databaseobject;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseReader;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;

import java.util.List;

/**
 * Created by nanye on 17/2/10.
 */

public class NoteObjectDatabaseModal {

    public static NoteObject addOrUpdateNote(Context context,
                                             String title,
                                             String content,
                                             int noteId) {
        if (context == null
                || TextUtils.isEmpty(title)) {
            return null;
        }

        final long now = System.currentTimeMillis();

        TimeCapsuleDatabaseWriter<NoteObject> writer =
                new TimeCapsuleDatabaseWriter<>(context, NoteObject.class);

        NoteObject noteObject = findNote(context, noteId);
        if (noteObject == null) {
            noteObject = new NoteObject(context);

            noteObject.setTitle(title);
            noteObject.setContent(content);
            noteObject.setTime(now);

            writer.insert(noteObject);
        } else {
            noteObject.setTitle(title);
            noteObject.setContent(content);
            noteObject.setTime(now);

            writer.update(noteObject);
        }

        return noteObject;
    }

    public static NoteObject findNote(Context context, int noteId) {
        if (context == null
                || noteId <= 0) {
            return null;
        }

        TimeCapsuleDatabaseReader<NoteObject> reader =
                new TimeCapsuleDatabaseReader<>(context, NoteObject.class);

        Query query = new Query(NoteObject.class);

        ExpressionToken selToken =
                NoteObject.COLUMN_ID.eq(noteId);
        if (selToken == null) {
            return null;
        }

        query.setSelection(selToken);


        return reader.queryLastOne(query);
    }

    public static List<NoteObject> listNotes(Context context) {
        if (context == null) {
            return null;
        }

        TimeCapsuleDatabaseReader<NoteObject> reader =
                new TimeCapsuleDatabaseReader<>(context, NoteObject.class);

        Query query = new Query(NoteObject.class);

        OrderingToken orderByToken =
                NoteObject.COLUMN_TIME.orderByDescending();
        if (orderByToken != null) {
            query.setOrderBy(orderByToken);
        }

        return reader.query(query);
    }

}
