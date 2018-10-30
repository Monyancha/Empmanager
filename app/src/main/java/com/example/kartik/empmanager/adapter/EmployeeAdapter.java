package com.example.kartik.empmanager.adapter;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartik.empmanager.EmployeeFragment;
import com.example.kartik.empmanager.R;
import com.example.kartik.empmanager.data.EmployeeContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kartik on 4/10/18.
 */

public class EmployeeAdapter extends CursorAdapter  {

    EmployeeFragment fragContext ;
    int emp_loader ;
    Context context ;
    HashMap<Long , Boolean > states ;
    boolean isFirst ;

    private static class ViewHolder {
        TextView nameTextView ;
        CheckBox workStatusSwitch ;


        ViewHolder(View view){
            nameTextView = view.findViewById(R.id.listEmpName);
            workStatusSwitch = view.findViewById(R.id.switchWorkStatus);
        }
    }

    public EmployeeAdapter(Context context , Cursor c , int emp_loader , EmployeeFragment employeeFragment){
        super(context , c , 0);
        fragContext = employeeFragment ;
        this.emp_loader = emp_loader ;
        this.context = context ;
        states = new HashMap<Long, Boolean>();
        isFirst = true ;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.emp_list_item , parent , false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);
        //notifyDataSetChanged();
        return view ;

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder)view.getTag();

        int nameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME);
        int workStatusColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS);

        String name = cursor.getString(nameColumnIndex);
        int status = cursor.getInt(workStatusColumnIndex);

        viewHolder.nameTextView.setText(name);

        //viewHolder.workStatusSwitch.setTag(1, status);
        viewHolder.workStatusSwitch.setTag(R.string.position,cursor.getPosition());
        long id = getItemId(cursor.getPosition());
        if (status == EmployeeContract.EMP_WORKING){
            viewHolder.workStatusSwitch.setTag(R.string.status,true);
            //states.put(id , true);
        }else{
            viewHolder.workStatusSwitch.setTag(R.string.status,false);
            //states.put(id , false);
        }

        viewHolder.workStatusSwitch.setChecked((boolean)viewHolder.workStatusSwitch.getTag(R.string.status));

        viewHolder.workStatusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long emp_id = getItemId((int)v.getTag(R.string.position));
                //boolean state = states.get(emp_id);
                boolean state = (boolean)v.getTag(R.string.status);
                int isWorking ;
                if(!state)
                    isWorking = EmployeeContract.EMP_WORKING ;
                else
                    isWorking = EmployeeContract.EMP_NOT_WORKING;
                //viewHolder.workStatusSwitch.setChecked(state);
                //CheckBox s = v.findViewById(v.getId());
                //s.setChecked(!state);
                ((CheckBox)v).setChecked(state);
                Uri uri = ContentUris.withAppendedId(EmployeeContract.CONTENT_URI, emp_id);
                updateWorkStatus(isWorking, uri , v ,state);
            }
        });


    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    private void updateWorkStatus(int isWorking , Uri uri , View v , boolean state){
        ContentValues values = new ContentValues();

        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMP_WORK_STATUS, isWorking);

        int rowsAffected = context.getContentResolver().update(uri , values , null , null);

        if(rowsAffected == 0){
            Toast.makeText(context, "error with updating contact kartik . Sorry!" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,  " upadted" , Toast.LENGTH_SHORT).show();
            v.setTag(R.string.status , state);
            fragContext.getLoaderManager().restartLoader(emp_loader , null , fragContext);
        }
    }

}

