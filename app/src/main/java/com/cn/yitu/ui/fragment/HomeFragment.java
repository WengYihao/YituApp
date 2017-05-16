package com.cn.yitu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.server.SecuritySend;
import com.cn.yitu.ui.BoatActivity;
import com.cn.yitu.ui.CleanActivity;
import com.cn.yitu.ui.CleanResultActivity;
import com.cn.yitu.ui.FerryCarActivity;
import com.cn.yitu.ui.R;
import com.cn.yitu.ui.SecurityActivity;
import com.cn.yitu.ui.SendActivity;
import com.cn.yitu.xutil.Convert;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shinelon on 2017/3/30.
 */

public class HomeFragment extends Fragment implements  LocationSource,AMapLocationListener,View.OnClickListener{

    private View view;
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private static double lat,lng;
    private boolean isFirst = true;
    private boolean onBind = false;
    private TextView start,send,task,route;
    private QueryHTTP server;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,null);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initView();
        initMap();
        return view;
    }

    /**
     * 初始化地图
     */
    private void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        setUpMap();
    }
    private void setUpMap(){
        aMap.setLocationSource(this);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 跟随模式
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }
    private void initView(){
        start = (TextView)view.findViewById(R.id.start);
        send = (TextView)view.findViewById(R.id.send);
        task = (TextView)view.findViewById(R.id.task);
        route = (TextView)view.findViewById(R.id.route);

        start.setOnClickListener(this);
        send.setOnClickListener(this);
        task.setOnClickListener(this);
        route.setOnClickListener(this);

        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();

        getTask();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                if (onBind){
                    new Thread(new SecuritySend(this)).start();
                    start.setText("结束任务");
                }
                break;
            case R.id.send:
                startActivity(new Intent(getActivity(),SendActivity.class));
                break;
            case R.id.task:

                switch (SharePreferenceXutil.getWorkId()){
                    case 1:
                        startActivity(new Intent(getActivity(),SecurityActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),CleanActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),FerryCarActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(),BoatActivity.class));
                        break;
                }
                break;
            case R.id.route:

                break;
        }
    }

    public void aa(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }).start();;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    startActivity(new Intent(getActivity(),SendActivity.class));
                    break;
            }
        }
    };
    /**
     * 获取是否绑定任务
     */
    private void getTask(){
        server.getTask(token, new CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber  = jsonObject.getInt("resultnumber");
                    switch (resultnumber){
                        case 200:
                            JSONObject json = jsonObject.getJSONObject("result");
                            if (json != null){
                                onBind = true;
                            }
                            break;
                    }

                }catch (Exception e){

                }
            }
        });
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 15));//定位成功移到当前定位点
                    isFirst = false;
                    Log.i("124","123456789");
                }
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                Log.i("124",lat+"---"+lng);
            }else{
                Log.i("123",aMapLocation.getErrorCode()+"错误码"+aMapLocation.getErrorInfo()+"错误信息");
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null){
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);// 设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);// 设置为高精度定位模式
            mLocationOption.setInterval(1000);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public static double getLat(){
        return lat;
    }
    public static double getLng(){
        return  lng;
    }
}
