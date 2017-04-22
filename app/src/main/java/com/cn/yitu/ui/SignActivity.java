package com.cn.yitu.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.xutil.Convert;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

public class SignActivity extends AppCompatActivity implements LocationListener,View.OnClickListener{

    private TextView sign;
    private LocationManager locationManager;
    private double lat,lng;
    private String token;
    private QueryHTTP server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        initView();
    }

    private void initView(){
        sign = (TextView)findViewById(R.id.sign);
        sign.setOnClickListener(this);
        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign:
                if (checkGPS()) {
                    Location location = getLocation();
                    Log.i("127", location.getLatitude() + "---" + location.getLongitude());
                    LatLng latLng = Convert.convert(new LatLng(location.getLatitude(), location.getLongitude()), this);
                    Log.i("127", latLng + "转换之后的结果");
                    server.sign(token, latLng.latitude, latLng.longitude, new CallBack() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("127", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int resultnumber = jsonObject.getInt("resultnumber");
                                switch (resultnumber) {
                                    case 200:
                                        ToastXutil.show("签到成功");
                                        stop();
                                        Intent intent = new Intent();
                                        intent.setClass(SignActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SignActivity.this.finish();
                                        break;
                                    case 259:
                                        ToastXutil.show("请进入景区后再签到");
                                        break;
                                }
                            } catch (Exception e) {
                                Log.i("126", e.getMessage() + "签到出错");
                            }
                        }
                    });
                } else {
                    ToastXutil.show("请打开GPS获取准确位置再进行签到");
                }
                break;
        }
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
        Location location = null;
        try{
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 2000, 5, this);

        }catch (Exception e){
            ToastXutil.show("手机缺少用户权限");
        }
        return location;
    }

    /**
     * 停止获取位置信息
     */
    private void stop(){
        locationManager.removeUpdates(this);
    }
}
