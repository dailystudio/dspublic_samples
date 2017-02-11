package com.dailystudio.simplenoterx.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.dataobject.database.DatabaseConnectivity;
import com.dailystudio.simplenoterx.R;
import com.dailystudio.simplenoterx.databaseobject.NoteObject;
import com.dailystudio.simplenoterx.databaseobject.NoteObjectDatabaseModal;
import com.dailystudio.simplenoterx.ui.NotesAdapter;

import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by nanye on 17/2/10.
 */

public class NoteListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;

    private View mEmptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, null);

        setupViews(view);

        return view;
    }

    private void setupViews(View fragmentView) {
        if (fragmentView == null) {
            return;
        }

        mRecyclerView =
                (RecyclerView) fragmentView.findViewById(android.R.id.list);
        if (mRecyclerView != null) {
            mAdapter = new NotesAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(
                    new GridLayoutManager(getContext(), 2));
        }

        mEmptyView = fragmentView.findViewById(android.R.id.empty);

        final Observable<List<NoteObject>> notesObservable = Observable.create(new Observable.OnSubscribe<List<NoteObject>>() {

            @Override
            public void call(final Subscriber<? super List<NoteObject>> subscriber) {
                final Context context = getContext();
                final ContentResolver resolver = context.getContentResolver();

                final ContentObserver observer =
                        new ContentObserver(mContentObserverHandler) {
                    @Override public void onChange(boolean selfChange) {
                        List<NoteObject> notes =
                                NoteObjectDatabaseModal.listNotes(getContext());
                        subscriber.onNext(notes);
                    }
                };

                DatabaseConnectivity connectivity =
                        new DatabaseConnectivity(context, NoteObject.class);

                resolver.registerContentObserver(
                        connectivity.getDatabaseObserverUri(), true, observer);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override public void call() {
                        resolver.unregisterContentObserver(observer);
                    }
                }));

                List<NoteObject> notes =
                        NoteObjectDatabaseModal.listNotes(getContext());

                subscriber.onNext(notes); // Trigger initial query.
            }

        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io());

/*        Observable.create(new Observable.OnSubscribe<List<NoteObject>>() {
            @Override public void call(Subscriber<? super List<NoteObject>> subscriber) {
                notesObservable.unsafeSubscribe(subscriber);
            }
        })*/
        notesObservable.subscribe(new Observer<List<NoteObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<NoteObject> noteObjects) {
                mAdapter.setNotes(noteObjects);

                setEmptyViewVisible((noteObjects == null
                        || noteObjects.size() <= 0));
            }
        });
    }

    private void setEmptyViewVisible(boolean visible) {
        if (mEmptyView == null) {
            return;
        }

        mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    final Handler mContentObserverHandler = new Handler(Looper.getMainLooper());

}
