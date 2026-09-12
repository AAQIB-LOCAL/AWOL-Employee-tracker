package com.awol.employeetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.awol.employeetracker.model.AttendanceRecord;
import com.awol.employeetracker.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "awol_employee_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table: Attendance
    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String COL_ATTENDANCE_ID = "id";
    public static final String COL_EMP_ID = "employee_id";
    public static final String COL_EMP_NAME = "employee_name";
    public static final String COL_DATE = "attendance_date";
    public static final String COL_CHECK_IN = "check_in_time";
    public static final String COL_CHECK_OUT = "check_out_time";
    public static final String COL_STATUS = "status";
    public static final String COL_LOC_IN = "location_in";
    public static final String COL_LOC_OUT = "location_out";
    public static final String COL_IS_SYNCED = "is_synced";

    // Table: Employees
    public static final String TABLE_EMPLOYEES = "employees";
    public static final String COL_E_ID = "id";
    public static final String COL_E_NAME = "name";
    public static final String COL_E_EMAIL = "email";
    public static final String COL_E_DEPT = "department";
    public static final String COL_E_ROLE = "role";
    public static final String COL_E_PHONE = "phone";
    public static final String COL_E_IS_PRESENT = "is_present";

    private static AttendanceDatabaseHelper instance;

    public static synchronized AttendanceDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AttendanceDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private AttendanceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + " ("
                + COL_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EMP_ID + " TEXT NOT NULL, "
                + COL_EMP_NAME + " TEXT, "
                + COL_DATE + " TEXT NOT NULL, "
                + COL_CHECK_IN + " TEXT, "
                + COL_CHECK_OUT + " TEXT, "
                + COL_STATUS + " TEXT DEFAULT 'PRESENT', "
                + COL_LOC_IN + " TEXT, "
                + COL_LOC_OUT + " TEXT, "
                + COL_IS_SYNCED + " INTEGER DEFAULT 0)";
        db.execSQL(createAttendanceTable);

        String createEmployeesTable = "CREATE TABLE " + TABLE_EMPLOYEES + " ("
                + COL_E_ID + " TEXT PRIMARY KEY, "
                + COL_E_NAME + " TEXT NOT NULL, "
                + COL_E_EMAIL + " TEXT NOT NULL, "
                + COL_E_DEPT + " TEXT, "
                + COL_E_ROLE + " TEXT DEFAULT 'EMPLOYEE', "
                + COL_E_PHONE + " TEXT, "
                + COL_E_IS_PRESENT + " INTEGER DEFAULT 0)";
        db.execSQL(createEmployeesTable);

        seedDemoEmployees(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    private void seedDemoEmployees(SQLiteDatabase db) {
        insertEmployeeInternal(db, new Employee("EMP001", "Aaqib Khan", "aaqib@company.com", "Engineering", "EMPLOYEE", "+1-555-0101", false));
        insertEmployeeInternal(db, new Employee("EMP002", "Sarah Connor", "sarah@company.com", "Product & Operations", "EMPLOYEE", "+1-555-0102", true));
        insertEmployeeInternal(db, new Employee("EMP003", "David Miller", "david@company.com", "Quality Assurance", "EMPLOYEE", "+1-555-0103", false));
        insertEmployeeInternal(db, new Employee("EMP004", "Emma Watson", "emma@company.com", "UI/UX Design", "EMPLOYEE", "+1-555-0104", true));
        insertEmployeeInternal(db, new Employee("ADM001", "Admin Director", "admin@company.com", "Executive", "ADMIN", "+1-555-0999", true));
    }

    private void insertEmployeeInternal(SQLiteDatabase db, Employee emp) {
        ContentValues cv = new ContentValues();
        cv.put(COL_E_ID, emp.getId());
        cv.put(COL_E_NAME, emp.getName());
        cv.put(COL_E_EMAIL, emp.getEmail());
        cv.put(COL_E_DEPT, emp.getDepartment());
        cv.put(COL_E_ROLE, emp.getRole());
        cv.put(COL_E_PHONE, emp.getPhone());
        cv.put(COL_E_IS_PRESENT, emp.isPresent() ? 1 : 0);
        db.insertWithOnConflict(TABLE_EMPLOYEES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // --- Attendance Operations ---

    public long recordCheckIn(String empId, String empName, String date, String time, String location) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EMP_ID, empId);
        cv.put(COL_EMP_NAME, empName);
        cv.put(COL_DATE, date);
        cv.put(COL_CHECK_IN, time);
        cv.put(COL_STATUS, "PRESENT");
        cv.put(COL_LOC_IN, location);
        cv.put(COL_IS_SYNCED, 0);

        long id = db.insert(TABLE_ATTENDANCE, null, cv);
        markEmployeePresent(empId, true);
        return id;
    }

    public boolean recordCheckOut(String empId, String date, String time, String location) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CHECK_OUT, time);
        cv.put(COL_LOC_OUT, location);
        cv.put(COL_IS_SYNCED, 0);

        int rows = db.update(TABLE_ATTENDANCE, cv, COL_EMP_ID + " = ? AND " + COL_DATE + " = ?", new String[]{empId, date});
        markEmployeePresent(empId, false);
        return rows > 0;
    }

    public AttendanceRecord getTodayAttendance(String empId, String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                COL_EMP_ID + " = ? AND " + COL_DATE + " = ?",
                new String[]{empId, date}, null, null, COL_ATTENDANCE_ID + " DESC", "1");

        AttendanceRecord record = null;
        if (cursor != null && cursor.moveToFirst()) {
            record = cursorToRecord(cursor);
            cursor.close();
        }
        return record;
    }

    public List<AttendanceRecord> getAttendanceHistory(String empId) {
        List<AttendanceRecord> records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                COL_EMP_ID + " = ?", new String[]{empId},
                null, null, COL_ATTENDANCE_ID + " DESC", "30");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                records.add(cursorToRecord(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return records;
    }

    private AttendanceRecord cursorToRecord(Cursor cursor) {
        return new AttendanceRecord(
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_ATTENDANCE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_EMP_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_EMP_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECK_IN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECK_OUT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOC_IN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOC_OUT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_SYNCED)) == 1
        );
    }

    // --- Employee Operations ---

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, null, null, null, null, COL_E_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursorToEmployee(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Employee> getPresentEmployees() {
        List<Employee> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, COL_E_IS_PRESENT + " = 1", null, null, null, COL_E_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursorToEmployee(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void markEmployeePresent(String empId, boolean isPresent) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_E_IS_PRESENT, isPresent ? 1 : 0);
        db.update(TABLE_EMPLOYEES, cv, COL_E_ID + " = ?", new String[]{empId});
    }

    public void addEmployee(Employee emp) {
        SQLiteDatabase db = getWritableDatabase();
        insertEmployeeInternal(db, emp);
    }

    private Employee cursorToEmployee(Cursor cursor) {
        return new Employee(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_DEPT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_ROLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_E_PHONE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_E_IS_PRESENT)) == 1
        );
    }

    public int markAllRecordsSynced() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_IS_SYNCED, 1);
        return db.update(TABLE_ATTENDANCE, cv, COL_IS_SYNCED + " = 0", null);
    }
}
