package com.vilect.librarymanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vilect.librarymanagement.LibraryClass;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.model.BillModel;

import static com.vilect.librarymanagement.LibraryClass.loggedUser;
import static com.vilect.librarymanagement.LibraryClass.userArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private CheckBox chkRemember;
    private TextInputEditText etUser;
    private TextInputEditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        setOnClick();
        rememberAccount();

        LibraryClass libraryClass = new LibraryClass(LoginActivity.this);

    }

    private void mapping()
    {
        btnLogin = findViewById(R.id.btnLogin);
        chkRemember = findViewById(R.id.chkRemember);
        etPass = findViewById(R.id.etPassword);
        etUser = findViewById(R.id.etUserName);
    }

    private void setOnClick()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = etUser.getText().toString();
                String pass = etPass.getText().toString();
                String name = "";
                String phone = "";
                boolean result = false;

                for (int i = 0; i < userArrayList.size(); i++)
                {
                    if (id.equals(userArrayList.get(i).getUsername()))
                    {
                        if (pass.equals(userArrayList.get(i).getPassword()))
                        {
                            name = userArrayList.get(i).getName();
                            result = true;
                            phone = userArrayList.get(i).getPhone();
                            break;
                        }
                    }
                }
                
                if (result)
                {
                    Toast.makeText(LoginActivity.this, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
                    loggedUser[0] = id;
                    loggedUser[1] = pass;
                    loggedUser[2] = name;
                    loggedUser[3] = phone;
                    Intent intent = new Intent(LoginActivity.this, BillActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Username or password is not correct!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void rememberAccount()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains("id"))
        {
            etUser.setText(sharedPreferences.getString("id", ""));
            etPass.setText(sharedPreferences.getString("pass", ""));
            chkRemember.setChecked(true);
        }

        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked)
                {
                    editor.putString("id", etUser.getText().toString());
                    editor.putString("pass", etPass.getText().toString());
                }
                else
                {
                    editor.clear();
                }
                editor.commit();
            }
        });
    }
}
