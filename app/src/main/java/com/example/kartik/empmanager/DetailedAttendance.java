package com.example.kartik.empmanager;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kartik.empmanager.adapter.AttendanceAdapter;
import com.example.kartik.empmanager.adapter.DetailedAttendanceAdapter;
import com.example.kartik.empmanager.data.EmployeeContract;


public class DetailedAttendance extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Cursor mCursor ;
    String mMonth ;

    private static final int DETAILEDATTENDANCE_LOADER = 110 ;

    RecyclerView mDetailedAttendanceRecycleView ;
    DetailedAttendanceAdapter detailedAttendanceAdapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.fragment_detailed_attendance, container, false);
        if(bundle != null) {

            String name =bundle.getString("name");
            getActivity().setTitle(name);

            mDetailedAttendanceRecycleView = view.findViewById(R.id.rVSingleEmployee );
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mDetailedAttendanceRecycleView.setLayoutManager(layoutManager);
            mDetailedAttendanceRecycleView.setHasFixedSize(true);

        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DETAILEDATTENDANCE_LOADER , null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Bundle bundle = this.getArguments();
        int emp_id = bundle.getInt("emp_id");
        String month = bundle.getString("month");
        String year = bundle.getString("year");
        mMonth = month ;

        String[] projection = {
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ID,
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS ,
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME,
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY,
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID ,
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE
        };

        String selection = EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_MONTH + " = ? AND " +
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_YEAR + " = ? AND " +
                EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID + " = ? " ;

        String[] selectionArgs = new String[]{
                 month , year , Integer.toString(emp_id)
        };


        return new CursorLoader(getContext() ,
                EmployeeContract.SINGLE_EMPLOYEE_ATTENDANE_URI,
                projection ,
                selection ,
                selectionArgs ,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null){
            int x = data.getCount();
            detailedAttendanceAdapter = new DetailedAttendanceAdapter(data.getCount() , data , mMonth);
            mDetailedAttendanceRecycleView.setAdapter(detailedAttendanceAdapter);
            mCursor = data ;

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        detailedAttendanceAdapter.swapCursor(null);
    }
}
