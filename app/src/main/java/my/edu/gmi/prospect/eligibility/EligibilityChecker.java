package my.edu.gmi.prospect.eligibility;

import java.util.ArrayList;
import java.util.List;

import my.edu.gmi.prospect.R;
import my.edu.gmi.prospect.model.Course;

/**
 * Rule engine that matches a prospect's SPM or O-Level/IGCSE results with the entry
 * requirements of every GMI programme. Pure Java (no Android views) so it is easy to test.
 */
public final class EligibilityChecker {

    private static final int MIN_DIPLOMA_CREDITS = 3;
    private static final int MIN_GUFP_IGCSE_CREDITS = 5;
    private static final int MIN_SKM_SPM_CREDITS = 1;

    private EligibilityChecker() { }

    /** Checks every course and returns the results with eligible programmes first. */
    public static List<EligibilityResult> checkAll(ProspectResult p, List<Course> courses) {
        List<EligibilityResult> eligible = new ArrayList<>();
        List<EligibilityResult> notEligible = new ArrayList<>();
        for (Course c : courses) {
            EligibilityResult r = check(p, c);
            (r.isEligible() ? eligible : notEligible).add(r);
        }
        eligible.addAll(notEligible);
        return eligible;
    }

    public static EligibilityResult check(ProspectResult p, Course course) {
        List<Integer> unmet = new ArrayList<>();
        switch (course.getRule()) {
            case DIP_ENG:
                requireCredits(p, MIN_DIPLOMA_CREDITS, R.string.req_min3_credits, unmet);
                requireCredit(p, Subject.MATH, R.string.req_credit_math, unmet);
                if (!p.hasCreditInScienceOrTechnical()) unmet.add(R.string.req_credit_scitech);
                requirePass(p, Subject.ENGLISH, R.string.req_pass_english, unmet);
                return diplomaResult(p, course, unmet);

            case DIP_COMP:
                requireCredits(p, MIN_DIPLOMA_CREDITS, R.string.req_min3_credits, unmet);
                requireCredit(p, Subject.MATH, R.string.req_credit_math, unmet);
                requirePass(p, Subject.ENGLISH, R.string.req_pass_english, unmet);
                return diplomaResult(p, course, unmet);

            case DIP_CREATIVE:
                requireCredits(p, MIN_DIPLOMA_CREDITS, R.string.req_min3_credits, unmet);
                requirePass(p, Subject.ENGLISH, R.string.req_pass_english, unmet);
                return diplomaResult(p, course, unmet);

            case GAPP:
                requireCredit(p, Subject.ENGLISH, R.string.req_credit_english, unmet);
                requireCredit(p, Subject.MATH, R.string.req_credit_math, unmet);
                requireCredit(p, Subject.ADD_MATH, R.string.req_credit_addmath, unmet);
                requireCredit(p, Subject.PHYSICS, R.string.req_credit_physics, unmet);
                requireCredit(p, Subject.CHEMISTRY, R.string.req_credit_chemistry, unmet);
                return new EligibilityResult(course, unmet.isEmpty(), false, unmet);

            case GUFP:
            default:
                if (p.getQualification() == Qualification.SPM) {
                    requireCredit(p, Subject.BM, R.string.req_credit_bm, unmet);
                    requireCredit(p, Subject.ENGLISH, R.string.req_credit_english, unmet);
                    requireCredit(p, Subject.MATH, R.string.req_credit_math, unmet);
                    requireCredit(p, Subject.ADD_MATH, R.string.req_credit_addmath, unmet);
                } else {
                    requireCredits(p, MIN_GUFP_IGCSE_CREDITS, R.string.req_min5_credits, unmet);
                    requireCredit(p, Subject.MATH, R.string.req_credit_math, unmet);
                }
                requireCredit(p, Subject.PHYSICS, R.string.req_credit_physics, unmet);
                requireCredit(p, Subject.CHEMISTRY, R.string.req_credit_chemistry, unmet);
                return new EligibilityResult(course, unmet.isEmpty(), false, unmet);
        }
    }

    /** True when every GAPP core subject is grade A, used to show the sponsorship note. */
    public static boolean qualifiesForGappSponsorship(ProspectResult p) {
        return p.hasDistinction(Subject.ENGLISH) && p.hasDistinction(Subject.MATH)
                && p.hasDistinction(Subject.ADD_MATH) && p.hasDistinction(Subject.PHYSICS)
                && p.hasDistinction(Subject.CHEMISTRY);
    }

    /** Diplomas also accept SKM Level 3 holders with at least one SPM credit. */
    private static EligibilityResult diplomaResult(ProspectResult p, Course course, List<Integer> unmet) {
        if (unmet.isEmpty()) return new EligibilityResult(course, true, false, unmet);
        boolean skmRoute = p.hasSkm3() && p.countCredits() >= MIN_SKM_SPM_CREDITS;
        return new EligibilityResult(course, skmRoute, skmRoute, unmet);
    }

    private static void requireCredit(ProspectResult p, Subject s, int reason, List<Integer> unmet) {
        if (!p.hasCredit(s)) unmet.add(reason);
    }

    private static void requirePass(ProspectResult p, Subject s, int reason, List<Integer> unmet) {
        if (!p.hasPass(s)) unmet.add(reason);
    }

    private static void requireCredits(ProspectResult p, int min, int reason, List<Integer> unmet) {
        if (p.countCredits() < min) unmet.add(reason);
    }
}
