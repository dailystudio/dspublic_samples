package com.dailystudio.simplenoterx.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.dailystudio.development.Logger;
import com.dailystudio.simplenoterx.Constants;
import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.databaseobject.NoteObjectDatabaseModal;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;

/**
 * Created by nanye on 17/2/17.
 */

public class DeleteConfirmDialogFragment extends DialogFragment {

    ArrayList<Integer> mNoteIds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        mNoteIds = arguments.getIntegerArrayList(Constants.EXTRA_NOTE_IDS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RxBus.get().post(Constants.EditModeEvent.EVENT_LEAVE);

                        int ret = NoteObjectDatabaseModal.deleteNodes(getContext(), mNoteIds);
                        Logger.debug("%d note(s) deleted", ret);
                        if (ret > 0) {
                            RxBus.get().post((ret == 1
                                    ? Constants.DbEvent.EVENT_DELETE_NOTE
                                        : Constants.DbEvent.EVENT_DELETE_NOTES));
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RxBus.get().post(Constants.EditModeEvent.EVENT_LEAVE);
                    }
                })
                .create();
    }

}
