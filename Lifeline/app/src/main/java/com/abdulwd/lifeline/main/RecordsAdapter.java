package com.abdulwd.lifeline.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulwd.lifeline.R;
import com.abdulwd.lifeline.models.Record;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

/**
 * Created by abdul on 24/3/18.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.TimeLineViewHolder> {

    private List<Record> data;

    RecordsAdapter(List<Record> data) {
        this.data = data;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_record, null);
        return new TimeLineViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        Record record = data.get(position);
        holder.department.setText(record.getDepartment());
        holder.hospital.setText(record.getHospital());
        holder.problem.setText(record.getProblem());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TextView problem;
        TextView hospital;
        TextView department;

        TimeLineViewHolder(View itemView) {
            super(itemView);
            problem = itemView.findViewById(R.id.patient_problem);
            hospital = itemView.findViewById(R.id.hospital);
            department = itemView.findViewById(R.id.department);
        }
    }
}
