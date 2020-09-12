package com.vilect.librarymanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.AddBookRecyclerViewCustomAdapter;

import static com.vilect.librarymanagement.activity.BillActivity.REQUEST_CODE_ADD_BOOK;

public class AddBookActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Button btnNext;
    private Button btnCancel;

    //để adapter ở đây để lấy mảng quantity
    private AddBookRecyclerViewCustomAdapter adapter = new AddBookRecyclerViewCustomAdapter(AddBookActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mapping();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddBookActivity.this);
        rv.setLayoutManager(linearLayoutManager);


        rv.setAdapter(adapter);

        setOnClick();

    }

    private void mapping()
    {
        rv = findViewById(R.id.rvAddBookActivity);
        btnCancel = findViewById(R.id.btnCancel);
        btnNext = findViewById(R.id.btnNext);
    }

    private void setOnClick()
    {
        //lấy dữ liệu số lượng các cuốn sách rồi gửi về cho activity để truyền vào Dialog
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quantity", adapter.quantity);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
