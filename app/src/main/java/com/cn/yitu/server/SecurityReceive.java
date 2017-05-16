package com.cn.yitu.server;

import android.util.Log;

import com.cn.yitu.ui.fragment.HomeFragment;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Shinelon on 2017/5/6.
 */

public class SecurityReceive implements Runnable {

    private InputStream inputStream;
    private HomeFragment fragment;
    private Socket socket;
    public static boolean isOpen = true;


    public void setFragment(HomeFragment fragment) {
        this.fragment = fragment;
    }

    public SecurityReceive(Socket socket) {
        this.socket = socket;
    }

    public SecurityReceive(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private String text = "null";

    public String getText() {
        return text;
    }
    @Override
    public void run() {
        int len = 0;
        byte[] buff = new byte[1024];
        try {
            inputStream = socket.getInputStream();
            while ((len = inputStream.read(buff)) != -1) {
                text = new String(buff, 0, len);
                Log.i("128", text + "返回值");
                    if (text.equals("end")) {
//                        homefragment.closeSocket();
                        socket.close();
                    }else {
                        JSONObject jsonObject = new JSONObject(text);
                        int resultnumber = jsonObject.getInt("resultnumber");
                        switch (resultnumber) {
                            case 200:
                                JSONObject json = jsonObject.getJSONObject("result");
                                Log.i("129",json.length()+"集合长度");
                                if (json.length() != 0){
                                    boolean isAccomplish = json.getBoolean("isAccomplish");
                                    boolean isEnter = json.getBoolean("isEnter");
                                    if (isEnter && isAccomplish && isOpen){
                                        fragment.aa();
                                        isOpen = false;
                                    }
                                }break;
                            default:
                                break;
                        }
                    }
            }
        } catch (Exception e) {
            Log.i("111", e + "Send出错");
            try {

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            try {
            } catch (Exception e2) {
                Log.i("130", "121");
            }
        }
    }
}
