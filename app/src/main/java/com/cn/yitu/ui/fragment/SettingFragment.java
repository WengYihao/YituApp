package com.cn.yitu.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.yitu.ui.R;
/**
 * Created by Shinelon on 2017/3/30.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView exit;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,null);
        return view;
    }

    private void initView(){
        exit = (TextView)view.findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:

                break;
        }

    }
}
