package com.example.kartik.empmanager;

import android.annotation.SuppressLint;
import android.support.v4.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.kartik.empmanager.data.EmployeeContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEmployeeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    View view ;

    private Uri mCurrentEmployeeUri ;

    private static final int GET_EMPLOYEE_LOADER = 10 ;

    String name ;
    int salary ;
    long phone ;
    String date ;
    boolean update;
    int status ;

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_employee_add ,container , false);

        Button btnAdd = view.findViewById(R.id.buttonAdd);
        Long emp_id ;
        final Bundle bundle = this.getArguments();

        if (bundle != null){
            getActivity().setTitle("Update employee");
            emp_id = bundle.getLong("emp_id");
            mCurrentEmployeeUri  = ContentUris.withAppendedId(EmployeeContract.CONTENT_URI , emp_id);
            Toast.makeText(getContext() , "emp id is " + Long.toString(emp_id) , Toast.LENGTH_SHORT).show();
            update = true ;
            btnAdd.setText("Update");
        }else{
            getActivity().setTitle("Add employee");
            String date = getDate();
            EditText eTDate = view.findViewById(R.id.editTextDate);
            eTDate.setText(date);
            mCurrentEmployeeUri = null ;
            update = false ;
            btnAdd.setText("Add");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                EditText eTsalary = view.findViewById(R.id.editTextSalary);
                EditText eTName = view.findViewById(R.id.editTextName);
                EditText eTphone = view.findViewById(R.id.editTextPhone);
                EditText eTDate = view.findViewById(R.id.editTextDate);
                Switch workStatusSwitch = view.findViewById(R.id.workStatusAddEmpFragSwitch);

                String name = eTName.getText().toString().trim();
                String salaryString = eTsalary.getText().toString();
                String phoneString = String.valueOf(eTphone.getText());

                boolean isPerformDbInsert = sanaityCheck(name , salaryString , phoneString);

                if(!isPerformDbInsert){
                    Toast.makeText(getContext(), "Something is empty" , Toast.LENGTH_SHORT).show();
                }else{
                    Long phone = Long.parseLong(phoneString);
                    int salary = Integer.parseInt(salaryString);

                    String date = eTDate.getText().toString();
                    int isWorking ;
                    if(workStatusSwitch.isChecked()){
                        isWorking = EmployeeContract.EMP_WORKING;
                    }else{
                        isWorking = EmployeeContract.EMP_NOT_WORKING ;
                    }
                    insertEmployee(name, salary, phone, date,isWorking ,update);
                }


            }
        });

        return view ;
    }

    private boolean sanaityCheck(String name , String salary , String phone){
        if(name.isEmpty()|| salary.isEmpty() || phone.isEmpty()){
            return false ;
        }
        return true ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader( GET_EMPLOYEE_LOADER, null ,  this);
        super.onActivityCreated(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void insertEmployee(String name , int salary , Long phone , String date , int isWorking ,boolean update) {
        ContentValues values = new ContentValues();

        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_JOINING_DATE, date);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME, name);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY, salary);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_PHONE, phone);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS , isWorking);

        if (update) {

            int rowsAffected = getActivity().getContentResolver().update(mCurrentEmployeeUri , values , null , null);

            if(rowsAffected == 0){
                Toast.makeText(getContext(), "error with updating contact kartik . Sorry!" , Toast.LENGTH_SHORT).show();
            }else{

                //Toast.makeText(getContext(), name + " upadted" , Toast.LENGTH_SHORT).show();
            }

        } else{
            values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS, EmployeeContract.EMP_WORKING);
            Uri newUri = getActivity().getContentResolver().insert(EmployeeContract.CONTENT_URI_SINGLE_ROW, values);
            if(newUri == null ){
                Toast.makeText(getContext(), "error with saving contact kartik , !sorry " , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Employee Added successfully" , Toast.LENGTH_SHORT).show();
            }
        }




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy ");
        String formattedDate = df.format(c.getTime());
        // Now formattedDate have current date/time
        return formattedDate ;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_PHONE,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_JOINING_DATE ,
                EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS
        };
        Log.i("uri in add emplFragemnt", "at" + mCurrentEmployeeUri);
        if (mCurrentEmployeeUri != null) {
            return new android.support.v4.content.CursorLoader(getContext(), mCurrentEmployeeUri, projection, null, null, null);
        }

        return null ;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME);
            int salaryColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY);
            int phoneColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_PHONE);
            int dateColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_JOINING_DATE);
            int statusColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS);
            name = cursor.getString(nameColumnIndex);
            salary = cursor.getInt(salaryColumnIndex);
            phone = cursor.getLong(phoneColumnIndex);
            date = cursor.getString(dateColumnIndex);
            status = cursor.getInt(statusColumnIndex);
            EditText eTsalary = view.findViewById(R.id.editTextSalary);
            EditText eTName = view.findViewById(R.id.editTextName);
            EditText eTphone = view.findViewById(R.id.editTextPhone);
            EditText eTDate = view.findViewById(R.id.editTextDate);

            eTName.setText(name);

            eTDate.setText(date);

            eTphone.setText(Long.toString(phone));

            eTsalary.setText(Integer.toString(salary));

            Toast.makeText(getContext(), "working status : "+Integer.toString(status) , Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
