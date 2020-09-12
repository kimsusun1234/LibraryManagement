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
import com.vilect.librarymanagement.model.BookModel;
import com.vilect.librarymanagement.model.DetailBillModel;

import static com.vilect.librarymanagement.LibraryClass.detailBillArrayList;
import static com.vilect.librarymanagement.LibraryClass.topBookArrangement;

public class DetailBillDAO {

    private Context context;
    private DatabaseReference db;
    private String detailBillID;

    public DetailBillDAO(Context context) {
        this.context = context;
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    public void insert(String bookID, String billID, int amount)
    {
        detailBillID = db.child("detailBill").push().getKey();

        db.child("detailBill").child(detailBillID).setValue(new DetailBillModel(detailBillID, bookID, billID, amount), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Added Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAll()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                detailBillArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    detailBillArrayList.add(data.getValue(DetailBillModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        db.child("detailBill").addValueEventListener(valueEventListener);
    }

    public void update(String id, String billID, String bookID, int amount)
    {
        db.child("detailBill").child(id).setValue(new DetailBillModel(id, bookID, billID, amount));
    }

    public void delete(String id)
    {
        db.child("detailBill").child(id).removeValue();
    }
}
