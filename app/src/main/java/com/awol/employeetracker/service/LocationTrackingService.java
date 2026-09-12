package com.awol.employeetracker.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.awol.employeetracker.R;
import com.awol.employeetracker.activity.EmployeeMainActivity;
import com.awol.employeetracker.database.AttendanceDatabaseHelper;
import com.awol.employeetracker.util.GeofenceHelper;
import com.awol.employeetracker.util.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationTrackingService extends Service {

    public static final String ACTION_LOCATION_UPDATE = "com.awol.employeetracker.ACTION_LOCATION_UPDATE";
    public static final String EXTRA_DISTANCE = "extra_distance";
    public static final String EXTRA_IS_INSIDE = "extra_is_inside";

    private static final String CHANNEL_ID = "awol_location_channel";
    private static final int NOTIFICATION_ID = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean lastInsideState = false;

    public static void startTracking(Context context) {
        Intent intent = new Intent(context, LocationTrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopTracking(Context context) {
        Intent intent = new Intent(context, LocationTrackingService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification("Monitoring workplace proximity..."));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void setupLocationUpdates() {
        LocationRequest request = LocationRequest.create();
        request.setInterval(10000); // 10 seconds
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    handleLocation(location);
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
        } catch (SecurityException ignored) {
        }
    }

    private void handleLocation(Location location) {
        SessionManager session = new SessionManager(this);
        float radius = session.getGeofenceRadius();

        double dist = GeofenceHelper.calculateDistanceMeters(
                location.getLatitude(), location.getLongitude(),
                GeofenceHelper.DEFAULT_OFFICE_LAT, GeofenceHelper.DEFAULT_OFFICE_LON
        );

        boolean isInside = dist <= radius;

        // Broadcast to Activity
        Intent broadcast = new Intent(ACTION_LOCATION_UPDATE);
        broadcast.putExtra(EXTRA_DISTANCE, dist);
        broadcast.putExtra(EXTRA_IS_INSIDE, isInside);
        sendBroadcast(broadcast);

        // Update notification
        String statusText = isInside
                ? "Inside workplace (~" + GeofenceHelper.formatDistance(dist) + ")"
                : "Outside workplace (~" + GeofenceHelper.formatDistance(dist) + ")";
        updateNotification(statusText);

        // Auto transition check
        if (isInside && !lastInsideState) {
            // Automatically log presence if checked-in today
            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            AttendanceDatabaseHelper db = AttendanceDatabaseHelper.getInstance(this);
            db.markEmployeePresent(session.getUserId(), true);
        }
        lastInsideState = isInside;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Geofence Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Shows active employee geofence tracking status");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification(String text) {
        Intent notificationIntent = new Intent(this, EmployeeMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("AWOL Geofence Active")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_location)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    private void updateNotification(String text) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, buildNotification(text));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
