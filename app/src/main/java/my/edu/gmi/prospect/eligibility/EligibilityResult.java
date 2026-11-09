package my.edu.gmi.prospect.eligibility;

import java.util.Collections;
import java.util.List;

import my.edu.gmi.prospect.model.Course;

/** Outcome of checking one course against a prospect's results. */
public class EligibilityResult {
    private final Course course;
    private final boolean eligible;
    private final boolean viaSkm;
    private final List<Integer> unmetReasons; // string resource ids

    public EligibilityResult(Course course, boolean eligible, boolean viaSkm, List<Integer> unmetReasons) {
        this.course = course;
        this.eligible = eligible;
        this.viaSkm = viaSkm;
        this.unmetReasons = Collections.unmodifiableList(unmetReasons);
    }

    public Course getCourse() { return course; }
    public boolean isEligible() { return eligible; }
    public boolean isViaSkm() { return viaSkm; }
    public List<Integer> getUnmetReasons() { return unmetReasons; }
}
