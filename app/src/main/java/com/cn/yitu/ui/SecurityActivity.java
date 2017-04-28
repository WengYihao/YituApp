package com.cn.yitu.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.yitu.adapter.CleanAdapter;
import com.cn.yitu.adapter.SecurityAdapter;
import com.cn.yitu.bean.CleanBean;
import com.cn.yitu.bean.SecurityBean;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 安保管理系统
 */
public class SecurityActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private ListView listView;
    private SecurityAdapter securityAdapter;
    private List<SecurityBean> listSecurity;
    private String token;
    private QueryHTTP server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initView();
        getSecurity();
    }

    private void initView(){
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.list);
        back.setOnClickListener(this);

        listSecurity = new ArrayList<>();
        securityAdapter = new SecurityAdapter(listSecurity,this);
        listView.setAdapter(securityAdapter);
        token = SharePreferenceXutil.getToken();
        server = new QueryHTTP();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharePreferenceXutil.saveSecurityId(listSecurity.get(position).getThe_security_line_id());
                Intent intent = new Intent();
                intent.setClass(SecurityActivity.this,SecurityResultActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getSecurity(){
        server.getSecurity(token, new CallBack() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber = jsonObject.getInt("resultnumber");
                    switch (resultnumber){
                        case 200:
                            JSONArray array = jsonObject.getJSONArray("result");
                            Gson gson = new Gson();
                            List<SecurityBean> list = gson.fromJson(array.toString(),new TypeToken<List<SecurityBean>>(){}.getType());
                            if (list.size() > 0){
                                listSecurity.clear();
                                listSecurity.addAll(list);
                                securityAdapter.setList(listSecurity);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.i("123",e.getMessage()+"解析报错");

                }
            }
        });
    }
}
