package my.edu.gmi.prospect.model;

/** Which entry-requirement rule a programme uses (see EligibilityChecker). */
public enum RuleType {
    /** 3 credits incl. Mathematics + 1 Science/Technical, pass English (or SKM 3). */
    DIP_ENG,
    /** 3 credits incl. Mathematics, pass English (or SKM 3). */
    DIP_COMP,
    /** 3 credits in any subject, pass English (or SKM 3). */
    DIP_CREATIVE,
    /** Credit in English, Maths, Add Maths, Physics, Chemistry. */
    GAPP,
    /** SPM: credit in BM, English, Maths, Add Maths, Physics, Chemistry. IGCSE: 5 credits incl. Maths, Physics, Chemistry. */
    GUFP
}
