package com.vilect.librarymanagement;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vilect.librarymanagement.dao.BillDAO;
import com.vilect.librarymanagement.dao.BookDAO;
import com.vilect.librarymanagement.dao.BookTypeDAO;
import com.vilect.librarymanagement.dao.DetailBillDAO;
import com.vilect.librarymanagement.dao.UserDAO;
import com.vilect.librarymanagement.model.BillModel;
import com.vilect.librarymanagement.model.BookModel;
import com.vilect.librarymanagement.model.BookTypeModel;
import com.vilect.librarymanagement.model.DetailBillModel;
import com.vilect.librarymanagement.model.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class LibraryClass
{
    //{id, name, pass, phone}
    public static String[] loggedUser = new String[4];
    public static ArrayList<BillModel> billArrayList = new ArrayList<>();
    public static ArrayList<BookModel> bookArrayList = new ArrayList<>();
    public static ArrayList<BookTypeModel> bookTypeArrayList = new ArrayList<>();
    public static ArrayList<DetailBillModel> detailBillArrayList = new ArrayList<>();
    public static ArrayList<UserModel> userArrayList = new ArrayList<>();
    //xử lí top book trong tháng
    public static ArrayList<BillModel> topBillArrayList = new ArrayList<>();
    public static ArrayList<BookModel> topBookArrayList = new ArrayList<>();
    public static ArrayList<Integer> topBookQuantity = new ArrayList<>();
    Context context;


    public LibraryClass(Context context) {
        this.context = context;
        //getdata
        BillDAO billDAO = new BillDAO(context);
        BookDAO bookDAO = new BookDAO(context);
        BookTypeDAO bookTypeDAO = new BookTypeDAO(context);
        DetailBillDAO detailBillDAO = new DetailBillDAO(context);
        UserDAO userDAO = new UserDAO(context);

        billDAO.getAllRuntime();
        bookDAO.getAllRuntime();
        bookTypeDAO.getAllRuntime();
        detailBillDAO.getAll();
        userDAO.getAll();

    }

    public static void topBookArrangement()
    {
        topBookArrayList.clear();
        topBookQuantity.clear();
        topBillArrayList.clear();

        //lấy tháng hiện tại, chuyển về tháng trước
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //ngày hôm nay
        Date date = new Date();
        Date date1 = new Date();
        //chuyển vào calendar để dễ xử lí
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.setTime(date);
        calendar1.setTime(date1);
        //trừ đi một tháng
        calendar.add(Calendar.MONTH, -1);
        calendar1.add(Calendar.MONTH, -1);
        //set về ngày 1, 30
        calendar.set(Calendar.DATE, 1);
        calendar1.set(Calendar.DATE, 30);
        //chuyển vào date mới để so sánh
        Date date2 = calendar.getTime();
        Date date3 = calendar1.getTime();

        //------------------------------------------------------------------------------------------
        //Lọc bill trong tháng
        try
        {
            for (int i = 0; i < billArrayList.size(); i++)
            {
                //lấy date ra r parse về Date để so sánh
                //Lấy phần giữa và hai đầu mút
                if ((sdf.parse(billArrayList.get(i).getDate()) == date2 || sdf.parse(billArrayList.get(i).getDate()).after(date2)) && (sdf.parse(billArrayList.get(i).getDate()).before(date3) || sdf.parse(billArrayList.get(i).getDate()) == date3))
                {
                    topBillArrayList.add(billArrayList.get(i));
                }
            }
        }
        catch (Exception e)
        {
            Log.e("billfilter", e.getMessage());
        }

        //clone bookArrayList sang topBookArrayList
        for (int i = 0; i < bookArrayList.size(); i++)
        {
            topBookArrayList.add(bookArrayList.get(i));
        }
        //Lấy số lượng sách bán được
        //duyệt sách

        for (int i = 0; i < topBookArrayList.size(); i++)
        {
            int count = 0;
            //quét sang bill
            for (int k = 0; k < topBillArrayList.size(); k++)
            {
                //quét detail
                for (int l = 0; l < detailBillArrayList.size(); l++)
                {
                    //nếu trùng billID
                    if (topBillArrayList.get(k).getId().equals(detailBillArrayList.get(l).getBillId()) && topBookArrayList.get(i).getId().equals(detailBillArrayList.get(l).getBookId()))
                    {
                        //cộng số lượng vào count
                        count = count + detailBillArrayList.get(l).getAmount();
                    }
                }

            }
            topBookQuantity.add(count);
        }

        //sắp xếp topBookArrayList theo thứ tự giảm dần

        for (int i = 0; i < topBookQuantity.size(); i++)
        {
            for (int k = i+1; k < topBookQuantity.size(); k++)
            {
                //nếu cái sau lớn hơn cái trước
                if (topBookQuantity.get(k) > topBookQuantity.get(i))
                {
                    //thì cho cái sau lên trên
                    int temp = topBookQuantity.get(i);
                    topBookQuantity.set(i, topBookQuantity.get(k));
                    topBookQuantity.set(k, temp);
                    //xếp luôn cả bookList
                    BookModel bookModel = topBookArrayList.get(i);
                    topBookArrayList.set(i, topBookArrayList.get(k));
                    topBookArrayList.set(k, bookModel);
                }
            }
        }

    }

    public static int caculateAmount(String billID)
    {
        int totalCost = 0;
        for (int t = 0; t < detailBillArrayList.size(); t++)
        {
            if (billID.equals(detailBillArrayList.get(t).getBillId()))
            {
                for (int l = 0; l < bookArrayList.size(); l++)
                {
                    if (detailBillArrayList.get(t).getBookId().equals(bookArrayList.get(l).getId()))
                    {
                        totalCost += bookArrayList.get(l).getPrice() * detailBillArrayList.get(t).getAmount();
                    }
                }

            }
        }
        return totalCost;
    }
}
