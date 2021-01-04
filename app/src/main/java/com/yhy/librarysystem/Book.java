package com.yhy.librarysystem;

public class Book {

    private  Double price;
    private int id,num;
    private String name,category,author;

    public Book(){
    }

    public int getId(){
        return id;
    }

    public int getNum() {
        return num;
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setId(int id){ this.id=id;}

    public void setNum(int num){this.num=num;}

    public void setAuthor(String author){this.author=author;}

    public void setName(String name){this.name=name;}

    public void setCategory(String category){this.category=category;}

    public Double getPrice() {
        return price;
    }
}
