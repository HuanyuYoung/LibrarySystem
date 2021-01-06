package com.yhy.librarysystem;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBUtil {
    private static String booktable="book";
    private static String usertable="reader";
    private static String borrowtable="note";
    /**
     * 数据库连接
     * @return
     */
    private static Connection getConn(){
        Connection con = null;
        try{
            String dbUrl="jdbc:mysql://rm-f8z54d2262ia8ou0a8o.mysql.rds.aliyuncs.com:3306/demo";
            String dbUserName="tortoise";
            String dbPassword="developer&Knight22";
            String jdbcName="com.mysql.jdbc.Driver";
            //加载驱动小程序
            Class.forName(jdbcName);
            //获得数据库连接
            con=DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
            if(con != null) {
                System.out.println("chenggong");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return con;
    }

    /**
     * 判断用户名、密码是否正确
     * @param name 用户名
     * @param password 密码
     * @return
     */
    public static boolean checkUser(String name,String password){
        Connection connection = getConn();
        try {
            String sql = "select pass,id,sex from " + usertable + " where name = ?;";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, name);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        rs.last();
                        int count = rs.getRow();
                        if(count == 0){
                            connection.close();
                            ps.close();
                            return false;
                        }
                        else{
                            util.password = rs.getString("pass");
                            util.sex=rs.getString("sex");
                            util.userId=Integer.parseInt(rs.getString("id"));
                            connection.close();
                            ps.close();
                            if(util.password.equals(password)){
                                util.username=name;
                                return true;
                            }else{ return false;}
                        }
                    }else { connection.close(); ps.close();  return false; }
                }else {  connection.close();  ps.close();  return  false; }
            }else { Log.e("DBUtils","连接失败");  return  false; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return false;
        }
    }

    /**
     * 统计图书种类
     * @return
     */
    public static List<String> getCategory(){
        List<String> category=new ArrayList<>();
        Connection connection = getConn();
        try {
            String sql = "select distinct category from " + booktable +";";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        while (rs.next()) {
                            category.add(rs.getString("category"));
                        }
                        connection.close();
                        ps.close();
                        return category;
                    }else { connection.close();  ps.close(); return null; }
                }else { connection.close();  ps.close(); return  null; }
            }else { Log.e("DBUtils","连接失败"); return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 从数据库中读取用户借阅记录
     * @param name 用户名
     * @return
     */
    public static List<BorrowMsg> getBorrowMsg(String name){
        List<BorrowMsg> borrow=new ArrayList<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn();
        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select "+ booktable +".id," + booktable+".name," + booktable+".category," +booktable+".writer,borrowdate,backdate from " + borrowtable +" inner join " + booktable + " where " +
                    borrowtable +".bookid="+booktable +".id" + " and reader = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, name);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        while (rs.next()){
                            // 注意：下标是从1开始的
                            BorrowMsg t=new BorrowMsg();
                            t.setId(Integer.parseInt(rs.getString("id")));
                            t.setAuthor(rs.getString("writer"));
                            t.setCategory(rs.getString("category"));
                            t.setName(rs.getString("name"));
                            t.setBorrow(rs.getString("borrowdate"));
                            t.setDeadline(rs.getString("backdate"));
                            borrow.add(t);
                        }
                        connection.close();
                        ps.close();
                        return  borrow;
                    }else {  connection.close(); ps.close(); return null; }
                }else { connection.close(); ps.close();  return  null; }
            }else { return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 图书名称、作者模糊查找图书信息
     * @param key 搜索关键字
     * @return
     */
    public static List<Book> getBookMsg(String key){
        List<Book> book=new ArrayList<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn();
        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select id,name,category,writer,count,price from " + booktable + " where writer like ? or name like ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, "%"+key+"%");
                    ps.setString(2, "%"+key+"%");
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        while (rs.next()){
                            Book t=new Book();
                            t.setId(Integer.parseInt(rs.getString("id")));
                            t.setAuthor(rs.getString("writer"));
                            t.setCategory(rs.getString("category"));
                            t.setName(rs.getString("name"));
                            t.setNum(Integer.parseInt(rs.getString("count")));
                            t.setPrice(Double.parseDouble(rs.getString("price")));
                            book.add(t);
                        }
                        connection.close();
                        ps.close();
                        return  book;
                    }else { connection.close(); ps.close(); return null; }
                }else { connection.close(); ps.close(); return  null; }
            }else { return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 图书类型查找图书信息
     * @param key 图书类型
     * @return
     */
    public static List<Book> getBookMsgBycategory(String key){
        List<Book> book=new ArrayList<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn();
        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select id,name,category,writer,count,price from " + booktable + " where category = ? ";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, key);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        while (rs.next()){
                            Book t=new Book();
                            t.setId(Integer.parseInt(rs.getString("id")));
                            t.setAuthor(rs.getString("writer"));
                            t.setCategory(rs.getString("category"));
                            t.setName(rs.getString("name"));
                            t.setNum(Integer.parseInt(rs.getString("count")));
                            t.setPrice(Double.parseDouble(rs.getString("price")));
                            book.add(t);
                        }
                        connection.close();
                        ps.close();
                        return  book;
                    }else { connection.close();  ps.close(); return null; }
                }else { connection.close(); ps.close(); return  null; }
            }else { return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }


    /**
     * 增加借书记录
     * @param id 图书编号
     * @para bookname 图书名
     * @return
     */
    public static boolean addBorrowRecord(int id,String bookname) {
        Connection connection = getConn();
        try {
            String sql = "insert into " + borrowtable + " (bookid,book,readerid,reader,borrowdate) values(?,?,?,?,?);";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, id+"");
                    ps.setString(2, bookname+"");
                    ps.setString(3, util.userId+"");
                    ps.setString(4, util.username+"");
                    ps.setString(5,util.getNowDateShort());
                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                    return true;
                } else {  connection.close(); ps.close(); return false;  }
            } else {  connection.close();  return false; }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
        }
    }

    /**
     * 图书数量-1
     * @param id 图书编号
     * @return
     */
    public static boolean minusBookNum(int id) {
        Connection connection = getConn();
        try {
            String sql="update " + booktable +" set count=count-1 where id = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, id+"");
                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                    return true;
                } else { connection.close(); ps.close(); return false; }
            } else { return false; }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
        }
    }

    /**
     * 归还图书
     * @return
     */
    public static boolean backBook(int userid,int bookid, String borrowdate) {
        Connection connection = getConn();
        try {
            String sql="update " + borrowtable +" set backdate= ? where bookid = ? and readerid=? and borrowdate=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, util.getNowDateShort());
                    ps.setString(2, bookid+"");
                    ps.setString(3, userid+"");
                    ps.setString(4, borrowdate);
                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                    return true;
                } else { connection.close();  ps.close();  return false;  }
            } else { return false; }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
        }
    }


    /**
     * 图书数量+1
     * @param id 图书编号
     * @return
     */
    public static boolean addBookNum(int id) {
        Connection connection = getConn();
        try {
            String sql="update " + booktable +" set count=count+1 where id = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, id+"");
                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                    return true;
                } else {  connection.close(); ps.close(); return false; }
            } else { return false; }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
        }
    }

}
