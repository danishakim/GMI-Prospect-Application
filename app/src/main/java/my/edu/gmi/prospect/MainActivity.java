package my.edu.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import my.edu.gmi.prospect.data.CourseRepository;
import my.edu.gmi.prospect.model.Category;
import my.edu.gmi.prospect.util.IntentUtils;
import my.edu.gmi.prospect.util.UiUtils;

/** Home dashboard: entry point to all features of the app. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindMenuCard(R.id.card_courses, R.drawable.ic_library, R.string.menu_courses,
                R.string.menu_courses_desc, R.color.gmi_blue, R.color.gmi_blue_light,
                () -> openCourses(null));
        bindMenuCard(R.id.card_eligibility, R.drawable.ic_assignment, R.string.menu_eligibility,
                R.string.menu_eligibility_desc, R.color.gmi_green, R.color.gmi_green_light,
                () -> startActivity(new Intent(this, EligibilityActivity.class)));
        bindMenuCard(R.id.card_contact, R.drawable.ic_phone, R.string.menu_contact,
                R.string.menu_contact_desc, R.color.gmi_red, R.color.gmi_red_light,
                () -> startActivity(new Intent(this, ContactActivity.class)));
        bindMenuCard(R.id.card_about, R.drawable.ic_info, R.string.menu_about,
                R.string.menu_about_desc, R.color.gmi_purple, R.color.gmi_purple_light,
                () -> startActivity(new Intent(this, AboutActivity.class)));

        CourseRepository repo = CourseRepository.getInstance(this);
        bindCategory(R.id.cat_diploma, Category.DIPLOMA, repo);
        bindCategory(R.id.cat_gapp, Category.GAPP, repo);
        bindCategory(R.id.cat_gufp, Category.GUFP, repo);

        findViewById(R.id.btn_apply).setOnClickListener(v -> IntentUtils.openOnlineApplication(this));
    }

    private void bindMenuCard(@IdRes int cardId, @DrawableRes int icon, @StringRes int title,
                              @StringRes int desc, @ColorRes int color, @ColorRes int lightColor,
                              Runnable onClick) {
        View card = findViewById(cardId);
        ImageView img = card.findViewById(R.id.img_icon);
        img.setImageResource(icon);
        UiUtils.tintIcon(card.findViewById(R.id.icon_bg), img, color, lightColor);
        ((TextView) card.findViewById(R.id.txt_title)).setText(title);
        ((TextView) card.findViewById(R.id.txt_desc)).setText(desc);
        card.setOnClickListener(v -> onClick.run());
    }

    private void bindCategory(@IdRes int rowId, Category category, CourseRepository repo) {
        View row = findViewById(rowId);
        ImageView img = row.findViewById(R.id.img_icon);
        UiUtils.tintIcon(row.findViewById(R.id.icon_bg), img, category.color, category.lightColor);
        ((TextView) row.findViewById(R.id.txt_title)).setText(category.fullName);
        int count = repo.getByCategory(category).size();
        ((TextView) row.findViewById(R.id.txt_count)).setText(getString(R.string.programme_count, count));
        row.setOnClickListener(v -> openCourses(category));
    }

    private void openCourses(Category category) {
        Intent intent = new Intent(this, CourseListActivity.class);
        if (category != null) intent.putExtra(CourseListActivity.EXTRA_CATEGORY, category.name());
        startActivity(intent);
    }
}
