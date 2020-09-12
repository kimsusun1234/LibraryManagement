package com.vilect.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vilect.librarymanagement.R;

import org.w3c.dom.Text;

import static com.vilect.librarymanagement.LibraryClass.topBookArrayList;
import static com.vilect.librarymanagement.LibraryClass.topBookQuantity;

public class TopBookRecyclerViewCustomAdapter extends RecyclerView.Adapter<TopBookRecyclerViewCustomAdapter.ViewHolder> {

    private Context context;

    public TopBookRecyclerViewCustomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_recycler_view_top_book, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(topBookArrayList.get(position).getTitle());
        holder.txtAuthor.setText(topBookArrayList.get(position).getAuthor());
        holder.txtPublisher.setText(topBookArrayList.get(position).getPublisher());
        holder.txtAmount.setText(""+topBookQuantity.get(position));

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtAuthor;
        TextView txtPublisher;
        TextView txtAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //mapping
            txtTitle = itemView.findViewById(R.id.txtTitleTopBook);
            txtAuthor = itemView.findViewById(R.id.txtAuthorTopBook);
            txtPublisher = itemView.findViewById(R.id.txtPublisherTopBook);
            txtAmount = itemView.findViewById(R.id.txtAmountTopBook);

        }
    }

}

