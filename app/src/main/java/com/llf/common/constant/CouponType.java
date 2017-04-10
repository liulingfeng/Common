package com.llf.common.constant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by llf on 2017/4/10.
 * 范围形的常量用枚举
 */

public enum CouponType {
    //现金券
    @SerializedName("1")
    CASH,
    //抵用券
    @SerializedName("2")
    DEBIT,
    //抵用券
    @SerializedName("3")
    DISCOUNT
}
