package my.edu.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import my.edu.gmi.prospect.adapter.CourseAdapter;
import my.edu.gmi.prospect.data.CourseRepository;
import my.edu.gmi.prospect.model.Category;

/** List of all GMI programmes with tabs (All / Diploma / GAPP / GUFP) and search. */
public class CourseListActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY = "extra_category";
    /** Tab order: index 0 = All, then one tab per Category. */
    private static final Category[] TAB_CATEGORIES = {null, Category.DIPLOMA, Category.GAPP, Category.GUFP};

    private CourseAdapter adapter;
    private RecyclerView recycler;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        setupToolbar(R.string.title_courses);

        recycler = findViewById(R.id.recycler_courses);
        txtEmpty = findViewById(R.id.txt_empty);
        adapter = new CourseAdapter(CourseRepository.getInstance(this).getAll(), course -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.getId());
            startActivity(intent);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        setupTabs(getInitialTab());
    }

    private int getInitialTab() {
        String name = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (name == null) return 0;
        for (int i = 1; i < TAB_CATEGORIES.length; i++) {
            if (TAB_CATEGORIES[i].name().equals(name)) return i;
        }
        return 0;
    }

    private void setupTabs(int selected) {
        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.addTab(tabs.newTab().setText(R.string.cat_all));
        for (int i = 1; i < TAB_CATEGORIES.length; i++) {
            tabs.addTab(tabs.newTab().setText(TAB_CATEGORIES[i].shortName));
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) { showCategory(tab.getPosition()); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { recycler.smoothScrollToPosition(0); }
        });
        TabLayout.Tab tab = tabs.getTabAt(selected);
        if (tab != null) tab.select();
        showCategory(selected);
    }

    private void showCategory(int tabIndex) {
        adapter.setCategory(TAB_CATEGORIES[tabIndex]);
        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean empty = adapter.getItemCount() == 0;
        txtEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_list, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }
                @Override public boolean onQueryTextChange(String newText) {
                    adapter.setQuery(newText);
                    updateEmptyState();
                    return true;
                }
            });
        }
        return true;
    }
}
