package com.example.kartik.empmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.example.kartik.empmanager.R;
import com.example.kartik.empmanager.data.EmployeeContract;

import java.util.HashMap;

public class TodayAttendanceAdapter1  extends RecyclerView.Adapter<TodayAttendanceAdapter1.TodayAttendanceViewHolder>{

    private int mNumberItems ;
    private Cursor mEmpCursor ;
    private boolean isHourTextViewClicked ;
    private boolean isOverTimeTextViewClicked ;
    public static HashMap<Integer , Attendance> attendanceHashMap ;

    final private ListItemClickListener mOnClickListener ;

    public TodayAttendanceAdapter1( int numberOfItems , Cursor data , ListItemClickListener listener ) {
        mNumberItems = numberOfItems;
        mEmpCursor = data;
        this.mOnClickListener = listener ;
        initHashMap();

    }

    @Override
    public TodayAttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.today_atendance_list_item ;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem , parent , shouldAttachToParentImmediately);
        TodayAttendanceViewHolder viewHolder = new TodayAttendanceViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodayAttendanceViewHolder holder, int position) {

        mEmpCursor.moveToPosition(position);
        String name = mEmpCursor.getString(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME));
        //int emp_id = (int)holder.switchAttendance.getTag();
        int emp_id = mEmpCursor.getInt(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ALAIS_EMP_ID));
        Attendance attendance = attendanceHashMap.get(emp_id);
        boolean isPresent = attendance.getMIsP();
        String hour = attendance.getmHours();
        String overTime = attendance.getmOverTime();

        holder.bind(name , isPresent , hour , overTime , emp_id);

    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class TodayAttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,  View.OnTouchListener{
        TextView listTAEmpName ;
        CheckBox switchAttendance ;
        EditText mOverTime ;
        EditText mHour ;

        public TodayAttendanceViewHolder(View itemView) {
            super(itemView);

            listTAEmpName = itemView.findViewById(R.id.listTAEmpName);
            switchAttendance = itemView.findViewById(R.id.switchAttendance);
            mHour = itemView.findViewById(R.id.editTextHours);
            mOverTime = itemView.findViewById(R.id.editTextOverTime);

            switchAttendance.setOnClickListener(this);
            mHour.setOnTouchListener(this);
            //mHour.setOnClickListener(this);
            mOverTime.setOnTouchListener(this);

            //mOverTime.addTextChangedListener(this);
        }

        void bind(String name , boolean isPresent , String hour , String overTime , int emp_id){
            listTAEmpName.setText(name);
            switchAttendance.setChecked(isPresent);
            mHour.setText(hour);
            mOverTime.setText(overTime);
            mHour.setTag(emp_id);
            mOverTime.setTag(emp_id);
            switchAttendance.setTag(emp_id);


        }

        @Override
        public void onClick(View v) {
            boolean isChecked = switchAttendance.isChecked();
            int clicked = getAdapterPosition();
            if(v.getId() == switchAttendance.getId()){
                int emp_id = (int)v.getTag();
                mOnClickListener.onListItemClick(emp_id , isChecked , clicked);
            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int emp_id = (int)v.getTag();
            boolean isChecked = switchAttendance.isChecked() ;
            String ot= mOverTime.getText().toString();
            String h = mHour.getText().toString();

            if(v.getId() == mHour.getId()){
                if(isOverTimeTextViewClicked == true){
                    isOverTimeTextViewClicked = false;
                }
                isHourTextViewClicked = mOnClickListener.onHourItemClick();
                MyCustomTextChangeListener myCustomTextChangeListener = new MyCustomTextChangeListener(isChecked , emp_id , ot);
                mHour.addTextChangedListener(myCustomTextChangeListener);
            }

            if(v.getId() == mOverTime.getId()){
                if(isHourTextViewClicked == true){
                    isHourTextViewClicked = false ;
                }
                isOverTimeTextViewClicked = mOnClickListener.onOverTimeClick();
                MyCustomTextChangeListener myCustomTextChangeListener = new MyCustomTextChangeListener(isChecked , emp_id , h);
                mOverTime.addTextChangedListener(myCustomTextChangeListener);

            }
            return false;
        }
    }

    public class Attendance {
        boolean mIsP ;
        String mHours ;
        String mOverTime ;

        public Attendance(boolean isP, String hours, String overtime){
            mIsP = isP ;
            mHours = hours ;
            mOverTime = overtime ;
        }
        public boolean getMIsP(){
            return mIsP ;
        }

        public String getmHours(){
            return mHours;
        }

        public String getmOverTime(){
            return mOverTime;
        }

    }

    public HashMap<Integer , Attendance> getAttendanceHashMap(){
        return attendanceHashMap ;
    }

    public Cursor getmEmpCursor(){
        return mEmpCursor ;
    }

    public interface ListItemClickListener{
        void onListItemClick(int emp_id , boolean isChecked , int clickedItemIndex);
        void onHourItemWatch(int emp_id , String hour , boolean isChecked , String overTime );
        void onOvertimeItemWatch(int emp_id , String overTime , boolean isChecked , String hour );
        boolean onHourItemClick();
        boolean onOverTimeClick();

    }

    public Cursor swapCursor(Cursor cursor) {
        if (mEmpCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mEmpCursor;
        this.mEmpCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class MyCustomTextChangeListener implements TextWatcher{

        int emp_id ;
        boolean isChecked ;
        String hour ;
        public MyCustomTextChangeListener(boolean isChecked , int emp_id , String h){
            this.emp_id = emp_id ;
            this.isChecked = isChecked ;
            hour = h ;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            s.toString();

            if(isHourTextViewClicked){
                mOnClickListener.onHourItemWatch(emp_id, s.toString() , isChecked ,hour);
            }

            if(isOverTimeTextViewClicked){
                mOnClickListener.onOvertimeItemWatch(emp_id , s.toString() , isChecked , hour);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void initHashMap(){
        attendanceHashMap = new HashMap<Integer, Attendance>();
        mEmpCursor.moveToFirst();

        String hour ="8";
        String ot = "0";

        do{
            boolean isWorking = true ;
            int emp_id = mEmpCursor.getInt(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ALAIS_EMP_ID));
            if(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.HOURSUM ) != -1 && mEmpCursor.getCount() > 0){

                int status = mEmpCursor.getInt(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ATTENCOUNT));
                hour = mEmpCursor.getString(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.HOURSUM));
                ot = mEmpCursor.getString(mEmpCursor.getColumnIndex(EmployeeContract.EmployeeEntry.OTSUM));

                if(status != EmployeeContract.EMP_WORKING){
                    isWorking = false ;
                }
            }

            attendanceHashMap.put(emp_id , new Attendance(isWorking , hour , ot));

        }while (mEmpCursor.moveToNext());
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

 public TodayAttendanceAdapter1(Context context, Cursor c , TodayAttendanceFragment fragment , ListView listView) {
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



 class CustomtextWatcher implements TextWatcher{

 int mPosition ;
 boolean mIsChecked ;
 String mH ;
 String mO ;
 boolean mIsHour ;

 public CustomtextWatcher (int position  , boolean isHour){
 mPosition = position;
 mIsHour = isHour ;
 Attendance attendance = attendanceHashMap.get(position);
 mIsChecked = attendance.getMIsP() ;
 mH = attendance.getmHours() ;
 mO = attendance.getmOverTime();
 }

 @Override
 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

 }

 @Override
 public void onTextChanged(CharSequence s, int start, int before, int count) {

 }

 @Override
 public void afterTextChanged(Editable s) {
 if(mIsHour) {
 attendanceHashMap.put(mPosition, new Attendance(mIsChecked, s.toString(), mO));
 }else{
 attendanceHashMap.put(mPosition ,new Attendance(mIsChecked , mH , s.toString()));
 }

 }



 }




 **/






