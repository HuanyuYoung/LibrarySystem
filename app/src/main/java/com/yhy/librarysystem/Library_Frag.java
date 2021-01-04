package com.yhy.librarysystem;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class Library_Frag extends Fragment{

    Timer timer = new java.util.Timer(true);
    private FlowTagGroup categoryList;
    private  SearchAdapter adapter;
    private ListView listView;
    private Button search;
    private TextView search_bookmsg;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.library_frag,container,false);
        return view;
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        search=(Button)getActivity().findViewById(R.id.search);
        search_bookmsg=(TextView)getActivity().findViewById(R.id.search_bookmsg);
        while(util.categoryDone!=true);
        initCategory();

        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                for(int i=0;i<util.CategoryList.size();i++){
                    ((MyTextView)categoryList.getChildAt(i)).isSelected=false;
                    ((MyTextView)categoryList.getChildAt(i)).setTextColor(Color.parseColor("#666666"));
                    ((MyTextView)categoryList.getChildAt(i)).setBackgroundResource(R.drawable.bg_comment_attribute_off);
                }
                searchBYname();
            }
        });

    }

    private void searchBYname(){
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                util.search_key=search_bookmsg.getText().toString();
                util.searchByName();
                boolean judge=true;
                if(util.BookList.size()==0)
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

    private void searchBYCategory(final String key){
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                util.searchByCategory(key);
                boolean judge=true;
                if(util.BookList.size()==0)
                    judge=false;
                Message msgdata = new Message();
                msgdata.what = 1002;
                Bundle data = new Bundle();
                data.putBoolean("judge",judge);
                msgdata.setData(data);
                dHandler.sendMessage(msgdata);
            }
        };
        new Thread(run).start();
    }

    private void showSearch(){
        adapter = new SearchAdapter(getContext(), R.layout.book_list, util.BookList);
        listView = (ListView) getActivity().findViewById(R.id.bookListview);
        listView.setAdapter(adapter);
    }

    private void initCategory(){
        categoryList = (FlowTagGroup)getActivity().findViewById(R.id.categoryListview);

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;

        for(int i = 0; i < util.CategoryList.size(); i ++) {
            MyTextView myTextView= new MyTextView(getActivity().getApplicationContext());
            myTextView.setText(util.CategoryList.get(i));
            myTextView.setTextColor(Color.parseColor("#666666"));
            myTextView.setBackgroundResource(R.drawable.bg_comment_attribute_off);
            myTextView.isSelected=false;
            categoryList.addView(myTextView, lp);
//          点击事件和拿到数据
            myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  选中状态isSelected=true
                    if(((MyTextView)v).isSelected==true){
                        Log.e("111","选中状态");
                        ((MyTextView) v).setTextColor(Color.parseColor("#666666"));
                        ((MyTextView)v).setBackgroundResource(R.drawable.bg_comment_attribute_off);
                        ((MyTextView)v).isSelected=false;
                        util.BookList.clear();
                        showSearch();
                    }
//                  不选中状态
                    else{
                        Log.e("111", "未选中状态");
                        for(int i=0;i<util.CategoryList.size();i++){
                            ((MyTextView)categoryList.getChildAt(i)).isSelected=false;
                            ((MyTextView)categoryList.getChildAt(i)).setTextColor(Color.parseColor("#666666"));
                            ((MyTextView)categoryList.getChildAt(i)).setBackgroundResource(R.drawable.bg_comment_attribute_off);
                        }
                        ((MyTextView)v).isSelected=true;
                        ((MyTextView) v).setTextColor(Color.parseColor("#ffffff"));
                        ((MyTextView)v).setBackgroundResource(R.drawable.bg_comment_attribute_on);
                        searchBYCategory(((MyTextView) v).getText().toString());
                    }

                }
            });
        }
    }

    Handler dHandler = new Handler() {
        public void handleMessage(android.os.Message msgdata) {
            boolean judge,change;
            switch (msgdata.what) {
                case 1001:
                    judge = msgdata.getData().getBoolean("judge");
                    if(!judge)
                        Alarm("查询为空");
                    showSearch();
                    break;
                case 1002:
                    judge = msgdata.getData().getBoolean("judge");
                    if(!judge)
                        Alarm("查询为空");
                    showSearch();
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
