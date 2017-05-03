package com.cn.yitu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.ui.BoatActivity;
import com.cn.yitu.ui.CleanActivity;
import com.cn.yitu.ui.FerryCarActivity;
import com.cn.yitu.ui.R;
import com.cn.yitu.ui.SecurityActivity;
import com.cn.yitu.xutil.Convert;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shinelon on 2017/3/30.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button security,cleaning,boat,ferry_car,helping;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,null);
        initView();
        return view;
    }

    private void initView(){
        security = (Button)view.findViewById(R.id.security);
        cleaning = (Button)view.findViewById(R.id.cleaning);
        boat = (Button)view.findViewById(R.id.boat);
        ferry_car = (Button)view.findViewById(R.id.ferry_car);
        helping = (Button)view.findViewById(R.id.helping);

        security.setOnClickListener(this);
        cleaning.setOnClickListener(this);
        boat.setOnClickListener(this);
        ferry_car.setOnClickListener(this);
        helping.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.security:
                startActivity(new Intent(getActivity(), SecurityActivity.class));//安保界面
                break;
            case R.id.cleaning:
                startActivity(new Intent(getActivity(), CleanActivity.class));//保洁界面
                break;
            case R.id.boat:
                startActivity(new Intent(getActivity(), BoatActivity.class));//保洁界面
                break;
            case R.id.ferry_car:
                startActivity(new Intent(getActivity(), FerryCarActivity.class));//保洁界面
                break;
            case R.id.helping:

                break;
        }
    }
}
