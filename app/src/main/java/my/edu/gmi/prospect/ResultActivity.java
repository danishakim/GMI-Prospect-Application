package my.edu.gmi.prospect;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

import my.edu.gmi.prospect.adapter.ResultAdapter;
import my.edu.gmi.prospect.data.CourseRepository;
import my.edu.gmi.prospect.eligibility.EligibilityChecker;
import my.edu.gmi.prospect.eligibility.EligibilityResult;
import my.edu.gmi.prospect.eligibility.ProspectResult;
import my.edu.gmi.prospect.model.Category;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.util.IntentUtils;
import my.edu.gmi.prospect.util.UiUtils;

/** Shows which GMI programmes the prospect qualifies for and links to online application. */
public class ResultActivity extends BaseActivity {

    public static final String EXTRA_RESULT = "extra_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setupToolbar(R.string.title_result);

        ProspectResult prospect = readProspect();
        if (prospect == null) {
            finish();
            return;
        }

        List<Course> courses = CourseRepository.getInstance(this).getAll();
        List<EligibilityResult> results = EligibilityChecker.checkAll(prospect, courses);
        int eligibleCount = 0;
        boolean gappEligible = false;
        for (EligibilityResult r : results) {
            if (r.isEligible()) {
                eligibleCount++;
                if (r.getCourse().getCategory() == Category.GAPP) gappEligible = true;
            }
        }

        bindSummary(prospect, eligibleCount, results.size(),
                gappEligible && EligibilityChecker.qualifiesForGappSponsorship(prospect));

        ResultAdapter adapter = new ResultAdapter(results, new ResultAdapter.Listener() {
            @Override public void onDetails(Course course) {
                Intent intent = new Intent(ResultActivity.this, CourseDetailActivity.class);
                intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.getId());
                startActivity(intent);
            }
            @Override public void onApply(Course course) {
                IntentUtils.openOnlineApplication(ResultActivity.this);
            }
        });
        RecyclerView recycler = findViewById(R.id.recycler_results);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        MaterialSwitch switchShowAll = findViewById(R.id.switch_show_all);
        switchShowAll.setOnCheckedChangeListener((b, checked) -> adapter.setShowNotEligible(checked));
        // If nothing matches, show every programme so the prospect can see what is missing
        switchShowAll.setChecked(eligibleCount == 0);

        findViewById(R.id.btn_check_again).setOnClickListener(v -> finish());
    }

    @SuppressWarnings("deprecation")
    private ProspectResult readProspect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return getIntent().getSerializableExtra(EXTRA_RESULT, ProspectResult.class);
        }
        return (ProspectResult) getIntent().getSerializableExtra(EXTRA_RESULT);
    }

    private void bindSummary(ProspectResult p, int eligible, int total, boolean showSponsorNote) {
        ImageView img = findViewById(R.id.img_summary);
        TextView title = findViewById(R.id.txt_summary_title);
        TextView desc = findViewById(R.id.txt_summary_desc);
        TextView stats = findViewById(R.id.txt_summary_stats);
        MaterialButton action = findViewById(R.id.btn_summary_action);
        boolean hasName = !p.getName().isEmpty();

        if (eligible > 0) {
            img.setImageResource(R.drawable.ic_check_circle);
            img.setImageTintList(UiUtils.colorList(this, R.color.gmi_green));
            title.setText(hasName ? getString(R.string.result_congrats, p.getName())
                    : getString(R.string.result_congrats_anon));
            desc.setText(getString(R.string.result_eligible_count, eligible, total));
            action.setText(R.string.apply_online);
            action.setOnClickListener(v -> IntentUtils.openOnlineApplication(this));
        } else {
            img.setImageResource(R.drawable.ic_cancel);
            img.setImageTintList(UiUtils.colorList(this, R.color.gmi_red));
            title.setText(hasName ? getString(R.string.result_none_title, p.getName())
                    : getString(R.string.result_none_title_anon));
            desc.setText(R.string.result_none_desc);
            action.setText(R.string.btn_contact_gmi);
            action.setIconResource(R.drawable.ic_phone);
            action.setOnClickListener(v -> startActivity(new Intent(this, ContactActivity.class)));
        }

        String summary = getString(R.string.result_summary, getString(p.getQualification().label),
                p.countCredits(), p.countPassesOnly());
        if (p.hasSkm3()) summary += getString(R.string.result_skm_suffix);
        stats.setText(summary);

        findViewById(R.id.txt_sponsor_note).setVisibility(showSponsorNote
                ? View.VISIBLE : View.GONE);
    }
}
