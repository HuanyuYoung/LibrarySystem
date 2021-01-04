package com.yhy.librarysystem;

import java.util.Date;

public class BorrowMsg {
    private int id;
    private String name,category,author,borrow,deadline;

    public BorrowMsg(){
    }

    public int getId(){
        return id;
    }

    public String getBorrow() {
        return borrow;
    }



    public String getName(){
        return  name;
    }

    public String getCategory(){
        return category;
    }

    public String getAuthor(){
        return author;
    }

    public void setId(int id){ this.id=id;}

    public void setBorrow(String date){this.borrow=date;}

    public void setAuthor(String author){this.author=author;}

    public void setName(String name){this.name=name;}

    public void setCategory(String category){this.category=category;}

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
