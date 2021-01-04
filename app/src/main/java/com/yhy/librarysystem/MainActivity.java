package com.yhy.librarysystem;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Library_Frag library_frag;
    private Mine_Frag mine_frag;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                util.getAllCategory();
            }
        }).start();
        initFragment();

    }

    /**
     * 初始化三个Fragment
     */
    private void initFragment() {
        library_frag = new Library_Frag();
        mine_frag = new Mine_Frag();
        fragments = new Fragment[]{library_frag, mine_frag};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, library_frag).show(library_frag).commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    /**
     * 判断选择的菜单
     */
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.search_nav:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }

                case R.id.mine_nav:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                }
            }
            return false;
        }
    };

    /**
     * 切换Fragment
     */
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.mainview,fragments[index]);

        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_nav: {
                if (lastfragment != 0) {
                    switchFragment(lastfragment, 0);
                    lastfragment = 0;

                }
                break;
            }
            case R.id.mine_nav:
            {
                if(lastfragment!=2)
                {
                    switchFragment(lastfragment,2);
                    lastfragment=2;

                }
                break;
            }
            default:
        }
        return true;
    }

}
