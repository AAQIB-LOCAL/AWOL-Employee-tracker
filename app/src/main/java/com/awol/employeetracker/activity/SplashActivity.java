package com.awol.employeetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.awol.employeetracker.R;
import com.awol.employeetracker.util.SessionManager;
import com.awol.employeetracker.util.ThemeHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(this);
        ThemeHelper.applyTheme(session.isDarkMode());

        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isFinishing()) {
                Intent intent;
                if (session.isLoggedIn()) {
                    if (session.isAdmin()) {
                        intent = new Intent(SplashActivity.this, AdminMainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, EmployeeMainActivity.class);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1200);
    }
}
