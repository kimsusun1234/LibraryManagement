package com.vilect.librarymanagement.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.vilect.librarymanagement.R;
import com.vilect.librarymanagement.adapter.AddBillDialogRecyclerViewCustomAdapter;
import com.vilect.librarymanagement.adapter.BillRecyclerviewCustomAdapter;
import com.vilect.librarymanagement.dao.BillDAO;
import com.vilect.librarymanagement.dao.BookDAO;
import com.vilect.librarymanagement.dao.DetailBillDAO;
import com.vilect.librarymanagement.listener.OnRecyclerViewItemClickListener;
import com.vilect.librarymanagement.model.BookModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vilect.librarymanagement.LibraryClass.billArrayList;
import static com.vilect.librarymanagement.LibraryClass.bookArrayList;
import static com.vilect.librarymanagement.LibraryClass.caculateAmount;
import static com.vilect.librarymanagement.LibraryClass.detailBillArrayList;

public class BillActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvBill;
    private FloatingActionButton fab;
    private BillDAO billDAO;
    private DetailBillDAO detailBillDAO;
    private String selectedBill;
    private ImageView ivSetting;
    private BookDAO bookDAO = new BookDAO(BillActivity.this);

    /* REQUEST_CODE là một giá trị int dùng để định danh mỗi request. Khi nhận được kết quả,
    hàm callback sẽ trả về cùng REQUEST_CODE này để ta có thể xác định và xử lý kết quả. */
    public static final int REQUEST_CODE_ADD_BOOK = 4463;
    public static final int REQUEST_CODE_EDIT_BILL = 9996;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        mapping();

        //Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        actionBar.setTitle(sdf.format(date));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setAdapter();
        setNavOnClick();
        setFab();

        //đặt lại listener cho db
        billDAO = new BillDAO(BillActivity.this);
        billDAO.getAll();
        detailBillDAO = new DetailBillDAO(BillActivity.this);
    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutBill);
        navigationView = findViewById(R.id.navBill);
        rvBill = findViewById(R.id.rvBill);
        fab = findViewById(R.id.fabBill);
    }

    public void setAdapter()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BillActivity.this);
        rvBill.setLayoutManager(linearLayoutManager);

        //"this" for the implemented down there
        BillRecyclerviewCustomAdapter adapter = new BillRecyclerviewCustomAdapter(BillActivity.this, this);
        rvBill.setAdapter(adapter);
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

    private void setNavOnClick()
    {
        //header oỬ vị trí số 0 vì chỉ có 1 layout header được sử dụng
        View view = navigationView.getHeaderView(0);
        ivSetting = view.findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.itBook:
                    {
                        Intent intent = new Intent(BillActivity.this, BookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBookType:
                    {
                        Intent intent = new Intent(BillActivity.this, BookTypeActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itBill:
                    {
                        Intent intent = new Intent(BillActivity.this, BillActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itAnalytics:
                    {
                        Intent intent = new Intent(BillActivity.this, AnalyticsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }

                    case R.id.itTopBook:
                    {
                        Intent intent = new Intent(BillActivity.this, TopBookActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
                return false;
            }
        });
    }

    //onRecyclerViewItemClick
    @Override
    public void setOnItemClickListener(final int position) {

        selectedBill = billArrayList.get(position).getId();
        //lấy list sách đã mua và số lượng để truyền vào adapter RecyclerView
        ArrayList<Integer> quantity = new ArrayList<>();
        ArrayList<BookModel> buyBook = new ArrayList<>();

        for (int i = 0; i < detailBillArrayList.size(); i++)
        {
            //nếu trùng bill id
            if (billArrayList.get(position).getId().equals(detailBillArrayList.get(i).getBillId()))
            {
                //tim sach
                for (int t = 0; t < bookArrayList.size(); t++)
                {
                    //nếu trùng book id
                    if (detailBillArrayList.get(i).getBookId().equals(bookArrayList.get(t).getId()))
                    {
                        //add data xong break vòng lặp để tối ưu tốc độ
                        quantity.add(detailBillArrayList.get(i).getAmount());
                        buyBook.add(bookArrayList.get(t));
                        break;
                    }
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog_bill, null, false));


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
        btnUpdate.setEnabled(false);
        Button btnDelete = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        Button btnEditBook = alertDialog.findViewById(R.id.btnEditBook);
        RecyclerView rvBillDialog = alertDialog.findViewById(R.id.rvBillDialog);
        TextView txtTotal = alertDialog.findViewById(R.id.txtTotalBillDialog);

        txtTotal.setText(caculateAmount(selectedBill)+" Đ");

        LinearLayoutManager layoutManager = new LinearLayoutManager(BillActivity.this);
        rvBillDialog.setLayoutManager(layoutManager);
        AddBillDialogRecyclerViewCustomAdapter adapter = new AddBillDialogRecyclerViewCustomAdapter(BillActivity.this, buyBook, quantity);
        rvBillDialog.setAdapter(adapter);

        //set buttonEdit
        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillActivity.this, AddBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_BILL);
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xóa bill thì phải xóa luôn detail bill
                //xóa bill trước
                billDAO.delete(selectedBill);

                //xóa detailbill sau
                for (int i = 0; i < detailBillArrayList.size(); i++)
                {
                    if (detailBillArrayList.get(i).getBillId().equals(selectedBill))
                    {
                        detailBillDAO.delete(detailBillArrayList.get(i).getId());
                    }
                }
                alertDialog.dismiss();
            }
        });
    }

    //hàm này được gọi khi AddBookActivity kết thúc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //nếu trùng requestCode

        //nếu là hành động thêm sách vào dialog new bill
        if (requestCode == REQUEST_CODE_ADD_BOOK)
        {
            //Kiểm tra result
            if (resultCode == Activity.RESULT_OK)
            {
                //thực hiện lệnh

                int[] quantity = data.getIntArrayExtra("quantity");
                //tạo một mảng, list mới chỉ chứa những sách sẽ mua
                final ArrayList<Integer> newQuantity = new ArrayList<>();
                final ArrayList<BookModel> buyBookList = new ArrayList<>();
                for (int i = 0; i < quantity.length; i++)
                {
                    if (quantity[i] != 0)
                    {
                        buyBookList.add(bookArrayList.get(i));
                        newQuantity.add(quantity[i]);
                    }
                }


                //Create dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.custom_dialog_add_bill, null, false));


                //set some button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Save", null);
                builder.setNeutralButton("Edit Book", null);

                //show Dialog
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //mapping
                Button btnSave = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnEditBook = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                RecyclerView rvDialog = alertDialog.findViewById(R.id.rvAddBookDialog);
                TextView txtTotalAddBillDialog = alertDialog.findViewById(R.id.txtTotalAddBillDialog);

                //Setup RecyclerView
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BillActivity.this);
                rvDialog.setLayoutManager(linearLayoutManager);

                final AddBillDialogRecyclerViewCustomAdapter adapterDialog = new AddBillDialogRecyclerViewCustomAdapter(BillActivity.this, buyBookList, newQuantity);
                rvDialog.setAdapter(adapterDialog);

                //-------------------------------------------------------------

                //caculating total cost
                int total = 0;
                //Vì hai ArrayList đã đồng bộ với nhau về vị trí rồi nên chỉ cần một vòng for để duyệt
                for (int i = 0; i < newQuantity.size(); i++)
                {
                    total+= newQuantity.get(i) * buyBookList.get(i).getPrice();
                }
                txtTotalAddBillDialog.setText(total+" Đ");

                //-------------------------------------------------------------

                //set button EditBook
                btnEditBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BillActivity.this, AddBookActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
                        alertDialog.dismiss();
                    }
                });

                //-------------------------------------------------------------

                //set button Save
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailBillDAO detailBillDAO = new DetailBillDAO(BillActivity.this);

                        //láy ngày, giờ
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                        String today = sdf.format(date);

                        //Chạy vòng for để add đủ bill detail
                        //Vì hai ArrayList đã đồng bộ với nhau về vị trí rồi nên chỉ cần một vòng for để quet
                        String billId = billDAO.insert(today);
                        for (int i = 0; i < buyBookList.size(); i++)
                        {
                            detailBillDAO.insert(buyBookList.get(i).getId(),billId, newQuantity.get(i));
                        }

                        //chạy vòng lặp để thay đổi số lượng sách trong kho

                        for (int i = 0; i < buyBookList.size(); i++)
                        {
                            for (int k = 0; k < bookArrayList.size(); i++)
                            {
                                //nẾU trÙNg id
                                if (buyBookList.get(i).getId().equals(bookArrayList.get(k).getId()))
                                {
                                    //thay đổi số lượng
                                    //lấy số lượng mua
                                    int buyAmount = newQuantity.get(i);
                                    //số lượng sách trong kho
                                    int available = bookArrayList.get(k).getQuantity();
                                    int remain = available - buyAmount;
                                    bookDAO.update(bookArrayList.get(k).getId(), bookArrayList.get(k).getTitle(), bookArrayList.get(k).getAuthor(),bookArrayList.get(k).getBookTypeId(), remain, bookArrayList.get(k).getPrice(), bookArrayList.get(k).getPublisher() );
                                }
                            }
                        }

                        alertDialog.dismiss();
                    }
                });

            }
        }
        else
        {
            //nẾu là hành động thêm sách vào dialog edit bill
            if (requestCode == REQUEST_CODE_EDIT_BILL)
            {
                if (resultCode == Activity.RESULT_OK)
                {

                    //làm lại quantity và buyBook
                    //lấy list sách đã mua và số lượng để truyền vào adapter RecyclerView
                    //lấy dữ liệu trả về
                    int[] quantity = data.getIntArrayExtra("quantity");

                    final ArrayList<Integer> newQuantity = new ArrayList<>();
                    final ArrayList<BookModel> buyBook = new ArrayList<>();

                    for (int i = 0; i < quantity.length; i++)
                    {
                        if (quantity[i] > 0)
                        {
                            newQuantity.add(quantity[i]);
                            buyBook.add(bookArrayList.get(i));
                        }
                    }


                    //tạo lại dialog như trên

                    AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
                    LayoutInflater inflater = getLayoutInflater();

                    builder.setView(inflater.inflate(R.layout.custom_dialog_bill, null, false));


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
                    builder.show();

                    //mapping
                    Button btnUpdate = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnDelete = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    Button btnEditBook = alertDialog.findViewById(R.id.btnEditBook);
                    RecyclerView rvBillDialog = alertDialog.findViewById(R.id.rvBillDialog);
                    TextView txtTotal = alertDialog.findViewById(R.id.txtTotalBillDialog);

                    //caculating total cost
                    int total = 0;
                    //Vì hai ArrayList đã đồng bộ với nhau về vị trí rồi nên chỉ cần một vòng for để duyệt
                    for (int i = 0; i < newQuantity.size(); i++)
                    {
                        total+= newQuantity.get(i) * buyBook.get(i).getPrice();
                    }

                    txtTotal.setText(total+" Đ");

                    LinearLayoutManager layoutManager = new LinearLayoutManager(BillActivity.this);
                    rvBillDialog.setLayoutManager(layoutManager);
                    AddBillDialogRecyclerViewCustomAdapter adapter = new AddBillDialogRecyclerViewCustomAdapter(BillActivity.this, buyBook, newQuantity);
                    rvBillDialog.setAdapter(adapter);

                    //set buttonUpdate

                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //kiểm tra detailBill vs billID
                            //xóa detailBill cũ
                            for (int i = 0; i < detailBillArrayList.size(); i++)
                            {
                                //nếu trùng
                                if (detailBillArrayList.get(i).getBillId().equals(selectedBill))
                                {
                                    //lấy số lượng các sách, cộng thêm vào kho, sau đó mới xóa
                                    for (int k = 0; k < bookArrayList.size(); k++)
                                    {
                                        if (bookArrayList.get(k).getId().equals(detailBillArrayList.get(i).getBookId()))
                                        {
                                            int getAmount = detailBillArrayList.get(i).getAmount();
                                            int newAmount = bookArrayList.get(k).getQuantity() + getAmount;
                                            bookDAO.update(bookArrayList.get(k).getId(), bookArrayList.get(k).getTitle(), bookArrayList.get(k).getAuthor(), bookArrayList.get(k).getBookTypeId(), newAmount, bookArrayList.get(k).getPrice(), bookArrayList.get(k).getPublisher());
                                        }
                                    }
                                    detailBillDAO.delete(detailBillArrayList.get(i).getId());
                                }
                            }

                            //thêm dữ liệu mới
                            for (int i = 0; i < buyBook.size(); i++)
                            {
                                detailBillDAO.insert(buyBook.get(i).getId(),selectedBill, newQuantity.get(i));
                            }

                            //update lại số lượng sách
                            for (int i = 0; i < buyBook.size(); i++)
                            {
                                for (int k = 0; k < bookArrayList.size(); i++)
                                {
                                    //nẾU trÙNg id
                                    if (buyBook.get(i).getId().equals(bookArrayList.get(k).getId()))
                                    {
                                        //thay đổi số lượng
                                        //lấy số lượng mới
                                        int buyAmount = newQuantity.get(i);
                                        //số lượng sách trong kho
                                        int available = bookArrayList.get(k).getQuantity();
                                        int remain = available - buyAmount;

                                        BookDAO bookDAO = new BookDAO(BillActivity.this);
                                        bookDAO.update(bookArrayList.get(k).getId(), bookArrayList.get(k).getTitle(), bookArrayList.get(k).getAuthor(),bookArrayList.get(k).getBookTypeId(), remain, bookArrayList.get(k).getPrice(), bookArrayList.get(k).getPublisher() );
                                    }
                                }
                            }

                        }
                    });

                    //set buttonEdit
                    btnEditBook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BillActivity.this, AddBookActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_EDIT_BILL);
                            alertDialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //xóa bill thì phải xóa luôn detail bill
                            //xóa bill trước
                            billDAO.delete(selectedBill);

                            //xóa detailbill sau
                            for (int i = 0; i < detailBillArrayList.size(); i++)
                            {
                                //lấy số lượng các sách, cộng thêm vào kho, sau đó mới xóa
                                for (int k = 0; k < bookArrayList.size(); k++)
                                {
                                    if (bookArrayList.get(k).getId().equals(detailBillArrayList.get(i).getBookId()))
                                    {
                                        int getAmount = detailBillArrayList.get(i).getAmount();
                                        int newAmount = bookArrayList.get(k).getQuantity() + getAmount;
                                        bookDAO.update(bookArrayList.get(k).getId(), bookArrayList.get(k).getTitle(), bookArrayList.get(k).getAuthor(), bookArrayList.get(k).getBookTypeId(), newAmount, bookArrayList.get(k).getPrice(), bookArrayList.get(k).getPublisher());
                                    }
                                }
                                if (detailBillArrayList.get(i).getBillId().equals(selectedBill))
                                {
                                    detailBillDAO.delete(detailBillArrayList.get(i).getId());
                                }
                            }
                            alertDialog.dismiss();
                        }
                    });

                }
            }
        }


    }

    private void setFab()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillActivity.this, AddBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
            }
        });

    }
}
