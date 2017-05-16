package com.cn.yitu.server;

import android.util.Log;

import com.cn.yitu.ui.SecurityResultActivity;
import com.cn.yitu.ui.fragment.HomeFragment;
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
    //    private String Ip = "119.23.251.169";// 服务器ip地址
    private String Ip = "192.168.100.8";// 服务器ip地址
    private Integer port = 8888;// 端口
    JsonObject jsonObject = null;
    private Socket socket = null;
    private OutputStream outputStream = null;
    private SecurityReceive receive;
    private HomeFragment fragment;

    public SecuritySend(HomeFragment fragment) {
        this.fragment = fragment;
    }

    public SecurityReceive getReceive() {
        return receive;
    }

    private void startsocket() {// 启动socket
        try {
            socket = new Socket(Ip, port);
            outputStream = socket.getOutputStream();
            receive = new SecurityReceive(socket);
            receive.setFragment(fragment);
            new Thread(receive).start();
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
                jsonObject.addProperty("lat",fragment.getLat());
                jsonObject.addProperty("lng",fragment.getLng());
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
    /**
     * 关闭socket
     */
    public void isHandle() {
        try {
            outputStream = socket.getOutputStream();
            outputStream.write("end".toString().getBytes("UTF-8"));
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("128", e + "发送end报错 ------ sos");
        }
    }
}
