package com.yhy.librarysystem;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_Frag  extends Fragment{

    private  BorrowAdapter adapter;
    private ListView listView;
    private Button update;
    private TextView usernameshow,useridshow;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mine_frag,container,false);
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update=(Button)getActivity().findViewById(R.id.update_borrow);
        usernameshow=(TextView) getActivity().findViewById(R.id.userNameShow);
        useridshow=(TextView) getActivity().findViewById(R.id.userIdShow);

        showUserMsg();
        updateBorrowList();

        update.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                updateBorrowList();
            }
        });
    }

    private void updateBorrowList(){
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                util.getBorrowList();
                boolean judge=true;
                if(util.BorrowList.size()==0)
                    judge=false;
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

    public  void showUserMsg(){
        if(util.sex.equals("男"))
            usernameshow.setText(util.username+" 先生");
        else
            usernameshow.setText(util.username+" 女士");
        useridshow.setText("ID: "+util.userId);
    }

    private void showBorrow(){
        adapter = new BorrowAdapter(getContext(), R.layout.reserve_list, util.BorrowList);
        listView = (ListView) getActivity().findViewById(R.id.borrowListview);
        listView.setAdapter(adapter);
    }


    Handler dHandler = new Handler() {
        public void handleMessage(android.os.Message msgdata) {
            boolean judge,change;
            switch (msgdata.what) {
                case 1001:
                    judge = msgdata.getData().getBoolean("judge");
                    if(!judge)
                        Alarm("查询为空");
                    showBorrow();
                    break;
                default:
                    break;
            }
        }
        ;
    };

    private void Alarm(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
}
