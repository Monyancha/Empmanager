package com.example.kartik.empmanager;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kartik.empmanager.adapter.AttendanceAdapter;
import com.example.kartik.empmanager.data.EmployeeContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AttendanceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> , AttendanceAdapter.ListItemClickListener{

    Cursor mCursor ;
    String month ;
    String year ;

    private static final int ATTENDACE_LOADER = 101 ;

    RecyclerView mAttenRecycleView ;

    AttendanceAdapter attendanceAdapter ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        String[] date = getDate();
        bundle.putString("month" , date[0]);
        bundle.putString("year" , date[1]);
        this.month = date[0];
        this.year = date[1];
        getLoaderManager().initLoader(ATTENDACE_LOADER , bundle , this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance , container , false);
        getActivity().setTitle("Calculate Salary");
        mAttenRecycleView = view.findViewById(R.id.rVAttendance);
        final Spinner monthSpinner = view.findViewById(R.id.spinnerMonth);
        final Spinner yearSpinner = view.findViewById(R.id.spinnerYear);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAttenRecycleView.setLayoutManager(layoutManager);
        mAttenRecycleView.setHasFixedSize(true);
        Button searchButton = view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = monthSpinner.getSelectedItem().toString();
                String year = yearSpinner.getSelectedItem().toString();
                Bundle bundle = new Bundle();
                bundle.putString("month" , month);
                bundle.putString("year" , year);
                getLoaderManager().restartLoader(ATTENDACE_LOADER,bundle , AttendanceFragment.this);
            }
        });
        return view;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String month ;
        String year ;
        month = args.getString("month");
        year = args.getString("year");
        this.month = month;
        this.year = year;
        String[] projection = getProjection();
        String selection = getSelection();
        String[] selectionArgs = getSelectionArgs(month , year);

        return new CursorLoader(getContext() ,
                EmployeeContract.DETAILED_ATTENDANCE_URI ,
                projection ,
                selection ,
                selectionArgs,
                null);
    }

    private String[] getProjection(){
        return new String[]{"e."+ EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME +
                " , e."+ EmployeeContract.EmployeeEntry._ID + " AS "+ EmployeeContract.EmployeeEntry.ALAIS_EMP_ID + ", "  +
                "  e."+ EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY + " , " +
                " a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID +" , " +
                " SUM  (a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY + " ) AS "+ EmployeeContract.EmployeeEntry.ATTENCOUNT + " , " +
                " SUM (a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS +" ) AS "+ EmployeeContract.EmployeeEntry.HOURSUM + " , " +
                " SUM (a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME + " ) AS "+ EmployeeContract.EmployeeEntry.OTSUM };
    }

    private String getSelection(){
        return " WHERE a." + EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH + " = ? AND "+
                " a."+ EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR + " = ? " ;
    }

    private String[] getSelectionArgs(String month , String year){
        return new String[]{month , year};
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(data != null){
            attendanceAdapter = new AttendanceAdapter(data.getCount() , data , this );
            mAttenRecycleView.setAdapter(attendanceAdapter);
            mCursor = data ;
        }else{
            Toast.makeText(getContext(),"no data found" , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        attendanceAdapter.swapCursor(null);
    }

    public Cursor getCursor(){
        return mCursor;
    }

    @Override
    public void onListItemClicked(int ClickedItemIndex, int emp_id , String name) {
        Bundle bundle = new Bundle();
        bundle.putInt("emp_id" , emp_id);
        bundle.putString("month" , month);
        bundle.putString("year" , year);
        bundle.putString("name" , name);

        DetailedAttendance dEA = new DetailedAttendance();
        dEA.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container , dEA);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String[] getDate(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat m = new SimpleDateFormat("MM");
        String month = m.format(c.getTime());

        SimpleDateFormat y = new SimpleDateFormat("YYYY");
        String year = y.format(c.getTime());

        String[] x = {month , year};

        return  x ;
    }
}
