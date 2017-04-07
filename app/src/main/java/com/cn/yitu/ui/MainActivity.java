package com.cn.yitu.ui;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.cn.yitu.ui.fragment.HomeFragment;
import com.cn.yitu.ui.fragment.SettingFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private RadioButton title01,title02;
    private HomeFragment homeFragment;
    private SettingFragment settingFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        title01 = (RadioButton)findViewById(R.id.title01);
        title02 = (RadioButton)findViewById(R.id.title02);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title01.setChecked(true);
        show(0);
    }

    private void show(int index){
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.container, homeFragment);
                }
                fragmentTransaction.show(homeFragment);
                break;
            case 1:
                if (settingFragment == null){
                    settingFragment = new SettingFragment();
                    fragmentTransaction.add(R.id.container,settingFragment);
                }
                fragmentTransaction.show(settingFragment);
                break;
        }
        switch (currentIndex){
            case 0:
                fragmentTransaction.hide(homeFragment);
                break;
            case 1:
                fragmentTransaction.hide(settingFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title01:
                show(0);
                Log.i("111","首页");
                break;
            case R.id.title02:
                show(1);
                Log.i("111","设置");
                break;
        }

    }
}
