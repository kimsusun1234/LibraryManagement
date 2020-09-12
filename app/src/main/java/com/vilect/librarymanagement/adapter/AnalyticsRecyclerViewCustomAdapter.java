package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.activity.AddBookActivity;
import com.vilect.librarymanagement.activity.BillActivity;
import com.vilect.librarymanagement.model.BillModel;
import com.vilect.librarymanagement.model.BookModel;

import java.util.ArrayList;

import static com.vilect.librarymanagement.LibraryClass.billArrayList;
import static com.vilect.librarymanagement.LibraryClass.bookArrayList;
import static com.vilect.librarymanagement.LibraryClass.caculateAmount;
import static com.vilect.librarymanagement.LibraryClass.detailBillArrayList;

public class AnalyticsRecyclerViewCustomAdapter extends RecyclerView.Adapter<AnalyticsRecyclerViewCustomAdapter.ViewHolder> {

    private Context context;
    //selectedFilter để truyền ArrayList đã được lọc vào
    private ArrayList<BillModel> dataList;

    public AnalyticsRecyclerViewCustomAdapter(Context context, ArrayList<BillModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mapping
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.txtBillDate.setText(dataList.get(position).getDate());
        holder.txtBillCost.setText(""+caculateAmount(dataList.get(position).getId()));
        //onItemClick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tạo dialog ở đây

                //lấy list sách đã mua và số lượng để truyền vào adapter RecyclerView
                ArrayList<Integer> quantity = new ArrayList<>();
                ArrayList<BookModel> buyBook = new ArrayList<>();

                for (int i = 0; i < detailBillArrayList.size(); i++)
                {
                    //nếu trùng bill id
                    if (billArrayList.get(position).getId().equals(detailBillArrayList.get(i).getBillId()))
                    {
                        //tim sach
                        for (int t = 0; t < bookArrayList.size(); t++)
                        {
                            //nếu trùng book id
                            if (detailBillArrayList.get(i).getBookId().equals(bookArrayList.get(t).getId()))
                            {
                                //add data xong break vòng lặp để tối ưu tốc độ
                                quantity.add(detailBillArrayList.get(i).getAmount());
                                buyBook.add(bookArrayList.get(t));
                                break;
                            }
                        }
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(LayoutInflater.from(context).inflate(R.layout.custom_dialog_analytics, null, false));

                //show Dialog
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //mapping
                RecyclerView rvBillDialog = alertDialog.findViewById(R.id.rvAnalyticsDialog);
                TextView txtTotal = alertDialog.findViewById(R.id.txtTotalAnalyticsDialog);
                txtTotal.setText(caculateAmount(dataList.get(position).getId())+" Đ");

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                rvBillDialog.setLayoutManager(layoutManager);
                AddBillDialogRecyclerViewCustomAdapter adapter = new AddBillDialogRecyclerViewCustomAdapter(context, buyBook, quantity);
                rvBillDialog.setAdapter(adapter);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtBillDate;
        private TextView txtBillCost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBillDate = itemView.findViewById(R.id.txtBillDate);
            txtBillCost = itemView.findViewById(R.id.txtBillCost);
        }
    }
}
