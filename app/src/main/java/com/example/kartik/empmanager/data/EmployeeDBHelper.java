package com.example.kartik.empmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmployeeDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "employee.db";

    private static final int DATABASE_VERSION = 10;

    public EmployeeDBHelper(Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //query for creating employee table
         String SQL_CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + EmployeeContract.EmployeeEntry.TABLE_NAME + "("
                 + EmployeeContract.EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME + " TEXT, "
                 + EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY + " INTEGER NOT NULL, "
                 + EmployeeContract.EmployeeEntry.COLUMN_EMP_JOINING_DATE + " TEXT , "
                 + EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS + " INTEGER NOT NULL , "
                 + EmployeeContract.EmployeeEntry.COLUMN_EMP_PHONE + " INTEGER NOT NULL DEFAULT 0) ;";

         //query for creating attendance table
        String SQL_CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE + "("
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID + " ID NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS+ " INTEGER NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE + " TEXT NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH + " TEXT NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR + " TEXT NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY + " INTEGER NOT NULL , "
                + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME + " INTEGER NOT NULL DEFAULT 0 , "
                + " FOREIGN KEY ( "+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID + " ) REFERENCES "
                + EmployeeContract.EmployeeEntry.TABLE_NAME+" ("+ EmployeeContract.EmployeeEntry._ID +" ) ) ;" ;



         Log.i("dhhelper " , SQL_CREATE_ATTENDANCE_TABLE);
         db.execSQL(SQL_CREATE_EMPLOYEE_TABLE);
         db.execSQL(SQL_CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeContract.EmployeeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE);
        if(oldversion<newVersion){
            onCreate(db);
        }
    }
}
