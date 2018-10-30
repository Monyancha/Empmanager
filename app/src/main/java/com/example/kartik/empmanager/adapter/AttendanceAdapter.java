package com.example.kartik.empmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kartik.empmanager.R;
import com.example.kartik.empmanager.data.EmployeeContract;

/**
 * Created by kartik on 24/10/18.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {


    private final ListItemClickListener mListItemClickedListener ;
    private int mNumberOfItems ;
    private Cursor mCursor ;


    public AttendanceAdapter(int numberOfItems , Cursor data , ListItemClickListener listItemClickedListener ){
        mCursor = data;
        mNumberOfItems = numberOfItems;
        this.mListItemClickedListener = listItemClickedListener;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_attendance_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately  = false ;

        View view = inflater.inflate(layoutIdForListItem , parent , shouldAttachToParentImmediately);
        AttendanceViewHolder attendanceViewHolder = new AttendanceViewHolder(view);

        return attendanceViewHolder;
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_NAME));
        String status = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ATTENCOUNT));
        String hour = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.HOURSUM));
        String ot = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.OTSUM));
        int salary = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMP_SALARY));
        String totalSalary = calculateSalary(hour , ot , salary);
        //int emp_id = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry._ID));
        int emp_id = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.ALAIS_EMP_ID));
        holder.bind(name , status , totalSalary , hour , ot , emp_id);
    }

    private String calculateSalary(String hour, String ot, int salary) {
        int totalOt = Integer.parseInt(ot);
        int totalHour = Integer.parseInt(hour);
        int totalWorkingHour = totalHour + totalOt ;
        int hourSalary = salary/(30*8);
        int totalSalary = hourSalary*totalWorkingHour ;
        return String.valueOf(totalSalary) ;
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView attenName;
        TextView attenStatus;
        TextView attenTotalHour;
        TextView attenTotalOT;
        TextView attenSalary;


        public AttendanceViewHolder(View itemView) {
            super(itemView);
            attenName = itemView.findViewById(R.id.fAIName);
            attenStatus = itemView.findViewById(R.id.fAIAtten);
            attenSalary = itemView.findViewById(R.id.fAISalary);
            attenTotalHour = itemView.findViewById(R.id.fAITotalHour);
            attenTotalOT = itemView.findViewById(R.id.fAITotalOverTime);

            itemView.setOnClickListener(this);

        }

        void bind (String name , String status , String salary , String hour , String oT , int emp_id){
            attenName.setText(name);
            attenTotalOT.setText(oT);
            attenTotalHour.setText(hour);
            attenStatus.setText(status);
            attenSalary.setText(salary);

            itemView.setTag(R.string.emp_id , emp_id);
            itemView.setTag(R.string.name , name);
        }

        @Override
        public void onClick(View v) {
            int emp_id = (int)v.getTag(R.string.emp_id);
            String name = (String)v.getTag(R.string.name);
            mListItemClickedListener.onListItemClicked(getAdapterPosition() , emp_id , name);
        }
    }

    public interface ListItemClickListener{
        void onListItemClicked(int ClickedItemIndex , int emp_id , String name);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}
