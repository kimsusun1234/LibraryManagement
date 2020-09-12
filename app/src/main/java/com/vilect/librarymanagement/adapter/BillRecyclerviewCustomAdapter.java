package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vilect.librarymanagement.LibraryClass;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;
import com.vilect.librarymanagement.model.BillModel;

import java.util.ArrayList;

import static com.vilect.librarymanagement.LibraryClass.billArrayList;
import static com.vilect.librarymanagement.LibraryClass.caculateAmount;

public class BillRecyclerviewCustomAdapter extends RecyclerView.Adapter<BillRecyclerviewCustomAdapter.ViewHolder> {

    private Context context;
    private OnRecyclerViewItemClickListener listener;

    public BillRecyclerviewCustomAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //inflate
    @NonNull
    @Override
    public BillRecyclerviewCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_bill, parent, false);

        return new ViewHolder(view, listener);
    }

    //display data
    @Override
    public void onBindViewHolder(@NonNull BillRecyclerviewCustomAdapter.ViewHolder holder, int position) {

        holder.txtBillDate.setText(billArrayList.get(position).getDate());
        holder.txtBillCost.setText(""+caculateAmount(billArrayList.get(position).getId()));

    }

    @Override
    public int getItemCount() {
        return billArrayList.size();
    }


    //mapping place
    //implement View.OnclickListener để bắt sự kiện onclick
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtBillDate;
        private TextView txtBillCost;
        private OnRecyclerViewItemClickListener listener;

        public ViewHolder(@NonNull View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);

            txtBillDate = itemView.findViewById(R.id.txtBillDate);
            txtBillCost = itemView.findViewById(R.id.txtBillCost);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }


        //event onClick
        @Override
        public void onClick(View view)
        {
            //call method setOnClickListener, put the argument in
            listener.setOnItemClickListener(getAdapterPosition());
        }

    }
}
