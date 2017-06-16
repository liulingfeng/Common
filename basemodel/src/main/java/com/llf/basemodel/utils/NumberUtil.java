package com.llf.basemodel.utils;

import java.text.DecimalFormat;

/**
 * Created by llf on 2017/6/16.
 * 数字小数点处理工具
 */

public class NumberUtil {
    public static String moneyFormat(double price) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(price);
        } catch (Exception e) {
            return "";
        }
    }
}
