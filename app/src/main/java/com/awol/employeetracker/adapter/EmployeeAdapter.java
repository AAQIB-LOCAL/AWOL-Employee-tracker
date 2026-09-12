package com.awol.employeetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awol.employeetracker.R;
import com.awol.employeetracker.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<Employee> employees = new ArrayList<>();

    public void setEmployees(List<Employee> list) {
        this.employees = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee emp = employees.get(position);
        holder.tvName.setText(emp.getName());
        holder.tvDetails.setText(emp.getId() + " • " + emp.getDepartment() + " • " + emp.getRole());

        if (emp.isPresent()) {
            holder.tvStatus.setText("PRESENT");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_present);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.status_present));
        } else {
            holder.tvStatus.setText("OFF DUTY");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_absent);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.status_absent));
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_emp_name);
            tvDetails = itemView.findViewById(R.id.tv_emp_details);
            tvStatus = itemView.findViewById(R.id.tv_emp_status);
        }
    }
}
