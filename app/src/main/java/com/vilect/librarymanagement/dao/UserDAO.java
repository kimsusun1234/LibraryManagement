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
import com.vilect.librarymanagement.model.UserModel;

import static com.vilect.librarymanagement.LibraryClass.topBookArrangement;
import static com.vilect.librarymanagement.LibraryClass.userArrayList;

public class UserDAO {

    private DatabaseReference db;
    private Context context;

    public UserDAO(Context context) {
        db = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public void insert (String username, String password, String name, String phone)
    {
        db.child("user").child(username).setValue(new UserModel(username, password, name, phone), new DatabaseReference.CompletionListener() {
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
                userArrayList.clear();

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    userArrayList.add(data.getValue(UserModel.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        db.child("user").addValueEventListener(valueEventListener);
    }

    public void update(String id, String name, String pass, String phone)
    {
        db.child("user").child(id).child("password").setValue(pass);
        db.child("user").child(id).child("name").setValue(name);
        db.child("user").child(id).child("phone")   .setValue(phone);
        db.child("user").child(id).child("username").setValue(id);
    }
}
