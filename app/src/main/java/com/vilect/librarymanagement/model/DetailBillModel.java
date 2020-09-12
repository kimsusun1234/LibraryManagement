package com.vilect.librarymanagement.model;

public class DetailBillModel {
    String id;
    String bookID;
    String billID;
    int amount;

    public DetailBillModel(){
        //constructor mặc định dùng để sử dụng hàm DataSnapshot.getValue(DetailBillModel.class)
    }

    public DetailBillModel(String id, String bookId, String billID, int amount) {
        this.id = id;
        this.bookID = bookId;
        this.billID = billID;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookID;
    }

    public void setBookId(String bookId) {
        this.bookID = bookId;
    }

    public String getBillId() {
        return billID;
    }

    public void setBillId(String billId) {
        this.billID = billId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
