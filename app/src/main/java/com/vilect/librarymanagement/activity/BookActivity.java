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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vilect.librarymanagement.LibraryClass;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.BookRecyclerViewCustomAdapter;
import com.vilect.librarymanagement.dao.BookDAO;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;
import com.vilect.librarymanagement.model.BillModel;
import com.vilect.librarymanagement.model.BookModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.vilect.librarymanagement.LibraryClass.bookArrayList;
import static com.vilect.librarymanagement.LibraryClass.bookTypeArrayList;

public class BookActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvBook;
    private FloatingActionButton fab;
    private ImageView ivSetting;

    //tao cac bien de kiem tra trong dialog
    boolean titleCheck = false;
    boolean authorCheck = false;
    boolean quantityCheck = false;
    boolean priceCheck = false;
    boolean publisherCheck = false;
    private BookDAO bookDAO;
    String selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mapping();

        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Book Manager");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        setNavigationViewOnClick();
        setAdapter();
        setFab();

        //đặt lại listener
        bookDAO = new BookDAO(BookActivity.this);
        bookDAO.getAll();

    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutBook);
        navigationView = findViewById(R.id.navBook);
        rvBook = findViewById(R.id.rvBook);
        fab = findViewById(R.id.fabBook);
    }

    public void setAdapter()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookActivity.this);
        rvBook.setLayoutManager(linearLayoutManager);

        //set adapter
        BookRecyclerViewCustomAdapter adapter = new BookRecyclerViewCustomAdapter(BookActivity.this, this);
        rvBook.setAdapter(adapter);
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

    private void setNavigationViewOnClick() {

        //header oỬ vị trí số 0 vì chỉ có 1 layout header được sử dụng
        View view = navigationView.getHeaderView(0);
        ivSetting = view.findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itBook: {
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBookType: {
                        Intent intent = new Intent(BookActivity.this, BookTypeActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBill: {
                        Intent intent = new Intent(BookActivity.this, BillActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itAnalytics:
                    {
                        Intent intent = new Intent(BookActivity.this, AnalyticsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itTopBook: {
                        Intent intent = new Intent(BookActivity.this, TopBookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setFab()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.custom_dialog_add_book, null, false));

                builder.setPositiveButton("Save", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //mapping
                final Button btnSave = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                final TextInputEditText etTitle = alertDialog.findViewById(R.id.etTitleAddBookDialog);
                final TextInputEditText etAuthor = alertDialog.findViewById(R.id.etAuthorAddBookDialog);
                final TextInputEditText etPublisher = alertDialog.findViewById(R.id.etPublisherAddBookDialog);
                final TextInputEditText etQuantity = alertDialog.findViewById(R.id.etQuantityAddBookDialog);
                final TextInputEditText etPrice= alertDialog.findViewById(R.id.etPriceAddBookDialog);

                final Spinner spBookType = alertDialog.findViewById(R.id.spAddBookDialog);

                //spinner
                //Spinner don gian nen khong can lam custom adapter

                ArrayList<String> bookTypeList = new ArrayList<>();
                for (int i = 0; i < bookTypeArrayList.size(); i++)
                {
                    bookTypeList.add(bookTypeArrayList.get(i).getName());
                }

                spBookType.setAdapter(new ArrayAdapter(BookActivity.this, android.R.layout.simple_spinner_item, bookTypeList));

                //Button Save
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = etTitle.getText().toString();
                        String author = etAuthor.getText().toString();
                        String bookTypeID = bookTypeArrayList.get(spBookType.getSelectedItemPosition()).getId();
                        String publisher = etPublisher.getText().toString();
                        String quantity = etQuantity.getText().toString();
                        String price = etPrice.getText().toString();

                        if (validForm(title, author, quantity, price, publisher))
                        {
                            BookDAO bookDAO = new BookDAO(BookActivity.this);
                            bookDAO.insert(title, author, bookTypeID, Integer.parseInt(etQuantity.getText().toString()), Integer.parseInt(etPrice.getText().toString()), publisher);

                            Toast.makeText(BookActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    //method disable cac nut bam trong dialog
    //sau moi lan text change se call ham nay de kiem tra xem enable nut bam duoc chua
    private boolean setEnableButton()
    {
        if (titleCheck && authorCheck && publisherCheck && quantityCheck && priceCheck)
        {
            return true;
        }
        return false;
    }

    //OnRecyclerViewItemClick
    @Override
    public void setOnItemClickListener(int position) {

        selectedBook = bookArrayList.get(position).getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog_book, null, false));


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
        final TextInputEditText etTitle = alertDialog.findViewById(R.id.etTitleDialog);
        final TextInputEditText etAuthor = alertDialog.findViewById(R.id.etAuthorDialog);
        final TextInputEditText etQuantity = alertDialog.findViewById(R.id.etQuantityDialog);
        final TextInputEditText etPublisher = alertDialog.findViewById(R.id.etPublisherDialog);
        final TextInputEditText etPrice = alertDialog.findViewById(R.id.etPriceDialog);
        final Spinner spType = alertDialog.findViewById(R.id.spTypeDialog);


        //set Button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String quantity = etQuantity.getText().toString();
                String price = etPrice.getText().toString();
                String bookTypeId = bookTypeArrayList.get(spType.getSelectedItemPosition()).getId();
                String publisher = etPublisher.getText().toString();

                if (validForm(title,author,quantity,price,publisher))
                {
                    bookDAO.update(selectedBook,title, author,bookTypeId,Integer.parseInt(quantity),Integer.parseInt(price), publisher);
                    alertDialog.dismiss();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BookActivity.this);
                builder1.setTitle("Are you sure?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookDAO.delete(selectedBook);
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

        //spinner
        ArrayList<String> bookTypeNameList = new ArrayList<>();
        for (int i = 0; i < bookTypeArrayList.size(); i++)
        {
            bookTypeNameList.add(bookTypeArrayList.get(i).getName());
        }
        spType.setAdapter(new ArrayAdapter<>(BookActivity.this, android.R.layout.simple_spinner_dropdown_item, bookTypeNameList));

        for (int i = 0; i < bookTypeArrayList.size(); i++)
        {
            if (bookTypeArrayList.get(i).getId().equals(bookArrayList.get(position).getBookTypeId()))
            {
                spType.setSelection(i);
                break;
            }
        }

        //editText
        etTitle.setText(bookArrayList.get(position).getTitle());
        etAuthor.setText(bookArrayList.get(position).getAuthor());
        etQuantity.setText(bookArrayList.get(position).getQuantity()+"");
        etPrice.setText(bookArrayList.get(position).getPrice()+"");
        etPublisher.setText(bookArrayList.get(position).getPublisher());
    }

    //valid form in dialog
    private boolean validForm (String title, String author, String quantity, String price, String publisher)
    {
        if (title.equals(""))
        {
            Toast.makeText(BookActivity.this, "Title can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (author.equals(""))
            {
                Toast.makeText(BookActivity.this, "Author can't be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                if (quantity.equals(""))
                {
                    Toast.makeText(BookActivity.this, "Quantity can't be empty!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else
                {
                    if (!quantity.matches("\\d+"))
                    {
                        Toast.makeText(BookActivity.this, "Quantity must be number!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else
                    {
                        if (price.equals(""))
                        {
                            Toast.makeText(BookActivity.this, "Price can't be empty!", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        else
                        {
                            if (!price.matches("\\d+"))
                            {
                                Toast.makeText(BookActivity.this, "Price must be number!", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            else
                            {
                                if (publisher.equals(""))
                                {
                                    Toast.makeText(BookActivity.this, "Publisher can't be empty!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
