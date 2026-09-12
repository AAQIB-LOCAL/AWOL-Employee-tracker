package com.awol.employeetracker;

import android.app.Application;

import com.awol.employeetracker.database.AttendanceDatabaseHelper;
import com.awol.employeetracker.util.SessionManager;
import com.awol.employeetracker.util.ThemeHelper;

public class AWOLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize user theme preference
        SessionManager session = new SessionManager(this);
        ThemeHelper.applyTheme(session.isDarkMode());

        // Initialize SQLite Database instance
        AttendanceDatabaseHelper.getInstance(this);
    }
}
