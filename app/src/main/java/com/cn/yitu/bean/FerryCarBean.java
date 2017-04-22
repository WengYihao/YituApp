package com.cn.yitu.bean;

/**
 * Created by Shinelon on 2017/4/22.
 */

public class FerryCarBean {

    private int ferry_push_id;//摆渡车id
    private String ferry_push;//摆渡车名字
    private int ferry_push_state_id;//摆渡车状态id
    private String ferry_push_state;//摆渡车状态

    public int getFerry_push_id() {
        return ferry_push_id;
    }

    public void setFerry_push_id(int ferry_push_id) {
        this.ferry_push_id = ferry_push_id;
    }

    public String getFerry_push() {
        return ferry_push;
    }

    public void setFerry_push(String ferry_push) {
        this.ferry_push = ferry_push;
    }

    public int getFerry_push_state_id() {
        return ferry_push_state_id;
    }

    public void setFerry_push_state_id(int ferry_push_state_id) {
        this.ferry_push_state_id = ferry_push_state_id;
    }

    public String getFerry_push_state() {
        return ferry_push_state;
    }

    public void setFerry_push_state(String ferry_push_state) {
        this.ferry_push_state = ferry_push_state;
    }
}
