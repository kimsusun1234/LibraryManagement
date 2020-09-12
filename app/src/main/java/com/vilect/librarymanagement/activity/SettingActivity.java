package com.vilect.librarymanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.dao.UserDAO;

import static com.vilect.librarymanagement.LibraryClass.loggedUser;
import static com.vilect.librarymanagement.LibraryClass.userArrayList;

public class SettingActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPhone;
    private TextView txtId;
    private Button btnUpdate;
    private Button btnChangePass;
    private UserDAO userDAO;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userDAO = new UserDAO(SettingActivity.this);
        mapping();

        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Personal Information");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        displayData();

        setBtnOnClick();


    }

    private void mapping()
    {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        txtId = findViewById(R.id.txtID);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnChangePass = findViewById(R.id.btnChangePass);
        toolbar = findViewById(R.id.toolbar);
    }

    //Home button actionbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayData()
    {
        for (int i = 0; i < userArrayList.size(); i++)
        {
            if (loggedUser[1].equals(userArrayList.get(i).getUsername()))
            {
                etName.setText(userArrayList.get(i).getName());
                etPhone.setText(userArrayList.get(i).getPhone());
                txtId.setText(userArrayList.get(i).getUsername());
                return;
            }
        }
    }

    private void setBtnOnClick()
    {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(LayoutInflater.from(SettingActivity.this).inflate(R.layout.custom_dialog_change_pass, null, false));
                builder.setPositiveButton("Ok", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //mapping
                final TextInputEditText etOld = alertDialog.findViewById(R.id.etOldPass);
                final TextInputEditText etNew = alertDialog.findViewById(R.id.etNewPass);
                final TextInputEditText etConfirm = alertDialog.findViewById(R.id.etConfirmPass);
                Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etOld.getText().toString().equals(""))
                        {
                            Toast.makeText(SettingActivity.this, "Old pass must not be blank", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (!etOld.getText().toString().equals(loggedUser[1]))
                            {
                                Toast.makeText(SettingActivity.this, "Old pass is not correct!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if (etConfirm.getText().toString().equals("") || etNew.getText().toString().equals(""))
                                {
                                    Toast.makeText(SettingActivity.this, "New pass & confirm pass must be the same!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if (!etConfirm.getText().toString().equals(etNew.getText().toString()))
                                    {
                                        Toast.makeText(SettingActivity.this, "New pass & confirm pass must be the same!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        userDAO.update(loggedUser[0], loggedUser[2], etNew.getText().toString(), loggedUser[3]);
                                        Toast.makeText(SettingActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }

                                }
                            }
                        }
                    }
                });

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Do you want to update your information?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString();
                        String phone = etPhone.getText().toString();

                        userDAO.update(loggedUser[0], name, loggedUser[1], phone);

                        Toast.makeText(SettingActivity.this, "Update information successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
