package com.example.kartik.empmanager.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class EmployeeContract {



    public final static String CONTENT_AUTHORITY = "com.example.kartik.empmanager";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATHS_EMPLOYEE = "employee";
    public static final String PATHS_EMPLOYEE_SINGLE = "employee/1";
    public static final String PATHS_WORK_STATUS ="work_status";
    public static final String PATHS_TODAY_ATTENDANCE_INSERT = "attendace";
    public static final String PATHS_GET_ATTENDANCE = "empatten";
    public static final String PATHS_GET_SINLE_EMPLOYEE_ATTENDANCE = "singleEmpAtten";
    public static final String PATHS_UPDATE_SINGLE_EMPLOYEE_ATTENDANCE = "updateEmpAtten" ;
    public static final String PATHS_IS_ATTENDANCE_PRESENT = "isPresent";

    public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATHS_EMPLOYEE);

    public final static Uri CONTENT_URI_SINGLE_ROW = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_EMPLOYEE_SINGLE );

    public final static Uri CONTENT_URI_TODAY_ATTENDANCE_INSERT = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_TODAY_ATTENDANCE_INSERT);

    public final static Uri DETAILED_ATTENDANCE_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_GET_ATTENDANCE);

    public final static Uri SINGLE_EMPLOYEE_ATTENDANE_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_GET_SINLE_EMPLOYEE_ATTENDANCE);

    public final static Uri UPDATE_SINGLE_EMPLOYEE_ATTENDANCE_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_UPDATE_SINGLE_EMPLOYEE_ATTENDANCE);

    public final static Uri IS_ATTENDANCE_PRESENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATHS_IS_ATTENDANCE_PRESENT);


    //For attendance table

    public static final int EMPLOYEE_LOADER = 0;

    private EmployeeContract() {}

    public final static int EMP_WORKING = 1;
    public final static int EMP_NOT_WORKING = 0 ;

    public static final class EmployeeEntry implements BaseColumns{

        public final static String TABLE_NAME = "employee";

        //column names for employee table
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EMP_NAME = "name";
        public final static String COLUMN_EMP_SALARY = "basic_salary";
        public final static String COLUMN_EMP_PHONE = "phone";
        public final static String COLUMN_EMP_WORK_STATUS = "work_status";
        public final static String COLUMN_EMP_JOINING_DATE = "joining_date";

        public final static String TABLE_ATTENDANCE = "attendance";

        //column names for attendance table
        public final static String COLUMN_ATTENDANCE_ID = BaseColumns._ID;
        public final static String COLUMN_ATTENDANCE_EMP_ID = "emp_id";
        public final static String COLUMN_ATTENDANCE_ENTRY = "entry";
        public final static String COLUMN_ATTENDANCE_HOURS = "hours";
        public final static String COLUMN_ATTENDANCE_OVER_TIME = "overtime";
        public final static String COLUMN_ATTENDANCE_DATE = "date" ;
        public final static String COLUMN_ATTENDANCE_MONTH = "month" ;
        public final static String COLUMN_ATTENDANCE_YEAR = "year" ;

        //column for attendanceFragment class
        public static final String HOURSUM= "hoursum";
        public static final String OTSUM= "otsum";
        public static final String ATTENCOUNT= "attencount";
        public static final String ALAIS_EMP_ID = "papu" ;

    }

}
