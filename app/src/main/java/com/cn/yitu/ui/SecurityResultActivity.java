package com.cn.yitu.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.cn.yitu.bean.LatlngBean;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.server.SecuritySend;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SecurityResultActivity extends AppCompatActivity implements View.OnClickListener{

    private MapView mapView;
    private AMap aMap;
    private TextView back,staff_name,staff_phone,onBind;
    private QueryHTTP server;
    private String token;
    private int id;
    private List<LatlngBean> list;
    private List<LatLng> latLngList;

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
        Log.i("123",list.toString()+"2222");

    }

    private void initView(){
        back = (TextView)findViewById(R.id.back);
        staff_name = (TextView)findViewById(R.id.staff_name);
        staff_phone = (TextView)findViewById(R.id.staff_phone);
        onBind = (TextView)findViewById(R.id.onBind);

        back.setOnClickListener(this);
        onBind.setOnClickListener(this);

        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
        id = SharePreferenceXutil.getSecurityId();
        list = new ArrayList<>();
        latLngList = new ArrayList<>();

        getLat();
        getTask();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.onBind:
                server.bingSecurity(token, id, new CallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("123", response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            int resultnumber = jsonObject.getInt("resultnumber");
                            if (resultnumber == 200) {
                                ToastXutil.show("绑定成功,请到首页开始任务!");
                                staff_phone.setVisibility(View.VISIBLE);
                                staff_phone.setText("员工电话:17665341329");
                                staff_name.setVisibility(View.VISIBLE);
                                staff_name.setText("员工姓名:翁益豪");
                                onBind.setText("已绑定");
                                onBind.setClickable(false);
                                SecurityResultActivity.this.finish();
                            }
                        } catch (Exception e) {
                            Log.i("123", e.getMessage() + "报错");
                        }
                    }
                });
                break;
//            case R.id.onStart:
//                new Thread(new SecuritySend(this)).start();
//                break;
        }
    }

    private void getTask(){
        server.getTask(token, new CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber  = jsonObject.getInt("resultnumber");
                    Log.i("126",response+"--");
                    switch (resultnumber){
                        case 200:
                            JSONObject json = jsonObject.getJSONObject("result");
//                            int i = json.length();
                            if (json.length() != 0){
                                staff_phone.setVisibility(View.VISIBLE);
                                staff_phone.setText("员工电话:17665341329");
                                staff_name.setVisibility(View.VISIBLE);
                                staff_name.setText("员工姓名:翁益豪");
                                onBind.setText("已绑定");
                                onBind.setClickable(false);
                                ToastXutil.show("请到首页开始任务");
                            }
                            break;
                    }

                }catch (Exception e){

                }
            }
        });
    }

    private void getLat(){
        server.getLine(token, id, new CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("123",response.toString()+"--");
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber = jsonObject.getInt("resultnumber");
                    switch (resultnumber){
                        case 200:
                            JSONObject json = jsonObject.getJSONObject("result");
                            JSONArray jsonArray = json.getJSONArray("the_security_line");
                            Gson gson = new Gson();
                            list = gson.fromJson(jsonArray.toString(), new TypeToken<List<LatlngBean>>() {
                            }.getType());
                            // 加一个箭头对象（NavigateArrow）对象在地图上
                            for (int i = 0;i<list.size();i++){
                                LatLng latLng = new LatLng(list.get(i).getLat(),list.get(i).getLng());
                                latLngList.add(latLng);
                            }
                            aMap.addNavigateArrow(new NavigateArrowOptions().addAll(latLngList).width(20));
                            aMap.moveCamera(CameraUpdateFactory
                                    .newCameraPosition(new CameraPosition(latLngList.get(0), 16f, 38.5f, 300)));
                            break;
                    }
                }catch (Exception e){
                    Log.i("123",e.getMessage()+"报错--");
                }
            }
        });
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
