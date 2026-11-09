package my.edu.gmi.prospect.eligibility;

/** Grade bands shared by every qualification. Order matters: higher ordinal = better. */
public enum GradeLevel {
    NOT_TAKEN, FAIL, PASS, CREDIT, DISTINCTION;

    public boolean isAtLeast(GradeLevel other) {
        return ordinal() >= other.ordinal();
    }
}
