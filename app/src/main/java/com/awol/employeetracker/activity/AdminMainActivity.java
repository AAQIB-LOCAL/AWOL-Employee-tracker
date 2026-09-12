package com.awol.employeetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.awol.employeetracker.R;
import com.awol.employeetracker.adapter.EmployeeAdapter;
import com.awol.employeetracker.database.AttendanceDatabaseHelper;
import com.awol.employeetracker.databinding.ActivityAdminMainBinding;
import com.awol.employeetracker.model.Employee;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private ActivityAdminMainBinding binding;
    private AttendanceDatabaseHelper db;
    private EmployeeAdapter employeeAdapter;
    private boolean showOnlyPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AttendanceDatabaseHelper.getInstance(this);

        initViews();
        setupRecyclerView();
        setupFilterToggle();
        refreshData();
    }

    private void initViews() {
        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        binding.fabAddEmployee.setOnClickListener(v -> showAddEmployeeDialog());
    }

    private void setupRecyclerView() {
        employeeAdapter = new EmployeeAdapter();
        binding.rvEmployeeList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvEmployeeList.setAdapter(employeeAdapter);
    }

    private void setupFilterToggle() {
        binding.toggleView.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                showOnlyPresent = (checkedId == R.id.btn_filter_present);
                refreshData();
            }
        });
    }

    private void refreshData() {
        List<Employee> allEmployees = db.getAllEmployees();
        List<Employee> presentEmployees = db.getPresentEmployees();

        int total = allEmployees.size();
        int present = presentEmployees.size();
        int absent = Math.max(0, total - present);

        binding.tvAdminTotalStaff.setText(String.valueOf(total));
        binding.tvAdminPresentToday.setText(String.valueOf(present));
        binding.tvAdminAbsentToday.setText(String.valueOf(absent));

        if (showOnlyPresent) {
            employeeAdapter.setEmployees(presentEmployees);
        } else {
            employeeAdapter.setEmployees(allEmployees);
        }
    }

    private void showAddEmployeeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register New Employee");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_login, null);
        // Using a clean simple programmatic form or dialog view
        final EditText etName = new EditText(this);
        etName.setHint("Full Name");

        final EditText etEmail = new EditText(this);
        etEmail.setHint("Company Email");

        final EditText etDept = new EditText(this);
        etDept.setHint("Department (e.g. Operations)");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);
        layout.addView(etName);
        layout.addView(etEmail);
        layout.addView(etDept);

        builder.setView(layout);

        builder.setPositiveButton("Register", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String dept = etDept.getText().toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
                String id = "EMP00" + (db.getAllEmployees().size() + 1);
                Employee emp = new Employee(id, name, email, TextUtils.isEmpty(dept) ? "General" : dept, "EMPLOYEE", "+1-555-0000", false);
                db.addEmployee(emp);
                Toast.makeText(AdminMainActivity.this, "Employee " + name + " added (" + id + ")", Toast.LENGTH_SHORT).show();
                refreshData();
            } else {
                Toast.makeText(AdminMainActivity.this, "Please enter both name and email", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
}
