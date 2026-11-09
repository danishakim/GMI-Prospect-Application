package my.edu.gmi.prospect.model;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Immutable programme information loaded from assets/courses.json. */
public class Course {
    private final String id;
    private final String name;
    private final Category category;
    private final RuleType rule;
    private final String department;
    private final String duration;
    private final String intake;
    private final String mode;
    private final String overview;
    private final List<String> requirements;
    private final List<String> careers;

    public Course(String id, String name, Category category, RuleType rule, String department,
                  String duration, String intake, String mode, String overview,
                  List<String> requirements, List<String> careers) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.rule = rule;
        this.department = department;
        this.duration = duration;
        this.intake = intake;
        this.mode = mode;
        this.overview = overview;
        this.requirements = Collections.unmodifiableList(requirements);
        this.careers = Collections.unmodifiableList(careers);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public RuleType getRule() { return rule; }
    public String getDepartment() { return department; }
    public String getDuration() { return duration; }
    public String getIntake() { return intake; }
    public String getMode() { return mode; }
    public String getOverview() { return overview; }
    public List<String> getRequirements() { return requirements; }
    public List<String> getCareers() { return careers; }

    /** Case-insensitive search on name and department. */
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) return true;
        String q = query.trim().toLowerCase(Locale.ROOT);
        return name.toLowerCase(Locale.ROOT).contains(q)
                || department.toLowerCase(Locale.ROOT).contains(q);
    }
}
