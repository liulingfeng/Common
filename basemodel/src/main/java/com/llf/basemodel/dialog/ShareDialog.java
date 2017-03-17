package com.llf.basemodel.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.llf.basemodel.R;

/**
 * Created by llf on 2017/3/2.
 */

public class ShareDialog {
    public static void show(Context context){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = dialog.getLayoutInflater().inflate(R.layout.dialog_share, null);
        dialog.setContentView(view);
        dialog.show();
    }
}
