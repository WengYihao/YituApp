package com.cn.yitu.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cn.yitu.ui.R;
/**
 * Created by Shinelon on 2017/3/30.
 */

public class SettingFragment extends Fragment{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,null);
        return view;
    }
}
