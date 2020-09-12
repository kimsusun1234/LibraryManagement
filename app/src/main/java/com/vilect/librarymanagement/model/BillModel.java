package com.vilect.librarymanagement.model;

import androidx.annotation.Nullable;

public class BillModel {
    private String id;
    private String date;

    public BillModel()
    {
        //constructor mặc định dùng để sử dụng hàm DataSnapshot.getValue(BillModel.class)
    }

    public BillModel(@Nullable String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
