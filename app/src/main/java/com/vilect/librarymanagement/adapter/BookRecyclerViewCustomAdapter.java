package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.vilect.librarymanagement.LibraryClass.*;

import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;
import com.vilect.librarymanagement.model.BookModel;

public class BookRecyclerViewCustomAdapter extends RecyclerView.Adapter<BookRecyclerViewCustomAdapter.ViewHolder> {


    private Context context;
    private OnRecyclerViewItemClickListener listener;

    public BookRecyclerViewCustomAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_book, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtTitle.setText(bookArrayList.get(position).getTitle());
        holder.txtAuthor.setText(bookArrayList.get(position).getAuthor());
        holder.txtAmount.setText(""+bookArrayList.get(position).getQuantity());
        holder.txtPrice.setText(bookArrayList.get(position).getPrice()+" Đ");

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView txtTitle;
        private TextView txtAuthor;
        private TextView txtAmount;
        private TextView txtPrice;
        private OnRecyclerViewItemClickListener listener;

        public ViewHolder(@NonNull View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtBookTitle);
            txtAuthor = itemView.findViewById(R.id.txtBookAuthor);
            txtAmount = itemView.findViewById(R.id.txtBookAmount);
            txtPrice = itemView.findViewById(R.id.txtBookPrice);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.setOnItemClickListener(getAdapterPosition());
        }
    }

}
