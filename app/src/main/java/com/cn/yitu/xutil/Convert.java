package com.cn.yitu.xutil;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

/**
 * Created by Shinelon on 2017/4/14.
 */

public class Convert {
    /**
     * 根据类型 转换 坐标
     */
    public static LatLng convert(LatLng sourceLatLng, Context context) {
        CoordinateConverter converter  = new CoordinateConverter(context);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.valueOf("GPS"));
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }
}
