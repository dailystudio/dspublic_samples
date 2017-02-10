package com.dailystudio.simplenoterx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.simplenoterx.R;

/**
 * Created by nanye on 17/2/9.
 */

public class EditNoteFragment extends AbsLoaderFragment<Void> {

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
    }

    @Override
    protected int getLoaderId() {
        return 0;
    }

    @Override
    protected Bundle createLoaderArguments() {
        return null;
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

}
