package com.example.kartik.empmanager;



import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.CursorLoader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.example.kartik.empmanager.adapter.TodayAttendanceAdapter;
import com.example.kartik.empmanager.data.EmployeeContract;

public class TodayAttendanceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ListView employeeListView ;
    boolean hoursFlag ;
    boolean insertFlag ;
    boolean allPflag ;
    boolean allP ;

    public boolean isAllSameFlag() {
        return allSameFlag;
    }

    public void setAllSameFlag(boolean allSameFlag) {
        this.allSameFlag = allSameFlag;
    }

    boolean allSameFlag ;

    TodayAttendanceAdapter mTodayAttendanceAdapter ;


    private static final int TODAY_ATTENDACE_LOADER = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.allP:
                if(item.isChecked()){
                    item.setChecked(false);
                    setAllP(false);
                }else{
                    item.setChecked(true);
                    setAllP(true);

                }
                setAllPflag(true);
                break;
            case R.id.allSame:
                if(item.isChecked()){
                    setHoursFlag(false);
                    item.setChecked(false);
                }else{
                    setHoursFlag(true);
                    item.setChecked(true);
                }
                setAllSameFlag(true);
                break;
            case R.id.addAtten:
                setInsertFlag(true);
                break;
        }
        mTodayAttendanceAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_today , container , false);
        getActivity().setTitle("Today's Attendance");
        employeeListView = view.findViewById(R.id.lViewTdyAttendance);
        mTodayAttendanceAdapter = new TodayAttendanceAdapter(getContext() , null , TodayAttendanceFragment.this ,employeeListView);
        employeeListView.setAdapter(mTodayAttendanceAdapter);

        TextView emptyView = view.findViewById(R.id.aTTextView);
        emptyView.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(TODAY_ATTENDACE_LOADER , null , this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME ,
        };

        String selection = EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS + " = ?" ;
        String[] selectionArgs ={Integer.toString(EmployeeContract.EMP_WORKING) };

        return new CursorLoader(getContext() ,
                EmployeeContract.CONTENT_URI,
                projection ,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {

        mTodayAttendanceAdapter.swapCursor(cursor);
        setAllPflag(false);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mTodayAttendanceAdapter.swapCursor(null);
    }

    public void setAllP(boolean flag){
        allP = flag ;
    }

    public boolean getAllP(){
        return allP ;
    }

    private void setHoursFlag(boolean flag){
        hoursFlag = flag;
    }

    public boolean getHoursflag(){
        return hoursFlag ;
    }

    public boolean getAllPFlag(){
        return allPflag ;
    }

    public void setAllPflag(boolean flag){
        allPflag = flag ;
    }

    public boolean getInsetFlag(){
        return insertFlag ;
    }

    public void setInsertFlag(boolean flag){
        insertFlag = flag ;
    }

}
