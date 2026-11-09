package my.edu.gmi.prospect.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import my.edu.gmi.prospect.R;
import my.edu.gmi.prospect.eligibility.EligibilityResult;
import my.edu.gmi.prospect.model.Course;
import my.edu.gmi.prospect.util.UiUtils;

/** Shows each programme with its eligibility status, missing requirements and an Apply button. */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    public interface Listener {
        void onDetails(Course course);
        void onApply(Course course);
    }

    private final List<EligibilityResult> allResults;
    private final List<EligibilityResult> visible = new ArrayList<>();
    private final Listener listener;

    public ResultAdapter(List<EligibilityResult> results, Listener listener) {
        this.allResults = results;
        this.listener = listener;
        setShowNotEligible(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setShowNotEligible(boolean show) {
        visible.clear();
        for (EligibilityResult r : allResults) {
            if (show || r.isEligible()) visible.add(r);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(visible.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return visible.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgStatus;
        private final TextView txtName, txtCategory, txtStatus, txtReason;
        private final Button btnDetails, btnApply;

        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = itemView.findViewById(R.id.img_status);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCategory = itemView.findViewById(R.id.txt_category);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtReason = itemView.findViewById(R.id.txt_reason);
            btnDetails = itemView.findViewById(R.id.btn_details);
            btnApply = itemView.findViewById(R.id.btn_apply);
        }

        void bind(EligibilityResult r, Listener listener) {
            Context c = itemView.getContext();
            Course course = r.getCourse();
            txtName.setText(course.getName());
            UiUtils.styleCategoryBadge(txtCategory, course.getCategory());

            if (r.isEligible()) {
                imgStatus.setImageResource(R.drawable.ic_check_circle);
                imgStatus.setImageTintList(UiUtils.colorList(c, R.color.gmi_green));
                txtStatus.setText(R.string.status_eligible);
                UiUtils.styleBadge(txtStatus, R.color.gmi_green, R.color.gmi_green_light);
                txtReason.setText(r.isViaSkm() ? R.string.eligible_via_skm : R.string.eligible_academic);
                btnApply.setVisibility(View.VISIBLE);
            } else {
                imgStatus.setImageResource(R.drawable.ic_cancel);
                imgStatus.setImageTintList(UiUtils.colorList(c, R.color.gmi_red));
                txtStatus.setText(R.string.status_not_eligible);
                UiUtils.styleBadge(txtStatus, R.color.gmi_red, R.color.gmi_red_light);
                txtReason.setText(c.getString(R.string.missing_prefix, joinReasons(c, r.getUnmetReasons())));
                btnApply.setVisibility(View.GONE);
            }
            btnDetails.setOnClickListener(v -> listener.onDetails(course));
            btnApply.setOnClickListener(v -> listener.onApply(course));
        }

        private static String joinReasons(Context c, List<Integer> reasons) {
            StringBuilder sb = new StringBuilder();
            String separator = c.getString(R.string.list_separator);
            for (int i = 0; i < reasons.size(); i++) {
                if (i > 0) sb.append(separator);
                sb.append(c.getString(reasons.get(i)));
            }
            return sb.toString();
        }
    }
}
