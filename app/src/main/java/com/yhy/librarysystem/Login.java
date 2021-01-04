package com.yhy.librarysystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private TextView username, passwd;
    private Button  login;
    private String name, pass;
    private CheckBox show_pass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        username = (TextView) findViewById(R.id.userName);
        passwd = (TextView) findViewById(R.id.passWord);
        login = (Button) findViewById(R.id.login);
        show_pass = (CheckBox) findViewById(R.id.show_pass);

        passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        show_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        //限制用户输入的用户名、密码在的正确范围
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                pass = passwd.getText().toString();
//                if (name.length() == 0) {
//                    Alarm("用户名不能为空");
//                } else if (pass.length() == 0) {
//                    Alarm("密码不能为空");
//                } else if (pass.length() < 6) {
//                    Alarm("密码长度太短");
//                } else if (name.length() < 3) {
//                    Alarm("用户名太短");
//                } else if (!(judgeNamePass(name) && judgeNamePass(pass))) {
//                    Alarm("用户名密码仅允许字母或数字");
//                } else {
                    LOGIN();
//                }
            }
        });
    }

    private void LOGIN() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //判断用户名密码是否正确
                boolean judge = DBUtil.checkUser(name, pass);
                Message msgdata = new Message();
                msgdata.what = 1001;
                Bundle data = new Bundle();
                 data.putBoolean("judge", judge);
                msgdata.setData(data);
                dHandler.sendMessage(msgdata);
            }
        };
        new Thread(run).start();
    }


    Handler dHandler = new Handler() {
        public void handleMessage(android.os.Message msgdata) {
            boolean judge;
            switch (msgdata.what) {
                case 1001:
                    judge = msgdata.getData().getBoolean("judge");
                    if (judge == true) {
                        util.username = name;
                        util.password = pass;
                        Intent intent = new Intent();
                        intent.setClass(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Alarm("用户不存在或用户名或密码错误");
                    }
                    break;

            }
        }
    };


    private void Alarm(String s) {
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }

    //用户名密码只能包括字母或数字
    private boolean judgeNamePass(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!((s.charAt(i) >= '0' && s.charAt(i) <= '9') || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')))
                return false;
        }
        return true;
    }
}