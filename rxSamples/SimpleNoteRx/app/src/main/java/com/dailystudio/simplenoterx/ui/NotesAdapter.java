package com.dailystudio.simplenoterx.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.app.ui.AbsArrayItemViewHolder;
import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.simplenoterx.Constants;
import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.activity.EditNoteActivity;
import com.dailystudio.simplenoterx.databaseobject.NoteObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanye on 17/2/10.
 */

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<NoteObject> mNotes;

    private Context mContext;

    public NotesAdapter(Context context) {
        mContext = context.getApplicationContext();
        mNotes = new ArrayList<>();
    }

    public void setNotes(List<NoteObject> notes) {
        mNotes.clear();

        if (notes != null
                && notes.size() > 0) {
            mNotes.addAll(notes);
        }

        notifyDataSetChanged();
    }

    public NoteObject getNoteAtPosition(int pos) {
        if (pos < 0 || pos >= mNotes.size()) {
            return null;
        }

        return mNotes.get(pos);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_note, null);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteObject noteObject = getNoteAtPosition(position);

        if (holder != null) {
            if (holder.itemView != null) {
                holder.itemView.setTag(noteObject);
                holder.itemView.setOnClickListener(mItemClickListener);
            }

            holder.bindNote(noteObject);
        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    private View.OnClickListener mItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == null) {
                return;
            }

            final Context context = v.getContext();
            if (context == null) {
                return;
            }

            Object o = v.getTag();
            if (o instanceof  NoteObject == false) {
                return;
            }

            NoteObject noteObject = (NoteObject)o;

            Intent i = new Intent();

            i.setClass(context.getApplicationContext(),
                    EditNoteActivity.class);
            i.putExtra(Constants.EXTRA_NOTE_ID, noteObject.getId());

            ActivityLauncher.launchActivity(context, i);
        }

    };

}
