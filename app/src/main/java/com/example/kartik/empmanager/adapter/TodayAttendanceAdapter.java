package com.example.kartik.empmanager.adapter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.example.kartik.empmanager.R;
import com.example.kartik.empmanager.TodayAttendanceFragment;
import com.example.kartik.empmanager.data.EmployeeContract;

import java.util.ArrayList;
import java.util.HashMap;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by kartik on 5/10/18.
 */

public class TodayAttendanceAdapter extends CursorAdapter {

    TodayAttendanceFragment fragment ;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    String hours ;
    String overTime ;
    boolean hourChanged ;

    HashMap<Integer , Attendance> attendanceMap;

    HashMap<Integer , Attendance> changed ;

    public TodayAttendanceAdapter(Context context, Cursor c , TodayAttendanceFragment fragment , ListView listView) {
        super(context, c , 0 );
        this.fragment = fragment ;
        attendanceMap = new HashMap<Integer, Attendance>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.today_atendance_list_item , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view ;
    }


    private static class ViewHolder{
        TextView nameTextView ;
        EditText hourTextView ;
        EditText overTimeTextView ;
        Switch attendanceSwitch ;

        ViewHolder(View view){
            nameTextView = view.findViewById(R.id.listTAEmpName);
            attendanceSwitch = view.findViewById(R.id.switchAttendance);
            hourTextView =  view.findViewById(R.id.editTextHours);
            overTimeTextView = view.findViewById(R.id.editTextOverTime);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        int nameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME);

        String name = cursor.getString(nameColumnIndex);

        viewHolder.nameTextView.setText(name);

        if(fragment.getAllPFlag()){
            viewHolder.attendanceSwitch.setChecked(fragment.getAllP());
            if(!fragment.getAllP()){
                viewHolder.hourTextView.setText("0");
                viewHolder.overTimeTextView.setText("0");
            }else{
                viewHolder.hourTextView.setText("8");
            }
        }

        viewHolder.attendanceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if(cursor.isLast()){
            fragment.setAllPflag(false);
            fragment.setAllSameFlag(false);
            hourChanged = false ;
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class Attendance {
        boolean isPresent ;
        int hours ;
        int overTime ;

        Attendance(boolean isP , int hours , int ot){
            isPresent = isP ;
            this.hours = hours;
            overTime = ot ;
        }
    }

}


/**
if (fragment.getHoursflag()) {
        viewHolder.hourTextView.addTextChangedListener(tW);
        viewHolder.overTimeTextView.addTextChangedListener(tW1);
        } else {
        viewHolder.hourTextView.removeTextChangedListener(tW);
        viewHolder.overTimeTextView.removeTextChangedListener(tW1);
        }

        if (hourChanged) {
        viewHolder.hourTextView.setText(hours);
        viewHolder.overTimeTextView.setText(overTime);
        //hourChanged = false ;
        }

        if(fragment.getAllP()){
        viewHolder.hourTextView.setText("8");
        }else{
        viewHolder.hourTextView.setText("0");
        viewHolder.overTimeTextView.setText("0");
        }

        if(fragment.getAllPFlag()) {
        viewHolder.attendanceSwitch.setChecked(fragment.getAllP());

        }else {

        viewHolder.attendanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
        viewHolder.hourTextView.setText("8");
        } else {
        viewHolder.hourTextView.setText("0");
        viewHolder.overTimeTextView.setText("0");
        }

        }
        });
        }

        if(fragment.getInsetFlag()){
        //query to insert into database
        //getdate
        //emp_id

        long emp_id = getItemId(cursor.getPosition());
        String hour = viewHolder.hourTextView.getText().toString();
        String overtime = viewHolder.overTimeTextView.toString();

        }

        if(cursor.isLast()){
        fragment.setAllPflag(false);
        fragment.setInsertFlag(false);
        hourChanged = false;
        }

 **/


/**
 viewHolder.attendanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
if(b){
viewHolder.hourTextView.setText("8");
}else {
viewHolder.hourTextView.setText("0");
viewHolder.overTimeTextView.setText("0");
}
}
});
 **/

/**
 TextWatcher tW = new TextWatcher() {
@Override
public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

}

@Override
public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
setHours(viewHolder.hourTextView.getText().toString());
hourChanged = true;
notifyDataSetChanged();
}

@Override
public void afterTextChanged(Editable editable) {

}
};

 TextWatcher tW1 = new TextWatcher() {
@Override
public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

}

@Override
public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
setOverTime(viewHolder.overTimeTextView.getText().toString());
hourChanged = true;
notifyDataSetChanged();
}

@Override
public void afterTextChanged(Editable editable) {

}
};
 **/