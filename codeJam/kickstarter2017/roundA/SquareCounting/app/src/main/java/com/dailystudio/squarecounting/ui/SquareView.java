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

    private final static int MIN_DIMEN = 2;
    private final static int DASH_PHASE = 50;

    private int mRows = MIN_DIMEN;
    private int mCols = MIN_DIMEN;
    private int mCellSize = MINI_SQUARE_SIZE;

    private float[] mDashIntervals;

    private int mLineWidth = 5;
    private int mLineColor = Color.GRAY;
    private Paint mLinePaint;

    private int mDotRadius = 10;
    private int mDotLineWidth = 10;
    private Paint mDotPaint;

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
        mLineColor = res.getColor(R.color.light_gray);

        mDotRadius = res.getDimensionPixelSize(
                R.dimen.default_square_dot_radius);
        mDotLineWidth = res.getDimensionPixelSize(
                R.dimen.default_square_dot_line_width);

        a.recycle();

        initMembers();
    }

    private void initMembers() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStrokeWidth(mLineWidth);
    }

    public void setSquareDimens(int rows, int cols) {
        mRows = rows;
        mCols = cols;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLinePaint.setPathEffect(new DashPathEffect(mDashIntervals, 3.0f));
//        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mLinePaint);

        Path p;
        int xOffset;
        int yOffset;
        for (int col = 0; col <= mCols; col++) {
            xOffset = col * mCellSize + mDotRadius / 2;

            p = new Path();

            p.moveTo(xOffset, 0);
            p.lineTo(xOffset, getHeight());
            canvas.drawPath(p, mLinePaint);
        }

        for (int row = 1; row < mRows; row++) {
            yOffset = row * mCellSize + mDotRadius / 2;

            p = new Path();

            p.moveTo(0, yOffset);
            p.lineTo(getWidth(), yOffset);
            canvas.drawPath(p, mLinePaint);
        }

        for (int col = 0; col <= mCols; col++) {
            for (int row = 0; row <= mRows; row++) {
                xOffset = col * mCellSize + mDotRadius / 2;
                yOffset = row * mCellSize + mDotRadius / 2;

                mDotPaint.setStyle(Paint.Style.STROKE);
                mDotPaint.setColor(Color.BLACK);
                canvas.drawCircle(xOffset, yOffset, mDotRadius, mDotPaint);

                mDotPaint.setStyle(Paint.Style.FILL);
                mDotPaint.setColor(Color.RED);
                canvas.drawCircle(xOffset, yOffset, mDotRadius, mDotPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        Logger.debug("widthSpecSize = %d, heightSpecSize = %d",
                widthSpecSize, heightSpecSize);

        int minSize = Math.min(widthSpecSize, heightSpecSize);
        int minDimen = Math.min(mRows, mCols);

        mCellSize = (int)Math.floor((float)minSize / minDimen);
        Logger.debug("minSize(%d) / minDimen(%d) = %d",
                minSize, minDimen, mCellSize);

        int width = mCellSize * mCols;
        int height = mCellSize * mRows;
        int dashInterval = Math.max(mCellSize / DASH_PHASE, 5);

        mDashIntervals = new float[] {
            dashInterval,
            dashInterval / 2,
        };

        setMeasuredDimension(width, height);

        Logger.debug("measured: width = %d, height = %d",
                getMeasuredWidth(),
                getMeasuredHeight());
    }

}
