package com.yhy.librarysystem;
/**
 * 用户借阅书籍信息类
 */
import java.util.Date;
public class BorrowMsg {
    private int id; //书ID
    private String name,category,author,borrow,deadline;  //书名、类型、作者、借阅时间、归还时间
    public BorrowMsg(){
    }

    public int getId(){ return id; }

    public String getBorrow() { return borrow; }

    public String getName(){ return  name; }

    public String getCategory(){ return category; }

    public String getAuthor(){ return author; }

    public void setId(int id){ this.id=id;}

    public void setBorrow(String date){this.borrow=date;}

    public void setAuthor(String author){this.author=author;}

    public void setName(String name){this.name=name;}

    public void setCategory(String category){this.category=category;}

    public String getDeadline() { return deadline; }

    public void setDeadline(String deadline) { this.deadline = deadline; }
}
