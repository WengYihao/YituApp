package com.cn.yitu.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.view.MyEditText;
import com.cn.yitu.xutil.Hide;
import com.cn.yitu.xutil.MD5;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.StringXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back,forget_pwd,register_tv,login;
    private MyEditText username_et,password_et;
    private QueryHTTP server;
    private String username,password,md5Password;
    private ProgressDialog progDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        automaticLogin();
    }

    private void initView(){
        back = (TextView)findViewById(R.id.back);
        forget_pwd = (TextView)findViewById(R.id.forget_pwd);
        register_tv = (TextView)findViewById(R.id.register_tv);
        login = (TextView)findViewById(R.id.login);
        username_et = (MyEditText)findViewById(R.id.username_et);
        password_et = (MyEditText)findViewById(R.id.password_et);
        if (!StringXutil.isEmpty(SharePreferenceXutil.getUserName()) && !StringXutil.isEmpty(SharePreferenceXutil.getPassword())){
            username_et.setText(SharePreferenceXutil.getUserName());
            password_et.setText(SharePreferenceXutil.getPassword());
        }
        back.setOnClickListener(this);
        forget_pwd.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        login.setOnClickListener(this);

        server = new QueryHTTP();
        progDialog = new ProgressDialog(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.forget_pwd:
                ToastXutil.show("忘记密码了吗?");
                break;
            case R.id.register_tv:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.login:
                username = username_et.getText().toString();
                password = password_et.getText().toString();
                if (StringXutil.isEmpty(username) && StringXutil.isEmpty(password)){
                    ToastXutil.show("用户名密码不能为空");
                }else {
                    try {
                        md5Password = MD5.getMD5(password);
                        showDialog();
                        login();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 快速登录
     */
    public void automaticLogin(){
        if (SharePreferenceXutil.isSuccess() && !SharePreferenceXutil.isExit()){
            server.automaticLogin(SharePreferenceXutil.getToken(), new CallBack() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int resultnumber = jsonObject.getInt("resultnumber");
                        switch (resultnumber){
                            case 200:
                                JSONObject json = jsonObject.getJSONObject("result");
                                boolean isSign = json.getBoolean("sign_in");
                                if (isSign){
                                    //如果签到了跳转首页
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }else{
                                    //如果没有,跳转签到界面
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this,SignActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                        }
                    }catch (Exception e){
                        Log.i("123",e.getMessage()+"baocuo");
                    }
                }
            });
        }
    }

    private void login(){
        server.login(username, md5Password, new CallBack() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("123",response+"返回值");
                    int resutltnumber = jsonObject.getInt("resultnumber");
                    switch(resutltnumber){
                        case 200:
                            dismissDialog();
                            ToastXutil.show("登录成功");
                            JSONObject json = jsonObject.getJSONObject("result");
                            String token = json.getString("account_token");
                            SharePreferenceXutil.saveUserNameAndPwd(username, password);
                            SharePreferenceXutil.saveToken(token);
                            SharePreferenceXutil.setSuccess(true);
                            SharePreferenceXutil.setExit(false);
                            int workId = json.getJSONObject("staff").getInt("type_of_work_id");
                            SharePreferenceXutil.saveWorkId(workId);
                            boolean isSign = json.getJSONObject("staff").getBoolean("sign_in");
                            if (isSign){
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,SignActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                            break;
                        case 201:
                            ToastXutil.show("输入参数为空");
                            dismissDialog();
                            break;
                        case 217:
                            ToastXutil.show("账号密码错误");
                            dismissDialog();
                            break;
                    }
                }catch(Exception e){
                    Log.i("112",e.getMessage()+"报错");
                }
            }
        });
    }
    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在连接服务器...");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                Hide.hideKeyboard(ev, view, LoginActivity.this);// 调用方法判断是否需要隐藏键盘
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
