package com.example.kartik.empmanager;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kartik.empmanager.adapter.TodayAttendanceAdapter1;
import com.example.kartik.empmanager.data.EmployeeContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class TodayAttendanceFragment1 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> , TodayAttendanceAdapter1.ListItemClickListener{

    RecyclerView employeeListView ;

    Cursor mCursor ;
    ProgressBar progressBar;
    TodayAttendanceAdapter1 mTodayAttendanceAdapter = null;
    boolean mIsGetAtten ;
    private String mDay ;
    private String mMonth ;
    private String mYear ;

    private static final int TODAY_ATTENDACE_LOADER = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mIsGetAtten = false ;
        String[] todayDate = getDate();
        mDay = todayDate[0];
        mMonth = todayDate[1];
        mYear = todayDate[2];
        checkIfAttenExist();
        getLoaderManager().initLoader(TODAY_ATTENDACE_LOADER , null , this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkIfAttenExist(){

        String[] selectionArgs = {mDay , mMonth , mYear};
        Uri uri = EmployeeContract.IS_ATTENDANCE_PRESENT_URI;
        Cursor c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            c = getContext().getContentResolver().query(uri , null , null, selectionArgs , null , null);
        }
        c.moveToFirst();
        int count = c.getInt(0);
        if(count > 0){
            mIsGetAtten = true ;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu , menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.allP:
                allP(item.isChecked());
                item.setChecked(!(item.isChecked()));
                break;
            case R.id.allSame:
                showPrompt();
                break;
            case R.id.addAtten:
                if(mCursor.getCount() >0 ) {
                    new MyAsycTask().execute(null, null, null);
                }
                break;
            case R.id.getAtten:
                mIsGetAtten = true ;
                showGetAttenPrompt();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_today1 , container , false);
        getActivity().setTitle("Today's Attendance");

        progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        employeeListView = view.findViewById(R.id.tAddentanceRV);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        employeeListView.setLayoutManager(layoutManager);
        employeeListView.setHasFixedSize(false);

        //TextView emptyView = view.findViewById(R.id.aTTextView);
        //emptyView.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = getProjectioin(mIsGetAtten);
        String selection = getSelection(mIsGetAtten);
        String[] selectionArgs = getSelectionArgs(mIsGetAtten);
        Uri uri = getUri(mIsGetAtten);
        return new CursorLoader(getContext() ,
               uri,
                projection ,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {

        mIsGetAtten = false ;
        setmCursor(cursor);
        if(cursor.getCount() > 0) {
            mTodayAttendanceAdapter = new TodayAttendanceAdapter1(cursor.getCount(), cursor , this );
            employeeListView.setAdapter(mTodayAttendanceAdapter);

        }else{
            //TextView emptyView = view.findViewById(R.id.aTTextView);
            //emptyView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mCursor.getCount() > 0) {
            mTodayAttendanceAdapter.swapCursor(null);
        }
    }

    @Override
    public void onListItemClick(int emp_id , boolean isChecked , int clickedItemIndex) {
        HashMap<Integer , TodayAttendanceAdapter1.Attendance> attendanceHashMap = mTodayAttendanceAdapter.getAttendanceHashMap();
        String h = "0";
        if(isChecked){
            h = "8";
        }
        attendanceHashMap.put(emp_id , mTodayAttendanceAdapter.new Attendance(isChecked , h , "0"));
        mTodayAttendanceAdapter.notifyItemChanged(clickedItemIndex);

    }

    @Override
    public void onHourItemWatch(int emp_id, String hour, boolean isChecked, String overTime ) {
        HashMap<Integer , TodayAttendanceAdapter1.Attendance> attendanceHashMap = mTodayAttendanceAdapter.getAttendanceHashMap();attendanceHashMap.put(emp_id , mTodayAttendanceAdapter.new Attendance(isChecked , hour , overTime));
        attendanceHashMap.put(emp_id , mTodayAttendanceAdapter.new Attendance(isChecked , hour , overTime));
        //TodayAttendanceAdapter1.Attendance a = attendanceHashMap.get(0);
        //Toast.makeText(getContext() , a.getmHours() , Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onOvertimeItemWatch(int emp_id, String overTime, boolean isChecked, String hour) {
        HashMap<Integer , TodayAttendanceAdapter1.Attendance> attendanceHashMap = mTodayAttendanceAdapter.getAttendanceHashMap();
        attendanceHashMap.put(emp_id , mTodayAttendanceAdapter.new Attendance(isChecked , hour , overTime));


    }

    @Override
    public boolean onHourItemClick() {
        return true;
    }

    @Override
    public boolean onOverTimeClick() {
        return true;
    }

    public void setmCursor(Cursor c){
        mCursor = c;
    }

    public Cursor getmCursor(){
        return mCursor ;
    }

    private void allP(boolean isChecked){
        String h = "0";
        if(!isChecked){
            h = "8";
        }
        if(mTodayAttendanceAdapter != null) {
            mCursor.moveToFirst();
            do{
                int emp_id = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ALAIS_EMP_ID));
                mTodayAttendanceAdapter.getAttendanceHashMap().put(emp_id , (mTodayAttendanceAdapter).new Attendance(!isChecked , h , "0"));
            }while (mTodayAttendanceAdapter.getmEmpCursor().moveToNext());
            mTodayAttendanceAdapter.notifyDataSetChanged();
        }
    }

    private void showPrompt(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptViews = li.inflate(R.layout.prompt1 , null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(promptViews);

        final EditText hEditText = (EditText) promptViews
                .findViewById(R.id.editTextPromtHoyr);

        final EditText oEditText = promptViews.findViewById(R.id.editTextpromtOverTime);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(mTodayAttendanceAdapter != null){
                                    mCursor.moveToFirst();
                                    do{
                                        int emp_id = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry._ID));
                                        TodayAttendanceAdapter1.Attendance attendance = mTodayAttendanceAdapter.getAttendanceHashMap().get(emp_id);
                                        boolean isChecked = attendance.getMIsP();
                                        mTodayAttendanceAdapter
                                                .getAttendanceHashMap()
                                                .put(emp_id, (mTodayAttendanceAdapter)
                                                        .new Attendance(isChecked, hEditText.getText().toString(), oEditText.getText().toString()));
                                    }while (mCursor.moveToNext());

                                    mTodayAttendanceAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showGetAttenPrompt(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptViews = li.inflate(R.layout.edit_attendance_prompt , null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(promptViews);

        final EditText dayEditText = promptViews
                .findViewById(R.id.editAtttenETDay);

        final EditText monthEditText = promptViews.findViewById(R.id.editAtttenETMonth);

        final EditText yearEditText = promptViews.findViewById(R.id.editAtttenETYear);

        dayEditText.setText(mDay);
        monthEditText.setText(mMonth);
        yearEditText.setText(mYear);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mDay = dayEditText.getText().toString();
                                mMonth = monthEditText.getText().toString();
                                mYear = yearEditText.getText().toString();
                                checkIfAttenExist();
                                getLoaderManager().restartLoader(TODAY_ATTENDACE_LOADER , null , TodayAttendanceFragment1.this);


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String[] getDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat d = new SimpleDateFormat("dd");
        String date = d.format(c.getTime());

        SimpleDateFormat m = new SimpleDateFormat("MM");
        String month = m.format(c.getTime());

        SimpleDateFormat y = new SimpleDateFormat("YYYY");
        String year = y.format(c.getTime());

        String[] x = {date , month , year};
        return  x ;
    }

    private class MyAsycTask extends AsyncTask<String , String, String >{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {

            //check if record exist for that day if not insert else update with the latest values
            boolean isExist = false;

            if(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.HOURSUM ) != -1 && mCursor.getCount() > 0){
                isExist = true ;
            }

            String error = insertIntoAttendance(isExist);

            return error;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String[] getProjectioin(boolean isGetAtten){
        String[] projection ;
        if(isGetAtten) {
            projection = new String[]{"e." + EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME +
                    " , e." + EmployeeContract.EmployeeEntry._ID + " AS " + EmployeeContract.EmployeeEntry.ALAIS_EMP_ID + ", " +
                    "  e." + EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY + " , " +
                    " a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID + " , " +
                    " SUM  (a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY + " ) AS " + EmployeeContract.EmployeeEntry.ATTENCOUNT + " , " +
                    " SUM (a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS + " ) AS " + EmployeeContract.EmployeeEntry.HOURSUM + " , " +
                    " SUM (a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME + " ) AS " + EmployeeContract.EmployeeEntry.OTSUM};
        }else{
            projection = new String[]{
                    EmployeeContract.EmployeeEntry._ID + " AS " + EmployeeContract.EmployeeEntry.ALAIS_EMP_ID,
                    EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME,
            };

        }

        return projection ;
    }

    private String getSelection(boolean isGetAtten){
        String selection ;
        if(isGetAtten){
            selection = " WHERE a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH + " = ? AND "+
                    " a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR + " = ? AND " +
                    "a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE + " = ? ";
        }else{
            selection = EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS + " = ?";
        }

        return selection ;
    }

    private String[] getSelectionArgs(boolean isGetAtten){
        String[] selectionArgs ;

        if(isGetAtten){
            selectionArgs = new String[]{mMonth , mYear , mDay};
        }else{
            selectionArgs = new String[]{Integer.toString(EmployeeContract.EMP_WORKING)};
        }
        return  selectionArgs ;
    }

    private Uri getUri(boolean isGetAtten){
        Uri uri ;
        if(isGetAtten){
            uri = EmployeeContract.DETAILED_ATTENDANCE_URI;
        }else{
            uri =  EmployeeContract.CONTENT_URI ;
        }

        return uri ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String insertIntoAttendance(boolean isExist){
        HashMap<Integer , TodayAttendanceAdapter1.Attendance >hashMap = mTodayAttendanceAdapter.getAttendanceHashMap();
        Cursor cursor = getmCursor();
        int hashSize = hashMap.size();
        int cSize = cursor.getCount();
        String error = null;
        if(hashSize == cSize){
            cursor.moveToFirst();
            do{
                int emp_id = cursor.getInt(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.ALAIS_EMP_ID));
                Uri uri = getUri(isExist , emp_id ,cursor);
                TodayAttendanceAdapter1.Attendance a = hashMap.get(emp_id);
                ContentValues contentValues = getContentValues(a , emp_id , isExist);
                long row_id ;
                if(isExist){
                    row_id = getContext().getContentResolver().update(uri , contentValues , null , null );
                }else {
                    Uri newUri = getContext().getContentResolver().insert(uri, contentValues);
                    row_id = ContentUris.parseId(newUri);
                }

            }while (cursor.moveToNext());
        }else{
            error = "there was error with cursor and datastructue used to save the recyclerview values ";
        }

        return error ;
    }

    private ContentValues getContentValues(TodayAttendanceAdapter1.Attendance a , int emp_id , boolean isExist){

        ContentValues contentValues = new ContentValues();
        int isP = 0;
        if (a.getMIsP()) {
            isP = 1;
        }

        contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS, Integer.parseInt(a.getmHours()));
        contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME, Integer.parseInt(a.getmOverTime()));
        contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY, isP);
        if(!isExist) {
            contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE, mDay);
            contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH, mMonth);
            contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR, mYear);
            contentValues.put(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID, emp_id);
        }

        return contentValues ;

    }

    private Uri getUri(boolean isExist , int emp_id , Cursor cursor) {
        Uri uri ;
        if(isExist){
            int atten_id = cursor.getInt(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID));
            uri = ContentUris.withAppendedId(EmployeeContract.UPDATE_SINGLE_EMPLOYEE_ATTENDANCE_URI , atten_id);
        }else{
            uri = ContentUris.withAppendedId(EmployeeContract.CONTENT_URI_TODAY_ATTENDANCE_INSERT, emp_id);
        }

        return uri ;
    }
}
