package com.yhy.librarysystem;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class BorrowAdapter extends android.widget.ArrayAdapter  {

    private final int resourceId;
    private TextView deadlineDateHint;
    private TextView deadlineDate;
    private Button backButton;
    private TextView backDone;
    private Context con;

    public BorrowAdapter(Context context, int textViewResourceId, List<BorrowMsg> objects) {
        super(context, textViewResourceId, objects);
        con=context;
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BorrowMsg borrow = (BorrowMsg) getItem(position); // 获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView bookId = (TextView) view.findViewById(R.id.borrowId);//获取该布局内的文本视图
        bookId.setText("ID: " + borrow.getId()+"");//为文本视图设置文本内容
        TextView bookName = (TextView) view.findViewById(R.id.borrowName);//获取该布局内的文本视图
        bookName.setText(borrow.getName());//为文本视图设置文本内容
        TextView borrowDate = (TextView) view.findViewById(R.id.borrowDate);//获取该布局内的文本视图
        borrowDate.setText(borrow.getBorrow()+"");//为文本视图设置文本内容
        TextView bookCategory= (TextView) view.findViewById(R.id.borrowCategory);//获取该布局内的文本视图
        bookCategory.setText(borrow.getCategory());//为文本视图设置文本内容
        TextView bookAuthor = (TextView) view.findViewById(R.id.borrowAuthor);//获取该布局内的文本视图
        bookAuthor.setText(borrow.getAuthor());//为文本视图设置文本内容
        deadlineDateHint= (TextView) view.findViewById(R.id.deadlineDateHint);
        deadlineDate= (TextView) view.findViewById(R.id.deadlineDate);
        backButton=(Button) view.findViewById(R.id.backButton);
        backDone= (TextView) view.findViewById(R.id.backDone);
        if(borrow.getDeadline()!=null){
            deadlineDateHint.setText("归还日期:");
            deadlineDate.setText(borrow.getDeadline());
            backButton.setVisibility(View.INVISIBLE);
            backDone.setVisibility(View.VISIBLE);
        }else{
            String deadline=util.getNextMonthDate(borrow.getBorrow());
            deadlineDate.setText(deadline);
            if(util.judgeOvertime(deadline)){
                ((TextView)deadlineDate).setTextColor(Color.parseColor("#CD0000"));
            }
        }

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(con).setTitle(
                        "您确定归还此图书吗?").setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                backBook(util.BorrowList.get(position).getId(),util.userId, util.BorrowList.get(position).getBorrow(), position);
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                }).show();

            }
        });

        return view;
    }

    private void backBook(final int bookid,final int userid,final String borrowdate,final int position){
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                boolean judge = DBUtil.backBook(userid,bookid,borrowdate);
                if(judge){
                    judge=DBUtil.addBookNum(bookid);
                }
                BorrowMsg t=util.BorrowList.get(position);
                util.BorrowList.get(position).setDeadline(util.getNowDateShort());
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
                        Alarm("归还失败");
                    }else{
                        backButton.setVisibility(View.INVISIBLE);
                        backDone.setVisibility(View.VISIBLE);
                        deadlineDateHint.setText("归还日期:");
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
