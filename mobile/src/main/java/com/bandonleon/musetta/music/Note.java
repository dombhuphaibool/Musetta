package com.bandonleon.musetta.music;

/**
 * Created by dombhuphaibool on 10/22/15.
 */
// Sharp in unicode is \u266F
// Flat in unicode is \u266D
// Double-Sharp in unicode is \uD834\uDD2A
// Double-Flat in unicode is \uD834\uDD2B
public enum Note {
    Invalid(-1, "Invalid", "Invalid", 0),

    C_Flat(11, "C♭", "Do♭", 1),
    C(0, "C", "Do", 2),
    C_Sharp(1, "C♯", "Do♯", 3),
    C_DoubleSharp(2, "C\uD834\uDD2A", "Do\uD834\uDD2A", 4),

    D_DoubleFlat(0, "D\uD834\uDD2B", "Re\uD834\uDD2B", 5),
    D_Flat(1, "D♭", "Re♭", 6),
    D(2, "D", "Re", 7),
    D_Sharp(3, "D♯", "Re♯", 8),
    D_DoubleSharp(4, "D\uD834\uDD2A", "Re\uD834\uDD2A", 9),

    E_DoubleFlat(2, "E\uD834\uDD2B", "Mi\uD834\uDD2B", 10),
    E_Flat(3, "E♭", "Mi♭", 11),
    E(4, "E", "Mi", 12),
    E_Sharp(5, "E♯", "Mi♯", 13),

    F_Flat(4, "F♭", "Fa♭", 14),
    F(5, "F", "Fa", 15),
    F_Sharp(6, "F♯", "Fa♯", 16),
    F_DoubleSharp(7, "F\uD834\uDD2A", "Fa\uD834\uDD2A", 17),

    G_DoubleFlat(5, "G\uD834\uDD2B", "Sol\uD834\uDD2B", 18),
    G_Flat(6, "G♭", "Sol♭", 19),
    G(7, "G", "Sol", 20),
    G_Sharp(8, "G♯", "Sol♯", 21),
    G_DoubleSharp(9, "G\uD834\uDD2A", "Sol\uD834\uDD2A", 22),

    A_DoubleFlat(7, "A\uD834\uDD2B", "La\uD834\uDD2B", 23),
    A_Flat(8, "A♭", "La♭", 24),
    A(9, "A", "La", 25),
    A_Sharp(10, "A♯", "La♯", 26),
    A_DoubleSharp(11, "A\uD834\uDD2A", "La\uD834\uDD2A", 27),

    B_DoubleFlat(9, "B\uD834\uDD2B", "Si\uD834\uDD2B", 28),
    B_Flat(10, "B♭", "Si♭", 29),
    B(11, "B", "Si", 30),
    B_Sharp(0, "B♯", "Si♯", 31);

    private static NoteMode sMode = NoteMode.Letter;
    public static void setMode(NoteMode mode) {
        sMode = mode;
    }
    public static NoteMode getMode() {
        return sMode;
    }

    private final int mPitch;
    private final String mLetterName;
    private final String mSolfegeName;
    private final int mOrdinal;

    Note(int pitch, String letterName, String solfegeName, int oridnal) {
        mPitch = pitch;
        mLetterName = letterName;
        mSolfegeName = solfegeName;
        mOrdinal = oridnal;
    }

    public int getPitch() {
        return mPitch;
    }

    public String getName() {
        return sMode == NoteMode.Letter ? mLetterName : mSolfegeName;
    }

    public int getOrdinalValue() {
        return mOrdinal;
    }

    public static Note fromOrdinal(int ordinal) {
        Note note = Invalid;
        switch (ordinal) {
            case 0:  note = Invalid;         break;
            case 1:  note = C_Flat;          break;
            case 2:  note = C;               break;
            case 3:  note = C_Sharp;         break;
            case 4:  note = C_DoubleSharp;   break;
            case 5:  note = D_DoubleFlat;    break;
            case 6:  note = D_Flat;          break;
            case 7:  note = D;               break;
            case 8:  note = D_Sharp;         break;
            case 9:  note = D_DoubleSharp;   break;
            case 10: note = E_DoubleFlat;    break;
            case 11: note = E_Flat;          break;
            case 12: note = E;               break;
            case 13: note = E_Sharp;         break;
            case 14: note = F_Flat;          break;
            case 15: note = F;               break;
            case 16: note = F_Sharp;         break;
            case 17: note = F_DoubleSharp;   break;
            case 18: note = G_DoubleFlat;    break;
            case 19: note = G_Flat;          break;
            case 20: note = G;               break;
            case 21: note = G_Sharp;         break;
            case 22: note = G_DoubleSharp;   break;
            case 23: note = A_DoubleFlat;    break;
            case 24: note = A_Flat;          break;
            case 25: note = A;               break;
            case 26: note = A_Sharp;         break;
            case 27: note = A_DoubleSharp;   break;
            case 28: note = B_DoubleFlat;    break;
            case 29: note = B_Flat;          break;
            case 30: note = B;               break;
            case 31: note = B_Sharp;         break;
            default: note = Invalid;         break;
        }
        return note;
    }

    public static Note sharpen(Note note) {
        Note sharpenNote = Invalid;
        switch (note) {
            case Invalid:
            case C_DoubleSharp:
            case D_DoubleSharp:
            case E_Sharp:
            case F_DoubleSharp:
            case G_DoubleSharp:
            case A_DoubleSharp:
            case B_Sharp:
                sharpenNote = Invalid;
                break;
            default:
                sharpenNote = fromOrdinal(note.getOrdinalValue() + 1);
        }
        return sharpenNote;
    }

    public static Note flatten(Note note) {
        Note flattenNote = Invalid;
        switch (note) {
            case Invalid:
            case C_Flat:
            case D_DoubleFlat:
            case E_DoubleFlat:
            case F_Flat:
            case G_DoubleFlat:
            case A_DoubleFlat:
            case B_DoubleFlat:
                flattenNote = Invalid;
                break;
            default:
                flattenNote = fromOrdinal(note.getOrdinalValue() - 1);
        }
        return flattenNote;
    }

    public static Note naturalize(Note note) {
        Note naturalizedNote = Invalid;
        switch (note) {
            case C_Flat:
            case C:
            case C_Sharp:
            case C_DoubleSharp:
                naturalizedNote = C;
                break;

            case D_DoubleFlat:
            case D_Flat:
            case D:
            case D_Sharp:
            case D_DoubleSharp:
                naturalizedNote = D;
                break;

            case E_DoubleFlat:
            case E_Flat:
            case E:
            case E_Sharp:
                naturalizedNote = E;
                break;

            case F_Flat:
            case F:
            case F_Sharp:
            case F_DoubleSharp:
                naturalizedNote = F;
                break;

            case G_DoubleFlat:
            case G_Flat:
            case G:
            case G_Sharp:
            case G_DoubleSharp:
                naturalizedNote = G;
                break;

            case A_DoubleFlat:
            case A_Flat:
            case A:
            case A_Sharp:
            case A_DoubleSharp:
                naturalizedNote = A;
                break;

            case B_DoubleFlat:
            case B_Flat:
            case B:
            case B_Sharp:
                naturalizedNote = B;
                break;

            default:
                naturalizedNote = Invalid;
                break;
        }
        return naturalizedNote;
    }

    private Interval adjustInterval(Interval baseInterval, int fromSemitone, int toSemitone) {
        Interval adjustedInterval = baseInterval;
        if (fromSemitone < toSemitone) {
            while (fromSemitone < toSemitone && adjustedInterval != Interval.Invalid) {
                adjustedInterval = Interval.raiseSemitone(adjustedInterval);
                ++fromSemitone;
            }
        } else if (fromSemitone > toSemitone) {
            while (fromSemitone > toSemitone && adjustedInterval != Interval.Invalid) {
                adjustedInterval = Interval.lowerSemitone(adjustedInterval);
                --fromSemitone;
            }
        }
        return adjustedInterval;
    }

    public Interval intervalFrom(Note noteBelow) {
        Interval baseInterval = naturalize(this).pitchIntervalFrom(naturalize(noteBelow));
        // For natural notes, tritone only occurs for B & F
        if (baseInterval == Interval.Tritone) {
            baseInterval = naturalize(noteBelow) == F ? Interval.AugmentedFourth : Interval.DiminishedFifth;
        }
        int baseSemitoneDiff = baseInterval.getSemitone();
        int realSemitoneDiff = pitchIntervalFrom(noteBelow).getSemitone();
        return adjustInterval(baseInterval, baseSemitoneDiff, realSemitoneDiff);
    }

    public Interval intervalTo(Note noteAbove) {
        Interval baseInterval = naturalize(this).pitchIntervalTo(naturalize(noteAbove));
        // For natural notes, tritone only occurs for B & F
        if (baseInterval == Interval.Tritone) {
            baseInterval = naturalize(noteAbove) == B ? Interval.AugmentedFourth : Interval.DiminishedFifth;
        }
        int baseSemitoneDiff = baseInterval.getSemitone();
        int realSemitoneDiff = pitchIntervalTo(noteAbove).getSemitone();
        return adjustInterval(baseInterval, baseSemitoneDiff, realSemitoneDiff);
    }

    // Calculate the interval just from converting notes to pitches
    public Interval pitchIntervalFrom(Note noteBelow) {
        int myPitch = getPitch();
        int pitchBelow = noteBelow.getPitch();
        if (myPitch < pitchBelow || (myPitch == pitchBelow && this != noteBelow)) {
            myPitch += 12;
        }
        return Interval.fromSemitone(myPitch - pitchBelow);
    }

    // Calculate the interval just from converting notes to pitches
    public Interval pitchIntervalTo(Note noteAbove) {
        int myPitch = getPitch();
        int pitchAbove = noteAbove.getPitch();
        if (myPitch > pitchAbove || (myPitch == pitchAbove && this != noteAbove)) {
            pitchAbove += 12;
        }
        return Interval.fromSemitone(pitchAbove - myPitch);
    }

    public static Note[] naturals() {
        return new Note[] { C, D, E, F, G, A, B };
    }

    public static Note[] sharps() {
        return new Note[] { C_Sharp, D_Sharp, F_Sharp, G_Sharp, A_Sharp };
    }

    public static Note[] flats() {
        return new Note[] { D_Flat, E_Flat, G_Flat, A_Flat, B_Flat };
    }

    public static Note[] allSharps() {
        return new Note[] { C_Sharp, D_Sharp, E_Sharp, F_Sharp, G_Sharp, A_Sharp, B_Sharp };
    }

    public static Note[] allFlats() {
        return new Note[] { C_Flat, D_Flat, E_Flat, F_Flat, G_Flat, A_Flat, B_Flat };
    }

    public static Note[] doubleSharps() {
        return new Note[] { C_DoubleSharp, D_DoubleSharp, F_DoubleSharp, G_DoubleSharp, A_DoubleSharp };
    }

    public static Note[] doubleFlats() {
        return new Note[] { D_DoubleFlat, E_DoubleFlat, G_DoubleFlat, A_DoubleFlat, B_DoubleFlat };
    }
}
