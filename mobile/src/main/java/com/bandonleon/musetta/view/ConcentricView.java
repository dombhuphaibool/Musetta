package com.bandonleon.musetta.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bandonleon.musetta.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dombhuphaibool on 10/15/15.
 */
public class ConcentricView extends View {

    public interface OnSectionTouchListener {
        boolean onSectionTouched(int section);
    }

    public static final int CENTER_SECTION_INDEX = -1;
    public static final int NO_SECTION_INDEX = -99;

    private static final int DEFAULT_NUM_SECTIONS = 5;
    private static final int DEFAULT_INNER_PCT = 50;
    private static final float DEFAULT_LABEL_TEXT_SIZE = 60.0f;

    private Paint mArcPaint;
    private Paint mCirclePaint;
    private Paint mLabelPaint;
    private Typeface mTypeface;

    private int mNumSections;
    private int mInnerCirclePct;    // value between 0-100 inclusive
    private RectF mOuterCircleBounds;
    private float mOuterRadius;
    private float mInnerRadius;

    private TouchTracker mTouchTracker;
    private OnSectionTouchListener mSectionTouchListener;

    private Map<Integer, String> mSectionLabels;

    protected Typeface getTypeface() {
        return mTypeface;
    }

    private void init(Context context) {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/journal.ttf");

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.FILL);
        mArcPaint.setColor(Color.BLUE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.GREEN);

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setColor(Color.BLACK);
        mLabelPaint.setTextSize(DEFAULT_LABEL_TEXT_SIZE);
        mLabelPaint.setTypeface(mTypeface);

        mNumSections = DEFAULT_NUM_SECTIONS;
        mInnerCirclePct = DEFAULT_INNER_PCT;
        mOuterCircleBounds = new RectF();

        mSectionLabels = new HashMap<>();

        mTouchTracker = new TouchTracker(context);
        mTouchTracker.setOnTouchListener(new TouchTracker.OnTouchListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                return ConcentricView.this.onTouchDown(event);
            }

            @Override
            public void onMove(MotionEvent event) {
                ConcentricView.this.onTouchMove(event);
            }

            @Override
            public void onUp(MotionEvent event) {
                ConcentricView.this.onTouchUp(event);
            }
        });
    }

    public ConcentricView(Context context) {
        super(context);
        init(context);
    }

    public ConcentricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConcentricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ConcentricView, 0, 0);

        try {
            mInnerCirclePct = a.getInteger(R.styleable.ConcentricView_innerPct, DEFAULT_INNER_PCT) % 101;
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float paddingLeft = (float) getPaddingLeft();
        float paddingTop = (float) getPaddingTop();

        float xPadding = paddingLeft + (float) getPaddingRight();
        float yPadding = paddingTop + (float) getPaddingBottom();

        float width = (float) w - xPadding;
        float height = (float) h - yPadding;

        float diameter = Math.min(width, height);
        float offsetLeft = ((float) w - diameter) * 0.5f;
        float offsetTop = ((float) h - diameter) * 0.5f;

        mOuterCircleBounds.set(offsetLeft, offsetTop, offsetLeft + diameter, offsetTop + diameter);
        mOuterRadius = diameter * 0.5f;
        mInnerRadius = mOuterRadius * (mInnerCirclePct * 0.01f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        @ColorInt int strokeColor = getResources().getColor(R.color.dark_grey);
        float strokeWidth = 5.0f;

        float centerX = mOuterCircleBounds.centerX();
        float centerY = mOuterCircleBounds.centerY();
        float startAngle = 0.0f;
        float sweepAngle = 360.0f / (float) mNumSections;
        // NoteItem: The angle starts at 0 degress at 3 o'clock and increment clockwise
        for (int i = 0; i < mNumSections; ++i) {
            mArcPaint.setStyle(Paint.Style.FILL);
            mArcPaint.setColor(getSectionColor(i));
            canvas.drawArc(mOuterCircleBounds, startAngle, sweepAngle, true, mArcPaint);

            mArcPaint.setStyle(Paint.Style.STROKE);
            mArcPaint.setStrokeWidth(strokeWidth);
            mArcPaint.setColor(strokeColor);
            canvas.drawArc(mOuterCircleBounds, startAngle, sweepAngle, true, mArcPaint);

            startAngle += sweepAngle;
        }
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(getCenterColor());
        canvas.drawCircle(centerX, centerY, mInnerRadius, mCirclePaint);

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(strokeWidth);
        mCirclePaint.setColor(strokeColor);
        canvas.drawCircle(centerX, centerY, mInnerRadius, mCirclePaint);

        // Draw section labels
        double labelAngle;
        float labelCenterX, labelCenterY;
        float labelRadius = mInnerRadius + ((mOuterRadius - mInnerRadius) * 0.5f);
        Rect labelBounds = new Rect();
        for (int sectionIdx = 0; sectionIdx < mNumSections; ++sectionIdx) {
            String sectionLabel = getSectionLabel(sectionIdx);
            if (!TextUtils.isEmpty(sectionLabel)) {
                labelAngle = Math.toRadians((sweepAngle * sectionIdx) + (sweepAngle * 0.5f));
                labelCenterX = centerX + (float) (labelRadius * Math.cos(labelAngle));
                labelCenterY = centerY + (float) (labelRadius * Math.sin(labelAngle));

                /*
                mLabelPaint.getTextBounds(sectionLabel, 0, sectionLabel.length(), labelBounds);
                labelCenterX -= (labelBounds.width() * 0.5f);
                labelCenterY += (labelBounds.height() * 0.5f);
                canvas.drawText(sectionLabel, labelCenterX, labelCenterY, mLabelPaint);
                */
                drawCenteredText(canvas, sectionLabel, labelCenterX, labelCenterY, mLabelPaint);
            }
        }

        // Draw center label
        String centerLabel = getCenterLabel();
        if (!TextUtils.isEmpty(centerLabel)) {
            /*
            mLabelPaint.getTextBounds(centerLabel, 0, centerLabel.length(), labelBounds);
            canvas.drawText(centerLabel, centerX - (labelBounds.width() * 0.5f),
                    centerY + (labelBounds.height() * 0.5f), mLabelPaint);
            */
            drawCenteredText(canvas, centerLabel, centerX, centerY, mLabelPaint);
        }
    }

    private Rect mCachedBounds;
    protected void drawCenteredText(Canvas canvas, String text, float x, float y, Paint paint) {
        if (mCachedBounds == null) {
            mCachedBounds = new Rect();
        }
        paint.getTextBounds(text, 0, text.length(), mCachedBounds);
        x -= (mCachedBounds.width() * 0.5f);
        y += (mCachedBounds.height() * 0.5f);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchTracker.onTouchEvent(event);
    }

    protected TouchTracker getTouchTracker() {
        return mTouchTracker;
    }

    protected boolean onTouchDown(MotionEvent event) {
        return checkTouchEvent(event);
    }

    protected void onTouchMove(MotionEvent event) {
        // Log.e("Dom", "Move to (" + event.getX() + ", " + event.getY() + ")");
    }

    protected void onTouchUp(MotionEvent event) {
        // Log.e("Dom", "Up at (" + event.getX() + ", " + event.getY() + ")");
    }

    private boolean checkTouchEvent(MotionEvent event) {
        float vectorX = event.getX() - mOuterCircleBounds.centerX();
        float vectorY = event.getY() - mOuterCircleBounds.centerY();
        float magSqrd = vectorX * vectorX + vectorY * vectorY;
        if (magSqrd < mInnerRadius * mInnerRadius) {
            return onSectionTouched(CENTER_SECTION_INDEX);
        } else if (magSqrd < mOuterRadius * mOuterRadius) {
            double angle = Math.toDegrees(Math.atan(vectorY / vectorX));
            if (vectorX < 0) {
                angle += 180;
            }
            while (angle < 0) {
                angle += 360;
            }
            double sectionDegrees = 360 / mNumSections;
            return onSectionTouched((int) (angle/sectionDegrees));
        }

        return false;
    }

    protected boolean onSectionTouched(int sectionIdx) {
        return mSectionTouchListener != null ? mSectionTouchListener.onSectionTouched(sectionIdx) : false;
    }

    protected @ColorInt int getCenterColor() {
        return Color.GREEN;
    }

    protected @ColorInt int getSectionColor(int sectionIdx) {
        return(sectionIdx % 2 == 0 ? Color.YELLOW : Color.MAGENTA);
    }

    protected String getCenterLabel() {
        return mSectionLabels.get(CENTER_SECTION_INDEX);
    }

    protected String getSectionLabel(int sectionIdx) {
        return mSectionLabels.get(sectionIdx);
    }

    public void setNumSections(int numSections) {
        if (mNumSections != numSections) {
            mNumSections = numSections;
            invalidate();
            requestLayout();
        }
    }

    public void setSectionLabel(int secionIdx, String label) {
        if (!TextUtils.isEmpty(label)) {
            mSectionLabels.put(secionIdx, label);
        } else {
            mSectionLabels.remove(secionIdx);
        }
    }

    public void setInnerCirclePct(int pct) {
        int newPct = Math.max(Math.min(pct, 100), 0);
        if (mInnerCirclePct != newPct) {
            mInnerCirclePct = newPct;
            invalidate();
            requestLayout();
        }
    }

    public void setOnSectionTouchListner(OnSectionTouchListener listener) {
        mSectionTouchListener = listener;
    }
}
