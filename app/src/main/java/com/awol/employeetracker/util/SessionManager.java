package com.awol.employeetracker.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "awol_session_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROLE = "user_role"; // "EMPLOYEE" or "ADMIN"
    private static final String KEY_USER_DEPT = "user_dept";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_GEOFENCE_SERVICE_ENABLED = "geofence_service_enabled";
    private static final String KEY_GEOFENCE_RADIUS = "geofence_radius";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String email, String role, String dept) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_DEPT, dept);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "EMP001");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Employee");
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "user@company.com");
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "EMPLOYEE");
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getUserRole());
    }

    public String getUserDepartment() {
        return pref.getString(KEY_USER_DEPT, "General");
    }

    public void setDarkMode(boolean enabled) {
        editor.putBoolean(KEY_DARK_MODE, enabled);
        editor.apply();
    }

    public boolean isDarkMode() {
        return pref.getBoolean(KEY_DARK_MODE, false);
    }

    public void setGeofenceServiceEnabled(boolean enabled) {
        editor.putBoolean(KEY_GEOFENCE_SERVICE_ENABLED, enabled);
        editor.apply();
    }

    public boolean isGeofenceServiceEnabled() {
        return pref.getBoolean(KEY_GEOFENCE_SERVICE_ENABLED, false);
    }

    public void setGeofenceRadius(float radiusMeters) {
        editor.putFloat(KEY_GEOFENCE_RADIUS, radiusMeters);
        editor.apply();
    }

    public float getGeofenceRadius() {
        return pref.getFloat(KEY_GEOFENCE_RADIUS, 100.0f);
    }

    public void logout() {
        boolean darkMode = isDarkMode();
        editor.clear();
        editor.putBoolean(KEY_DARK_MODE, darkMode);
        editor.apply();
    }
}
