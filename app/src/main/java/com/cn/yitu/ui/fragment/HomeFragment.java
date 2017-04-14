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

import com.amap.api.maps.model.LatLng;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.ui.R;
import com.cn.yitu.xutil.Convert;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

/**
 * Created by Shinelon on 2017/3/30.
 */

public class HomeFragment extends Fragment implements LocationListener,View.OnClickListener{

    private View view;
    private LocationManager locationManager;
    private Button sign;
    private double lat,lng;
    private QueryHTTP server;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,null);
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        initView();
        return view;
    }

    private void initView(){
        sign = (Button)view.findViewById(R.id.sign);
        sign.setOnClickListener(this);

        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
        getSign();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign:
                if (checkGPS()){
                    Location location = getLocation();
                    Log.i("127",location.getLatitude()+"---"+location.getLongitude());
                    LatLng latLng = Convert.convert(new LatLng(location.getLatitude(),location.getLongitude()),getActivity());
                    Log.i("127",latLng+"转换之后的结果");
                    server.sign(token, latLng.latitude, latLng.longitude, new CallBack() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("127",response.toString());
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                int resultnumber = jsonObject.getInt("resultnumber");
                                switch (resultnumber){
                                    case 200:
                                        ToastXutil.show("签到成功");
                                        stop();
                                        break;
                                    case 259:
                                        ToastXutil.show("请进入景区后再签到");
                                        break;
                                }
                            }catch (Exception e){
                                Log.i("126",e.getMessage()+"签到出错");
                            }
                        }
                    });
                }else{
                    ToastXutil.show("请打开GPS获取准确位置再进行签到");
                }
                break;
        }
    }

    private void getSign(){
        server.getSign(token, new CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i("127",response.toString());
            }
        });
    }
    private boolean checkGPS(){
        //判断GPS是否正常启动
        boolean isOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isOn){
            return true;
        }else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,0);
            return false;
        }
    }
    /**
     * 位置信息变化时触发
     */
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.i("126", "维度:" + lat + "\n经度:"
                    + lng);
        }else {
            Log.i("126", "获取不到数据");
        }
    }
    /**
     * GPS状态变化时触发
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    /**
     * GPS开启时触发
     */
    @Override
    public void onProviderEnabled(String provider) {

    }
    /**
     * GPS禁用时触发
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * 获取位置信息
     * @return
     */
    private Location getLocation()
    {
        //获取位置管理服务
        // locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //查找服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
        criteria.setAltitudeRequired(false); //海拔信息：不需要
        criteria.setBearingRequired(false); //方位信息: 不需要
        criteria.setCostAllowed(true);  //是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗
        String provider = locationManager.getBestProvider(criteria, true); //获取GPS信息
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 2000, 5, this);
        return location;
    }

    /**
     * 停止获取位置信息
     */
    private void stop(){
        locationManager.removeUpdates(this);
        //关闭GPS
        Settings.Secure.setLocationProviderEnabled(getActivity().getContentResolver(), LocationManager.GPS_PROVIDER, false);
    }
}
