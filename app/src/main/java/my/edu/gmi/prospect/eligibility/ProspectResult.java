package my.edu.gmi.prospect.eligibility;

import java.io.Serializable;

/** The prospect's examination results, passed from the checker screen to the result screen. */
public class ProspectResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final Qualification qualification;
    private final boolean hasSkm3;
    private final GradeLevel[] grades = new GradeLevel[Subject.values().length];

    public ProspectResult(String name, Qualification qualification, boolean hasSkm3) {
        this.name = name == null ? "" : name.trim();
        this.qualification = qualification;
        this.hasSkm3 = hasSkm3 && qualification == Qualification.SPM;
        java.util.Arrays.fill(grades, GradeLevel.NOT_TAKEN);
    }

    public void setGrade(Subject subject, GradeLevel level) {
        grades[subject.ordinal()] = level;
    }

    public GradeLevel getGrade(Subject subject) {
        return grades[subject.ordinal()];
    }

    public boolean hasCredit(Subject s) { return getGrade(s).isAtLeast(GradeLevel.CREDIT); }
    public boolean hasPass(Subject s) { return getGrade(s).isAtLeast(GradeLevel.PASS); }
    public boolean hasDistinction(Subject s) { return getGrade(s) == GradeLevel.DISTINCTION; }

    public int countCredits() {
        int n = 0;
        for (GradeLevel g : grades) if (g.isAtLeast(GradeLevel.CREDIT)) n++;
        return n;
    }

    /** Passes that are not credits (grade D/E for SPM). */
    public int countPassesOnly() {
        int n = 0;
        for (GradeLevel g : grades) if (g == GradeLevel.PASS) n++;
        return n;
    }

    public boolean hasCreditInScienceOrTechnical() {
        for (Subject s : Subject.values()) {
            if (s.isScienceOrTechnical() && hasCredit(s)) return true;
        }
        return false;
    }

    public String getName() { return name; }
    public Qualification getQualification() { return qualification; }
    public boolean hasSkm3() { return hasSkm3; }
}
