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

import static com.vilect.librarymanagement.LibraryClass.bookArrayList;

public class AddBookRecyclerViewCustomAdapter extends RecyclerView.Adapter<AddBookRecyclerViewCustomAdapter.ViewHolder>  {

    private Context context;
    public int[] quantity = new int[bookArrayList.size()];

    public AddBookRecyclerViewCustomAdapter(Context context) {
        this.context = context;

        //ban đầu chưa có sách nào được chọn
        //tạo mảng để chứa thông tin số lượng của mỗi mã sách
        for (int i = 0; i < bookArrayList.size(); i++)
        {
            quantity[i] = 0;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_recycler_view_book_choosing, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.txtTitle.setText(bookArrayList.get(position).getTitle());
        holder.txtAuthor.setText(bookArrayList.get(position).getAuthor());
        holder.txtPrice.setText(bookArrayList.get(position).getPrice()+" Đ");

        holder.btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Khi bấm vào nút +
                //tạo biết count chứa số hiện tại đang hiển thị
                int count = Integer.parseInt(holder.txtAmount.getText().toString());
                //nếu count = số sách hiện tại trong kho
                if ( count == bookArrayList.get(position).getQuantity())
                {
                    //không cộng thêm
                    Toast.makeText(context, "There are only " + count + " books left!", Toast.LENGTH_SHORT).show();
                    quantity[position] = count;
                }
                else
                {
                    //cộng thêm 1, hiển thị, đổi giá trị trong mảng
                    count++;
                    holder.txtAmount.setText(count+"");
                    quantity[position] = count;
                }
            }
        });

        holder.btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.txtAmount.getText().toString());
                if ( count == 0)
                {
                    Toast.makeText(context, "Can't decrease anymore!", Toast.LENGTH_SHORT).show();
                    quantity[position] = count;
                }
                else
                {
                    count--;
                    holder.txtAmount.setText(count+"");
                    quantity[position] = count;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtAuthor;
        TextView txtAmount;
        TextView txtPrice;
        Button btnInc;
        Button btnDec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitleAddBillDialog);
            txtAuthor = itemView.findViewById(R.id.txtAuthorAddBillDialog);
            txtAmount = itemView.findViewById(R.id.txtQuantityAddBillDialog);
            txtPrice = itemView.findViewById(R.id.txtPriceAddBillDialog);
            btnInc = itemView.findViewById(R.id.btnIncreaseAddBookDialog);
            btnDec = itemView.findViewById(R.id.btnDecreaseAddBillDialog);
        }

    }
}
