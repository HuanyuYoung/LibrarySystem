package com.yhy.librarysystem;
/**
 * 图书类
 */
public class Book {

    private  Double price; //书价格
    private int id,num;    //书ID、现存数量
    private String name,category,author;   //书名、类型、作者

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
