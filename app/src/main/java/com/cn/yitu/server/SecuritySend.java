package com.cn.yitu.server;

import android.util.Log;

import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Shinelon on 2017/4/28.
 */

public class SecuritySend implements Runnable{

    private String accountToken;
    private double lat,lng;
    private String Ip = "192.168.100.8";// 服务器ip地址
    private Integer port = 8888;// 端口
    JsonObject jsonObject = null;
    private Socket socket = null;
    private OutputStream outputStream = null;

    private void startsocket() {// 启动socket
        try {
            socket = new Socket(Ip, port);
            outputStream = socket.getOutputStream();
        } catch (Exception e) {
            ToastXutil.show("接收1出错！");
            Log.i("129", e + "上传出错");
        }

    }
    @Override
    public void run() {
        try {
            startsocket();// 启动socket
            jsonObject = new JsonObject();
            while (socket != null) {
                Thread.sleep(3000);
                jsonObject.addProperty("accountToken", SharePreferenceXutil.getToken());
                jsonObject.addProperty("lat",1.1);
                jsonObject.addProperty("lng",2.2);
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                Log.i("129", jsonObject.toString() + "111");
                outputStream.flush();
            }
        } catch (Exception e) {
            Log.i("129", e.getMessage() + "上传出错");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    Log.i("129", e + "上传出错");
                }
            }
        }
    }
}
