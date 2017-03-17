package llf.videomodel.utils;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by llf on 2017/3/16.
 * 调节亮度
 */

public class LightUtil {
    /**
     * 调节亮度
     * @param activity
     * @param value
     */
    public static void SetLightness(Activity activity, int value) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = (value <= 0 ? 1 : value) / 255f;
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            Log.d("视频播放","无法改变亮度");
        }
    }

    /**
     * 获取亮度0~255的值
     * @param activity
     * @return
     */
    public static int GetLightness(Activity activity) {
        return Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
    }
}
