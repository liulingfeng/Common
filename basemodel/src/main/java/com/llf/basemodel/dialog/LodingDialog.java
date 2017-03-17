package com.llf.basemodel.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.llf.basemodel.R;

/**
 * Created by llf on 2017/3/1.
 * 加载的dialog
 */

public class LodingDialog {
    private static ProgressDialog lodingDialog;

    public static void showWaittingDialog(Context context) {

        lodingDialog = new ProgressDialog(context, R.style.CustomProgressDialog);
        LayoutInflater mInflater = lodingDialog.getLayoutInflater();
        View mView = mInflater.inflate(R.layout.dialog_loading, null);
        lodingDialog.setCancelable(true);
        lodingDialog.setCanceledOnTouchOutside(false);
        lodingDialog.setContentView(mView);
        lodingDialog.show();
    }

    public static void closeWaittingDialog() {
        if (lodingDialog != null)
            lodingDialog.dismiss();
    }
}
