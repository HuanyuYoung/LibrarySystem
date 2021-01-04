package com.yhy.librarysystem;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SearchAdapter extends android.widget.ArrayAdapter {
    private final int resourceId;
    private Book book;
    private TextView bookNum ;
    private Context con;

    public SearchAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId, objects);
        con=context;
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        book = (Book) getItem(position); // 获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView bookId = (TextView) view.findViewById(R.id.bookId);//获取该布局内的文本视图
        bookId.setText("ID: "+ book.getId()+"");//为文本视图设置文本内容
        TextView bookName = (TextView) view.findViewById(R.id.bookName);//获取该布局内的文本视图
        bookName.setText(book.getName());//为文本视图设置文本内容
        bookNum = (TextView) view.findViewById(R.id.bookNum);//获取该布局内的文本视图
        bookNum.setText(book.getNum()+"");//为文本视图设置文本内容
        TextView bookCategory= (TextView) view.findViewById(R.id.category);//获取该布局内的文本视图
        bookCategory.setText(book.getCategory());//为文本视图设置文本内容
        TextView bookAuthor = (TextView) view.findViewById(R.id.author);//获取该布局内的文本视图
        bookAuthor.setText(book.getAuthor());//为文本视图设置文本内容
        TextView bookPrice = (TextView) view.findViewById(R.id.bookPrice);//获取该布局内的文本视图
        bookPrice.setText(book.getPrice()+"RMB");//为文本视图设置文本内容

        Button reserveButton=(Button) view.findViewById(R.id.reserve);

        reserveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(util.BookList.get(position).getNum()>0) {
                    new AlertDialog.Builder(con).setTitle(
                            "您确定预定此图书吗?").setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    reserveBook(util.BookList.get(position).getId(), util.BookList.get(position).getName(), position);
                                }
                            }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            }).show();
                }else {
                    Alarm("图书数量为0，无法预定");
                }
            }
        });

        return view;
    }

    private void reserveBook(final int bookid,final String bookname,final int position){
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                boolean judge = DBUtil.addBorrowRecord(bookid,bookname);
                if(judge){
                    judge=DBUtil.minusBookNum(bookid);
                }
                Book t=util.BookList.get(position);
                util.BookList.get(position).setNum(t.getNum()-1);
                Message msgdata = new Message();
                msgdata.what = 1001;
                Bundle data = new Bundle();
                data.putBoolean("judge",judge);
                msgdata.setData(data);
                dHandler.sendMessage(msgdata);
            }
        };
        new Thread(run).start();
    }

    Handler dHandler = new Handler(){
        public void handleMessage(android.os.Message msgdata) {
            boolean judge;
            switch (msgdata.what)
            {
                case 1001:
                    judge = msgdata.getData().getBoolean("judge");
                    if(judge!=true)
                    {
                        Alarm("预定失败");
                    }else{
                        notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        };
    };

    private void Alarm(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }

}
