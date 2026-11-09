package my.edu.gmi.prospect;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import my.edu.gmi.prospect.util.IntentUtils;

/** About Us: background, vision, mission and reasons to choose GMI. */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar(R.string.title_about);

        findViewById(R.id.btn_website).setOnClickListener(v ->
                IntentUtils.openUrl(this, getString(R.string.gmi_website_url)));

        ((TextView) findViewById(R.id.txt_version)).setText(getString(R.string.app_version, versionName()));
    }

    private String versionName() {
        try {
            String v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            return v == null ? "" : v;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
