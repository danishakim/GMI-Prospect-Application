package my.edu.gmi.prospect.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.edu.gmi.prospect.model.Category;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.model.RuleType;

/**
 * Single source of programme data. Courses are read once from assets/courses.json
 * and cached in memory (Singleton pattern), so every screen shares the same list.
 */
public final class CourseRepository {

    private static final String TAG = "CourseRepository";
    private static final String FILE_NAME = "courses.json";
    private static volatile CourseRepository instance;

    private final List<Course> courses;

    private CourseRepository(Context context) {
        courses = Collections.unmodifiableList(load(context.getApplicationContext()));
    }

    public static CourseRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (CourseRepository.class) {
                if (instance == null) instance = new CourseRepository(context);
            }
        }
        return instance;
    }

    public List<Course> getAll() {
        return courses;
    }

    /** @param category null returns every course. */
    public List<Course> getByCategory(@Nullable Category category) {
        if (category == null) return courses;
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c.getCategory() == category) result.add(c);
        }
        return result;
    }

    @Nullable
    public Course findById(String id) {
        for (Course c : courses) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    private static List<Course> load(Context context) {
        List<Course> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(readAsset(context));
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                list.add(new Course(
                        o.getString("id"),
                        o.getString("name"),
                        Category.valueOf(o.getString("category")),
                        RuleType.valueOf(o.getString("rule")),
                        o.getString("department"),
                        o.getString("duration"),
                        o.getString("intake"),
                        o.getString("mode"),
                        o.getString("overview"),
                        toList(o.getJSONArray("requirements")),
                        toList(o.getJSONArray("careers"))));
            }
        } catch (IOException | JSONException | IllegalArgumentException e) {
            Log.e(TAG, "Unable to load course data", e);
        }
        return list;
    }

    private static String readAsset(Context context) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = context.getAssets().open(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private static List<String> toList(JSONArray array) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) list.add(array.getString(i));
        return list;
    }
}
