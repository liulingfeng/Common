package com.llf.basemodel.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by llf on 2017/5/9.
 * 强制更新的dialog
 */

public class UpdateDialog {
    public static void show(Context context, String content, final OnUpdate onUpdate) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("应用更新");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        float density = context.getResources().getDisplayMetrics().density;
        TextView tv = new TextView(context);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));
        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);
        tv.setText(content);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onUpdate.ok();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnUpdate{
        void cancel();
        void ok();
    }
}
