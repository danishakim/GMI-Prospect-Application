package my.edu.gmi.prospect.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import my.edu.gmi.prospect.R;
import my.edu.gmi.prospect.model.Category;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.util.UiUtils;

/** RecyclerView adapter for the course list, with category + search filtering. */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    private final List<Course> allCourses;
    private final List<Course> visibleCourses = new ArrayList<>();
    private final OnCourseClickListener listener;
    @Nullable private Category category;
    private String query = "";

    public CourseAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.allCourses = courses;
        this.listener = listener;
        applyFilter();
    }

    public void setCategory(@Nullable Category category) {
        this.category = category;
        applyFilter();
    }

    public void setQuery(String query) {
        this.query = query == null ? "" : query;
        applyFilter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void applyFilter() {
        visibleCourses.clear();
        for (Course c : allCourses) {
            if ((category == null || c.getCategory() == category) && c.matches(query)) {
                visibleCourses.add(c);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.bind(visibleCourses.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return visibleCourses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        private final View iconBg;
        private final ImageView imgIcon;
        private final TextView txtCategory, txtName, txtDepartment, txtDuration;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            iconBg = itemView.findViewById(R.id.icon_bg);
            imgIcon = itemView.findViewById(R.id.img_icon);
            txtCategory = itemView.findViewById(R.id.txt_category);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDepartment = itemView.findViewById(R.id.txt_department);
            txtDuration = itemView.findViewById(R.id.txt_duration);
        }

        void bind(Course course, OnCourseClickListener listener) {
            Category cat = course.getCategory();
            UiUtils.styleCategoryBadge(txtCategory, cat);
            UiUtils.tintIcon(iconBg, imgIcon, cat.color, cat.lightColor);
            txtName.setText(course.getName());
            txtDepartment.setText(course.getDepartment());
            txtDuration.setText(course.getDuration());
            itemView.setOnClickListener(v -> listener.onCourseClick(course));
        }
    }
}
