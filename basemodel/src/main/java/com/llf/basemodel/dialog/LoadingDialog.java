package com.llf.basemodel.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.llf.basemodel.R;
import com.llf.basemodel.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by llf on 2016/10/14.
 * ProgressDialog必须要先show再设置ContentView
 * 加载的dialog
 */

public class LoadingDialog {
    private static ProgressDialog mWaittingDialog;

    public static void showWaittingDialog(Context context) {
        try {
            if (mWaittingDialog != null) {
                mWaittingDialog.dismiss();
                mWaittingDialog = null;
            }

            mWaittingDialog = new ProgressDialog(context, R.style.CustomProgressDialog);
            mWaittingDialog.setCancelable(false);
            LayoutInflater mInflater = mWaittingDialog.getLayoutInflater();
            View mView = mInflater.inflate(R.layout.dialog_loading, null);
            mWaittingDialog.setContentView(mView);
            mWaittingDialog.show();
            // 3秒后还未完成任务，则设置为可取消
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (mWaittingDialog != null)
                        mWaittingDialog.setCancelable(true);
                }
            };
            Timer timer = new Timer(true);
            timer.schedule(task, 3000);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeWaittingDialog() {
        try {
            if (mWaittingDialog != null)
                mWaittingDialog.dismiss();
            mWaittingDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
