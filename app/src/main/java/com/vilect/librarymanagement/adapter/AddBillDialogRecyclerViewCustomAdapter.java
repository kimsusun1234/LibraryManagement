package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.model.BookModel;

import java.util.ArrayList;

import static com.vilect.librarymanagement.LibraryClass.bookArrayList;

public class AddBillDialogRecyclerViewCustomAdapter extends RecyclerView.Adapter<AddBillDialogRecyclerViewCustomAdapter.ViewHolder>{

    private Context context;
    private ArrayList<BookModel> dataList;
    private ArrayList<Integer> quantity;

    public AddBillDialogRecyclerViewCustomAdapter(Context context, ArrayList<BookModel> dataList, ArrayList<Integer> quantity) {
        this.context = context;
        this.dataList = dataList;
        this.quantity = quantity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_recycler_view_add_bill_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.txtTitleAddBillDialog.setText(dataList.get(position).getTitle());
        holder.txtAuthorAddBillDialog.setText(dataList.get(position).getAuthor());
        holder.txtQuantityAddBillDialog.setText(""+quantity.get(position));
        holder.txtPriceAddBillDialog.setText(dataList.get(position).getPrice()+" Đ");

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitleAddBillDialog;
        TextView txtAuthorAddBillDialog;
        TextView txtQuantityAddBillDialog;
        TextView txtPriceAddBillDialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleAddBillDialog = itemView.findViewById(R.id.txtTitleAddBillDialog);
            txtAuthorAddBillDialog = itemView.findViewById(R.id.txtAuthorAddBillDialog);
            txtQuantityAddBillDialog = itemView.findViewById(R.id.txtQuantityAddBillDialog);
            txtPriceAddBillDialog = itemView.findViewById(R.id.txtPriceAddBillDialog);
        }
    }
}
