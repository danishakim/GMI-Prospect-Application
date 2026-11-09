package my.edu.gmi.prospect.eligibility;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;

import my.edu.gmi.prospect.R;

/**
 * Supported entry qualifications. Each one has its own grade scale; the index of a grade
 * in the array resource is mapped to a {@link GradeLevel}.
 */
public enum Qualification {
    SPM(R.string.qual_spm, R.array.grades_spm,
            // Not taken, A+, A, A-, B+, B, C+, C, D, E, G
            new GradeLevel[]{GradeLevel.NOT_TAKEN, GradeLevel.DISTINCTION, GradeLevel.DISTINCTION,
                    GradeLevel.DISTINCTION, GradeLevel.CREDIT, GradeLevel.CREDIT, GradeLevel.CREDIT,
                    GradeLevel.CREDIT, GradeLevel.PASS, GradeLevel.PASS, GradeLevel.FAIL}),
    IGCSE(R.string.qual_igcse, R.array.grades_igcse,
            // Not taken, A*, A, B, C, D, E, F, G, U
            new GradeLevel[]{GradeLevel.NOT_TAKEN, GradeLevel.DISTINCTION, GradeLevel.DISTINCTION,
                    GradeLevel.CREDIT, GradeLevel.CREDIT, GradeLevel.PASS, GradeLevel.PASS,
                    GradeLevel.PASS, GradeLevel.PASS, GradeLevel.FAIL});

    @StringRes public final int label;
    @ArrayRes public final int gradesArray;
    private final GradeLevel[] levels;

    Qualification(int label, int gradesArray, GradeLevel[] levels) {
        this.label = label;
        this.gradesArray = gradesArray;
        this.levels = levels;
    }

    public GradeLevel levelOf(int gradeIndex) {
        if (gradeIndex < 0 || gradeIndex >= levels.length) return GradeLevel.NOT_TAKEN;
        return levels[gradeIndex];
    }
}
