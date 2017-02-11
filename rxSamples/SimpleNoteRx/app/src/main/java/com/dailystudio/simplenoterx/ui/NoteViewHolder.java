package com.dailystudio.simplenoterx.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.databaseobject.NoteObject;

import java.text.SimpleDateFormat;

/**
 * Created by nanye on 17/2/10.
 */
public class NoteViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitleView;
    private TextView mContentView;
    private TextView mFooterView;

    public NoteViewHolder(View itemView) {
        super(itemView);

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        if (itemView == null) {
            return;
        }

        mTitleView = (TextView) itemView.findViewById(R.id.note_title);
        mContentView = (TextView) itemView.findViewById(R.id.note_content);
        mFooterView = (TextView) itemView.findViewById(R.id.note_footer);
    }

    public void bindNote(NoteObject noteObject) {
        if (noteObject == null) {
            return;
        }

        if (mTitleView != null) {
            mTitleView.setText(noteObject.getTitle());
        }

        if (mContentView != null) {
            mContentView.setText(noteObject.getContent());
        }

        if (mFooterView != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

            mFooterView.setText(sdf.format(noteObject.getTime()));
        }
    }

}
