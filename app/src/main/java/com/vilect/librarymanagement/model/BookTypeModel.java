package com.vilect.librarymanagement.model;

public class BookTypeModel {

    private String id;
    private String name;
    private String des;
    private int position;

    public BookTypeModel(){
        //constructor mặc định dùng để sử dụng hàm DataSnapshot.getValue(BookTypeModel.class)
    }

    public BookTypeModel(String id, String name, String des, int position) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
