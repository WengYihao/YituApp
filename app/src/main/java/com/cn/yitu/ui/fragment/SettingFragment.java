package com.cn.yitu.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.ui.R;
import com.cn.yitu.ui.SignActivity;
import com.cn.yitu.view.show.ToastDialog;
import com.cn.yitu.xutil.ActivityCollector;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

/**
 * Created by Shinelon on 2017/3/30.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView exit;
    private RelativeLayout layout;
    private QueryHTTP server;
    private String token;
    private double lat,lng;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,null);
        initView();
        return view;
    }

    private void initView(){
        layout = (RelativeLayout)view.findViewById(R.id.layout);
        exit = (TextView)view.findViewById(R.id.exit);

        layout.setOnClickListener(this);
        exit.setOnClickListener(this);

        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
        lat = HomeFragment.getLat();
        lng = HomeFragment.getLng();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout:
                server.signOut(token,lat,lng, new CallBack() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("123",response+"***");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int resultnumber = jsonObject.getInt("resultnumber");
                            switch (resultnumber){
                                case 200:
                                    ToastXutil.show("签退成功");
                                    startActivity(new Intent(getActivity(), SignActivity.class));
                                    ActivityCollector.finishAll();
                                    break;
                            }
                        }catch (Exception e){

                        }
                    }
                });

                break;
            case R.id.exit:
                ToastDialog.exit(getActivity());
                break;
        }

    }
}
