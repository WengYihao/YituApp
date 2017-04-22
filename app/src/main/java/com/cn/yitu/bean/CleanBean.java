package com.cn.yitu.bean;

import java.util.List;

/**
 * Created by Shinelon on 2017/4/21.
 */

public class CleanBean {

    private List<LatlngBean> itemBeen;
    private int cleaning_area_id;

    public List<LatlngBean> getItemBeen() {
        return itemBeen;
    }

    public int getCleaning_area_id() {
        return cleaning_area_id;
    }

    public void setItemBeen(List<LatlngBean> itemBeen) {
        this.itemBeen = itemBeen;
    }

    public void setCleaning_area_id(int cleaning_area_id) {
        this.cleaning_area_id = cleaning_area_id;
    }
}
