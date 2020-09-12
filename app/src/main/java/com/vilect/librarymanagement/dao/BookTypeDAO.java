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
import com.vilect.librarymanagement.activity.BookTypeActivity;
import com.vilect.librarymanagement.model.BookTypeModel;

import static com.vilect.librarymanagement.LibraryClass.bookTypeArrayList;

public class BookTypeDAO {

    private DatabaseReference db;
    private Context context;
    private String bookTypeID;

    public BookTypeDAO(Context context) {
        this.db = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public void insert(String name, String des, int position)
    {
        bookTypeID = db.child("bookType").push().getKey();

        db.child("bookType").child(bookTypeID).setValue(new BookTypeModel(bookTypeID, name, des, position), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Added Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllRuntime()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookTypeArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    bookTypeArrayList.add(data.getValue(BookTypeModel.class));
                }

                Toast.makeText(context, "Get Data Successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        db.child("bookType").addListenerForSingleValueEvent(valueEventListener);
    }

    public void getAll()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookTypeArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    bookTypeArrayList.add(data.getValue(BookTypeModel.class));
                }
                ((BookTypeActivity)context).setAdapter();
                Toast.makeText(context, "Get Data Successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        db.child("bookType").addValueEventListener(valueEventListener);

    }

    public void update(String id, String name, String des, String position)
    {
        db.child("bookType").child(id).setValue(new BookTypeModel(id, name, des, Integer.parseInt(position)), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    public void delete(String id)
    {
        db.child("bookType").child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
