package com.vilect.librarymanagement.model;

public class BookModel {
    String id;
    String title;
    String author;
    String bookTypeId;
    int quantity;
    int price;
    String publisher;

    public BookModel()
    {
        //constructor mặc định dùng để sử dụng hàm DataSnapshot.getValue(BookModel.class)
    }

    public BookModel(String id, String title, String author, String bookTypeId, int quantity, int price, String publisher) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.publisher = publisher;
        this.bookTypeId = bookTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBookTypeId() {
        return bookTypeId;
    }

    public void setBookTypeId(String bookTypeId) {
        this.bookTypeId = bookTypeId;
    }
}
