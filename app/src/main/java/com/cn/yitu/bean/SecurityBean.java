package com.cn.yitu.bean;

import java.util.List;

/**
 * Created by Shinelon on 2017/4/22.
 */

public class SecurityBean {
    private int the_security_line_id;
    private List<LatlngBean> the_security_line;

    public int getThe_security_line_id() {
        return the_security_line_id;
    }

    public void setThe_security_line_id(int the_security_line_id) {
        this.the_security_line_id = the_security_line_id;
    }

    public List<LatlngBean> getThe_security_line() {
        return the_security_line;
    }

    public void setThe_security_line(List<LatlngBean> the_security_line) {
        this.the_security_line = the_security_line;
    }
}
