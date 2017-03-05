package com.dailystudio.squarecounting.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.dailystudio.development.Logger;
import com.dailystudio.squarecounting.R;

/**
 * Created by nanye on 17/3/5.
 */

public class SquareView extends View {

    private final static int MINI_SQUARE_SIZE = 10;

    private final static int MIN_DIMEN = 4;
    private final static int DASH_PHASE = 50;

    private final static int SQUARE_DOTS_AXIS = 2;
    private final static int SQUARE_DOTS_COUNT = 4;

    private int mRows = MIN_DIMEN;
    private int mCols = MIN_DIMEN;
    private int mCellSize = MINI_SQUARE_SIZE;

    private float[] mDashIntervals;

    private int mLineWidth = 1;
    private int mDotLineWidth = 2;
    private int mLineColor = Color.GRAY;
    private Paint mLinePaint;
    private Paint mDotsLinePaint;

    private int mDotRadius = 5;
    private int mDotOutlineWidth = 1;
    private int mDotColor = Color.GRAY;
    private int mDotOutlineColor = Color.BLACK;
    private Paint mDotPaint;

    private int[][] mSquareDots = new int[SQUARE_DOTS_COUNT][SQUARE_DOTS_AXIS];

    public SquareView(Context context) {
        this(context, null);
    }

    public SquareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SquareView, defStyle, 0);

        int rows = a.getInt(R.styleable.SquareView_rows, MIN_DIMEN);
        int cols = a.getInt(R.styleable.SquareView_cols, MIN_DIMEN);
        setSquareDimens(rows, cols);

        final Resources res = getResources();

        mLineWidth = res.getDimensionPixelSize(
                R.dimen.default_square_line_width);
        mDotLineWidth = res.getDimensionPixelSize(
                R.dimen.default_square_dot_line_width);
        mLineColor = res.getColor(R.color.defaultSquareViewLineColor);

        mDotRadius = res.getDimensionPixelSize(
                R.dimen.default_square_dot_radius);
        mDotOutlineWidth = res.getDimensionPixelSize(
                R.dimen.default_square_dot_outline_width);
        mDotColor = res.getColor(R.color.defaultSquareViewDotColor);
        mDotOutlineColor = res.getColor(R.color.defaultSquareViewDotOutlineColor);

        a.recycle();

        initMembers();
    }

    private void initMembers() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        mDotsLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotsLinePaint.setColor(Color.BLACK);
        mDotsLinePaint.setStrokeWidth(mDotLineWidth);
        mDotsLinePaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStrokeWidth(mDotOutlineWidth);
/*

        int dots[][] =  {
            {2, 0},
            {3, 2},
            {1, 3},
            {0, 1},
        };

        setSquareDots(dots);
*/
    }

    public void setSquareDimens(int rows, int cols) {
        mRows = rows;
        mCols = cols;

        clearSquareDots();

        invalidate();
    }

    public void setSquareDots(int[][] dots) {
        if (dots == null) {
            return;
        }

        for (int i = 0; i < SQUARE_DOTS_COUNT; i++) {
            for (int j = 0; j < SQUARE_DOTS_AXIS; j++) {
                mSquareDots[i][j] = dots[i][j];
            }
        }

        invalidate();
    }

    public void clearSquareDots() {
        for (int i = 0; i < SQUARE_DOTS_COUNT; i++) {
            for (int j = 0; j < SQUARE_DOTS_AXIS; j++) {
                mSquareDots[i][j] = -1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLinePaint.setPathEffect(new DashPathEffect(mDashIntervals, 3.0f));
//        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mLinePaint);

        Path p;
        int xOffset;
        int yOffset;
        for (int col = 0; col < mCols; col++) {
            xOffset = col * mCellSize + mDotRadius + mDotOutlineWidth;

            p = new Path();

            p.moveTo(xOffset, mDotRadius + mDotOutlineWidth);
            p.lineTo(xOffset, mDotRadius + mDotOutlineWidth + (mRows - 1) * mCellSize);
            canvas.drawPath(p, mLinePaint);
        }

        for (int row = 0; row < mRows; row++) {
            yOffset = row * mCellSize + mDotRadius + mDotOutlineWidth;

            p = new Path();

            p.moveTo(mDotRadius + mDotOutlineWidth, yOffset);
            p.lineTo(mDotRadius + mDotOutlineWidth + (mCols - 1) * mCellSize, yOffset);
            canvas.drawPath(p, mLinePaint);
        }

        p = null;
        int x0 = -1;
        int y0 = -1;
        for (int i = 0; i < SQUARE_DOTS_COUNT; i++) {
            if (mSquareDots[i][0] == -1
                    || mSquareDots[i][1] == -1) {
                continue;
            }

            xOffset = mSquareDots[i][1] * mCellSize + mDotRadius + mDotOutlineWidth;
            yOffset = mSquareDots[i][0] * mCellSize + mDotRadius + mDotOutlineWidth;

            if (i == 0) {
                p = new Path();

                x0 = xOffset;
                y0 = yOffset;
                p.moveTo(xOffset, yOffset);
            } else {
                if (p != null) {
                    p.lineTo(xOffset, yOffset);
                }
            }
        }

        if (p != null) {
            if (x0 != -1 && y0 != -1) {
                p.lineTo(x0, y0);
            }

            canvas.drawPath(p, mDotsLinePaint);
        }

        int dotIndex = -1;
        for (int col = 0; col < mCols; col++) {
            for (int row = 0; row < mRows; row++) {
                dotIndex = getSquareDotIndex(row, col);

                xOffset = col * mCellSize + mDotRadius + mDotOutlineWidth;
                yOffset = row * mCellSize + mDotRadius + mDotOutlineWidth;

                mDotPaint.setStyle(Paint.Style.STROKE);
                mDotPaint.setColor(mDotOutlineColor);
                canvas.drawCircle(xOffset, yOffset, mDotRadius, mDotPaint);

                mDotPaint.setStyle(Paint.Style.FILL);
                mDotPaint.setColor((dotIndex != -1 ?
                        Color.BLACK : mDotColor));
                canvas.drawCircle(xOffset, yOffset, mDotRadius, mDotPaint);
            }
        }
    }

    private int getSquareDotIndex(int row, int col) {
        for (int i = 0; i < SQUARE_DOTS_COUNT; i++) {
            if (mSquareDots[i][0] == row && mSquareDots[i][1] == col) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        Logger.debug("widthSpecSize = %d, heightSpecSize = %d",
                widthSpecSize, heightSpecSize);

        int minSize = Math.min(widthSpecSize, heightSpecSize);
        int maxDimen = Math.max(mRows, mCols) - 1;

        int cellTotalSize = minSize - 2 * mDotRadius - 2 * mDotOutlineWidth;
//        minSize = minSize - 2 * mDotRadius;

        mCellSize = Math.round((float)cellTotalSize / maxDimen);
        Logger.debug("cellTotalSize(%d) / maxDimen(%d) = %d",
                cellTotalSize, maxDimen, mCellSize);

        int dashInterval = Math.max(mCellSize / DASH_PHASE, 5);

        mDashIntervals = new float[] {
            dashInterval,
            dashInterval / 2,
        };

        setMeasuredDimension(minSize, minSize);

        Logger.debug("measured: width = %d, height = %d",
                getMeasuredWidth(),
                getMeasuredHeight());
    }

}
