package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;

import org.w3c.dom.Text;

import static com.vilect.librarymanagement.LibraryClass.bookTypeArrayList;

public class BookTypeRecyclerViewCustomAdapter extends RecyclerView.Adapter<BookTypeRecyclerViewCustomAdapter.ViewHolder> {

    private Context context;
    private OnRecyclerViewItemClickListener listener;

    public BookTypeRecyclerViewCustomAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_book_type, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtName.setText(bookTypeArrayList.get(position).getName());
        holder.txtDes.setText(bookTypeArrayList.get(position).getDes());
        holder.txtPosition.setText(""+bookTypeArrayList.get(position).getPosition());

    }

    @Override
    public int getItemCount() {
        return bookTypeArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private OnRecyclerViewItemClickListener listener;
        private TextView txtName;
        private TextView txtDes;
        private TextView txtPosition;

        public ViewHolder(@NonNull View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            txtName = itemView.findViewById(R.id.txtType);
            txtDes = itemView.findViewById(R.id.txtTypeDes);
            txtPosition = itemView.findViewById(R.id.txtTypePosition);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.setOnItemClickListener(getAdapterPosition());
        }
    }
}
