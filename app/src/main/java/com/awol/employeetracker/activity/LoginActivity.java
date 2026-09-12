package com.awol.employeetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awol.employeetracker.databinding.ActivityLoginBinding;
import com.awol.employeetracker.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);

        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> performLogin());

        binding.btnDemoEmployee.setOnClickListener(v -> {
            session.createLoginSession("EMP001", "Aaqib Khan", "aaqib@company.com", "EMPLOYEE", "Engineering");
            Toast.makeText(this, "Logged in as Employee (Demo)", Toast.LENGTH_SHORT).show();
            navigateToEmployeeDashboard();
        });

        binding.btnDemoAdmin.setOnClickListener(v -> {
            session.createLoginSession("ADM001", "Admin Director", "admin@company.com", "ADMIN", "Executive");
            Toast.makeText(this, "Logged in as Admin (Demo)", Toast.LENGTH_SHORT).show();
            navigateToAdminDashboard();
        });
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Please enter your work email or ID");
            binding.etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 4) {
            binding.etPassword.setError("Password must be at least 4 characters");
            binding.etPassword.requestFocus();
            return;
        }

        boolean isAdminSelected = binding.rbAdmin.isChecked();
        String role = isAdminSelected ? "ADMIN" : "EMPLOYEE";
        String name = isAdminSelected ? "Admin Director" : "Aaqib Khan";
        String empId = isAdminSelected ? "ADM001" : "EMP001";
        String dept = isAdminSelected ? "Executive" : "Engineering";

        session.createLoginSession(empId, name, email, role, dept);
        Toast.makeText(this, "Welcome back, " + name, Toast.LENGTH_SHORT).show();

        if (isAdminSelected) {
            navigateToAdminDashboard();
        } else {
            navigateToEmployeeDashboard();
        }
    }

    private void navigateToEmployeeDashboard() {
        Intent intent = new Intent(LoginActivity.this, EmployeeMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }
}
