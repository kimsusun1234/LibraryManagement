package com.vilect.librarymanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.AnalyticsRecyclerViewCustomAdapter;
import com.vilect.librarymanagement.dao.BillDAO;
import com.vilect.librarymanagement.dao.DetailBillDAO;
import com.vilect.librarymanagement.model.BillModel;
import com.vilect.librarymanagement.model.BookModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vilect.librarymanagement.LibraryClass.billArrayList;

public class AnalyticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvAnalytics;
    private BillDAO billDAO;
    private DetailBillDAO detailBillDAO;
    private String selectedBill;
    private String startDate = "";
    private String endDate = "";
    private TextView txtStart;
    private TextView txtEnd;
    private Button btnStart;
    private Button btnEnd;
    private ArrayList<BillModel> filteredBill = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Date dateStart;
    private Date dateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        mapping();
        setNavigationViewOnClick();
        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Analytics");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setButtonOnClick();
    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutAnalytics);
        navigationView = findViewById(R.id.navAnalytics);
        rvAnalytics = findViewById(R.id.rvAnalytics);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        txtStart = findViewById(R.id.txtStart);
        txtEnd = findViewById(R.id.txtEnd);
    }

    private void setAdapter()
    {

    }

    //bật drawer


    private void setNavigationViewOnClick()
    {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itBook: {
                        Intent intent = new Intent(AnalyticsActivity.this, BookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBookType: {
                        Intent intent = new Intent(AnalyticsActivity.this, BookTypeActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBill: {
                        Intent intent = new Intent(AnalyticsActivity.this, BillActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itAnalytics:
                    {
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itTopBook: {
                        Intent intent = new Intent(AnalyticsActivity.this, TopBookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setButtonOnClick()
    {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnalyticsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.date_picker, null, false));

                builder.setPositiveButton("Select date", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog datePickerDialog = builder.create();
                datePickerDialog.show();

                //mapping
                final DatePicker datePicker = datePickerDialog.findViewById(R.id.datePicker);
                Button btnSelect = datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String month = ""+(datePicker.getMonth()+1);
                        String day = ""+datePicker.getDayOfMonth();

                        if (Integer.parseInt(month) < 10)
                        {
                            month = "0" + (datePicker.getMonth()+1);
                        }

                        if (Integer.parseInt(day) < 10)
                        {
                            day = "0" + datePicker.getDayOfMonth();
                        }

                        startDate = day + "-" + month + "-" + datePicker.getYear();

                        if (validDate())
                        {
                            txtStart.setText(startDate);
                            datePickerDialog.dismiss();
                        }

                        //nẾu endDate ko bị trống
                        //nghĩa là đc parse r
                        if (dateEnd != null & dateStart != null)
                        {
                            for (int i = 0; i < billArrayList.size(); i++)
                            {
                                try
                                {
                                    if (sdf.parse(billArrayList.get(i).getDate()).after(dateStart) || sdf.parse(billArrayList.get(i).getDate()).before(dateEnd))
                                    {
                                        filteredBill.add(billArrayList.get(i));
                                    }
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            setRecyclerViewAdapter(filteredBill);
                        }
                    }
                });
            }
        });
        //--------------------------------------------

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnalyticsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.date_picker, null, false));

                builder.setPositiveButton("Select date", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog datePickerDialog = builder.create();
                datePickerDialog.show();

                //mapping
                final DatePicker datePicker = datePickerDialog.findViewById(R.id.datePicker);
                Button btnSelect = datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String month = ""+(datePicker.getMonth()+1);
                        String day = ""+datePicker.getDayOfMonth();

                        if (Integer.parseInt(month) < 10)
                        {
                            month = "0" + (datePicker.getMonth()+1);
                        }

                        if (Integer.parseInt(day) < 10)
                        {
                            day = "0" + datePicker.getDayOfMonth();
                        }


                        endDate = day + "-" + month + "-" + datePicker.getYear();

                        if (validDate())
                        {
                            txtEnd.setText(endDate);
                            datePickerDialog.dismiss();

                        }

                        if (dateEnd != null & dateStart != null)
                        {
                            for (int i = 0; i < billArrayList.size(); i++)
                            {
                                try
                                {
                                    //demo parse
                                    if (sdf.parse(billArrayList.get(i).getDate()).after(dateStart) && sdf.parse(billArrayList.get(i).getDate()).before(dateEnd))
                                    {
                                        filteredBill.add(billArrayList.get(i));
                                    }
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            setRecyclerViewAdapter(filteredBill);
                        }
                    }
                });
            }
        });
    }

    private void setRecyclerViewAdapter(ArrayList<BillModel> filteredBill)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this);
        rvAnalytics.setLayoutManager(layoutManager);
        rvAnalytics.setAdapter(new AnalyticsRecyclerViewCustomAdapter(AnalyticsActivity.this, filteredBill));
    }

    //check endDate phải lớn hơn startDate
    private boolean validDate()
    {

        try
        {

            if (!startDate.equals("") && !endDate.equals(""))
            {
                dateStart = sdf.parse(startDate);
                dateEnd = sdf.parse(endDate);
                if (dateEnd.before(dateStart))
                {
                    Toast.makeText(AnalyticsActivity.this, "End date must be after start date!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}
