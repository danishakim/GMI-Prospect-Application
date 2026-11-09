package my.edu.gmi.prospect.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import my.edu.gmi.prospect.model.Category;

/** Small helpers to colour views consistently by programme category. */
public final class UiUtils {

    private UiUtils() { }

    public static ColorStateList colorList(Context context, @ColorRes int color) {
        return ColorStateList.valueOf(ContextCompat.getColor(context, color));
    }

    /** Coloured pill badge, e.g. "Diploma". */
    public static void styleBadge(TextView badge, @ColorRes int textColor, @ColorRes int bgColor) {
        Context c = badge.getContext();
        badge.setTextColor(ContextCompat.getColor(c, textColor));
        badge.setBackgroundTintList(colorList(c, bgColor));
    }

    public static void styleCategoryBadge(TextView badge, Category category) {
        badge.setText(category.shortName);
        styleBadge(badge, category.color, category.lightColor);
    }

    /** Tints a circular icon background and its icon with the category colours. */
    public static void tintIcon(View circle, ImageView icon, @ColorRes int color, @ColorRes int lightColor) {
        Context c = icon.getContext();
        circle.setBackgroundTintList(colorList(c, lightColor));
        icon.setImageTintList(colorList(c, color));
    }
}
