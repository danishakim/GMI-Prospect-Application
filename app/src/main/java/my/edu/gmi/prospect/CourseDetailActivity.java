package my.edu.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import my.edu.gmi.prospect.data.CourseRepository;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.util.IntentUtils;
import my.edu.gmi.prospect.util.UiUtils;

/** Full information about one programme, with Check Eligibility, Apply and Share actions. */
public class CourseDetailActivity extends BaseActivity {

    public static final String EXTRA_COURSE_ID = "extra_course_id";
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setupToolbar(R.string.title_course_detail);

        course = CourseRepository.getInstance(this).findById(getIntent().getStringExtra(EXTRA_COURSE_ID));
        if (course == null) {
            finish();
            return;
        }

        TextView badge = findViewById(R.id.txt_category);
        UiUtils.styleCategoryBadge(badge, course.getCategory());
        ((TextView) findViewById(R.id.txt_name)).setText(course.getName());
        ((TextView) findViewById(R.id.txt_department)).setText(course.getDepartment());
        ((TextView) findViewById(R.id.txt_duration)).setText(course.getDuration());
        ((TextView) findViewById(R.id.txt_intake)).setText(course.getIntake());
        ((TextView) findViewById(R.id.txt_mode)).setText(course.getMode());
        ((TextView) findViewById(R.id.txt_overview)).setText(course.getOverview());
        ((TextView) findViewById(R.id.txt_requirements)).setText(toBullets(course.getRequirements()));
        ((TextView) findViewById(R.id.txt_careers)).setText(toBullets(course.getCareers()));

        findViewById(R.id.btn_check).setOnClickListener(v ->
                startActivity(new Intent(this, EligibilityActivity.class)));
        findViewById(R.id.btn_apply).setOnClickListener(v -> IntentUtils.openOnlineApplication(this));
    }

    private String toBullets(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append('\n');
            sb.append(getString(R.string.bullet_item, items.get(i)));
        }
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            String text = getString(R.string.share_course_text, course.getName(),
                    course.getDuration(), getString(R.string.oa_url));
            IntentUtils.shareText(this, getString(R.string.share_via), text);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
