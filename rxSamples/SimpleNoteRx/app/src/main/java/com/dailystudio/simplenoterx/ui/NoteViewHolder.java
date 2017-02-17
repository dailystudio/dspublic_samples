package com.dailystudio.simplenoterx.ui;

import android.content.Context;
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
            if (noteObject.isSelected()) {
                mTitleView.setBackgroundResource(
                        R.drawable.card_header_bg_disabled);
            } else {
                mTitleView.setBackgroundResource(
                        R.drawable.card_header_bg);
            }
        }

        if (mContentView != null) {
            mContentView.setText(noteObject.getContent());

            if (noteObject.isSelected()) {
                mContentView.setTextAppearance(
                        mContentView.getContext(),
                        R.style.NoteCardContentDisabled);
            } else {
                mContentView.setTextAppearance(
                        mContentView.getContext(),
                        R.style.NoteCardContent);
            }
        }

        if (mFooterView != null) {
            final Context context = mFooterView.getContext();

            SimpleDateFormat sdf = new SimpleDateFormat(
                    context.getString(R.string.note_date_fmt));

            mFooterView.setText(sdf.format(noteObject.getTime()));

            if (noteObject.isSelected()) {
                mFooterView.setBackgroundResource(
                        R.drawable.round_bg_disabled);
            } else {
                mFooterView.setBackgroundResource(
                        R.drawable.card_header_bg);
            }
        }
    }

}
