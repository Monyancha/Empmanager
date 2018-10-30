package com.example.kartik.empmanager;

//import android.app.LoaderManager;



import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kartik.empmanager.adapter.AttendanceAdapter;
import com.example.kartik.empmanager.adapter.EmployeeAdapter;
import com.example.kartik.empmanager.data.EmployeeContract;

import static com.example.kartik.empmanager.data.EmployeeContract.EMPLOYEE_LOADER;

public class EmployeeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {



    EmployeeAdapter mEmployeeAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee , container , false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Employee List");
        getActivity().setTitle("Employee List");
        mEmployeeAdapter = new EmployeeAdapter(getContext() , null , EMPLOYEE_LOADER , EmployeeFragment.this);
        ListView employeeListView = view.findViewById(R.id.lViewEmp);
        employeeListView.setAdapter(mEmployeeAdapter);
        employeeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putLong("emp_id" , id);
                bundle.putInt("Update_emp" , 1);

                AddEmployeeFragment aEF = new AddEmployeeFragment();
                aEF.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container , aEF);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(EMPLOYEE_LOADER , null , this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME ,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS
        };
        //Cursor c = getActivity().getContentResolver().query(EmployeeContract.CONTENT_URI, projection,null,null,null);
        return new CursorLoader(getContext() ,
                EmployeeContract.CONTENT_URI,
                projection ,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        mEmployeeAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mEmployeeAdapter.swapCursor(null);
    }


}
