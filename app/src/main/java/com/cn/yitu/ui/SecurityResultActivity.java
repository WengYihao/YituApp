package com.cn.yitu.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

public class SecurityResultActivity extends AppCompatActivity implements LocationSource,AMapLocationListener,View.OnClickListener{

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private double lat,lng;
    private boolean isFirst = true;
    private TextView staff_name,staff_phone,onBind,onStart;
    private QueryHTTP server;
    private String token;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_result);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initView();
        initMap();
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
        staff_name = (TextView)findViewById(R.id.staff_name);
        staff_phone = (TextView)findViewById(R.id.staff_phone);
        onBind = (TextView)findViewById(R.id.onBind);
        onStart = (TextView)findViewById(R.id.onStart);

        onBind.setOnClickListener(this);
        onStart.setOnClickListener(this);

        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
        id = SharePreferenceXutil.getSecurityId();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onBind:
                server.bingSecurity(token, id, new CallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("123", response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            int resultnumber = jsonObject.getInt("resultnumber");
                            if (resultnumber == 200) {
                                ToastXutil.show("绑定成功");
                                staff_phone.setVisibility(View.VISIBLE);
                                staff_phone.setText("员工电话:17665341329");
                                staff_name.setVisibility(View.VISIBLE);
                                staff_name.setText("员工姓名:翁益豪");
                            }
                        } catch (Exception e) {
                            Log.i("123", e.getMessage() + "报错");
                        }
                    }
                });
                break;
            case R.id.onStart:
                new Thread(new SecuritySend()).start();
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18));//定位成功移到当前定位点
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
            mlocationClient = new AMapLocationClient(SecurityResultActivity.this);
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
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
