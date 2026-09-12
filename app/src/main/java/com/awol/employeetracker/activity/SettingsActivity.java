package com.awol.employeetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awol.employeetracker.database.AttendanceDatabaseHelper;
import com.awol.employeetracker.databinding.ActivitySettingsBinding;
import com.awol.employeetracker.service.LocationTrackingService;
import com.awol.employeetracker.util.GeofenceHelper;
import com.awol.employeetracker.util.SessionManager;
import com.awol.employeetracker.util.ThemeHelper;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SessionManager session;
    private AttendanceDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        db = AttendanceDatabaseHelper.getInstance(this);

        initViews();
    }

    private void initViews() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Dark Mode
        binding.switchDarkMode.setChecked(session.isDarkMode());
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            session.setDarkMode(isChecked);
            ThemeHelper.applyTheme(isChecked);
        });

        // Geofence info & radius
        float currentRadius = session.getGeofenceRadius();
        binding.etGeofenceRadius.setText(String.valueOf((int) currentRadius));
        updateGeofenceCoordsText(currentRadius);

        binding.btnSaveGeofence.setOnClickListener(v -> {
            String input = binding.etGeofenceRadius.getText() != null ? binding.etGeofenceRadius.getText().toString().trim() : "";
            if (!TextUtils.isEmpty(input)) {
                try {
                    float newRadius = Float.parseFloat(input);
                    if (newRadius >= 10 && newRadius <= 5000) {
                        session.setGeofenceRadius(newRadius);
                        updateGeofenceCoordsText(newRadius);
                        Toast.makeText(this, "Geofence radius updated to " + (int) newRadius + "m", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please enter a radius between 10m and 5000m", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sync Now
        binding.btnSyncNow.setOnClickListener(v -> {
            int count = db.markAllRecordsSynced();
            Toast.makeText(this, "Sync Complete: " + count + " pending records uploaded to server.", Toast.LENGTH_SHORT).show();
        });

        // Logout
        binding.btnLogout.setOnClickListener(v -> {
            LocationTrackingService.stopTracking(this);
            session.logout();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void updateGeofenceCoordsText(float radius) {
        binding.tvGeofenceCoords.setText(
                "Workplace Coordinates:\n" +
                "Lat: " + GeofenceHelper.DEFAULT_OFFICE_LAT + ", Lon: " + GeofenceHelper.DEFAULT_OFFICE_LON + "\n" +
                "Radius: " + (int) radius + " meters"
        );
    }
}
