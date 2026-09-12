package com.awol.employeetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awol.employeetracker.R;
import com.awol.employeetracker.model.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<AttendanceRecord> records = new ArrayList<>();

    public void setRecords(List<AttendanceRecord> newRecords) {
        this.records = newRecords != null ? newRecords : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = records.get(position);
        holder.tvDate.setText(record.getDate());

        String inTime = record.getCheckInTime() != null ? record.getCheckInTime() : "--:--";
        String outTime = record.getCheckOutTime() != null ? record.getCheckOutTime() : "--:--";
        holder.tvTimes.setText("In: " + inTime + "  •  Out: " + outTime);

        holder.tvStatus.setText(record.getStatus());
        if ("PRESENT".equalsIgnoreCase(record.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_present);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.status_present));
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_absent);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.status_absent));
        }

        holder.tvSync.setText(record.isSynced() ? "Synced" : "Local Pending");
        holder.tvSync.setTextColor(record.isSynced()
                ? holder.itemView.getContext().getResources().getColor(R.color.colorPrimary)
                : holder.itemView.getContext().getResources().getColor(R.color.status_geofence_out));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTimes, tvStatus, tvSync;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_record_date);
            tvTimes = itemView.findViewById(R.id.tv_record_times);
            tvStatus = itemView.findViewById(R.id.tv_record_status);
            tvSync = itemView.findViewById(R.id.tv_record_sync);
        }
    }
}
