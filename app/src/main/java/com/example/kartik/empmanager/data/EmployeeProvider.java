package com.example.kartik.empmanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class EmployeeProvider extends ContentProvider {

    private static final String LOG_TAG = EmployeeProvider.class.getSimpleName();

    private static final int EMPLOYEE = 100;
    private static final int EMPLOYEE_ID = 101 ;
    private static final int WORK_STATUS_ID = 102;
    private static final int TODAY_ATTENDENCE_INSERT = 103 ;
    private static final int LOAD_ATTENDANCE = 104 ;
    private static final int LOAD_SINGLE_ATTENDANCE = 105 ;
    private static final int UPDATE_SINGLE_ATTENDANCE = 106 ;
    private static final int IS_ATTENDANCE_PRESENT = 107 ;

    private EmployeeDBHelper mdbHelper ;

    private final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_EMPLOYEE , EMPLOYEE);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_EMPLOYEE+"/#" , EMPLOYEE_ID );

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_WORK_STATUS + "/#" , WORK_STATUS_ID);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_TODAY_ATTENDANCE_INSERT + "/#" , TODAY_ATTENDENCE_INSERT);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_GET_ATTENDANCE , LOAD_ATTENDANCE);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_GET_SINLE_EMPLOYEE_ATTENDANCE , LOAD_SINGLE_ATTENDANCE);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_UPDATE_SINGLE_EMPLOYEE_ATTENDANCE + "/#" , UPDATE_SINGLE_ATTENDANCE);

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY , EmployeeContract.PATHS_IS_ATTENDANCE_PRESENT , IS_ATTENDANCE_PRESENT);

    }

    @Override
    public boolean onCreate() {
        mdbHelper = new EmployeeDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mdbHelper.getReadableDatabase();

        Cursor c ;

        int match = sUriMatcher.match(uri);
        switch(match){
            case EMPLOYEE:
                c = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME ,projection , selection , selectionArgs , null , null , sortOrder);
                break;
            case EMPLOYEE_ID:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                c = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME , projection , selection , selectionArgs , null , null , sortOrder);
                break;
            case LOAD_ATTENDANCE:
                c = queryAttendance(database , projection , selectionArgs , selection);
                break;

            case LOAD_SINGLE_ATTENDANCE:
                c = database.query(EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE ,
                        projection ,
                        selection ,
                        selectionArgs ,
                        null ,
                        null ,
                        null);
                break ;

            case IS_ATTENDANCE_PRESENT:
                String query = "SELECT COUNT(*) FROM " + EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE +
                        " WHERE " + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE + " = ? AND " +
                        EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH + " = ? AND " +
                        EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR + " = ? " ;

                c = database.rawQuery(query , selectionArgs);
                break ;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return c;
    }


    private Cursor queryAttendance(SQLiteDatabase db , String[] projection , String[] selectionArgs , String selection) {
        Cursor cursor ;
        String query = "SELECT " +
                projection[0] +
                " FROM "+ EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE + " AS a INNER JOIN "+
                EmployeeContract.EmployeeEntry.TABLE_NAME + " AS e ON e."+
                EmployeeContract.EmployeeEntry._ID + " = a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID +
                selection +
                " GROUP BY e." + EmployeeContract.EmployeeEntry._ID +";";

        cursor = db.rawQuery(query , selectionArgs);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case EMPLOYEE_ID:
                return insertEmployee(uri , contentValues);
            case TODAY_ATTENDENCE_INSERT:
                return insertTodayAttendance(uri , contentValues);
            default:
                throw new IllegalArgumentException("Insetrion is not supported for " + uri);
        }

    }

    private Uri insertEmployee(Uri uri, ContentValues contentValues){
        SQLiteDatabase database = mdbHelper.getWritableDatabase();
        long id = database.insert(EmployeeContract.EmployeeEntry.TABLE_NAME , null , contentValues);
        if (id == -1 ){
            Log.e(LOG_TAG , "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri , id);
    }

    private Uri insertTodayAttendance(Uri uri , ContentValues contentValues){
        SQLiteDatabase database = mdbHelper.getWritableDatabase();
        long id = database.insert(EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE , null , contentValues);
        if (id == -1 ){
            Log.e(LOG_TAG , "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri , id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mdbHelper.getWritableDatabase();
        switch(match){

            case EMPLOYEE_ID:
                return updateEmployee(uri , contentValues , database);
            case UPDATE_SINGLE_ATTENDANCE:
                return updateAttendance(uri , contentValues , database);
            default:
                return 0 ;
        }

    }

    private int updateAttendance(Uri uri, ContentValues contentValues , SQLiteDatabase database) {
        String selection = EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID + " = ?";
        String[] selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};

        int  numOfRowAffected = database.update(EmployeeContract.EmployeeEntry.TABLE_ATTENDANCE , contentValues , selection , selectionArgs );

        return numOfRowAffected ;
    }

    private int updateEmployee(Uri uri , ContentValues contentValues , SQLiteDatabase database){

        String selection = EmployeeContract.EmployeeEntry._ID + " = ?";
        String[] selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};

        int  numOfRowAffected = database.update(EmployeeContract.EmployeeEntry.TABLE_NAME , contentValues , selection , selectionArgs );

        return numOfRowAffected ;
    }
}
