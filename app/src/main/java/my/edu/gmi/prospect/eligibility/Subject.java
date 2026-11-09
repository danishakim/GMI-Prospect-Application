package my.edu.gmi.prospect.eligibility;

import androidx.annotation.StringRes;

import my.edu.gmi.prospect.R;

/** Subjects shown in the Eligibility Checker, grouped as they appear on screen. */
public enum Subject {
    BM(R.string.subj_bm, Group.CORE),
    ENGLISH(R.string.subj_english, Group.CORE),
    MATH(R.string.subj_math, Group.CORE),
    ADD_MATH(R.string.subj_addmath, Group.SCIENCE),
    PHYSICS(R.string.subj_physics, Group.SCIENCE),
    CHEMISTRY(R.string.subj_chemistry, Group.SCIENCE),
    BIOLOGY(R.string.subj_biology, Group.SCIENCE),
    SCIENCE(R.string.subj_science, Group.SCIENCE),
    TECHNICAL(R.string.subj_technical, Group.SCIENCE),
    OTHER_1(R.string.subj_other1, Group.OTHER),
    OTHER_2(R.string.subj_other2, Group.OTHER),
    OTHER_3(R.string.subj_other3, Group.OTHER);

    public enum Group { CORE, SCIENCE, OTHER }

    @StringRes public final int label;
    public final Group group;

    Subject(int label, Group group) {
        this.label = label;
        this.group = group;
    }

    /** Subjects accepted as "1 Science / Technical subject" for diploma entry. */
    public boolean isScienceOrTechnical() {
        return this == PHYSICS || this == CHEMISTRY || this == BIOLOGY
                || this == SCIENCE || this == TECHNICAL;
    }
}
