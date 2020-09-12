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
import com.vilect.librarymanagement.activity.BookActivity;
import com.vilect.librarymanagement.model.BookModel;
import com.vilect.librarymanagement.model.BookTypeModel;

import static com.vilect.librarymanagement.LibraryClass.bookArrayList;

public class BookDAO {

    private Context context;
    private DatabaseReference db;
    private String bookID;

    public BookDAO(Context context) {
        this.context = context;
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    public void insert(String title, String author, String bookTypeId, int quantity, int price, String publisher)
    {
        bookID = db.child("book").push().getKey();

        db.child("book").child(bookID).setValue(new BookModel(bookID, title, author, bookTypeId, quantity, price, publisher), new DatabaseReference.CompletionListener() {
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
                bookArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    bookArrayList.add(data.getValue(BookModel.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        db.child("book").addListenerForSingleValueEvent(valueEventListener);
    }

    public void  getAll()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    bookArrayList.add(data.getValue(BookModel.class));
                }

                ((BookActivity)context).setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Get Data Failed! Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        db.child("book").addValueEventListener(valueEventListener);
    }

    public void update(String id, String title, String author, String bookTypeId, int quantity, int price, String publisher)
    {
        db.child("book").child(id).setValue(new BookModel(id, title, author, bookTypeId, quantity, price, publisher), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void delete(String id)
    {
        db.child("book").child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
