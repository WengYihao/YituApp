package com.cn.yitu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.view.MyEditText;
import com.cn.yitu.xutil.Hide;
import com.cn.yitu.xutil.MD5;
import com.cn.yitu.xutil.StringXutil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity implements View.OnClickListener{

    private TextView back;
    private MyEditText username,age,gongzhong,phoneNumber,passWord,registerNumber,code;
    private RadioButton man,woman;
    private Button getCode,register,cancel;
    private String one,two,three,four,five,six,seven,sex;
    private QueryHTTP server;
    private TimeCount time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        back = (TextView)findViewById(R.id.back);
        username = (MyEditText)findViewById(R.id.username);
        age = (MyEditText)findViewById(R.id.age);
        gongzhong = (MyEditText)findViewById(R.id.gongzhong);
        phoneNumber = (MyEditText)findViewById(R.id.phoneNumber);
        passWord = (MyEditText)findViewById(R.id.passWord);
        registerNumber = (MyEditText)findViewById(R.id.registerNumber);
        code = (MyEditText)findViewById(R.id.code);
        man = (RadioButton)findViewById(R.id.man);
        woman = (RadioButton)findViewById(R.id.woman);
        getCode = (Button)findViewById(R.id.getCode);
        register = (Button)findViewById(R.id.register);
        cancel = (Button)findViewById(R.id.cancel);

        back.setOnClickListener(this);
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);

        server = new QueryHTTP();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.register:
                register();
                break;
            case R.id.cancel:
                break;
            case R.id.getCode:
                time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                four = phoneNumber.getText().toString().trim();
                if (StringXutil.isPhoneNumberValid(four)) {
                    SMSSDK.getVerificationCode("86", four);
                    ToastXutil.show("已发送短信至+86" + four);
                    time.start();
                } else {
                    ToastXutil.show("请输入正确的手机号");
                }
                break;
        }
    }
    private void register(){
        one = username.getText().toString();
        two = age.getText().toString();
        three = gongzhong.getText().toString();
        four = phoneNumber.getText().toString();
        try{
            five = MD5.getMD5(passWord.getText().toString());
        }catch (Exception e){
            Log.i("123","密码加密报错");
        }
        six = registerNumber.getText().toString();
        seven = code.getText().toString();
        if (man.isChecked()){
            sex = "男";
        }else if (woman.isChecked()){
            sex = "女";
        }
        if (StringXutil.isEmpty(one) && StringXutil.isEmpty(two)
                &&StringXutil.isEmpty(three) && StringXutil.isEmpty(four)
                && StringXutil.isEmpty(five) && StringXutil.isEmpty(six)
                && StringXutil.isEmpty(seven) && StringXutil.isEmpty(sex)){
            ToastXutil.show("以上信息部能为空");
        }else{
            server.register(one, two, three, sex, four, five, six, seven, new CallBack() {
                @Override
                public void onResponse(String response) {
                    Log.i("111",response.toString()+"----");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int resultnumber = jsonObject.getInt("resultnumber");
                        switch (resultnumber){
                            case 200:
                                ToastXutil.show("注册成功!");
                                Intent intent = new Intent();
                                intent.setClass(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                break;
                            case 201:
                                ToastXutil.show("输入参数为空");
                                break;
                            case 204:
                                ToastXutil.show("系统错误,请稍后在试!");
                                break;
                            case 212:
                                ToastXutil.show("短信验证码有误!");
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                Hide.hideKeyboard(ev, view, RegisterActivity.this);// 调用方法判断是否需要隐藏键盘
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCode.setText("重新获取");
            getCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getCode.setClickable(false);
            getCode.setText(millisUntilFinished / 1000 + "s后重新获取");
        }
    }
}
