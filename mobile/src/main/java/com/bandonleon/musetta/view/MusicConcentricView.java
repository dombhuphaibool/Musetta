package com.bandonleon.musetta.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.music.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dombhuphaibool on 10/15/15.
 */
public class MusicConcentricView extends ConcentricView {

    public interface OnNoteSelectedListener {
        void onNoteSelected(Note note);
    }

    // This should map to xml attribute note of MusicConcentricView in attrs.xml
    public enum NoteItem {
        A(0, Note.A, R.color.note_white),
        B(1, Note.B, R.color.note_yellow),
        C(2, Note.C, R.color.note_brown),
        D(3, Note.D, R.color.note_blue),
        E(4, Note.E, R.color.note_purple),
        F(5, Note.F, R.color.note_red),
        G(6, Note.G, R.color.note_green);

        private final int mValue;
        private final Note mNote;
        private final @ColorRes int mColor;
        NoteItem(int value, Note note, @ColorRes int color) {
            mValue = value;
            mNote = note;
            mColor = color;

        }

        public static NoteItem fromIntValue(int value) {
            NoteItem noteItem = A;
            switch (value) {
                case 0: noteItem = A; break;
                case 1: noteItem = B; break;
                case 2: noteItem = C; break;
                case 3: noteItem = D; break;
                case 4: noteItem = E; break;
                case 5: noteItem = F; break;
                case 6: noteItem = G; break;
                default:
            }
            return noteItem;
        }

        public static NoteItem fromNote(Note note) {
            NoteItem noteItem = A;
            switch (Note.naturalize(note)) {
                case A: noteItem = A; break;
                case B: noteItem = B; break;
                case C: noteItem = C; break;
                case D: noteItem = D; break;
                case E: noteItem = E; break;
                case F: noteItem = F; break;
                case G: noteItem = G; break;
                default:
            }
            return noteItem;
        }

        public int intValue() {
            return mValue;
        }

        public @ColorRes int color() {
            return mColor;
        }

        public String label() {
            return mNote.getName();
        }
    }

    private static final NoteItem DEFAULT_NOTE_ITEM = NoteItem.D;
    private static final int NUM_NOTE_ITEMS = NoteItem.values().length;

    private static final float DEFAULT_ACCIDENTAL_RADIUS = 120.0f;
    private static final float ACCIDENTAL_X_OFFSET = 200.0f;
    private static final float ACCIDENTAL_Y_OFFSET = 120.0f;
    private static final float DEFAULT_LABEL_TEXT_SIZE = 60.0f;

    private OnNoteSelectedListener mNoteSelectedListener;

    private Note mCurrentNote;
    private List<NoteItem> mOtherNoteItems;
    private Paint mAccidentalsPaint;
    private Paint mLabelPaint;

    private PointF mFlatCenter;
    private PointF mSharpCenter;
    private float mAccidentalsRadius;
    private boolean mFlatSelected;
    private boolean mSharpSelected;
    private int mSectionSelected;

    private PointF mHorizontalFlatPos;
    private PointF mHorizontalSharpPos;
    private PointF mVerticalFlatPos;
    private PointF mVerticalSharpPos;

    private boolean mTrackTouch;

    public MusicConcentricView(Context context) {
        super(context);
        init(context);
        setCurrentNote(DEFAULT_NOTE_ITEM.mNote);
        setNumSections(NUM_NOTE_ITEMS - 1);  // 1 note is in the center
    }

    public MusicConcentricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /*
     * NoteItem that attr numSections of ConcentricView will be ignored and will always be set
     * to NUM_NOTES (7) for MusicConcentricView
     */
    public MusicConcentricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        NoteItem currentNote = DEFAULT_NOTE_ITEM;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MusicConcentricView, 0, 0);

        try {
            currentNote = NoteItem.fromIntValue(a.getInteger(R.styleable.MusicConcentricView_note,
                    DEFAULT_NOTE_ITEM.intValue()));
        } finally {
            a.recycle();
        }

        setCurrentNote(currentNote.mNote);
        // Override number of sections to be equal to the number of notes
        setNumSections(NUM_NOTE_ITEMS - 1);  // 1 note is in the center
    }

    private void init(Context context) {
        mAccidentalsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAccidentalsPaint.setColor(Color.GRAY);

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setColor(Color.BLACK);
        mLabelPaint.setTextSize(DEFAULT_LABEL_TEXT_SIZE);

        mAccidentalsRadius = DEFAULT_ACCIDENTAL_RADIUS;
        mFlatCenter = new PointF();
        mSharpCenter = new PointF();

        mFlatSelected = false;
        mSharpSelected = false;
        mSectionSelected = NO_SECTION_INDEX;

        mHorizontalFlatPos = new PointF();
        mHorizontalSharpPos = new PointF();
        mVerticalFlatPos = new PointF();
        mVerticalSharpPos = new PointF();
    }

    @Override
    protected @ColorInt int getCenterColor() {
        return getResources().getColor(NoteItem.fromNote(mCurrentNote).color());
    }

    @Override
    protected @ColorInt int getSectionColor(int sectionIdx) {
        return getResources().getColor(mOtherNoteItems.get(sectionIdx).color());
    }

    @Override
    protected String getCenterLabel() {
        return mCurrentNote.getName();
    }

    @Override
    protected String getSectionLabel(int sectionIdx) {
        return mOtherNoteItems.get(sectionIdx).label();
    }

    public void setCurrentNote(Note note) {
        if (mOtherNoteItems == null) {
            mOtherNoteItems = new ArrayList<>();
        } else {
            mOtherNoteItems.clear();
        }

        NoteItem[] noteItems = NoteItem.values();
        for (NoteItem item : noteItems) {
            if (item.mNote != Note.naturalize(note)) {
                mOtherNoteItems.add(item);
            }
        }
        Random rand = new Random();
        NoteItem tmpNoteItem;
        int randIndex = 0;
        int numSwitches = mOtherNoteItems.size() - 1;
        while (numSwitches > 0) {
            // Allow the probability of choosing the current index as the position
            // and only switch if we need to
            randIndex = rand.nextInt(numSwitches + 1);
            if (randIndex != numSwitches) {
                tmpNoteItem = mOtherNoteItems.get(numSwitches);
                mOtherNoteItems.set(numSwitches, mOtherNoteItems.get(randIndex));
                mOtherNoteItems.set(randIndex, tmpNoteItem);
            }
            --numSwitches;
        }
        mCurrentNote = note;
        invalidate();
    }

    @Override
    protected boolean onSectionTouched(int sectionIdx) {
        boolean outerSectionSelected = false;
        if (sectionIdx == 1 || sectionIdx == 4) {
            mFlatCenter.set(mHorizontalFlatPos.x, mHorizontalFlatPos.y);
            mSharpCenter.set(mHorizontalSharpPos.x, mHorizontalSharpPos.y);
            outerSectionSelected = true;
        } else if (sectionIdx != CENTER_SECTION_INDEX) {
            mFlatCenter.set(mVerticalFlatPos.x, mVerticalFlatPos.y);
            mSharpCenter.set(mVerticalSharpPos.x, mVerticalSharpPos.y);
            outerSectionSelected = true;
        }

        if (outerSectionSelected) {
            mTrackTouch = true;
            invalidate();
        }

        mSectionSelected = sectionIdx;

        return super.onSectionTouched(sectionIdx);
    }

    /*
    @Override
    protected boolean onTouchDown(MotionEvent event) {
        boolean handled = super.onTouchDown(event);

        PointF downPos = getTouchTracker().getLastDownPos();
        mFlatCenter.set(downPos.x - ACCIDENTAL_X_OFFSET, downPos.y - ACCIDENTAL_Y_OFFSET);
        mSharpCenter.set(downPos.x + ACCIDENTAL_X_OFFSET, downPos.y - ACCIDENTAL_Y_OFFSET);

        if (handled) {
            invalidate();
        }
        return handled;
    }
    */

    @Override
    protected void onTouchMove(MotionEvent event) {
        super.onTouchMove(event);

        if (mTrackTouch) {
            float radiusSqrd = mAccidentalsRadius * mAccidentalsRadius;

            float flatVectorX = event.getX() - mFlatCenter.x;
            float flatVectorY = event.getY() - mFlatCenter.y;
            boolean flatSelected = ((flatVectorX * flatVectorX) + (flatVectorY * flatVectorY) < radiusSqrd);

            float sharpVectorX = event.getX() - mSharpCenter.x;
            float sharpVectorY = event.getY() - mSharpCenter.y;
            boolean sharpSelected = ((sharpVectorX * sharpVectorX) + (sharpVectorY * sharpVectorY) < radiusSqrd);

            boolean performInvalidate = (flatSelected != mFlatSelected || sharpSelected != mSharpSelected);
            mFlatSelected = flatSelected;
            mSharpSelected = sharpSelected;
            if (performInvalidate) {
                invalidate();
            }
        }
    }

    @Override
    protected void onTouchUp(MotionEvent event) {
        super.onTouchUp(event);

        if (mNoteSelectedListener != null) {
            Note noteSelected = mSectionSelected == CENTER_SECTION_INDEX ?
                    mCurrentNote : mOtherNoteItems.get(mSectionSelected).mNote;
            if (mFlatSelected) {
                noteSelected = Note.flatten(noteSelected);
            } else if (mSharpSelected) {
                noteSelected = Note.sharpen(noteSelected);
            }
            mNoteSelectedListener.onNoteSelected(noteSelected);
        }

        mFlatSelected = false;
        mSharpSelected = false;
        mSectionSelected = NO_SECTION_INDEX;
        mTrackTouch = false;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int centerX = w / 2;
        int centerY = h / 2;

        mHorizontalFlatPos.set(centerX - ACCIDENTAL_X_OFFSET, centerY);
        mHorizontalSharpPos.set(centerX + ACCIDENTAL_X_OFFSET, centerY);
        mVerticalFlatPos.set(centerX, centerY - ACCIDENTAL_Y_OFFSET);
        mVerticalSharpPos.set(centerX, centerY + ACCIDENTAL_X_OFFSET);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TouchTracker tracker = getTouchTracker();
        if (tracker.isTrackingTouchMove()) {
            @ColorInt int strokeColor = getResources().getColor(R.color.dark_grey);
            float strokeWidth = 5.0f;

            mAccidentalsPaint.setStyle(Paint.Style.FILL);
            mAccidentalsPaint.setColor(mFlatSelected ? Color.GREEN : Color.GRAY);
            canvas.drawCircle(mFlatCenter.x, mFlatCenter.y, mAccidentalsRadius, mAccidentalsPaint);

            mAccidentalsPaint.setColor(mSharpSelected ? Color.GREEN : Color.GRAY);
            canvas.drawCircle(mSharpCenter.x, mSharpCenter.y, mAccidentalsRadius, mAccidentalsPaint);

            mAccidentalsPaint.setStyle(Paint.Style.STROKE);
            mAccidentalsPaint.setStrokeWidth(strokeWidth);
            mAccidentalsPaint.setColor(strokeColor);
            canvas.drawCircle(mFlatCenter.x, mFlatCenter.y, mAccidentalsRadius, mAccidentalsPaint);
            canvas.drawCircle(mSharpCenter.x, mSharpCenter.y, mAccidentalsRadius, mAccidentalsPaint);

            drawCenteredText(canvas, "♭", mFlatCenter.x, mFlatCenter.y, mLabelPaint);
            drawCenteredText(canvas, "♯", mSharpCenter.x, mSharpCenter.y, mLabelPaint);
        }
    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener listener) {
        mNoteSelectedListener = listener;
    }
}
