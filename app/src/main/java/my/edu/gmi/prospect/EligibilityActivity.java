package my.edu.gmi.prospect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.EnumMap;
import java.util.Map;

import my.edu.gmi.prospect.eligibility.ProspectResult;
import my.edu.gmi.prospect.eligibility.Qualification;
import my.edu.gmi.prospect.eligibility.Subject;

/** Lets the prospect enter SPM or O-Level/IGCSE grades and runs the eligibility check. */
public class EligibilityActivity extends BaseActivity {

    private static final String STATE_QUALIFICATION = "state_qualification";
    private static final String STATE_GRADES = "state_grades";

    private final Map<Subject, Spinner> gradeSpinners = new EnumMap<>(Subject.class);
    private Qualification qualification = Qualification.SPM;
    private TextInputEditText edtName;
    private MaterialSwitch switchSkm;
    private MaterialButtonToggleGroup toggleQualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility);
        setupToolbar(R.string.title_eligibility);

        edtName = findViewById(R.id.edt_name);
        switchSkm = findViewById(R.id.switch_skm);
        toggleQualification = findViewById(R.id.toggle_qualification);

        buildSubjectRows();

        toggleQualification.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            qualification = checkedId == R.id.btn_igcse ? Qualification.IGCSE : Qualification.SPM;
            onQualificationChanged();
        });

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            onQualificationChanged();
        }

        findViewById(R.id.btn_reset).setOnClickListener(this::resetForm);
        findViewById(R.id.btn_check).setOnClickListener(this::checkEligibility);
    }

    /** Inflates one "subject + grade spinner" row for every Subject, in its group container. */
    private void buildSubjectRows() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Subject subject : Subject.values()) {
            ViewGroup container = containerFor(subject.group);
            View row = inflater.inflate(R.layout.item_subject_grade, container, false);
            ((TextView) row.findViewById(R.id.txt_subject)).setText(subject.label);
            Spinner spinner = row.findViewById(R.id.spinner_grade);
            spinner.setSaveEnabled(false); // all rows share one id, so we save state manually
            gradeSpinners.put(subject, spinner);
            container.addView(row);
        }
    }

    private LinearLayout containerFor(Subject.Group group) {
        switch (group) {
            case CORE: return findViewById(R.id.container_core);
            case SCIENCE: return findViewById(R.id.container_science);
            default: return findViewById(R.id.container_other);
        }
    }

    /** Swap grade scale (SPM A+..G or IGCSE A*..U) and show SKM option only for SPM. */
    private void onQualificationChanged() {
        ArrayAdapter<CharSequence> gradeAdapter = ArrayAdapter.createFromResource(
                this, qualification.gradesArray, android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Spinner spinner : gradeSpinners.values()) {
            spinner.setAdapter(gradeAdapter);
        }
        boolean isSpm = qualification == Qualification.SPM;
        switchSkm.setVisibility(isSpm ? View.VISIBLE : View.GONE);
        if (!isSpm) switchSkm.setChecked(false);
    }

    private void resetForm(View v) {
        edtName.setText(null);
        switchSkm.setChecked(false);
        for (Spinner spinner : gradeSpinners.values()) spinner.setSelection(0);
        Snackbar.make(v, R.string.reset_done, Snackbar.LENGTH_SHORT).show();
    }

    private void checkEligibility(View v) {
        if (selectedIndex(Subject.ENGLISH) == 0 || selectedIndex(Subject.MATH) == 0) {
            Snackbar.make(v, R.string.error_no_grades, Snackbar.LENGTH_LONG).show();
            return;
        }
        String name = edtName.getText() == null ? "" : edtName.getText().toString();
        ProspectResult result = new ProspectResult(name, qualification, switchSkm.isChecked());
        for (Subject subject : Subject.values()) {
            result.setGrade(subject, qualification.levelOf(selectedIndex(subject)));
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_RESULT, result);
        startActivity(intent);
    }

    private int selectedIndex(Subject subject) {
        Spinner spinner = gradeSpinners.get(subject);
        return spinner == null ? 0 : spinner.getSelectedItemPosition();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_QUALIFICATION, qualification.name());
        int[] grades = new int[Subject.values().length];
        for (Subject s : Subject.values()) grades[s.ordinal()] = selectedIndex(s);
        outState.putIntArray(STATE_GRADES, grades);
    }

    private void restoreState(Bundle state) {
        String q = state.getString(STATE_QUALIFICATION, Qualification.SPM.name());
        qualification = Qualification.valueOf(q);
        toggleQualification.check(qualification == Qualification.IGCSE ? R.id.btn_igcse : R.id.btn_spm);
        onQualificationChanged();
        int[] grades = state.getIntArray(STATE_GRADES);
        if (grades == null) return;
        for (Subject s : Subject.values()) {
            Spinner spinner = gradeSpinners.get(s);
            if (spinner != null && s.ordinal() < grades.length) spinner.setSelection(grades[s.ordinal()]);
        }
    }
}
