package com.bandonleon.musetta.music;

/**
 * Created by dombhuphaibool on 10/22/15.
 */
public enum Interval {
    Invalid(-2, "Invalid", 0),
    Unrecognized(-1, "Unrecognized", 1),

    Unison(0, "Unison", 2),

    DiminishedSecond(0, "Diminished Second", 3),
    MinorSecond(1, "Minor Second", 4),
    MajorSecond(2, "Major Second", 5),
    AugmentedSecond(3, "Augmented Second", 6),

    DiminishedThird(2, "Diminished Third", 7),
    MinorThird(3, "Minor Third", 8),
    MajorThird(4, "Major Third", 9),
    AugmentedThird(5, "Augmented Third", 10),

    DiminishedFourth(4, "Diminished Fourth", 11),
    PerfectFourth(5, "Perfect Fourth", 12),
    AugmentedFourth(6, "Augmented Fourth", 13),

    Tritone(6, "Tritone", 14),

    DiminishedFifth(6, "Diminished Fifth", 15),
    PerfectFifth(7, "Perfect Fifth", 16),
    AugmentedFifth(8, "Augmented Fifth", 17),

    DiminishedSixth(7, "Diminished Sixth", 18),
    MinorSixth(8, "Minor Sixth", 19),
    MajorSixth(9, "Major Sixth", 20),
    AugmentedSixth(10, "Augmented Sixth", 21),

    DiminishedSeventh(9, "Diminished Seventh", 22),
    MinorSeventh(10, "Minor Seventh", 23),
    MajorSeventh(11, "Major Seventh", 24),
    AugmentedSeventh(12, "Augmented Seventh", 25),

    Octave(12, "Octave", 26);

    private final int mSemitone;
    private final String mName;
    private final int mOrdinal;

    Interval(int semitone, String name, int ordinal) {
        mSemitone = semitone;
        mName = name;
        mOrdinal = ordinal;
    }

    public String getName() {
        return mName;
    }

    public int getSemitone() {
        return mSemitone;
    }

    public int getOrdinalValue() {
        return mOrdinal;
    }

    public static Interval[] commonValues() {
        return new Interval[] { Unison, MinorSecond, MajorSecond, MinorThird, MajorThird,
                PerfectFourth, DiminishedFifth, PerfectFifth, MinorSixth, MajorSixth,
                MinorSeventh, MajorSeventh };
    }

    public static Interval fromSemitone(int semitone) {
        Interval interval = Unrecognized;
        switch (semitone) {
            case 0: interval = Unison; break;
            case 1: interval = MinorSecond; break;
            case 2: interval = MajorSecond; break;
            case 3: interval = MinorThird; break;
            case 4: interval = MajorThird; break;
            case 5: interval = PerfectFourth; break;
            case 6: interval = Tritone; break;
            case 7: interval = PerfectFifth; break;
            case 8: interval = MinorSixth; break;
            case 9: interval = MajorSixth; break;
            case 10: interval = MinorSeventh; break;
            case 11: interval = MajorSeventh; break;
            case 12: interval = Octave; break;
            default: interval = Unrecognized; break;
        }
        return interval;
    }

    public static Interval fromOrdinal(int ordinal) {
        Interval interval = Invalid;
        switch (ordinal) {
            case 0:  interval = Invalid;            break;
            case 1:  interval = Unrecognized;       break;
            case 2:  interval = Unison;             break;
            case 3:  interval = DiminishedSecond;   break;
            case 4:  interval = MinorSecond;        break;
            case 5:  interval = MajorSecond;        break;
            case 6:  interval = AugmentedSecond;    break;
            case 7:  interval = DiminishedThird;    break;
            case 8:  interval = MinorThird;         break;
            case 9:  interval = MajorThird;         break;
            case 10: interval = AugmentedThird;     break;
            case 11: interval = DiminishedFourth;   break;
            case 12: interval = PerfectFourth;      break;
            case 13: interval = AugmentedFourth;    break;
            case 14: interval = Tritone;            break;
            case 15: interval = DiminishedFifth;    break;
            case 16: interval = PerfectFifth;       break;
            case 17: interval = AugmentedFifth;     break;
            case 18: interval = DiminishedSixth;    break;
            case 19: interval = MinorSixth;         break;
            case 20: interval = MajorSixth;         break;
            case 21: interval = AugmentedSixth;     break;
            case 22: interval = DiminishedSeventh;  break;
            case 23: interval = MinorSeventh;       break;
            case 24: interval = MajorSeventh;       break;
            case 25: interval = AugmentedSeventh;   break;
            case 26: interval = Octave;             break;
            default: interval = Invalid;            break;
        }
        return interval;
    }

    public static Interval raiseSemitone(Interval interval) {
        Interval raisedInterval = Invalid;
        switch (interval) {
            case Invalid:
            case Unrecognized:
            case Unison:
            case AugmentedSecond:
            case AugmentedThird:
            case AugmentedFourth:
            case Tritone:
            case AugmentedFifth:
            case AugmentedSixth:
            case AugmentedSeventh:
            case Octave:
                raisedInterval = Invalid;
                break;

            default:
                raisedInterval = fromOrdinal(interval.getOrdinalValue() + 1);
                break;
        }
        return raisedInterval;
    }

    public static Interval lowerSemitone(Interval interval) {
        Interval loweredInterval = Invalid;
        switch (interval) {
            case Invalid:
            case Unrecognized:
            case Unison:
            case DiminishedSecond:
            case DiminishedThird:
            case DiminishedFourth:
            case Tritone:
            case DiminishedFifth:
            case DiminishedSixth:
            case DiminishedSeventh:
            case Octave:
                loweredInterval = Invalid;
                break;

            default:
                loweredInterval = fromOrdinal(interval.getOrdinalValue() - 1);
                break;
        }
        return loweredInterval;
    }
}
