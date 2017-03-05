package com.dailystudio.squarecounting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dailystudio.development.Logger;
import com.dailystudio.squarecounting.ui.SquareCounting;
import com.dailystudio.squarecounting.ui.SquareView;

public class MainActivity extends AppCompatActivity {

    private SquareView mSquareView;

    private class SquareCountingAsyncTask extends AsyncTask<Integer, int[][], Long> {

        @Override
        protected Long doInBackground(Integer... dimens) {
            if (dimens.length < 2) {
                return 0l;
            }

            int rows = dimens[0];
            int cols = dimens[1];

            SquareCounting counting = new SquareCounting(rows, cols);
            counting.setOnCoutingListener(new SquareCounting.OnCountingListener() {
                @Override
                public void onNewSquare(int[][] dots) {
                    Logger.debug("new square");
                    publishProgress(dots);
                }
            });
            counting.run();


            return counting.getSquareCount();
        }

        @Override
        protected void onProgressUpdate(int[][]... values) {
            super.onProgressUpdate(values);
            if (mSquareView != null
                    && values != null) {
                mSquareView.setSquareDots(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Logger.debug("count = %d", aLong);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupViews();
    }

    private void setupViews() {
        mSquareView = (SquareView)findViewById(R.id.square_view);
        mSquareView.setSquareDimens(2, 4);

        Integer[] dimens = {2, 4};

        new SquareCountingAsyncTask().execute(dimens);
    }

}
