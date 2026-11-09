package my.edu.gmi.prospect;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import my.edu.gmi.prospect.data.CourseRepository;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.util.IntentUtils;

/** Contact Us: address with embedded map, phone, fax, email, website and an enquiry form. */
public class ContactActivity extends BaseActivity {

    private WebView webMap;
    private TextInputLayout tilName, tilEmail, tilPhone, tilMessage;
    private MaterialAutoCompleteTextView dropdownProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setupToolbar(R.string.title_contact);

        setupMap();
        setupContactRows();
        setupEnquiryForm();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupMap() {
        webMap = findViewById(R.id.web_map);
        WebSettings settings = webMap.getSettings();
        settings.setJavaScriptEnabled(true); // required by the Google Maps embed
        settings.setDomStorageEnabled(true);
        webMap.setWebViewClient(new WebViewClient());
        webMap.loadUrl(getString(R.string.gmi_map_embed_url));

        findViewById(R.id.btn_open_map).setOnClickListener(v ->
                IntentUtils.openMap(this, getString(R.string.gmi_map_query)));
    }

    private void setupContactRows() {
        bindRow(R.id.row_phone, R.drawable.ic_phone, R.string.label_phone, R.string.gmi_phone,
                R.string.action_call, () -> IntentUtils.dial(this, getString(R.string.gmi_phone_uri)));
        bindRow(R.id.row_fax, R.drawable.ic_fax, R.string.label_fax, R.string.gmi_fax,
                R.string.action_copy, this::copyFax);
        bindRow(R.id.row_email, R.drawable.ic_email, R.string.label_email, R.string.gmi_email,
                R.string.action_email, () -> IntentUtils.email(this, getString(R.string.gmi_email), "", ""));
        bindRow(R.id.row_website, R.drawable.ic_web, R.string.label_website, R.string.gmi_website,
                R.string.action_visit, () -> IntentUtils.openUrl(this, getString(R.string.gmi_website_url)));
        bindRow(R.id.row_hours, R.drawable.ic_time, R.string.label_hours, R.string.gmi_hours, 0, null);
    }

    /** Fills one included item_contact_row. actionText 0 hides the action button. */
    private void bindRow(@IdRes int rowId, @DrawableRes int icon, @StringRes int label,
                         @StringRes int value, @StringRes int actionText, @Nullable Runnable action) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.img_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.txt_label)).setText(label);
        ((TextView) row.findViewById(R.id.txt_value)).setText(value);
        Button btn = row.findViewById(R.id.btn_action);
        if (action == null) {
            btn.setVisibility(View.GONE);
            return;
        }
        btn.setText(actionText);
        btn.setOnClickListener(v -> action.run());
        row.setOnClickListener(v -> action.run());
    }

    private void copyFax() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(getString(R.string.label_fax), getString(R.string.gmi_fax)));
            Toast.makeText(this, R.string.fax_copied, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupEnquiryForm() {
        tilName = findViewById(R.id.til_full_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);
        tilMessage = findViewById(R.id.til_message);
        dropdownProgramme = findViewById(R.id.dropdown_programme);

        List<String> programmes = new ArrayList<>();
        programmes.add(getString(R.string.enquiry_general));
        for (Course c : CourseRepository.getInstance(this).getAll()) programmes.add(c.getName());
        dropdownProgramme.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, programmes));
        dropdownProgramme.setText(programmes.get(0), false);

        for (TextInputLayout til : new TextInputLayout[]{tilName, tilEmail, tilPhone, tilMessage}) {
            clearErrorWhenTyping(til);
        }
        findViewById(R.id.btn_send).setOnClickListener(v -> submitEnquiry());
    }

    private void submitEnquiry() {
        String name = textOf(tilName);
        String email = textOf(tilEmail);
        String phone = textOf(tilPhone);
        String programme = dropdownProgramme.getText().toString();
        String message = textOf(tilMessage);

        boolean valid = true;
        if (name.isEmpty()) { tilName.setError(getString(R.string.error_required)); valid = false; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email)); valid = false;
        }
        if (!phone.isEmpty() && !Patterns.PHONE.matcher(phone).matches()) {
            tilPhone.setError(getString(R.string.error_phone)); valid = false;
        }
        if (message.isEmpty()) { tilMessage.setError(getString(R.string.error_required)); valid = false; }
        if (!valid) return;

        String subject = getString(R.string.enquiry_subject, programme);
        String body = getString(R.string.enquiry_body, name, email, phone, programme, message);
        Toast.makeText(this, R.string.enquiry_sent, Toast.LENGTH_SHORT).show();
        IntentUtils.email(this, getString(R.string.gmi_email), subject, body);
    }

    private static String textOf(TextInputLayout til) {
        return til.getEditText() == null ? "" : til.getEditText().getText().toString().trim();
    }

    private static void clearErrorWhenTyping(TextInputLayout til) {
        if (til.getEditText() == null) return;
        til.getEditText().addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                if (!TextUtils.isEmpty(til.getError())) til.setError(null);
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webMap != null) webMap.onResume();
    }

    @Override
    protected void onPause() {
        if (webMap != null) webMap.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webMap != null) webMap.destroy();
        super.onDestroy();
    }
}
