package com.llf.basemodel.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.llf.basemodel.R;

/**
 * Created by llf on 2017/3/2.
 */

public class ShareDialog {
    public static void show(Context context, final OneShare oneShare) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = dialog.getLayoutInflater().inflate(R.layout.dialog_share, null);
        LinearLayout weixin = (LinearLayout) view.findViewById(R.id.weixin);
        LinearLayout qq = (LinearLayout) view.findViewById(R.id.qq);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneShare.weixinShare();
                dialog.dismiss();
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneShare.qqShare();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public interface OneShare {
        void weixinShare();
        void qqShare();
    }
}
