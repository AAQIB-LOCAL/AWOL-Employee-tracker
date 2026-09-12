package com.awol.employeetracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.awol.employeetracker.service.LocationTrackingService;
import com.awol.employeetracker.util.SessionManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SessionManager session = new SessionManager(context);
            if (session.isLoggedIn() && session.isGeofenceServiceEnabled()) {
                LocationTrackingService.startTracking(context);
            }
        }
    }
}
