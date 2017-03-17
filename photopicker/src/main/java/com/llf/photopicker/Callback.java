package com.llf.photopicker;

import java.io.File;

/**
 * Created by llf on 2017/3/10.
 * 选择情况监听
 */

public interface Callback {
    void onSingleImageSelected(String path);
    void onImageSelected(String path);
    void onImageUnselected(String path);
    void onCameraShot(File imageFile);
}
