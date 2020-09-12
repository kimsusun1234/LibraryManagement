package com.vilect.librarymanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.vilect.librarymanagement.LibraryClass;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.BookTypeRecyclerViewCustomAdapter;
import com.vilect.librarymanagement.dao.BookTypeDAO;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;
import com.vilect.librarymanagement.model.BillModel;
import com.vilect.librarymanagement.model.BookTypeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.vilect.librarymanagement.LibraryClass.bookTypeArrayList;

public class BookTypeActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvBookType;
    private FloatingActionButton fab;
    private BookTypeDAO bookTypeDAO;
    private ImageView ivSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_type);

        mapping();

        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Book Type");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setOnClick();
        setAdapter();
        setFab();

        bookTypeDAO = new BookTypeDAO(BookTypeActivity.this);
        bookTypeDAO.getAll();
    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutBookType);
        navigationView = findViewById(R.id.navBookType);
        rvBookType = findViewById(R.id.rvBookType);
        fab = findViewById(R.id.fabBookType);
    }

    public void setAdapter()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookTypeActivity.this);
        rvBookType.setLayoutManager(linearLayoutManager);

        //set adapter
        BookTypeRecyclerViewCustomAdapter adapter = new BookTypeRecyclerViewCustomAdapter(BookTypeActivity.this, this);
        rvBookType.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setOnClick() {

        //header oỬ vị trí số 0 vì chỉ có 1 layout header được sử dụng
        View view = navigationView.getHeaderView(0);
        ivSetting = view.findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookTypeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itBook: {
                        Intent intent = new Intent(BookTypeActivity.this, BookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBookType: {
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itAnalytics:
                    {
                        Intent intent = new Intent(BookTypeActivity.this, AnalyticsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBill: {
                        Intent intent = new Intent(BookTypeActivity.this, BillActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itTopBook: {
                        Intent intent = new Intent(BookTypeActivity.this, TopBookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
                return false;
            }
        });
    }

    //set fab
    private void setFab()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookTypeActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.custom_dialog_add_book_type, null, false));
                builder.setPositiveButton("Save", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //mapping
                final TextInputEditText etName = alertDialog.findViewById(R.id.etNameAddBookTypeDialog);
                final TextInputEditText etPosition = alertDialog.findViewById(R.id.etPositionAddBookTypeDialog);
                final EditText etDes = alertDialog.findViewById(R.id.etDesAddBookTypeDialog);
                Button btnSave = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        String position = etPosition.getText().toString();
                        String des = etDes.getText().toString();

                        if (validForm(name, position))
                        {
                            bookTypeDAO.insert(name, des, Integer.parseInt(position));
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    //kiem tra thong tin nguoi dung nhap vao
    private boolean validForm(String name, String position)
    {
        if (name.equals(""))
        {
            Toast.makeText(BookTypeActivity.this, "Name can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (position.equals(""))
            {
                Toast.makeText(BookTypeActivity.this, "Position can't be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                if (!position.matches("\\d+"))
                {
                    Toast.makeText(BookTypeActivity.this, "Position must be number", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
    }

    @Override
    public void setOnItemClickListener(int position) {
        final String id = bookTypeArrayList.get(position).getId();
        Toast.makeText(BookTypeActivity.this, ""+position, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(BookTypeActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog_book_type, null, false));


        //set some button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Update", null);
        builder.setNeutralButton("Delete", null);

        //show Dialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //mapping
        Button btnUpdate = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnDelete = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        final TextInputEditText etName = alertDialog.findViewById(R.id.etBookTypeNameDialog);
        final TextInputEditText etPosition = alertDialog.findViewById(R.id.etBookTypePositionDialog);
        final EditText etDes = alertDialog.findViewById(R.id.etBookTypeDesDialog);

        //set button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String position = etPosition.getText().toString();
                String des = etDes.getText().toString();

                if (validForm(name, position))
                {
                    bookTypeDAO.update(id, name, des, position);
                    alertDialog.dismiss();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(BookTypeActivity.this);
                builder1.setTitle("Are you sure?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookTypeDAO.delete(id);
                        alertDialog.dismiss();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        });

        //display data
        etName.setText(bookTypeArrayList.get(position).getName());
        etDes.setText(bookTypeArrayList.get(position).getDes());
        etPosition.setText(bookTypeArrayList.get(position).getPosition()+"");
    }
}
