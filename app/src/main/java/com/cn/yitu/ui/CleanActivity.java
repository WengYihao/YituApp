package com.cn.yitu.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.yitu.adapter.CleanAdapter;
import com.cn.yitu.bean.CleanBean;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 保洁管理系统
 */
public class CleanActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private ListView listView;
    private CleanAdapter cleanAdapter;
    private List<CleanBean> listClean;
    private String token;
    private QueryHTTP server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        initView();
        getClean();
    }
    private void initView(){
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.list);
        back.setOnClickListener(this);

        listClean = new ArrayList<>();
        cleanAdapter = new CleanAdapter(listClean,this);
        listView.setAdapter(cleanAdapter);
        token = SharePreferenceXutil.getToken();
        server = new QueryHTTP();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getClean(){
        server.getClean(token, new CallBack() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber = jsonObject.getInt("resultnumber");
                    Log.i("123",resultnumber+"----");
                    switch (resultnumber){
                        case 200:
                            Log.i("123","200"+"----");
                            JSONArray array = jsonObject.getJSONArray("result");
                            Gson gson = new Gson();
                            List<CleanBean> list = gson.fromJson(array.toString(),new TypeToken<List<CleanBean>>(){}.getType());
                            if (list.size() > 0){
                                Log.i("123","300"+"----");
                                listClean.clear();
                                listClean.addAll(list);
                                cleanAdapter.setList(listClean);
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
