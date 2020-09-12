package com.vilect.librarymanagement.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vilect.librarymanagement.LibraryClass;
import com.vilect.librarymanagement.activity.BillActivity;
import com.vilect.librarymanagement.model.BillModel;

import java.util.ArrayList;

import static com.vilect.librarymanagement.LibraryClass.billArrayList;

public class BillDAO
{
    private Context context;
    DatabaseReference db;
    private String billID;
    private BillActivity billActivity = new BillActivity();

    public BillDAO(Context context)
    {
        this.db = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    //trả về id để điền vào DetailBill
    public String insert(String date)
    {
        //Mỗi id là một node con, chứa các thuộc tính của BillModel
        //Add một dòng mới, lấy id của dòng đó
        //push dùng để tạo ra một node con có id bất kì
        billID = db.child("bill").push().getKey(); //get key của node vừa tạo


        //truy cập vào node bill, sau đó truy cập vào node "billID" và set Value
        db.child("bill").child(billID).setValue(new BillModel(billID, date), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                //Khi thêm thành công
                Toast.makeText(context, "Saved Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
        return billID;
    }

    public void getAllRuntime()
    {
        //Firebase cập nhật dữ liệu theo thời gian thực, vậy nên ta cũng lấy dữ liệu theo thời gian thực
        //tạo một valueEventListener để bắt sự kiện data thay đổi cho một child nào đó
        //onDataChange sẽ được gọi một lần đầu tiên đính kèm vào một DatabaseReference, sau đó sẽ ở chế độ lắng nghe sự kiện
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billArrayList.clear();
                //dữ liệu được trả về là một Object, thông qua đối tượng dataSnapshot

                //với mỗi phần tử kiểu DataSnapshot của collection dataSnapshot.getChildren()
                //lấy phần tử đó ra, ép vào kiểu BillModel r add vào List
                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    BillModel billModel = data.getValue(BillModel.class);
                    billArrayList.add(billModel); //lẤy object về và ép thành kiểu BillModel
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
    };
    //gắn listener cho node "bill" của db(DatabaseReference)
        db.child("bill").addListenerForSingleValueEvent(valueEventListener);
    }

    public void getAll()
    {
        //Firebase cập nhật dữ liệu theo thời gian thực, vậy nên ta cũng lấy dữ liệu theo thời gian thực
        //tạo một valueEventListener để bắt sự kiện data thay đổi cho một child nào đó
        //onDataChange sẽ được gọi một lần đầu tiên đính kèm vào một DatabaseReference, sau đó sẽ ở chế độ lắng nghe sự kiện
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billArrayList.clear();
                //dữ liệu được trả về là một Object, thông qua đối tượng dataSnapshot

                //với mỗi phần tử kiểu DataSnapshot của collection dataSnapshot.getChildren()
                //lấy phần tử đó ra, ép vào kiểu BillModel r add vào List
                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    BillModel billModel = data.getValue(BillModel.class);
                    billArrayList.add(billModel); //lẤy object về và ép thành kiểu BillModel
                }

                ((BillActivity)context).setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        //gắn listener cho node "bill" của db(DatabaseReference)
        db.child("bill").addValueEventListener(valueEventListener);


    }

    public void delete(String billID)
    {
        db.child("bill").child(billID).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
