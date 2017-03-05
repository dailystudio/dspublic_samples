package com.dailystudio.squarecounting.ui;

import com.dailystudio.development.Logger;

/**
 * Created by nanye on 17/3/5.
 */

public class SquareCounting implements Runnable {

    public interface OnCountingListener {
        public void onNewSquare(int[][] dots);
    }

    private int mRows;
    private int mCols;
    private long mSquareCount = 0;

    private OnCountingListener mListener;

    public SquareCounting(int rows, int cols) {
        mRows = rows;
        mCols = cols;
    }

    public void setOnCoutingListener(OnCountingListener l) {
        mListener = l;
    }

    @Override
    public void run() {
        Logger.debug("square counting: row = %d, cols = %d",
                mRows, mCols);
        if (mRows <= 0 || mCols <= 0) {
            mSquareCount = 0;
            return;
        }

        int r, c;
        int sideLen;
        int[] d0;
        int[] d1;
        int[] d2;
        int[] d3;
        for (r = 0; r < mRows; r++) {
            for (c = 0; c < mCols; c++) {
                for (sideLen = 1; sideLen < mRows + mCols; sideLen++) {
                    d0 = new int[] {r, c};

                    d1 = getNextDot(r, c, sideLen, 1);
                    if (!inSquare(d1[0], d1[1])) {
                        continue;
                    }

                    d2 = getNextDot(d1[0], d1[1], sideLen, 2);
                    if (!inSquare(d2[0], d2[1])) {
                        continue;
                    }

                    d3 = getNextDot(d2[0], d2[1], sideLen, 3);
                    if (inSquare(d3[0], d3[1])) {
                        mSquareCount++;

                        if (mListener != null) {
                            int[][] dots = {d0, d1, d2, d3};

                            mListener.onNewSquare(dots);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean inSquare(int r, int c) {
        return (r >= 0 && r < mRows) && (c >= 0 && c < mCols);
    }

    private int[] getNextDot(int r, int c, int step, int dotIndex) {
        int nr = -1, nc = -1;

        if (dotIndex < 2) {
            nr = r + step;
            if (nr >= mRows) {
                step = nr - (mRows - 1);
                nr = (mRows - 1);
            }

            nc = c + step;

            if (nc >= mCols) {
                step = nc - (mCols - 1);
                nr = (mRows - 1) - step;
            }
        } else {
            nr = r - step;
            if (nr < 0) {
                step = -nr;
                nr = 0;
            }

            nc = c - step;
        }

        return new int[] {nr, nc};
    }

    public long getSquareCount() {
        return mSquareCount;
    }

}
