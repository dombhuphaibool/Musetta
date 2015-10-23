package com.bandonleon.musetta.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by dombhuphaibool on 10/17/15.
 */
public class TouchTracker {
    public interface OnTouchListener {
        boolean onDown(MotionEvent event);
        void onMove(MotionEvent event);
        void onUp(MotionEvent event);
    }

    private boolean mTrackMove;
    private GestureDetectorCompat mGestureDetector;
    private OnTouchListener mListener;

    private PointF mDownPos;
    private PointF mMovePos;
    private PointF mUpPos;

    public TouchTracker(Context context) {
        mTrackMove = false;
        mDownPos = new PointF();
        mMovePos = new PointF();
        mUpPos = new PointF();
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                mDownPos.set(event.getX(), event.getY());
                if (mListener != null) {
                    mTrackMove = mListener.onDown(event);
                }
                return mTrackMove;
            }
        });
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mListener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mTrackMove) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_MOVE) {
                mMovePos.set(event.getX(), event.getY());
                if (mListener != null) {
                    mListener.onMove(event);
                }
            } else if (action == MotionEvent.ACTION_UP) {
                mUpPos.set(event.getX(), event.getY());
                if (mListener != null) {
                    mListener.onUp(event);
                }
                mTrackMove = false;
            }
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public boolean isTrackingTouchMove() {
        return mTrackMove;
    }

    // NoteItem: In order to optimize, we do not create a copy of the point. If the caller
    // changes the coordinates of the point, it will be saved by TouchTracker.
    public PointF getLastDownPos() {
        return mDownPos;
    }

    public PointF getLastMovePos() {
        return mMovePos;
    }

    public PointF getLastUpPos() {
        return mUpPos;
    }
}
