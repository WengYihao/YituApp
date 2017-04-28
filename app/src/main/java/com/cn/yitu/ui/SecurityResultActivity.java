package com.cn.yitu.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.server.SecuritySend;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

public class SecurityResultActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView staff_name,staff_phone,onBind,onStart;
    private QueryHTTP server;
    private String token;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_result);
        initView();
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
}
