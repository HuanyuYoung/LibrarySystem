package com.yhy.librarysystem;

import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 工具类
 */

public class util {

    //存储用户信息
    public static int userId;
    public static String username,password,sex;
    //存储搜索得到的图书信息
    public static List<Book> BookList = new ArrayList<>();
    //存储用户借书信息
    public static List<BorrowMsg> BorrowList =new ArrayList<>();
    //存储所有图书种类
    public static List<String> CategoryList=new ArrayList<>();
    //搜索关键字
    public static String search_key;

    public static boolean categoryDone=false;

    public static void getAllCategory(){
        //查询所有图书种类
        CategoryList=DBUtil.getCategory();
        categoryDone=true;
    }

    public static void searchByCategory(String cat){
        //根据图书种类查询
        BookList=DBUtil.getBookMsgBycategory(cat);
    }

    public static void getBorrowList(){
        //获取用户借书信息
        BorrowList=DBUtil.getBorrowMsg(username);
    }

    public static void searchByName(){
        //根据图书名或作者名模糊查询
        BookList=DBUtil.getBookMsg(search_key);
    }

    public static String getNowDateShort() {
        //得到现在的时间
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int date = c.get(Calendar.DATE);

        if(month<10 && date<10)
            return year + "-0" + month + "-0" + date;
        else if(month<10)
            return year + "-0" + month + "-" + date;
        else if(date <10)
            return year + "-" + month + "-0" + date;
        else
            return year + "-" + month + "-" + date;
    }

    public static String getNextMonthDate(String date){
        //根据date得到最迟归还时间
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7))+1;
        int day = Integer.parseInt(date.substring(8,10));
        if(month>12){
            month=1;
            year++;
        }
        if(month<10 && day<10)
            return year + "-0" + month + "-0" + day;
        else if(month<10)
            return year + "-0" + month + "-" + day;
        else if(day <10)
            return year + "-" + month + "-0" + day;
        else
            return year + "-" + month + "-" + day;
    }

    public static boolean judgeOvertime(String deadline){
        //判断图书是否超时未归还
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);

        int yearD=Integer.parseInt(deadline.substring(0,4));
        int monthD=Integer.parseInt(deadline.substring(5,7));
        int dayD=Integer.parseInt(deadline.substring(8,10));

        if(year>yearD) return true;
        else {
            if (month > monthD) return true;
            else {
                if (month == monthD) {
                    if (day > dayD) return true;
                    else return false;
                } else {
                    return false;
                }
            }
        }

    }
}
