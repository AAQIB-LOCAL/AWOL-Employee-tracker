package com.awol.employeetracker.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.awol.employeetracker.R;
import com.awol.employeetracker.adapter.AttendanceAdapter;
import com.awol.employeetracker.database.AttendanceDatabaseHelper;
import com.awol.employeetracker.databinding.ActivityEmployeeMainBinding;
import com.awol.employeetracker.model.AttendanceRecord;
import com.awol.employeetracker.service.LocationTrackingService;
import com.awol.employeetracker.util.GeofenceHelper;
import com.awol.employeetracker.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class EmployeeMainActivity extends AppCompatActivity {

    private ActivityEmployeeMainBinding binding;
    private SessionManager session;
    private AttendanceDatabaseHelper db;
    private AttendanceAdapter attendanceAdapter;

    private final Handler timeHandler = new Handler(Looper.getMainLooper());
    private Runnable timeRunnable;

    private boolean isCheckedIn = false;
    private AttendanceRecord todayRecord;
    private String todayDateStr;

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationTrackingService.ACTION_LOCATION_UPDATE.equals(intent.getAction())) {
                double distance = intent.getDoubleExtra(LocationTrackingService.EXTRA_DISTANCE, 0.0);
                boolean isInside = intent.getBooleanExtra(LocationTrackingService.EXTRA_IS_INSIDE, false);
                updateGeofenceUI(distance, isInside);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        db = AttendanceDatabaseHelper.getInstance(this);

        initViews();
        setupRecyclerView();
        setupClockTimer();
        setupGeofenceServiceToggle();
        refreshAttendanceData();

        checkLocationPermissions();
    }

    private void initViews() {
        binding.tvEmployeeName.setText(session.getUserName());
        binding.tvDepartment.setText(session.getUserDepartment() + " • " + session.getUserId());

        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        binding.btnClockAction.setOnClickListener(v -> showBiometricPromptAndClock());
    }

    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceAdapter();
        binding.rvAttendanceHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAttendanceHistory.setAdapter(attendanceAdapter);
    }

    private void setupClockTimer() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                Date now = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());

                binding.tvCurrentTime.setText(timeFormat.format(now));
                binding.tvCurrentDate.setText(dateFormat.format(now));

                updateShiftTimer(now);
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }

    private void updateShiftTimer(Date now) {
        if (isCheckedIn && todayRecord != null && todayRecord.getCheckInTime() != null) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                Date inTime = parser.parse(todayRecord.getCheckInTime());
                if (inTime != null) {
                    SimpleDateFormat currentHourMin = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    Date currentParsed = parser.parse(currentHourMin.format(now));
                    if (currentParsed != null) {
                        long diffMs = Math.max(0, currentParsed.getTime() - inTime.getTime());
                        long hours = diffMs / (1000 * 60 * 60);
                        long minutes = (diffMs / (1000 * 60)) % 60;
                        binding.tvShiftTimer.setText(String.format(Locale.getDefault(), "Shift Elapsed: %02d hrs %02d mins", hours, minutes));
                        binding.tvStatTodayHours.setText(String.format(Locale.getDefault(), "%.1f hrs", hours + (minutes / 60.0)));
                    }
                }
            } catch (Exception ignored) {
            }
        } else {
            binding.tvShiftTimer.setText("Shift: Off Clock");
        }
    }

    private void refreshAttendanceData() {
        todayDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todayRecord = db.getTodayAttendance(session.getUserId(), todayDateStr);

        if (todayRecord != null && todayRecord.getCheckInTime() != null && todayRecord.getCheckOutTime() == null) {
            isCheckedIn = true;
            binding.tvClockStatusBadge.setText(R.string.status_checked_in);
            binding.tvClockStatusBadge.setBackgroundResource(R.drawable.bg_badge_present);
            binding.tvClockStatusBadge.setTextColor(getResources().getColor(R.color.status_present));

            binding.btnClockAction.setText(R.string.clock_out);
            binding.btnClockAction.setBackgroundColor(getResources().getColor(R.color.status_absent));
            binding.btnClockAction.setIconResource(R.drawable.ic_clock);
        } else {
            isCheckedIn = false;
            binding.tvClockStatusBadge.setText(R.string.status_checked_out);
            binding.tvClockStatusBadge.setBackgroundResource(R.drawable.bg_badge_absent);
            binding.tvClockStatusBadge.setTextColor(getResources().getColor(R.color.status_absent));

            binding.btnClockAction.setText(R.string.clock_in);
            binding.btnClockAction.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.btnClockAction.setIconResource(R.drawable.ic_fingerprint);
        }

        List<AttendanceRecord> history = db.getAttendanceHistory(session.getUserId());
        attendanceAdapter.setRecords(history);

        // Stats summary
        int count = history.size();
        binding.tvStatWeekDays.setText(Math.min(5, Math.max(1, count)) + " / 5");
        binding.tvStatMonthPct.setText((count > 0 ? "92%" : "0%"));
    }

    private void showBiometricPromptAndClock() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                == BiometricManager.BIOMETRIC_SUCCESS) {

            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(EmployeeMainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    toggleClockStatus();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    // Fallback to clocking in on manual tap if biometric error is user cancel
                    Toast.makeText(EmployeeMainActivity.this, "Biometric: " + errString, Toast.LENGTH_SHORT).show();
                }
            });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.biometric_prompt_title))
                    .setSubtitle(getString(R.string.biometric_prompt_subtitle))
                    .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            // Direct toggle if biometric sensor not available
            toggleClockStatus();
        }
    }

    private void toggleClockStatus() {
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        if (!isCheckedIn) {
            // Clock IN
            db.recordCheckIn(session.getUserId(), session.getUserName(), todayDateStr, currentTime, "Office Geofence");
            Toast.makeText(this, "Clocked In successfully at " + currentTime, Toast.LENGTH_SHORT).show();
        } else {
            // Clock OUT
            db.recordCheckOut(session.getUserId(), todayDateStr, currentTime, "Office Geofence");
            Toast.makeText(this, "Clocked Out successfully at " + currentTime, Toast.LENGTH_SHORT).show();
        }

        refreshAttendanceData();
    }

    private void setupGeofenceServiceToggle() {
        binding.switchGeofenceService.setChecked(session.isGeofenceServiceEnabled());
        binding.switchGeofenceService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            session.setGeofenceServiceEnabled(isChecked);
            if (isChecked) {
                LocationTrackingService.startTracking(this);
                Toast.makeText(this, "Automated GPS Geofence Service Started", Toast.LENGTH_SHORT).show();
            } else {
                LocationTrackingService.stopTracking(this);
                Toast.makeText(this, "GPS Geofence Service Stopped", Toast.LENGTH_SHORT).show();
            }
        });

        if (session.isGeofenceServiceEnabled()) {
            LocationTrackingService.startTracking(this);
        }
    }

    private void updateGeofenceUI(double distanceMeters, boolean isInside) {
        binding.tvGeofenceDistance.setText("Distance from workplace: ~" + GeofenceHelper.formatDistance(distanceMeters));
        if (isInside) {
            binding.tvGeofenceBadge.setText(R.string.geofence_inside);
            binding.tvGeofenceBadge.setBackgroundResource(R.drawable.bg_badge_present);
            binding.tvGeofenceBadge.setTextColor(getResources().getColor(R.color.status_present));
        } else {
            binding.tvGeofenceBadge.setText(R.string.geofence_outside);
            binding.tvGeofenceBadge.setBackgroundResource(R.drawable.bg_badge_absent);
            binding.tvGeofenceBadge.setTextColor(getResources().getColor(R.color.status_absent));
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(locationReceiver, new IntentFilter(LocationTrackingService.ACTION_LOCATION_UPDATE));
        refreshAttendanceData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(locationReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }
}
