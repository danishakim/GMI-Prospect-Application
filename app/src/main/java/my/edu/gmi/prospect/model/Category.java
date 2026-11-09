package my.edu.gmi.prospect.model;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import my.edu.gmi.prospect.R;

/** The three groups of programmes offered by GMI. */
public enum Category {
    DIPLOMA(R.string.cat_diploma, R.string.cat_diploma_full, R.color.gmi_blue, R.color.gmi_blue_light),
    GAPP(R.string.cat_gapp, R.string.cat_gapp_full, R.color.gmi_red, R.color.gmi_red_light),
    GUFP(R.string.cat_gufp, R.string.cat_gufp_full, R.color.gmi_purple, R.color.gmi_purple_light);

    @StringRes public final int shortName;
    @StringRes public final int fullName;
    @ColorRes public final int color;
    @ColorRes public final int lightColor;

    Category(int shortName, int fullName, int color, int lightColor) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.color = color;
        this.lightColor = lightColor;
    }
}
