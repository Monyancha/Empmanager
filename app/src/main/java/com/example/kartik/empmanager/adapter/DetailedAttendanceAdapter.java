package com.example.kartik.empmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kartik.empmanager.DetailedAttendance;
import com.example.kartik.empmanager.R;
import com.example.kartik.empmanager.data.EmployeeContract;

/**
 * Created by kartik on 26/10/18.
 */

public class DetailedAttendanceAdapter extends RecyclerView.Adapter<DetailedAttendanceAdapter.DetailedAttendanceViewHolder> {
    private int mNumberOfItems ;
    Cursor mCursor ;
    String mMonth;

    public DetailedAttendanceAdapter(int numberOfItems , Cursor data , String month ){
        mNumberOfItems = numberOfItems;
        mCursor = data ;
        mMonth = month;
    }

    @Override
    public DetailedAttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_detailed_attendance_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false ;

        View view = inflater.inflate(layoutIdForListItem , parent , shouldAttachToParentImmediately);
        DetailedAttendanceViewHolder detailedAttendanceViewHolder = new DetailedAttendanceViewHolder(view);

        return detailedAttendanceViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailedAttendanceViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String hour = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_HOURS));
        String ot = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_OVER_TIME));
        int intStatus = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_ENTRY));
        String status = "A" ;
        String date = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_DATE));
        if(intStatus == EmployeeContract.EMP_WORKING){
            status = "P";
        }
        int emp_id = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_ATTENDANCE_EMP_ID));
        String completeDate = date+"/" +mMonth ;

        holder.bind(hour , ot , status , completeDate , emp_id);
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }


    class DetailedAttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView detailedAHour ;
        TextView detailedAOT ;
        TextView detailedStatus ;
        TextView detailedDate ;


        public DetailedAttendanceViewHolder(View itemView) {
            super(itemView);
            detailedAHour = itemView.findViewById(R.id.hourSingleEmployee);
            detailedAOT = itemView.findViewById(R.id.oTsingleEmployee);
            detailedStatus = itemView.findViewById(R.id.statusSinglEmployee);
            detailedDate = itemView.findViewById(R.id.dateSingleEmployee);

        }

        void bind(String hour , String ot , String status , String date , int emp_id){
            detailedStatus.setText(status);
            detailedDate.setText(date);
            detailedAHour.setText(hour);
            detailedAOT.setText(ot);
            itemView.setTag(R.string.emp_id , emp_id);
            itemView.setTag(R.string.month , mMonth);
        }

        @Override
        public void onClick(View v) {

        }
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
