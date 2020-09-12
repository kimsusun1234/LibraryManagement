package com.vilect.librarymanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.TopBookRecyclerViewCustomAdapter;
import com.vilect.librarymanagement.model.BookModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.vilect.librarymanagement.LibraryClass.bookArrayList;
import static com.vilect.librarymanagement.LibraryClass.topBookArrangement;

public class TopBookActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvTopBook;
    private ImageView ivSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_book);

        mapping();

        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Top 10 Book");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        topBookArrangement();
        setAdapter();
        setOnClick();
    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutTopBook);
        navigationView = findViewById(R.id.navTopBook);
        rvTopBook = findViewById(R.id.rvTopBook);
    }

    private void setAdapter()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TopBookActivity.this);
        rvTopBook.setLayoutManager(linearLayoutManager);
        rvTopBook.setAdapter(new TopBookRecyclerViewCustomAdapter(TopBookActivity.this));
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
                Intent intent = new Intent(TopBookActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itBook: {
                        Intent intent = new Intent(TopBookActivity.this, BookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBookType: {
                        Intent intent = new Intent(TopBookActivity.this, BookTypeActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBill: {
                        Intent intent = new Intent(TopBookActivity.this, BillActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itAnalytics:
                    {
                        Intent intent = new Intent(TopBookActivity.this, AnalyticsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itTopBook: {
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
