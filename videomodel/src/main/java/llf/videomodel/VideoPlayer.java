package llf.videomodel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import llf.videomodel.utils.LightUtil;
import llf.videomodel.utils.PlayerUtil;

import static llf.videomodel.utils.PlayerUtil.dip2px;

/**
 * Created by llf on 2017/3/16.
 * 视频播放器
 */

public class VideoPlayer extends FrameLayout implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        SurfaceHolder.Callback,
        GestureDetector.OnGestureListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener {
    private Context context;
    private Activity activity;
    private boolean autoPlay;

    private LinearLayout controlLL, playError, netError;
    private SurfaceView mSurfaceView;
    private ImageView ivPlay, ivAdjust, playerLock, ivBattery, ivPlayCenter, gesture_iv_player_volume, gesture_iv_progress;
    private SeekBar mSeekBar;
    private TextView tvPlayTime, tvAllTime, tvTitle, tvSystemTime, geture_tv_volume_percentage, geture_tv_progress_time, geture_tv_light_percentage;
    private RelativeLayout topMenu, playerScreen, gesture_volume_layout, gesture_progress_layout, gesture_light_layout;
    private ProgressBar mProgressBar;

    // SurfaceView的创建比较耗时，要注意
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private boolean isFullscreen;//是否全屏
    private boolean isPrepare;
    private boolean isLockScreen;
    private boolean isNeedBatteryListen = true, isNeedNetChangeListen = true;
    private GestureDetector gestureDetector;
    private AudioManager audiomanager;
    private int maxVolume, currentVolume;

    private String videoPath;//视频地址
    private String videoTitle = "视频标题";//标题
    private int video_position = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Timer timer_controller, timer_video_time;
    private TimerTask task_controller, task_video_timer;

    //控件的位置信息
    private float mediaPlayerX;
    private float mediaPlayerY;

    public VideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        this.activity = (Activity) context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayer, defStyleAttr, 0);
        autoPlay = a.getBoolean(R.styleable.VideoPlayer_autoPlay, true);
        a.recycle();

        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_player, this);
        controlLL = (LinearLayout) view.findViewById(R.id.controlLL);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        ivPlay = (ImageView) view.findViewById(R.id.ivPlay);
        ivAdjust = (ImageView) view.findViewById(R.id.ivAdjust);
        tvAllTime = (TextView) view.findViewById(R.id.tvAllTime);
        tvPlayTime = (TextView) view.findViewById(R.id.tvPlayTime);
        mSeekBar = (SeekBar) view.findViewById(R.id.progress);
        tvSystemTime = (TextView) view.findViewById(R.id.tv_system_time);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        topMenu = (RelativeLayout) view.findViewById(R.id.rl_top_menu);
        playerScreen = (RelativeLayout) view.findViewById(R.id.player_rl_screen);
        playerLock = (ImageView) view.findViewById(R.id.player_iv_lock);
        playError = (LinearLayout) view.findViewById(R.id.player_ll_error);
        netError = (LinearLayout) view.findViewById(R.id.player_ll_net);
        mProgressBar = (ProgressBar) view.findViewById(R.id.player_progressBar);
        ivBattery = (ImageView) view.findViewById(R.id.iv_battery);
        ivPlayCenter = (ImageView) view.findViewById(R.id.player_iv_play_center);

        mSeekBar.setOnSeekBarChangeListener(this);
        ivPlay.setOnClickListener(this);
        ivAdjust.setOnClickListener(this);
        playerLock.setOnClickListener(this);
        playError.setOnClickListener(this);
        netError.setOnClickListener(this);
        ivPlayCenter.setOnClickListener(this);

        //初始化
        tvSystemTime.setText(PlayerUtil.getCurrentHHmmTime());
        controlLL.setVisibility(View.GONE);
        topMenu.setVisibility(View.GONE);
        playerLock.setVisibility(View.GONE);
        initLock();
        playerScreen.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        playError.setVisibility(View.GONE);
        netError.setVisibility(View.GONE);
        ivPlayCenter.setVisibility(View.GONE);
        initTopMenu();

        if (!autoPlay) {
            ivPlayCenter.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        //初始化SurfaceView
        initSurfaceView();

        //初始化手势
        initGesture();

        //存储控件的位置信息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayerX = getX();
                mediaPlayerY = getY();
            }
        }, 1000);
    }

    private void initLock() {
        if (isFullscreen) {
            playerLock.setVisibility(View.VISIBLE);
        } else {
            playerLock.setVisibility(View.GONE);
        }
    }

    private void initTopMenu() {
        tvTitle.setText(videoTitle);
        if (isFullscreen) {
            topMenu.setVisibility(View.VISIBLE);
        } else {
            topMenu.setVisibility(View.GONE);
        }
    }

    private void initSurfaceView() {
        // 得到SurfaceView容器，播放的内容就是显示在这个容器里面
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        // SurfaceView的一个回调方法
        surfaceHolder.addCallback(this);
    }

    /**
     * 没有网络
     */
    private void showNoNetView() {
        ivPlayCenter.setVisibility(View.GONE);
        netError.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        playError.setVisibility(View.GONE);
    }

    /**
     * 视频地址操作
     */
    private void showErrorView() {
        ivPlayCenter.setVisibility(View.GONE);
        netError.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        playError.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏下方操作杆
     */
    private void dismissControllerMenu() {
        if (isFullscreen && !isLockScreen) {
            playerLock.setVisibility(View.GONE);
        }
        topMenu.setVisibility(View.GONE);
        controlLL.setVisibility(View.GONE);
    }

    /**
     * 上下菜单的显示和隐藏
     */
    private void initBottomMenuState() {
        tvSystemTime.setText(PlayerUtil.getCurrentHHmmTime());
        if (controlLL.getVisibility() == View.GONE) {
            initControllerTask();
            controlLL.setVisibility(View.VISIBLE);
            if (isFullscreen) {
                topMenu.setVisibility(View.VISIBLE);
                playerLock.setVisibility(View.VISIBLE);
            }
        } else {
            destroyControllerTask(true);
        }
    }

    /**
     * 设置计时器,控制器的影藏和显示
     */
    private void initControllerTask() {
        timer_controller = new Timer();
        task_controller = new TimerTask() {
            @Override
            public void run() {
                destroyControllerTask(false);
            }
        };
        timer_controller.schedule(task_controller, 5000);
        initTimeTask();
    }

    private void destroyControllerTask(boolean isMainThread) {
        if (isMainThread) {
            dismissControllerMenu();
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    dismissControllerMenu();
                }
            });
        }
        if (timer_controller != null && task_controller != null) {
            timer_controller.cancel();
            task_controller.cancel();
            timer_controller = null;
            task_controller = null;
        }
        destroyTimeTask();
    }

    /**
     * 播放进度
     */
    private void initTimeTask() {
        timer_video_time = new Timer();
        task_video_timer = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer == null) {
                            return;
                        }
                        //设置时间
                        tvPlayTime.setText(PlayerUtil.long2Str(mediaPlayer.getCurrentPosition()));
                        tvAllTime.setText(PlayerUtil.long2Str(mediaPlayer.getDuration()));
                        //进度条
                        int progress = mediaPlayer.getCurrentPosition();
                        mSeekBar.setProgress(progress);
                    }
                });
            }
        };
        timer_video_time.schedule(task_video_timer, 0, 1000);
    }

    private void destroyTimeTask() {
        if (timer_video_time != null && task_video_timer != null) {
            timer_video_time.cancel();
            task_video_timer.cancel();
            timer_video_time = null;
            task_video_timer = null;
        }
    }

    private void unLockScreen() {
        isLockScreen = false;
        playerLock.setImageResource(R.drawable.player_lock_open);
    }

    private void lockScreen() {
        isLockScreen = true;
        playerLock.setImageResource(R.drawable.player_lock_close);
    }

    private BatteryReceiver batteryReceiver;

    private void registerBatteryReceiver() {
        if (batteryReceiver == null) {
            //注册广播接受者
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            //创建广播接受者对象
            batteryReceiver = new BatteryReceiver();
            //注册receiver
            context.registerReceiver(batteryReceiver, intentFilter);
        }
    }

    private void unRegisterBatteryReceiver() {
        if (batteryReceiver != null) {
            context.unregisterReceiver(batteryReceiver);
        }
    }

    /**
     * 电量广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                int battery = (level * 100) / scale;

                ivBattery.setVisibility(View.VISIBLE);
                if (battery > 0 && battery < 20) {
                    ivBattery.setImageResource(R.drawable.player_battery_01);
                } else if (battery >= 20 && battery < 40) {
                    ivBattery.setImageResource(R.drawable.player_battery_02);
                } else if (battery >= 40 && battery < 65) {
                    ivBattery.setImageResource(R.drawable.player_battery_03);
                } else if (battery >= 65 && battery < 90) {
                    ivBattery.setImageResource(R.drawable.player_battery_04);
                } else if (battery >= 90 && battery <= 100) {
                    ivBattery.setImageResource(R.drawable.player_battery_05);
                } else {
                    ivBattery.setVisibility(View.GONE);
                }
            }
        }
    }

    private NetChangeReceiver netChangeReceiver;
    private OnNetChangeListener onNetChangeListener;

    private void registerNetReceiver() {
        if (netChangeReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            netChangeReceiver = new NetChangeReceiver();
            context.registerReceiver(netChangeReceiver, filter);
        }
    }

    private void unregisterNetReceiver() {
        if (netChangeReceiver != null) {
            context.unregisterReceiver(netChangeReceiver);
        }
    }

    public class NetChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onNetChangeListener == null || !isNeedNetChangeListen) {
                return;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) { //WiFi网络
                    onNetChangeListener.onWifi();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {   //3g网络
                    onNetChangeListener.onMobile();
                } else {    //其他

                }
            } else {
                onNetChangeListener.onNoAvailable();
            }
        }
    }

    /**
     * 设置横屏
     */
    public void setLandscape() {
        isFullscreen = true;
        //设置横屏
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (controlLL.getVisibility() == View.VISIBLE) {
            topMenu.setVisibility(View.VISIBLE);
        }
        initLock();
    }

    /**
     * 设置竖屏
     */
    public void setProtrait() {
        isFullscreen = false;
        //设置横屏
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        topMenu.setVisibility(View.GONE);
        unLockScreen();
        initLock();
    }

    /**
     * 系统设置改变触发,如屏幕方向改变
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int screenWidth = PlayerUtil.getScreenWidth(activity);
        int screenHeight = PlayerUtil.getScreenHeight(activity);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //计算视频的大小16：9
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * 9 / 16;

            setX(mediaPlayerX);
            setY(mediaPlayerY);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;

            setX(0);
            setY(0);
        }
        setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivPlay) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.video_play);
                } else {
                    mediaPlayer.start();
                    ivPlay.setImageResource(R.drawable.video_pause);
                }
            }
        } else if (i == R.id.ivAdjust) {
            if (isFullscreen) {
                setProtrait();
            } else {
                setLandscape();
            }
        } else if (i == R.id.player_iv_lock) {
            if (isFullscreen) {
                if (isLockScreen) {
                    unLockScreen();
                    initBottomMenuState();
                } else {
                    lockScreen();
                    destroyControllerTask(true);
                }
            }
        } else if (i == R.id.player_ll_error || i == R.id.player_ll_net || i == R.id.player_iv_play_center) {
            //播放
            playVideo(videoPath, videoTitle, 0);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //避免拖到最后
            int maxCanSeekTo = seekBar.getMax() - 5 * 1000;
            if (seekBar.getProgress() < maxCanSeekTo) {
                mediaPlayer.seekTo(seekBar.getProgress());
            } else {
                mediaPlayer.seekTo(maxCanSeekTo);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(holder); // 添加到容器中
        //播放完成的监听
        mediaPlayer.setOnCompletionListener(this);
        // 异步准备的一个监听函数，准备好了就调用里面的方法
        mediaPlayer.setOnPreparedListener(this);
        //播放错误的监听
        mediaPlayer.setOnErrorListener(this);
        //缓冲
        mediaPlayer.setOnBufferingUpdateListener(this);
        //第一次初始化需不需要主动播放
        if (autoPlay) {
            //判断当前有没有网络（播放的是网络视频）
            if (!PlayerUtil.isNetworkConnected(context) && videoPath.startsWith("http")) {
                showNoNetView();
            } else {
                //手机网络给提醒
                if (PlayerUtil.isMobileConnected(context)) {
                    Toast.makeText(context, "请注意,当前网络状态切换为3G/4G网络", Toast.LENGTH_SHORT).show();
                }
                //添加播放路径
                try {
                    mediaPlayer.setDataSource(videoPath);
                    // 准备开始,异步准备，自动在子线程中
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        autoPlay = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //保存播放位置
        if (mediaPlayer != null) {
            video_position = mediaPlayer.getCurrentPosition();
        }
        destroyControllerTask(true);
        pauseVideo();
    }

    /**
     * 手势相关
     */
    private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_LIGHT = 2f;// 协调亮度滑动时的步长，避免每次滑动都改变，导致改变过快
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHTNESS = 3;

    private void initGesture() {
        gesture_volume_layout = (RelativeLayout) findViewById(R.id.gesture_voice_layout);
        geture_tv_volume_percentage = (TextView) findViewById(R.id.gesture_voice_percentage);
        gesture_iv_player_volume = (ImageView) findViewById(R.id.gesture_player_voice);

        gesture_progress_layout = (RelativeLayout) findViewById(R.id.gesture_progress_layout);
        geture_tv_progress_time = (TextView) findViewById(R.id.gesture_progress_percentage);
        gesture_iv_progress = (ImageView) findViewById(R.id.gesture_player_progress);

        //亮度的布局
        gesture_light_layout = (RelativeLayout) findViewById(R.id.gesture_light_layout);
        geture_tv_light_percentage = (TextView) findViewById(R.id.gesture_light_percentage);

        gesture_volume_layout.setVisibility(View.GONE);
        gesture_progress_layout.setVisibility(View.GONE);
        gesture_light_layout.setVisibility(View.GONE);

        gestureDetector = new GestureDetector(getContext(), this);
        setLongClickable(true);
        gestureDetector.setIsLongpressEnabled(true);//是否允许长点击，长按默认允许
        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 刚刚手指接触到触摸屏的那一刹那，就是触的那一下
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //手指按在触摸屏上，它的时间范围在按下起效，在长按之前。
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //手指离开触摸屏的那一刹那。
        if (!isPrepare || isLockScreen) {
            return false;
        }
        initBottomMenuState();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //手指在触摸屏上滑动。
        if (!isPrepare || isLockScreen) {
            return false;
        }

        int FLAG = 0;

        // 横向的距离变化大则调整进度，纵向的变化大则调整音量或亮度
        if (Math.abs(distanceX) >= Math.abs(distanceY)) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                FLAG = GESTURE_MODIFY_PROGRESS;
            }
        } else {
            int intX = (int) e1.getX();
            int screenWidth = PlayerUtil.getScreenWidth((Activity) context);
            if (intX > screenWidth / 2) {
                FLAG = GESTURE_MODIFY_VOLUME;
            } else {
                //左边是亮度
                FLAG = GESTURE_MODIFY_BRIGHTNESS;
            }
        }

        if (GESTURE_FLAG != 0 && GESTURE_FLAG != FLAG) {
            return false;
        }

        GESTURE_FLAG = FLAG;

        if (FLAG == GESTURE_MODIFY_PROGRESS) {
            //表示是横向滑动,可以添加快进
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.VISIBLE);
            try {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                        if (distanceX >= dip2px(context, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                            gesture_iv_progress.setImageResource(R.drawable.player_backward);
                            if (mediaPlayer.getCurrentPosition() > 3 * 1000) {// 避免为负
                                int cpos = mediaPlayer.getCurrentPosition();
                                mediaPlayer.seekTo(cpos - 3000);
                                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            } else {
                                //什么都不做
                                mediaPlayer.seekTo(3000);
                            }
                        } else if (distanceX <= -dip2px(context, STEP_PROGRESS)) {// 快进
                            gesture_iv_progress.setImageResource(R.drawable.player_forward);
                            if (mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration() - 5 * 1000) {// 避免超过总时长
                                int cpos = mediaPlayer.getCurrentPosition();
                                mediaPlayer.seekTo(cpos + 3000);
                                // 把当前位置赋值给进度条
                                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        }
                    }
                    String timeStr = PlayerUtil.long2Str(mediaPlayer.getCurrentPosition()) + " / "
                            + PlayerUtil.long2Str(mediaPlayer.getDuration());
                    geture_tv_progress_time.setText(timeStr);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (FLAG == GESTURE_MODIFY_VOLUME) {
            //右边是音量
            gesture_volume_layout.setVisibility(View.VISIBLE);
            gesture_light_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.GONE);
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                if (currentVolume == 0) {// 静音，设定静音独有的图片
                    gesture_iv_player_volume.setImageResource(R.drawable.player_volume_close);
                }
                if (distanceY >= dip2px(context, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                    gesture_iv_player_volume.setImageResource(R.drawable.player_volume_open);
                } else if (distanceY <= -dip2px(context, STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                        if (currentVolume == 0) {// 静音，设定静音独有的图片
                            gesture_iv_player_volume.setImageResource(R.drawable.player_volume_close);
                        }
                    }
                }
                int percentage = (currentVolume * 100) / maxVolume;
                geture_tv_volume_percentage.setText(String.valueOf(percentage + "%"));
                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            }
        }
        //调节亮度
        else if (FLAG == GESTURE_MODIFY_BRIGHTNESS) {
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.VISIBLE);
            gesture_progress_layout.setVisibility(View.GONE);
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                // 亮度调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                int mLight = LightUtil.GetLightness((Activity) context);
                if (mLight >= 0 && mLight <= 255) {
                    if (distanceY >= dip2px(context, STEP_LIGHT)) {
                        if (mLight > 245) {
                            LightUtil.SetLightness((Activity) context, 255);
                        } else {
                            LightUtil.SetLightness((Activity) context, mLight + 10);
                        }
                    } else if (distanceY <= -PlayerUtil.dip2px(context, STEP_LIGHT)) {// 亮度调小
                        if (mLight < 10) {
                            LightUtil.SetLightness((Activity) context, 0);
                        } else {
                            LightUtil.SetLightness((Activity) context, mLight - 10);
                        }
                    }
                } else if (mLight < 0) {
                    LightUtil.SetLightness((Activity) context, 0);
                } else {
                    LightUtil.SetLightness((Activity) context, 255);
                }
                //获取当前亮度
                int currentLight = LightUtil.GetLightness((Activity) context);
                int percentage = (currentLight * 100) / 255;
                geture_tv_light_percentage.setText(String.valueOf(percentage + "%"));
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //手指按在持续一段时间，并且没有松开。
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //手指在触摸屏上迅速移动，并松开的动作。
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.GONE);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if(mediaPlayer==null||!mediaPlayer.isPlaying()){
            //这里一定要判断，此方法有可能会在mediaPlayer prepare之前执行
            //会报Attempt to call getDuration in wrong state: mPlayer=0x73dca8a9c0, mCurrentState=4这个错误
            return;
        }
        if (percent >= 0 && percent <= 100) {
            int secondProgress = mp.getDuration() * percent / 100;
            mSeekBar.setSecondaryProgress(secondProgress);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ivPlay.setImageResource(R.drawable.video_play);
        destroyControllerTask(true);
        video_position = 0;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what != -38) {  //这个错误不管
            showErrorView();
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start(); // 开始播放
        isPrepare = true;
        if (video_position > 0) {
            mediaPlayer.seekTo(video_position);
            video_position = 0;
        }
        // 把得到的总长度和进度条的匹配
        mSeekBar.setMax(mediaPlayer.getDuration());
        ivPlay.setImageResource(R.drawable.video_pause);
        tvPlayTime.setText(PlayerUtil.long2Str(mediaPlayer.getCurrentPosition()));
        tvAllTime.setText(PlayerUtil.long2Str(mediaPlayer.getDuration()));
        //延时：避免出现上一个视频的画面闪屏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initBottomMenuState();
                playerScreen.setVisibility(View.GONE);
            }
        }, 500);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++
    // ######## 对外提供的方法 ########
    //+++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 设置视频信息
     *
     * @param url   视频地址
     * @param title 视频标题
     */
    public void setDataSource(String url, String title) {
        //赋值
        videoPath = url;
        videoTitle = title;
    }
    /**
     * 视频播放相关
     */
    /**
     * 播放视频
     *
     * @param url   视频地址
     * @param title 视频标题
     */
    public void playVideo(String url, String title) {
        playVideo(url, title, video_position);
    }

    /**
     * 播放视频（支持上次播放位置）
     * 自己记录上一次播放的位置，然后传递position进来就可以了
     *
     * @param url      视频地址
     * @param title    视频标题
     * @param position 视频跳转的位置
     */
    public void playVideo(String url, String title, int position) {
        //地址判空处理
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "播放地址为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //销毁ControllerView
        destroyControllerTask(true);

        //赋值
        videoPath = url;
        videoTitle = title;
        tvTitle.setText(videoTitle);
        video_position = position;
        isPrepare = false;

        //判断当前有没有网络（播放的是网络视频）
        if (!PlayerUtil.isNetworkConnected(context) && url.startsWith("http")) {
            showNoNetView();
            return;
        }

        //重置MediaPlayer
        resetMediaPlayer();

        //判断广播相关
        if (isNeedBatteryListen) {
            registerBatteryReceiver();
        } else {
            unRegisterBatteryReceiver();
            ivBattery.setVisibility(View.GONE);
        }
        //网络监听的广播
        if (isNeedNetChangeListen) {
            registerNetReceiver();
        } else {
            unregisterNetReceiver();
        }
    }

    /**
     * 重置MediaPlayer
     */
    private void resetMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                //重置mediaPlayer
                mediaPlayer.reset();
                //添加播放路径
                mediaPlayer.setDataSource(videoPath);
                // 准备开始,异步准备，自动在子线程中
                mediaPlayer.prepareAsync();
            }
        } catch (Exception e) {
            Toast.makeText(context, "播放器初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放视频
     */
    public void startVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            ivPlay.setImageResource(R.drawable.video_pause);
        }
    }

    /**
     * 暂停视频
     */
    public void pauseVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            ivPlay.setImageResource(R.drawable.video_play);
            video_position = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 判断是不是全屏状态
     *
     * @return
     */
    public boolean isFullScreen() {
        return isFullscreen;
    }

    /**
     * 获取当前播放的位置
     */
    public int getVideoCurrentPosition() {
        int position = 0;
        if (mediaPlayer != null) {
            position = mediaPlayer.getCurrentPosition();
        }
        return position;
    }

    /**
     * 获取视频总长度
     */
    public int getVideoTotalDuration() {
        int position = 0;
        if (mediaPlayer != null) {
            position = mediaPlayer.getDuration();
        }
        return position;
    }

    /**
     * 销毁资源
     */
    public void destroyVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();// 释放资源
            mediaPlayer = null;
        }
        surfaceHolder = null;
        mSurfaceView = null;
        video_position = 0;
        unRegisterBatteryReceiver();
        unregisterNetReceiver();
        if (onNetChangeListener != null) {
            onNetChangeListener = null;
        }
        destroyTimeTask();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置是否需要电量监听
     */
    public void setIsNeedBatteryListen(boolean isNeedBatteryListen) {
        this.isNeedBatteryListen = isNeedBatteryListen;
    }

    public void setOnNetChangeListener(OnNetChangeListener onNetChangeListener) {
        this.onNetChangeListener = onNetChangeListener;
    }

    /**
     * 设置是否需要网络变化监听
     */
    public void setIsNeedNetChangeListen(boolean isNeedNetChangeListen) {
        this.isNeedNetChangeListen = isNeedNetChangeListen;
    }

    public interface OnNetChangeListener {
        //wifi
        void onWifi();

        //手机
        void onMobile();

        //不可用
        void onNoAvailable();
    }
}
