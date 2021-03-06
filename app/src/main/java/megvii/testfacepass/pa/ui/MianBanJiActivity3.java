package megvii.testfacepass.pa.ui;



import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.SerialPort;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.common.pos.api.util.TPS980PosUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hwit.HwitManager;
import com.pingan.ai.access.common.PaAccessControlMessage;
import com.pingan.ai.access.common.PaAccessDetectConfig;
import com.pingan.ai.access.common.PaAccessNativeConfig;
import com.pingan.ai.access.entiry.PaAccessFaceInfo;
import com.pingan.ai.access.entiry.YuvInfo;
import com.pingan.ai.access.impl.OnPaAccessDetectListener;
import com.pingan.ai.access.manager.PaAccessControl;
import com.pingan.ai.access.result.PaAccessCompareFacesResult;
import com.pingan.ai.access.result.PaAccessDetectFaceResult;
import com.pingan.ai.access.result.PaAccessMultiFaceBaseInfo;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import java.util.concurrent.ExecutionException;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import megvii.testfacepass.pa.MyApplication;
import megvii.testfacepass.pa.R;
import megvii.testfacepass.pa.beans.AddFacesBean;
import megvii.testfacepass.pa.beans.BaoCunBean;
import megvii.testfacepass.pa.beans.DaKaBean;
import megvii.testfacepass.pa.beans.FailedPersonBean;
import megvii.testfacepass.pa.beans.HuiFuBean;
import megvii.testfacepass.pa.beans.IDCardBean;
import megvii.testfacepass.pa.beans.IDCardBean_;
import megvii.testfacepass.pa.beans.IDCardTakeBean;
import megvii.testfacepass.pa.beans.LogingBean;
import megvii.testfacepass.pa.beans.OpenDoorBean;
import megvii.testfacepass.pa.beans.Subject;
import megvii.testfacepass.pa.beans.Subject_;
import megvii.testfacepass.pa.beans.TimeStateBean;
import megvii.testfacepass.pa.beans.ZhiLingBean;
import megvii.testfacepass.pa.camera.CameraManager;
import megvii.testfacepass.pa.camera.CameraManager2;
import megvii.testfacepass.pa.camera.CameraPreview;
import megvii.testfacepass.pa.camera.CameraPreview2;
import megvii.testfacepass.pa.camera.CameraPreviewData;
import megvii.testfacepass.pa.camera.CameraPreviewData2;
import megvii.testfacepass.pa.dialog.MiMaDialog3;
import megvii.testfacepass.pa.dialog.MiMaDialog4;
import megvii.testfacepass.pa.utils.BitmapUtil;
import megvii.testfacepass.pa.utils.DateUtils;
import megvii.testfacepass.pa.utils.DengUT;
import megvii.testfacepass.pa.utils.FileUtil;
import megvii.testfacepass.pa.utils.GsonUtil;
import megvii.testfacepass.pa.utils.NV21ToBitmap;
import megvii.testfacepass.pa.utils.SettingVar;
import megvii.testfacepass.pa.utils.GetCpuState;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;



public class MianBanJiActivity3 extends Activity implements CameraManager.CameraListener,
        CameraManager2.CameraListener2, SensorEventListener {


    @BindView(R.id.xiping)
    ImageView xiping;
    @BindView(R.id.tishiyu)
    TextView tishiyu;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    private int timeUpdataState=1;
    private NetWorkStateReceiver netWorkStateReceiver = null;
    private SensorManager sm;
    private Box<Subject> subjectBox = null;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Box<IDCardBean> idCardBeanBox = MyApplication.myApplication.getIdCardBeanBox();
    private Bitmap msrBitmap = null;
    //    private RequestOptions myOptions = new RequestOptions()
//            .fitCenter()
//            .error(R.drawable.erroy_bg)
//            .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
//    // .transform(new GlideRoundTransform(MainActivity.this,10));
//
//    private RequestOptions myOptions2 = new RequestOptions()
//            .fitCenter()
//            .error(R.drawable.erroy_bg)
//            //   .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
//            .transform(new GlideCircleTransform270(MyApplication.myApplication, 2, Color.parseColor("#ffffffff"), 270));
    private  Connection conn=null;
    private  Channel channel=null;
    private ConnectionFactory factory=null;

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            .connectTimeout(20000, TimeUnit.MILLISECONDS)
            .readTimeout(20000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
            //        .retryOnConnectionFailure(true)
            .build();
    private final Timer timer = new Timer();
    private TimerTask task;
    private final Timer timer2 = new Timer();
    private TimerTask task2;
    private LinkedBlockingQueue<ZhiLingBean> linkedBlockingQueue;
    /* 相机实例 */
    private CameraManager manager;
    private CameraManager2 manager2;
    /* 显示人脸位置角度信息 */
    // private XiuGaiGaoKuanDialog dialog = null;
    /* 相机预览界面 */
    private CameraPreview cameraView;
    private CameraPreview2 cameraView2;
    private static final int cameraWidth = 480;
    private static final int cameraHeight = 640;
    private boolean isOP = true;
    private int heightPixels;
    private int widthPixels;
    int screenState = 0;// 0 横 1 竖
    TanChuangThread tanChuangThread;
    // private ConcurrentHashMap<Long, Integer> concurrentHashMap = new ConcurrentHashMap<Long, Integer>();
    private int dw, dh;
    private Box<BaoCunBean> baoCunBeanDao = null;
   // private Box<HuiFuBean> huiFuBeanBox = null;
    private Box<DaKaBean> daKaBeanBox = null;
    private BaoCunBean baoCunBean = null;
    private TimeChangeReceiver timeChangeReceiver;
    private WeakHandler mHandler;
  //  private static boolean isLink = true;
    private PaAccessControl paAccessControl;
    private Float mCompareThres;
    private static String faceId = "";
    private long feature2 = -1;
    private NV21ToBitmap nv21ToBitmap;
    private SoundPool soundPool;
    //定义一个HashMap用于存放音频流的ID
    private HashMap<Integer, Integer> musicId = new HashMap<>();
    private int pp = 0;
    // private Subject subjectOnly;
    private ReadThread mReadThread;
    private InputStream mInputStream;
    //private OutputStream mOutputStream;
    private int w, h, cameraH, cameraW;
    private float s1 = 0, s2 = 0;
    private Timer mTimer;//距离感应
    private TimerTask mTimerTask;//距离感应
    private int pm = 0;
    private boolean onP1 = true, onP2 = true;
    private boolean isPM = true;
    private boolean isPM2 = true;
    private float juli = 0;
   // private ValueAnimator anim;
    private String JHM = null;
   // private boolean isERM = true;
    TextView tvTitle_Ir;
    TextView tvName_Ir;//识别结果弹出信息的名字
    TextView tvTime_Ir;//识别结果弹出信息的时间
    TextView tvFaceTips_Ir;//识别信息提示
    LinearLayout layout_loadbg_Ir;//识别提示大框

    RelativeLayout layout_true_gif_Ir, layout_error_gif_Ir;//蓝色图片动画 红色图片动画
    ImageView iv_true_gif_in_Ir, iv_true_gif_out_Ir, iv_error_gif_in_Ir, iv_error_gif_out_Ir;//定义旋转的动画
    Animation gifClockwise, gifAntiClockwise;
    LinearInterpolator lir_gif;
 //   private Box<IDCardTakeBean> idCardTakeBeanBox=MyApplication.myApplication.getIdCardTakeBeanBox();
    private int jiqiType=2;
    private String getPerosnPath=null;
    private String perosnNotifyPath=null;
    private PaAccessDetectConfig paAccessDetectConfig;
    private LogingBean logingBean=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     //   huiFuBeanBox = MyApplication.myApplication.getHuiFuBeanBox();
        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
        daKaBeanBox=MyApplication.myApplication.getDaKaBeanBox();
        baoCunBean = baoCunBeanDao.get(123456L);
        JHM = baoCunBean.getJihuoma();
        if (JHM == null)
            JHM = "";
        subjectBox = MyApplication.myApplication.getSubjectBox();
        mCompareThres = baoCunBean.getShibieFaZhi();
        if (baoCunBean.getDangqianChengShi2()!=null){
            switch (baoCunBean.getDangqianChengShi2()){
                case "天波":
                    jiqiType=0;
                    break;
                case "涂鸦":
                    jiqiType=1;
                    break;
                case "户外防水8寸屏":
                    jiqiType=2;
                    break;
            }
        }

        baoCunBeanDao.put(baoCunBean);

        //每分钟的广播
        // private TodayBean todayBean = null;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);
        linkedBlockingQueue = new LinkedBlockingQueue<>();
        EventBus.getDefault().register(this);//订阅
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;
        nv21ToBitmap = new NV21ToBitmap(MianBanJiActivity3.this);
        /* 初始化界面 */
        //  Log.d("MianBanJiActivity3", "jh:" + baoCunBean);
        //初始化soundPool,设置可容纳12个音频流，音频流的质量为5，
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)   //设置允许同时播放的流的最大值
                .setAudioAttributes(abs)   //完全可以设置为null
                .build();
        //通过load方法加载指定音频流，并将返回的音频ID放入musicId中

        musicId.put(1, soundPool.load(this, R.raw.tongguo, 1));
        musicId.put(2, soundPool.load(this, R.raw.wuquanxian, 1));
        musicId.put(3, soundPool.load(this, R.raw.xinxibupipei, 1));
        musicId.put(4, soundPool.load(this, R.raw.xianshibie, 1));
        musicId.put(5, soundPool.load(this, R.raw.shuaka, 1));

        initView();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (baoCunBean != null) {
            try {
                //PaAccessControl.getInstance().getPaAccessDetectConfig();
                paAccessControl = PaAccessControl.getInstance();
                paAccessControl.setOnPaAccessDetectListener(onDetectListener);
                paAccessControl.setLogEnable(false);
                initFaceConfig();
               paAccessDetectConfig=paAccessControl.getPaAccessDetectConfig();

            } catch (Exception e) {
                Log.d("MianBanJiActivity3", e.getMessage() + "初始化失败");
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(4000);
                w = cameraView.getMeasuredWidth();
                h = cameraView.getMeasuredHeight();
                cameraH = manager.getCameraheight();
                cameraW = manager.getCameraWidth();
                s1 = (float) w / (float) cameraH;
                s2 = (float) h / (float) cameraW;
                if (paAccessControl != null)
                    paAccessControl.startFrameDetect();
            }
        }).start();

        try {
            SerialPort mSerialPort = MyApplication.myApplication.getSerialPort();
            //mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

        } catch (Exception e) {
            Log.d("MianBanJiActivity", e.getMessage() + "dddddddd");
        }

        mReadThread = new ReadThread();
        mReadThread.start();

        tanChuangThread = new TanChuangThread();
        tanChuangThread.start();


        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NotNull Message msg) {
                switch (msg.what) {
                    case 111: {
                        Subject subject = (Subject) msg.obj;
                        //Log.d("MianBanJiActivity3", "subject:" + subject);
                        if (subject.getTeZhengMa() != null) {
                            //  Log.d("MianBanJiActivity3", "ddd3");
                         //   faceView.setTC(BitmapFactory.decodeFile(MyApplication.SDPATH3 + File.separator + subject.getTeZhengMa() + ".png")
                            //        , subject.getName(), subject.getDepartmentName());

                           // DengUT.openDool();
                            DaKaBean daKaBean=new DaKaBean();
                            daKaBean.setId2(subject.getTeZhengMa());
                            daKaBean.setName(subject.getPerson_name());
                            daKaBean.setTime2(System.currentTimeMillis());
                            daKaBeanBox.put(daKaBean);

                            //启动定时器或重置定时器
                            if (task != null) {
                                task.cancel();
                                //timer.cancel();
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = 222;
                                        mHandler.sendMessage(message);
                                    }
                                };
                                timer.schedule(task, 6000);
                            } else {
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = 222;
                                        mHandler.sendMessage(message);
                                    }
                                };
                                timer.schedule(task, 6000);
                            }
                        } else {
                            //  Log.d("MianBanJiActivity3", "ddd4");

                            //faceView.setTC(BitmapUtil.rotateBitmap(msrBitmap, SettingVar.msrBitmapRotation), subject.getName(), subject.getDepartmentName());
                        }

                        break;
                    }
                    case 222: {//关门
                       // DengUT.closeDool();
                    //    if (jiqiType==2){
                     //       DengUT.closeDool8cun();
                    //    }
                        break;
                    }
                    case 333:
                        onP1 = true;
                        onP2 = true;
                        if (paAccessControl != null)
                            paAccessControl.startFrameDetect();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                xiping.setVisibility(View.GONE);
                                tishiyu.setVisibility(View.GONE);
                            }
                        });
                       // if (anim != null)
                         //   anim.cancel();
                        DengUT.openLOED();
                        break;
                    case 444:
                        onP1 = false;
                        onP2 = false;
                        if (paAccessControl != null)
                            paAccessControl.stopFrameDetect();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                xiping.setVisibility(View.VISIBLE);
                                tishiyu.setVisibility(View.VISIBLE);
                            }
                        });
//                        anim = ValueAnimator.ofFloat(0, 1.0f);
//                        anim.setDuration(4000);
//                        anim.setRepeatMode(ValueAnimator.REVERSE);
//                        anim.setRepeatCount(-1);
//                        Interpolator interpolator = new LinearInterpolator();
//                        anim.setInterpolator(interpolator);
//                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                float currentValue = (Float) animation.getAnimatedValue();
//                                tishiyu.setAlpha(currentValue);
//                                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
//                                tishiyu.requestLayout();
//                            }
//                        });
//                        anim.start();
                        DengUT.closeLOED();
                        break;
                    case 555:{//8寸面板机，5秒没人关屏
                        Log.d("MianBanJiActivity3", "收到");
                        if (baoCunBean.getDangqianChengShi2() != null && baoCunBean.getDangqianChengShi2().equals("户外防水8寸屏")) {
                            DengUT.closeLOED8cun();
                        }
                        break;
                    }
                }
                return false;
            }
        });
        NfcManager mNfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        mNfcAdapter = mNfcManager.getDefaultAdapter();
        if (mNfcAdapter == null) {
            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "设备不支持NFC", TastyToast.LENGTH_LONG, TastyToast.INFO);
            tastyToast.setGravity(Gravity.CENTER, 0, 0);
            tastyToast.show();
        } else if (!mNfcAdapter.isEnabled()) {
            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "请先去设置里面打开NFC开关", TastyToast.LENGTH_LONG, TastyToast.INFO);
            tastyToast.setGravity(Gravity.CENTER, 0, 0);
            tastyToast.show();
        } else if ((mNfcAdapter != null) && (mNfcAdapter.isEnabled())) {
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        init_NFC();
        guanPing();//关屏
        baoCunBean.setDangqianChengShi2("户外防水8寸屏");
        link_loging(baoCunBean.getTuisongDiZhi());

    }

    @OnClick(R.id.root_layout)
    public void onViewClicked() {
        if (baoCunBean.isShowShiPingLiu()){
            MiMaDialog3 miMaDialog=new MiMaDialog3(MianBanJiActivity3.this,baoCunBean.getMima2());
            WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
            params.width=dw;
            params.height=dh+60;
            miMaDialog.getWindow().setGravity(Gravity.CENTER);
            miMaDialog.getWindow().setAttributes(params);
            miMaDialog.show();
        }
    }


    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    final byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        // Log.d("ReadThread", "buffer.length:" + byteToString(buffer));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                readdd(buffer);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    private void readdd(byte[] idid) {
        String sdfds = byteToString(idid);
        if (sdfds != null) {
            sdfds = sdfds.substring(6, 14);
        } else {
            return;
        }
        sdfds = sdfds.toUpperCase();
        List<IDCardBean> idCardBeanList = idCardBeanBox.query().equal(IDCardBean_.idCard, sdfds).build().find();
        if (idCardBeanList.size() > 0) {
//            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "验证成功!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//            tastyToast.setGravity(Gravity.CENTER, 0, 0);
//            tastyToast.show();
//            soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
           // DengUT.openDool();
            IDCardBean cardBean=idCardBeanList.get(0);

         //   link_shuaka(sdfds,cardBean.getName());
            //启动定时器或重置定时器
//            if (task != null) {
//                task.cancel();
//                //timer.cancel();
//                task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        Message message = new Message();
//                        message.what = 222;
//                        mHandler.sendMessage(message);
//                    }
//                };
//                timer.schedule(task, 6000);
//            } else {
//                task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        Message message = new Message();
//                        message.what = 222;
//                        mHandler.sendMessage(message);
//                    }
//                };
//                timer.schedule(task, 6000);
//            }
//
//            IDCardTakeBean takeBean=new IDCardTakeBean();
//            takeBean.setIdCard(sdfds);
//            takeBean.setName(cardBean.getName());
//            takeBean.setTime(System.currentTimeMillis());
//            idCardTakeBeanBox.put(takeBean);

        } else {
//            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "验证失败!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//            tastyToast.setGravity(Gravity.CENTER, 0, 0);
//            tastyToast.show();
//            soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
        }
    }


    @Override
    protected void onResume() {
        Log.d("MianBanJiActivity3", "重新开始");
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(this.getIntent().getAction())) {
                processIntent(this.getIntent());
            }
        }
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netWorkStateReceiver, filter);
        }

        if (baoCunBean.getDangqianChengShi2() != null && baoCunBean.getDangqianChengShi2().equals("天波")) {
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        int ret = TPS980PosUtil.getPriximitySensorStatus();
                        if (ret == 1) {
                            isPM2 = true;
                            //有人
                            if (isPM) {
                                isPM = false;
                                onP1 = true;
                                onP2 = true;
                                pm = 0;
                                if (paAccessControl != null)
                                    paAccessControl.startFrameDetect();
                                Message message = new Message();
                                message.what = 333;
                                mHandler.sendMessage(message);
                            }
                        } else {
                            isPM = true;
                            if (isPM2) {
                                pm++;
                                if (pm == 8) {
                                    if (paAccessControl != null)
                                        paAccessControl.stopFrameDetect();
                                    Message message = new Message();
                                    message.what = 444;
                                    mHandler.sendMessage(message);
                                    isPM2 = false;
                                    onP1 = false;
                                    onP2 = false;
                                    pm = 0;
                                }
                            }
                        }
                    }
                };
            }
            if (mTimer == null) {
                mTimer = new Timer();
            }
            mTimer.schedule(mTimerTask, 0, 1000);
        }
        if (baoCunBean.getDangqianChengShi2() != null && baoCunBean.getDangqianChengShi2().equals("涂鸦") && paAccessControl != null) {
            Sensor defaultSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sm.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (juli > 0) {
                            isPM2 = true;
                            //有人
                            if (isPM) {
                                isPM = false;
                                onP1 = true;
                                onP2 = true;
                                pm = 0;
                                if (paAccessControl != null)
                                    paAccessControl.startFrameDetect();
                                Message message = new Message();
                                message.what = 333;
                                mHandler.sendMessage(message);
                            }
                        } else {
                            isPM = true;
                            if (isPM2) {
                                pm++;
                                if (pm == 8) {
                                    if (paAccessControl != null)
                                        paAccessControl.stopFrameDetect();
                                    Message message = new Message();
                                    message.what = 444;
                                    mHandler.sendMessage(message);
                                    isPM2 = false;
                                    onP1 = false;
                                    onP2 = false;
                                    pm = 0;

                                    if (DengUT.isOPENRed) {
                                        DengUT.isOPENRed = false;
                                        DengUT.closeRed();
                                    }
                                    if (DengUT.isOPENGreen) {
                                        DengUT.isOPENGreen = false;
                                        DengUT.closeGreen();
                                    }
                                    if (DengUT.isOPEN) {
                                        DengUT.isOPEN = false;
                                        DengUT.closeWrite();
                                    }
                                }
                            }
                        }
                    }
                };
            }
            if (mTimer == null) {
                mTimer = new Timer();
            }
            mTimer.schedule(mTimerTask, 0, 1000);
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("距离", "" + event.values[0]);
        juli = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onNewIntent(Intent intent) {
        //  super.onNewIntent(intent);
        // Log.d("SheZhiActivity2", "intent:" + intent);
        processIntent(intent);
    }


    private YuvInfo rgb, ir;

    /* 相机回调函数 */
    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {
        /* 如果SDK实例还未创建，则跳过 */
        if (paAccessControl == null && onP1) {
            return;
        }
        rgb = new YuvInfo(cameraPreviewData.nv21Data, SettingVar.cameraId, SettingVar.faceRotation, cameraPreviewData.width, cameraPreviewData.height);
        if (!baoCunBean.isHuoTi()) {
            paAccessControl.offerFrameBuffer(rgb);
        }
    }


    /* 相机回调函数 */
    @Override
    public void onPictureTaken2(CameraPreviewData2 cameraPreviewData) {
        /* 如果SDK实例还未创建，则跳过 */
        // Log.d("MianBanJiActivity3", "cameraPreviewData2.rotation:" + cameraPreviewData.front);
        if (paAccessControl == null && onP2) {
            return;
        }
        //  paAccessControl.offerFrameBuffer(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height,SettingVar.faceRotation, SettingVar.getCaneraID());
        try {
            ir = new YuvInfo(cameraPreviewData.nv21Data, cameraPreviewData.front, SettingVar.faceRotation2, cameraPreviewData.width, cameraPreviewData.height);
            if (rgb == null || !baoCunBean.isHuoTi())
                return;
            int result = paAccessControl.offerIrFrameBuffer(rgb, ir);//提供数据到队列
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d("MianBanJiActivity3", "cameraPreviewData.result:" + result);
        /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
    }



    private class TanChuangThread extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {
                    ZhiLingBean commandsBean = linkedBlockingQueue.take();
                    paAccessControl.stopFrameDetect();
                    List<String> successfulList=new ArrayList<>();
                    List<FailedPersonBean> failedList=new ArrayList<>();
                    if (commandsBean.getCode()==-99999){//删除
                      JSONArray array= commandsBean.getJsonObject().getJSONObject("params").getJSONArray("Ids");//拿到id列表
                        int size=array.length();
                        for (int i=0;i<size;i++){
                            String idid=array.get(i).toString();
                            try {
                                Log.d("TanChuangThread",idid);
                                PaAccessFaceInfo face = paAccessControl.queryFaceById(idid);
                                if (face != null) {
                                    paAccessControl.deleteFaceById(face.faceId);
                                }
                                List<Subject> subjects=subjectBox.query().equal(Subject_.teZhengMa,idid).build().find();
                                for (Subject subject:subjects){
                                    subjectBox.remove(subject);
                                }
                                File file=new File(MyApplication.SDPATH3+File.separator+idid+".jpg");
                                Log.d("TanChuangThread", "删除人脸图片文件:" + file.delete());
                                successfulList.add(idid);
                            }catch (Exception e){
                                failedList.add(new FailedPersonBean(idid,e.getMessage()+""));
                            }
                        }
                        //提交记录
                        link_sync_person(successfulList,failedList,commandsBean.getJsonObject());
                        paAccessControl.startFrameDetect();
                    }else {//新增修改
                        for (ZhiLingBean.PersonListBean personListBean : commandsBean.getPerson_list()) {
                            PaAccessFaceInfo face = paAccessControl.queryFaceById(personListBean.getPerson_id());
                            PaAccessDetectFaceResult detectResult = null;
                            Bitmap bitmap = null;
                            try {
                                if (personListBean.getFace_list()!=null && personListBean.getFace_list().get(0) != null)
                                    bitmap = Glide.with(MianBanJiActivity3.this).asBitmap()
                                            .load(personListBean.getFace_list().get(0).getImg_url())
                                            // .sizeMultiplier(0.5f)
                                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                            .get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {//有图片
                                detectResult = paAccessControl.detectFaceByBitmap(bitmap, paAccessDetectConfig);
                                if (detectResult != null && detectResult.message == PaAccessControlMessage.RESULT_OK) {
                                    if (face != null) {
                                        paAccessControl.deleteFaceById(face.faceId);
                                        List<Subject> subjects=subjectBox.query().equal(Subject_.teZhengMa,personListBean.getPerson_id()).build().find();
                                        for (Subject subject:subjects){
                                            subjectBox.remove(subject);
                                        }
                                    }
                                    BitmapUtil.saveBitmapToSD(bitmap, MyApplication.SDPATH3, personListBean.getPerson_id() + ".jpg");
                                    try {
                                        paAccessControl.addFace(personListBean.getPerson_id(), detectResult.feature, MyApplication.GROUP_IMAGE);
                                        Subject subject = new Subject();
                                        subject.setBirthday(personListBean.getBirthday());
                                        subject.setPerson_name(personListBean.getPerson_name());
                                        subject.setCount(personListBean.getValid_time().getCount());
                                        subject.setEnd_time(personListBean.getValid_time().getEnd_time());
                                        subject.setFingerTemplate(personListBean.getFingerTemplate());
                                        subject.setGroup_id(personListBean.getGroup_id());
                                        subject.setGroup_name(personListBean.getGroup_name());
                                        subject.setGroup_type(personListBean.getGroup_type());
                                        subject.setIc_card(personListBean.getIc_card());
                                        subject.setId_card(personListBean.getId_card());
                                        subject.setPerson_id(personListBean.getPerson_id());
                                        subject.setPerson_type(personListBean.getPerson_type());
                                        subject.setSex(personListBean.getSex());
                                        subject.setStart_time(personListBean.getValid_time().getStart_time());
                                        subject.setTeZhengMa(personListBean.getPerson_id());
                                        subjectBox.put(subject);
                                        successfulList.add(personListBean.getPerson_id());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        failedList.add(new FailedPersonBean(personListBean.getPerson_id(),e.getMessage()));
                                    }
                                } else {
                                    if (detectResult!=null)
                                        failedList.add(new FailedPersonBean(personListBean.getPerson_id(),"图片质量不合格,错误码:"+detectResult.message));
                                    else
                                        failedList.add(new FailedPersonBean(personListBean.getPerson_id(),"检测图片失败"));
                                }
                            } else {//没图片加入失败记录
                                failedList.add(new FailedPersonBean(personListBean.getPerson_id(),"下载图片失败"));
                            }
                        }
                        //提交记录
                        link_sync_person(successfulList,failedList,commandsBean.getJsonObject());
                        paAccessControl.startFrameDetect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        public void interrupt() {
            isRing = true;
            // Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }


    @Override
    protected void onPause() {
        DengUT.openLOED8cun();
        super.onPause();
        Log.d("MianBanJiActivity3", "暂停");
        if (mNfcAdapter != null) {
            stopNFC_Listener();
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (sm != null)
            sm.unregisterListener(this);

    }


    private void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
        SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
        SettingVar.cameraId = preferences.getInt("cameraId", SettingVar.cameraId);
        SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
        SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
        SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);
        SettingVar.cameraPreviewRotation2 = preferences.getInt("cameraPreviewRotation2", SettingVar.cameraPreviewRotation2);
        SettingVar.faceRotation2 = preferences.getInt("faceRotation2", SettingVar.faceRotation2);
        SettingVar.msrBitmapRotation = preferences.getInt("msrBitmapRotation", SettingVar.msrBitmapRotation);

        final int mCurrentOrientation = getResources().getConfiguration().orientation;
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }
        setContentView(R.layout.activity_mianbanji3);

        ButterKnife.bind(this);

        ImageView shezhi = findViewById(R.id.shezhi);
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiMaDialog4 miMaDialog = new MiMaDialog4(MianBanJiActivity3.this, baoCunBean.getMima());
                WindowManager.LayoutParams params = miMaDialog.getWindow().getAttributes();
                params.width = dw;
                params.height = dh;
//                miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                miMaDialog.getWindow().setAttributes(params);
                miMaDialog.show();
                DengUT.openDool();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;
        /* 初始化界面 */
       // faceView = findViewById(R.id.fcview);
        //faceView.setwh(widthPixels, heightPixels);
        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
        /* 注册相机回调函数 */
        manager.setListener(this);

        manager2 = new CameraManager2();
        cameraView2 = findViewById(R.id.preview2);
        manager2.setPreviewDisplay(cameraView2);
        /* 注册相机回调函数 */
        manager2.setListener(this);

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tishibg.getLayoutParams();
//        layoutParams.height = (int) (heightPixels * 0.2f);
//        tishibg.setLayoutParams(layoutParams);
//        tishibg.invalidate();
//        if (baoCunBean.getWenzi1() == null) {
//            gongsi.setText("请设置公司名称");
//        } else {
//            gongsi.setText(baoCunBean.getWenzi1());
//        }

      //  ivSetting_Ir = findViewById(R.id.ivSetting_Ir);


        //layoutFaceSuccess_Ir = findViewById(R.id.layoutFaceSuccess_Ir);//识别结果弹出信息
        tvName_Ir = findViewById(R.id.tvName_Ir);//名字
        tvTime_Ir = findViewById(R.id.tvTime_Ir);//时间
        tvFaceTips_Ir = findViewById(R.id.tvFaceTips_Ir);//识别信息提示
        layout_loadbg_Ir = findViewById(R.id.layout_loadbg_Ir);//头像区域的显示的底图背景

        layout_true_gif_Ir = findViewById(R.id.layout_true_gif_Ir);
        layout_error_gif_Ir = findViewById(R.id.layout_error_gif_Ir);
        iv_true_gif_in_Ir = findViewById(R.id.iv_true_gif_in_Ir);
        iv_true_gif_out_Ir = findViewById(R.id.iv_true_gif_out_Ir);
        iv_error_gif_in_Ir = findViewById(R.id.iv_error_gif_in_Ir);
        iv_error_gif_out_Ir = findViewById(R.id.iv_error_gif_out_Ir);
        tvTitle_Ir = findViewById(R.id.tvTitle_Ir);

        //region 动画
        gifClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_face_clockwise);
        gifAntiClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_face_anti_clockwise);
        lir_gif = new LinearInterpolator();//设置为匀速旋转
        gifClockwise.setInterpolator(lir_gif);
        gifAntiClockwise.setInterpolator(lir_gif);

        /*iv_true_gif_in_Ir.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        iv_error_gif_in_Ir.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        iv_true_gif_in_Ir.startAnimation(gifAntiClockwise);
        iv_error_gif_in_Ir.startAnimation(gifAntiClockwise);*/

        iv_true_gif_out_Ir.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        iv_error_gif_out_Ir.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        iv_true_gif_out_Ir.startAnimation(gifClockwise);
        iv_error_gif_out_Ir.startAnimation(gifClockwise);
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
        tvTitle_Ir.setTypeface(tf);
        if (baoCunBean.getWenzi1() == null) {
            tvTitle_Ir.setText("请设置公司名称");
        } else {
            tvTitle_Ir.setText(baoCunBean.getWenzi1());
        }

        showUIResult(1,"","");

    }


    OnPaAccessDetectListener onDetectListener = new OnPaAccessDetectListener() {
        //每一帧数据的回调
        @Override
        public void onFaceDetectFrame(int message, PaAccessDetectFaceResult faceDetectFrame) {

            if (message == 1001) {//没人脸
                faceId = "";
                feature2 = -1;
                //  tishi.setVisibility(View.GONE);
                if (DengUT.isOPEN || DengUT.isOPENRed || DengUT.isOPENGreen) {
                    Log.d("MianBanJiActivity3", "进来");
                    DengUT.isOPEN = false;
                    DengUT.isOPENGreen = false;
                    DengUT.isOPENRed = false;
                    DengUT.isOpenDOR = false;
                //    DengUT.closeWrite();
                    if (jiqiType==2){
                        DengUT.closeWrite8cun();
                    }
                    //启动定时器或重置定时器
                    if (task2 != null) {
                        task2.cancel();
                        //timer.cancel();
                        task2 = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 555;
                                mHandler.sendMessage(message);
                            }
                        };
                        timer2.schedule(task2, 5000);
                    } else {
                        task2 = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 555;
                                mHandler.sendMessage(message);
                            }
                        };
                        timer2.schedule(task2, 5000);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // faceView.clera();
                            showUIResult(1,"","");
                        }
                    });
                }

            }else if (message==1002){//检测到了人脸
                //启动定时器或重置定时器
                if (task2 != null) {
                    task2.cancel();
                  //  DengUT.isOPEN = true;
                }
            }

//            if (faceDetectFrame != null) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                       // showFacePassFace(faceDetectFrame);
//                        showUIResult(2,"","");
//                    }
//                });
//
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showUIResult(1,"","");
//                       // faceView2.clear();
//                        //faceView2.invalidate();
//                    }
//                });
//            }

        }

        @Override
        public void onFaceDetectResult(int var1, PaAccessDetectFaceResult detectResult) {
            // Log.d("Robin","detectResult : " + detectResult.facePassFrame.blurness);
            if (detectResult == null)
                return;
            // Log.d("MianBanJiActivity3", "detectResult.feature:" + detectResult.feature);
            PaAccessCompareFacesResult paFacePassCompareResult = paAccessControl.compareFaceToAll(detectResult.feature);
            if (paFacePassCompareResult == null || paFacePassCompareResult.message != PaAccessControlMessage.RESULT_OK) {
                Log.d("MianBanJiActivity", "没有人脸信息");
                return;
            }
            if (!DengUT.isOPEN) {
                DengUT.isOPEN = true;
               // DengUT.openWrite();
                if (jiqiType==2){
                    DengUT.openWrite8cun();
                    DengUT.openLOED8cun();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showUIResult(2,"","");
                    }
                });
            }
            //人脸信息完整的
            String id = paFacePassCompareResult.id;
            //  Log.d("MianBanJiActivity3", "detectResult.frameHeight:" + detectResult.frameHeight);
            //  Log.d("MianBanJiActivity3", "detectResult.frameWidth:" + detectResult.frameWidth);
            //    faceView.setFace(detectResult.rectX,detectResult.rectY,detectResult.rectW,detectResult.rectH,detectResult.frameWidth,detectResult.frameHeight);
            // String gender = getGender(detectResult.gender);
            //   boolean attriButeEnable = PaAccessControl.getInstance().getPaAccessDetectConfig().isAttributeEnabled(); //Robin 是否检测了人脸属性
             //  Log.d("MianBanJiActivity3", "paFacePassCompareResult.compareScore:" + paFacePassCompareResult.compareScore);
            //百分之一误识为0.52；千分之一误识为0.56；万分之一误识为0.60 比对阈值可根据实际情况调整
            if (paFacePassCompareResult.compareScore > mCompareThres) {
                feature2 = detectResult.trackId;
                // 不相等 弹窗
                if (!id.equals(faceId)) {
                    faceId = id;
                    final Subject subject = subjectBox.query().equal(Subject_.teZhengMa, id).build().findUnique();
                    if (subject != null) {
                        try {
                            long t1=Long.valueOf(DateUtils.dataOne(subject.getEnd_time()));
                            if (t1<System.currentTimeMillis()){//已经过期
                                paAccessControl.stopFrameDetect();
                                PaAccessFaceInfo face = paAccessControl.queryFaceById(subject.getPerson_id());
                                if (face != null) {
                                    paAccessControl.deleteFaceById(face.faceId);
                                }
                                subjectBox.remove(subject);
                                paAccessControl.startFrameDetect();
                                DengUT.isOPEN=true;
                                return;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            paAccessControl.startFrameDetect();
                        }
                        //subjectOnly = subject;
                        // linkedBlockingQueue.offer(subject);
                        Message message2 = Message.obtain();
                        message2.what = 111;
                        message2.obj = subject;
                        mHandler.sendMessage(message2);
//                        if (!DengUT.isOPENGreen) {
//                            DengUT.isOPENGreen = true;
//                            DengUT.openGreen();
//                        }
                        DengUT.isOPEN=true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                showUIResult(4,subject.getPerson_name(),subject.getPerson_type());
                                msrBitmap = nv21ToBitmap.nv21ToBitmap(detectResult.rgbFrame, detectResult.frameWidth, detectResult.frameHeight);
                                link_shangchuanshualian(subject.getTeZhengMa(), msrBitmap,paFacePassCompareResult.compareScore+"","0",subject.getPerson_type());
                            }
                        }).start();
                    } else {
                        EventBus.getDefault().post("没有查询到人员信息");
                    }
                }

            } else {
                //陌生人
                //  Log.d("MianBanJiActivity3", "陌生人"+id);
//                if (isShow)
//                tishi.setVisibility(View.VISIBLE)
                pp++;
                if (pp>9) {
                    faceId = "";
                    pp = 0;
                    if (feature2 == -1) {
                        feature2 = detectResult.trackId;
                        msrBitmap = nv21ToBitmap.nv21ToBitmap(detectResult.rgbFrame, detectResult.frameWidth, detectResult.frameHeight);
                        // Bitmap bitmap = BitmapUtil.getBitmap(facePassFrame.frame, facePassFrame.frmaeWidth, facePassFrame.frameHeight, facePassFrame.frameOri);
                        // bitmap = BitmapUtil.getCropBitmap(bitmap, facePassFrame.rectX, facePassFrame.rectY, facePassFrame.rectW, facePassFrame.rectH);
                        //  tianqi_im.setImageBitmap(msrBitmap);
                        // Log.d("MianBanJiActivity3", "msrBitmap:" + msrBitmap.getWidth());
                        link_shangchuanshualian("", msrBitmap,paFacePassCompareResult.compareScore+"","1","5");
                        Subject subject1 = new Subject();
                        //subject1.setW(bitmap.getWidth());
                        //subject1.setH(bitmap.getHeight());
                        //图片在bitmabToBytes方法里面做了循转
                        // subject1.setTxBytes(BitmapUtil.bitmabToBytes(bitmap));
                        subject1.setId(System.currentTimeMillis());
                        subject1.setPerson_name("陌生人");
                        subject1.setTeZhengMa(null);
                        subject1.setPerson_type("5");//1 白名单，2 黑名单，3 访客，4 VIP，5 陌生人
                        //linkedBlockingQueue.offer(subject1);
                        Message message2 = Message.obtain();
                        message2.what = 111;
                        message2.obj = subject1;
                        mHandler.sendMessage(message2);
//                        if (!DengUT.isOPENRed) {
//                            DengUT.isOPENRed = true;
//                            DengUT.openRed();
//                        }
                        DengUT.isOPEN=true;
                        showUIResult(3,"陌生人","");
                    } else if (feature2 != detectResult.trackId) {
                        faceId = "";
                        msrBitmap = nv21ToBitmap.nv21ToBitmap(detectResult.rgbFrame, detectResult.frameWidth, detectResult.frameHeight);
                        // Bitmap bitmap = BitmapUtil.getBitmap(facePassFrame.frame, facePassFrame.frmaeWidth, facePassFrame.frameHeight, facePassFrame.frameOri);
                        //  bitmap = BitmapUtil.getCropBitmap(bitmap, facePassFrame.rectX, facePassFrame.rectY, facePassFrame.rectW, facePassFrame.rectH);
                        link_shangchuanshualian("", msrBitmap,paFacePassCompareResult.compareScore+"","1","5");
                        Subject subject1 = new Subject();
                        // subject1.setW(bitmap.getWidth());
                        // subject1.setH(bitmap.getHeight());
                        //图片在bitmabToBytes方法里面做了循转
                        // subject1.setTxBytes(BitmapUtil.bitmabToBytes(bitmap));
                        subject1.setId(System.currentTimeMillis());
                        subject1.setPerson_name("陌生人");
                        subject1.setTeZhengMa(null);
                        subject1.setPerson_type("5");//1 白名单，2 黑名单，3 访客，4 VIP，5 陌生人
                        // linkedBlockingQueue.offer(subject1);
                        Message message2 = Message.obtain();
                        message2.what = 111;
                        message2.obj = subject1;
                        mHandler.sendMessage(message2);
                        showUIResult(3,"陌生人","");
//                        if (!DengUT.isOPENRed) {
//                            DengUT.isOPENRed = true;
//                            DengUT.openRed();
//                        }
                        DengUT.isOPEN=true;
                    }
                }
            }
        }

        @Override
        public void onMultiFacesDetectFrameBaseInfo(int i, List<PaAccessMultiFaceBaseInfo> list) {

          //  Log.d("MianBanJiActivity3", "list.size():" + list.size());

        }
    };


    private void showFacePassFace(PaAccessDetectFaceResult detectResult) {
      //  faceView2.clear();
//        Log.d("MianBanJiActivity333", "detectResult.frameWidth:" + detectResult.frameWidth);
//        Log.d("MianBanJiActivity333", "detectResult.frameHeight:" + detectResult.frameHeight);
//                Log.d("MianBanJiActivity333", "widthPixels:" + widthPixels);
//                Log.d("MianBanJiActivity333", "heightPixels:" + heightPixels);
//                float s1 = (float) widthPixels / (float)detectResult.frameWidth;
//                float s2 = (float) heightPixels / (float)detectResult.frameHeight;

        //  Log.d("facefacelist", "width " + (face.rect.right - face.rect.left) + " height " + (face.rect.bottom - face.rect.top) );
        //  Log.d("facefacelist", "smile " + face.smile);

//            StringBuilder faceIdString = new StringBuilder();
//            faceIdString.append("ID = ").append(face.trackId);
//            StringBuilder faceRollString = new StringBuilder();
//            faceRollString.append("旋转: ").append((int) face.pose.roll).append("°");
//            StringBuilder facePitchString = new StringBuilder();
//            facePitchString.append("上下: ").append((int) face.pose.pitch).append("°");
//            StringBuilder faceYawString = new StringBuilder();
//            faceYawString.append("左右: ").append((int) face.pose.yaw).append("°");
//            StringBuilder faceBlurString = new StringBuilder();
//            faceBlurString.append("模糊: ").append(face.blur);
        Matrix mat = new Matrix();

//            Log.d("rrr", "w:" + w);
//            Log.d("rrr", "h:" + h);
//            Log.d("rrr", "cameraWidth:" + cameraWidth);
//            Log.d("rrr", "cameraHeight:" + cameraHeight);


        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
//        Log.d("MianBanJiActivity3", "detectResult.rectX:" + detectResult.rectX);
//        Log.d("MianBanJiActivity3", "detectResult.rectY:" + detectResult.rectY);
//        Log.d("MianBanJiActivity3", "detectResult.rectW:" + detectResult.rectW);
//        Log.d("MianBanJiActivity3", "detectResult.rectH:" + detectResult.rectH);
//        Log.d("MianBanJiActivity3", "detectResult.rectH:------------------------");
        boolean mirror = false;
        switch (0) {
            case 0:
                left = w - detectResult.rectX * s1 - detectResult.rectW * s1;
                top = detectResult.rectY * s2 - 40;
                right = (w - detectResult.rectX * s1);
                bottom = detectResult.rectY * s2 + detectResult.rectH + 80;
                // mat.setScale(mirror ? -1 : 1, 1);
                // mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                // mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                break;
            case 90:
                // mat.setScale(mirror ? -1 : 1, 1);
                //  mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                //  mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                left = detectResult.rectY * s2 - 40;
                top = (w - detectResult.rectX * s1);
                right = detectResult.rectY * s2 + detectResult.rectH + 80;
                bottom = w - detectResult.rectX * s1 - detectResult.rectW * s1;
                break;
            case 180:
                mat.setScale(1, mirror ? -1 : 1);
                mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                left = detectResult.rectW + detectResult.rectX;
                top = detectResult.rectH + detectResult.rectY;
                right = detectResult.rectX;
                bottom = detectResult.rectY;
                break;
            case 270:
                mat.setScale(mirror ? -1 : 1, 1);
                mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                left = cameraHeight - (detectResult.rectH + detectResult.rectY);
                top = detectResult.rectX;
                right = cameraHeight - detectResult.rectY;
                bottom = detectResult.rectW + detectResult.rectX;
        }
        RectF drect = new RectF();
        RectF srect = new RectF(left, top, right, bottom);

        mat.mapRect(drect, srect);
       // faceView2.addRect(drect);
       // faceView2.addId(detectResult.trackId + "");
//            faceView.addRoll(faceRollString.toString());
//            faceView.addPitch(facePitchString.toString());
//            faceView.addYaw(faceYawString.toString());
//            faceView.addBlur(faceBlurString.toString());
        //           faceView.addSmile(smileString.toString());
     //   faceView.invalidate();
       // faceView2.invalidate();
    }

    @Override
    protected void onStop() {
        Log.d("MianBanJiActivity3", "停止");
        if (paAccessControl != null) {
            paAccessControl.stopFrameDetect();
        }

        if (manager != null) {
            manager.release();
        }
        if (manager2 != null) {
            manager2.release();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("MianBanJiActivity3", "onRestart");
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MianBanJiActivity3", "onStart");
    }


    @Override
    protected void onDestroy() {
        Log.d("MianBanJiActivity3", "onDestroy");
        if (tanChuangThread != null) {
            tanChuangThread.isRing = true;
            tanChuangThread.interrupt();
        }
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        unregisterReceiver(timeChangeReceiver);
        unregisterReceiver(netWorkStateReceiver);
        EventBus.getDefault().unregister(this);//解除订阅
        if (manager != null) {
            manager.release();
        }
        timer.cancel();
        if (task != null)
            task.cancel();

        timer2.cancel();
        if (task2 != null)
            task2.cancel();

      //  DengUT.closeWrite();
      //  DengUT.closeGreen();
      //  DengUT.closeRed();

        super.onDestroy();
    }

    private static final int REQUEST_CODE_CHOOSE_PICK = 1;


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (keyCode == KeyEvent.KEYCODE_MENU) {
//              // startActivity(new Intent(MianBanJiActivity3.this, SheZhiActivity2.class));
//              //  finish();
//            }
//
//        }
//
//        return super.onKeyDown(keyCode, event);
//
//    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String event) {

        if (event.equals("ditu123")) {
            // if (baoCunBean.getTouxiangzhuji() != null)
            //    daBg.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getTouxiangzhuji()));
            baoCunBean = baoCunBeanDao.get(123456L);

            //   Log.d("MainActivity101", "dfgdsgfdgfdgfdg");
            return;
        }

        if (event.equals("kaimen")) {
            menjing1();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(8000);
                    menjing2();
                }
            }).start();
            return;
        }
        if (event.equals("guanbimain")) {
            finish();
            return;
        }
        Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, event, TastyToast.LENGTH_LONG, TastyToast.INFO);
        tastyToast.setGravity(Gravity.CENTER, 0, 0);
        tastyToast.show();

    }


    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_TIME_TICK:
                    timeUpdataState++;
                   if (timeUpdataState>=6){//5分钟传一次
                       timeUpdataState=1;
                       link_updataState(baoCunBean.getTuisongDiZhi());
                   }
//                    String xiaoshiss = DateUtils.timeMinute(System.currentTimeMillis() + "");
//                    if (xiaoshiss.split(":")[0].equals("03") && xiaoshiss.split(":")[1].equals("40")) {
//                    }
                    break;
                case Intent.ACTION_TIME_CHANGED:
                    //设置了系统时间
                    // Toast.makeText(context, "system time changed", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_TIMEZONE_CHANGED:
                    //设置了系统时区的action
                    //  Toast.makeText(context, "system time zone changed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }




    //上传记录
    private void link_shangchuanshualian(String person_id, Bitmap bitmap,String capture_score,String capture_status,String person_type) {
        if (baoCunBean.getHoutaiDiZhi() == null || baoCunBean.getHoutaiDiZhi().equals("")) {
            return;
        }
       // Bitmap bb = BitmapUtil.rotateBitmap(bitmap, SettingVar.msrBitmapRotation);
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        try {
            object.put("device_sn", baoCunBean.getTuisongDiZhi());
            object.put("token", baoCunBean.getXgToken());
            object.put("person_id", person_id);
            object.put("person_name", baoCunBean.getXgToken());
            object.put("person_type",person_type);
            object.put("capture_img", BitmapUtil.bitmapToBase64(bitmap));
            object.put("capture_time", DateUtils.tim(System.currentTimeMillis()+""));
            object.put("capture_score", capture_score);
            object.put("capture_status", capture_status);//0 ：通过，白名单比对成功时传        1 ：不通过，用于陌生人采集场景
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi() + "/attendance-record");
        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "上传识别记录失败", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    OpenDoorBean openDoorBean = gson.fromJson(jsonObject, OpenDoorBean.class);
                    if (openDoorBean.isOutput()){//开门
                        soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
                        DengUT.openDool8cun();
                    }else {
                        soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
                    }
                } catch (Exception e) {
                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");

                }
            }
        });
    }


    private void guanPing() {
//        Intent intent = new Intent();
//        intent.setAction("LYD_SHOW_NAVIGATION_BAR");
//        intent.putExtra("type", 0);
//        this.sendBroadcast(intent);
//        sendBroadcast(new Intent("com.android.internal.policy.impl.hideNavigationBar"));
//        sendBroadcast(new Intent("com.android.systemui.statusbar.phone.statusclose"));
      //  if (jiqiType==2){//8寸防水面板机
            HwitManager.HwitSetHideSystemBar(MianBanJiActivity3.this);
            HwitManager.HwitSetDisableSlideShowSysBar(1);

     //   }

    }

    private void menjing1() {
        // TPS980PosUtil.setJiaJiPower(1);
        DengUT.openDool();
      //  TPS980PosUtil.setRelayPower(1);
        Log.d("MianBanJiActivity3", "打开");
    }

    private void menjing2() {
        //  TPS980PosUtil.setJiaJiPower(0);
        DengUT.closeDool();
       // TPS980PosUtil.setRelayPower(0);
        Log.d("MianBanJiActivity3", "关闭");
    }


    /**
     * 获取本地化后的config
     * 注册和比对使用不同的设置
     */
    private void initFaceConfig() {
        //Robin 使用比对的设置
        PaAccessDetectConfig faceDetectConfig = PaAccessControl.getInstance().getPaAccessDetectConfig();
        faceDetectConfig.setFaceConfidenceThreshold(0.85f); //检测是不是人的脸。默认使用阀值 0.85f，阈值视具体 情况而定，最大为 1。
        faceDetectConfig.setYawThreshold(60);//人脸识别角度
        faceDetectConfig.setRollThreshold(60);
        faceDetectConfig.setPitchThreshold(60);
        // 注册图片模糊度可以设置0.9f（最大值1.0）这样能让底图更清晰。比对的模糊度可以调低一点，这样能加快识别速度，识别模糊度建议设置0.1f
        faceDetectConfig.setBlurnessThreshold(0.5f);
        faceDetectConfig.setMinBrightnessThreshold(30); // 人脸图像最小亮度阀值，默认为 30，数值越小越 暗，太暗会影响人脸检测和活体识别，可以根据 需求调整。
        faceDetectConfig.setMaxBrightnessThreshold(240);// 人脸图像最大亮度阀值，默认为 240，数值越大 越亮，太亮会影响人脸检测和活体识别，可以根 据需求调整。
        faceDetectConfig.setAttributeEnabled(false);//人脸属性开关，默认关闭。会检测出人脸的性别 和年龄。人脸属性的检测会消耗运算资源，可视 情况开启，未开启性别和年龄都返回-1
        faceDetectConfig.setLivenessEnabled(baoCunBean.isHuoTi());//活体开关
       // faceDetectConfig.setTrackingMode(true); //Robin 跟踪模式跟踪模式，开启后会提高检脸检出率，减小检脸耗时。门禁场景推荐开启。图片检测会强制关闭
        faceDetectConfig.setIrEnabled(baoCunBean.isHuoTi()); //非Ir模式，因为是单例模式，所以最好每个界面都设置是否开启Ir模式
        //faceDetectConfig.setMinScaleThreshold(0.1f);//设置最小检脸尺寸，可以用这个来控制最远检脸距离。默认采用最小值 0.1，约 1.8 米，在 640*480 的预览分辨率下，最小人脸尺寸 为(240*0.1)*(240*0.1)即 24*24。 0.2 的最远 识别距离约 1.2 米;0.3 的最远识别距离约约 0.8 米。detectFaceMinScale 取值范围 [0.1,0.3]。门禁场景推荐 0.1;手机场景推荐 0.3。
        PaAccessNativeConfig nativeConfig = new PaAccessNativeConfig();
        //Robin 检测人脸数为1
        nativeConfig.faceNumber = 1;
        paAccessControl.setPaAccessNativeConfig(nativeConfig);
        paAccessControl.setPaAccessDetectConfig(faceDetectConfig);
        PaAccessControl.getInstance().setPaAccessDetectConfig(faceDetectConfig);
    }


    private void init_NFC() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }

    private void stopNFC_Listener() {
        mNfcAdapter.disableForegroundDispatch(this);
    }

    public void processIntent(Intent intent) {
        //  String data = null;
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // String[] techList = tag.getTechList();
        // Log.d("Mian", "tag.describeContents():" + tag.describeContents());
        byte[] ID;
        //  data = tag.toString();
        if (tag == null)
            return;
        ID = tag.getId();
//        data += "\n\nUID:\n" + byteToString(ID);
//        data += "\nData format:";
//        for (String tech : techList) {
//            data += "\n" + tech;
//        }
        // Log.d("MianBanJiActivity3", byteToString(ID));
        String sdfds = byteToString(ID);
        if (sdfds != null) {
            sdfds = sdfds.toUpperCase();
            List<IDCardBean> idCardBeanList = idCardBeanBox.query().equal(IDCardBean_.idCard, sdfds).build().find();
            if (idCardBeanList.size() > 0) {
//                Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "验证成功!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                tastyToast.setGravity(Gravity.CENTER, 0, 0);
//                tastyToast.show();
//                soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
              //  DengUT.openDool();
            //    IDCardBean cardBean=idCardBeanList.get(0);

                //link_shuaka(sdfds,cardBean.getName());

                //启动定时器或重置定时器
//                if (task != null) {
//                    task.cancel();
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 6000);
//                } else {
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 6000);
//                }
//
//                IDCardTakeBean takeBean=new IDCardTakeBean();
//                takeBean.setIdCard(sdfds);
//                takeBean.setName(cardBean.getName());
//                takeBean.setTime(System.currentTimeMillis());
//                idCardTakeBeanBox.put(takeBean);

            } else {
//                Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "验证失败!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                tastyToast.setGravity(Gravity.CENTER, 0, 0);
//                tastyToast.show();
//                soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
            }
        }


    }

    /**
     * 将byte数组转化为字符串
     *
     * @param src
     * @return
     */
    public static String byteToString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            // System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }


    public class NetWorkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //以太网
                NetworkInfo wifiNetworkInfo1 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo1.isConnected() || wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                    //有网
                    Log.d("MianBanJiActivity3", "有网1");

                } else {
                    //没网
                    Log.d("MianBanJiActivity3", "没网1");

                }

//				if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
//				} else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//				}
//API大于23时使用下面的方式进行网络监听
            } else {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                Log.d("MianBanJiActivity3", "networks.length:" + networks.length);
                if (networks.length == 0) {
                    //没网
                    Log.d("MianBanJiActivity3", "没网2");

                }
                for (Network network : networks) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                    if (networkInfo.isConnected()) {
                        //连接上
                        Log.d("MianBanJiActivity3", "有网2");


                        break;
                    }
                }

            }
        }
    }


    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 90, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



    /**
     * 显示结果UI
     *
     * @param state 状态 1 初始状态  2 识别中,出现提示语  3 识别失败  4 识别成功
     */
    protected void showUIResult(final int state, final String name, final String detectFaceTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("MianBanJiActivity3", "state:" + state);
                switch (state) {
                    case 1: {//初始状态
                        layout_true_gif_Ir.setVisibility(View.INVISIBLE);//蓝色图片动画
                        layout_error_gif_Ir.setVisibility(View.INVISIBLE);//红色图片动画
                        iv_true_gif_in_Ir.setVisibility(View.INVISIBLE);//蓝色圈内层
                        iv_true_gif_out_Ir.setVisibility(View.INVISIBLE);//蓝色圈外层
                        iv_error_gif_in_Ir.setVisibility(View.INVISIBLE);//红色圈内层
                        iv_error_gif_out_Ir.setVisibility(View.INVISIBLE);//红色圈外层
                        layout_loadbg_Ir.setVisibility(View.INVISIBLE);//识别结果大框
                        tvName_Ir.setVisibility(View.GONE);//姓名
                        tvTime_Ir.setVisibility(View.GONE);//时间
                        tvFaceTips_Ir.setVisibility(View.GONE);//识别提示
                        tvName_Ir.setText("");
                        tvTime_Ir.setText("");
                        tvFaceTips_Ir.setText("");
                        break;
                    }
                    case 2: {//识别中,出现提示语
                        layout_true_gif_Ir.setVisibility(View.VISIBLE);//蓝色图片动画
                        layout_error_gif_Ir.setVisibility(View.INVISIBLE);//红色图片动画
                        iv_true_gif_in_Ir.setVisibility(View.VISIBLE);//蓝色圈内层
                        iv_true_gif_out_Ir.setVisibility(View.VISIBLE);//蓝色圈外层
                        iv_error_gif_in_Ir.setVisibility(View.INVISIBLE);//红色圈内层
                        iv_error_gif_out_Ir.setVisibility(View.INVISIBLE);//红色圈外层
                        layout_loadbg_Ir.setVisibility(View.VISIBLE);//识别结果大框
                        layout_loadbg_Ir.setBackgroundResource(R.mipmap.true_bg);//切换背景
                        tvName_Ir.setVisibility(View.GONE);//姓名
                        tvTime_Ir.setVisibility(View.GONE);//时间
                        tvFaceTips_Ir.setVisibility(View.VISIBLE);//识别提示
                        tvName_Ir.setText("");
                        tvTime_Ir.setText("");
                        tvFaceTips_Ir.setText("识别中,请稍后...");
                        break;
                    }
                    case 3: {//识别失败
                        layout_true_gif_Ir.setVisibility(View.INVISIBLE);//蓝色图片动画
                        layout_error_gif_Ir.setVisibility(View.VISIBLE);//红色图片动画
                        iv_true_gif_in_Ir.setVisibility(View.INVISIBLE);//蓝色圈内层
                        iv_true_gif_out_Ir.setVisibility(View.INVISIBLE);//蓝色圈外层
                        iv_error_gif_in_Ir.setVisibility(View.VISIBLE);//红色圈内层
                        iv_error_gif_out_Ir.setVisibility(View.VISIBLE);//红色圈外层
                        layout_loadbg_Ir.setVisibility(View.VISIBLE);//识别结果大框
                        layout_loadbg_Ir.setBackgroundResource(R.mipmap.error_bg);//切换背景
                        tvName_Ir.setVisibility(View.GONE);//姓名
                        tvTime_Ir.setVisibility(View.GONE);//时间
                        tvFaceTips_Ir.setVisibility(View.VISIBLE);//识别提示
                        tvName_Ir.setText("");
                        tvTime_Ir.setText("");
                        tvFaceTips_Ir.setText("无权限通过,请重试");
                        break;
                    }
                    case 4: {//识别成功
                        layout_true_gif_Ir.setVisibility(View.VISIBLE);//蓝色图片动画
                        layout_error_gif_Ir.setVisibility(View.INVISIBLE);//红色图片动画
                        iv_true_gif_in_Ir.setVisibility(View.VISIBLE);//蓝色圈内层
                        iv_true_gif_out_Ir.setVisibility(View.VISIBLE);//蓝色圈外层
                        iv_error_gif_in_Ir.setVisibility(View.INVISIBLE);//红色圈内层
                        iv_error_gif_out_Ir.setVisibility(View.INVISIBLE);//红色圈外层
                        layout_loadbg_Ir.setVisibility(View.VISIBLE);//识别结果大框
                        layout_loadbg_Ir.setBackgroundResource(R.mipmap.true_bg);//切换背景
                        tvName_Ir.setVisibility(View.VISIBLE);//姓名
                        tvTime_Ir.setVisibility(View.VISIBLE);//时间
                        tvFaceTips_Ir.setVisibility(View.GONE);//识别提示
                        tvName_Ir.setText(name);
                        tvTime_Ir.setText("部门:"+detectFaceTime);
                        tvFaceTips_Ir.setText("");
                        break;
                    }
                }
            }
        });
    }

    //登录
    private void link_loging(String deviceId){
        if (baoCunBean.getHoutaiDiZhi()==null || baoCunBean.getHoutaiDiZhi().equals("")){
            EventBus.getDefault().post("后台地址不正确");
            return;
        }
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

        JSONObject object = new JSONObject();
        try {
            object.put("sign_type", "device_sign");
            object.put("sign_code", "device_code");
            object.put("device_sn", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi()+"/device-login");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
                EventBus.getDefault().post("网络请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("AllConnects", "token:"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    logingBean = gson.fromJson(jsonObject, LogingBean.class);
                    if (logingBean.getCode()==0 && logingBean.isSuccess()){
                        //登录成功
                        float score=logingBean.getScore();
                        float a= (score-80)==0?0: (float) ((score-80) / 25.0);
                        baoCunBean.setShibieFaZhi(baoCunBean.getShibieFaZhi()+a);
                        baoCunBean.setHuoTi(logingBean.isAlive());
                      //  baoCunBean.setHuoTi(false);
                        baoCunBean.setXgToken(logingBean.getToken());
                        baoCunBeanDao.put(baoCunBean);
                        baoCunBean=baoCunBeanDao.get(123456);
                        mCompareThres = baoCunBean.getShibieFaZhi();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                manager.open(getWindowManager(), SettingVar.cameraId, cameraWidth, cameraHeight);//前置是1
                                if (baoCunBean.isHuoTi()) {
                                    if (SettingVar.cameraId == 1) {
                                        manager2.open(getWindowManager(), 0, cameraWidth, cameraHeight, SettingVar.cameraPreviewRotation2);//最后一个参数是红外预览方向
                                    } else {
                                        manager2.open(getWindowManager(), 1, cameraWidth, cameraHeight, SettingVar.cameraPreviewRotation2);//最后一个参数是红外预览方向
                                    }
                                }
                            }
                        });
                        //连接mq
                        factory = new ConnectionFactory();
                        // "guest"/"guest" by default, limited to localhost connections
                        factory.setUsername(logingBean.getRabbitmq().getLogin());
                        factory.setPassword(logingBean.getRabbitmq().getPassword());
                        factory.setVirtualHost(logingBean.getRabbitmq().getVhost());
                        factory.setHost(logingBean.getRabbitmq().getHost());
                        factory.setPort(logingBean.getRabbitmq().getPort());
                        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
                        conn = factory.newConnection();
                        channel = conn.createChannel();
                        //声明一个队列
                        channel.queueDeclare(logingBean.getRabbitmq().getQueue(), logingBean.getRabbitmq().getExchange().isDurable(), false, false, null);
                        Consumer consumer = new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                                String message = new String(body, StandardCharsets.UTF_8);
                                System.out.println("MianBanJiActivity3 [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                                try {
                                    JsonObject jsonObject2 = GsonUtil.parse(message).getAsJsonObject();
                                    Gson gson2 = new Gson();
                                    AddFacesBean addFacesBean = gson2.fromJson(jsonObject2, AddFacesBean.class);
                                    getPerosnPath=addFacesBean.getPath();
                                    perosnNotifyPath=addFacesBean.getNotify();
                                    if (addFacesBean.getMethod().equals("sync-person")){
                                        //新增。修改
                                        link_add(addFacesBean,1);
                                    }else if (addFacesBean.getMethod().equals("delete-person")){
                                        //删除
                                        link_add(addFacesBean,2);
                                    }
                                }catch (Exception e){
                                    EventBus.getDefault().post("MQ消息解析失败"+e.getMessage());
                                }
//{"method":"sync-person","person_list":["Member_18443015"],"path":"http://113.92.35.143:9001/person-list","notify":"http://113.92.35.143:9001/person-notify","params":{"Hid":"","Ids":["Member_18443015"]}}'
                            }
                        };
                        channel.basicConsume(logingBean.getRabbitmq().getQueue(), true, consumer);
                        Log.d("MianBanJiActivity3", "channel.isOpen():" + channel.isOpen());

                    //    String message="{\"method\":\"sync-person\",\"person_list\":[\"Member_18443015\"],\"path\":\"http://21n2c53681.iask.in:9001/person-list\",\"notify\":\"http://21n2c53681.iask.in:9001/person-notify\",\"params\":{\"Hid\":\"\",\"Ids\":[\"Member_18443015\"]}}\n";
//                        try {
//                            JsonObject jsonObject2 = GsonUtil.parse(message).getAsJsonObject();
//                            Gson gson2 = new Gson();
//                            AddFacesBean addFacesBean = gson2.fromJson(jsonObject2, AddFacesBean.class);
//                            getPerosnPath=addFacesBean.getPath();
//                            perosnNotifyPath=addFacesBean.getNotify();
//                            if (addFacesBean.getMethod().equals("sync-person")){
//                                //新增。修改
//                                link_add(addFacesBean,1);
//                            }else if (addFacesBean.getMethod().equals("delete-person")){
//                                //删除
//                                link_add(addFacesBean,2);
//                            }
//
//                        }catch (Exception e){
//                            EventBus.getDefault().post("MQ消息解析失败"+e.getMessage());
//                        }

                    }else {
                        //登录失败
                        EventBus.getDefault().post("登录失败");
                    }

                }catch (Exception e){
                    EventBus.getDefault().post("登录失败"+e.getMessage());
                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                }

            }
        });
    }


    //登录
    private void link_loging_40004(String deviceId){
        if (baoCunBean.getHoutaiDiZhi()==null || baoCunBean.getHoutaiDiZhi().equals("")){
            EventBus.getDefault().post("后台地址不正确");
            return;
        }
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        try {
            object.put("sign_type", "device_sign");
            object.put("sign_code", "device_code");
            object.put("device_sn", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi()+"/device-login");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
                EventBus.getDefault().post("网络请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("AllConnects", "token:"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    LogingBean logingBean = gson.fromJson(jsonObject, LogingBean.class);
                    Log.d("MianBanJiActivity3", "logingBean.isSuccess():" + logingBean.isSuccess());
                    if (logingBean.getCode()==0 && logingBean.isSuccess()){
                        //登录成功
                        float score=logingBean.getScore();
                        float a= (score-80)==0?0: (float) ((score-80) / 25.0);
                        baoCunBean.setShibieFaZhi(baoCunBean.getShibieFaZhi()+a);
                        baoCunBean.setHuoTi(logingBean.isAlive());
                        baoCunBean.setXgToken(logingBean.getToken());
                        baoCunBeanDao.put(baoCunBean);
                        baoCunBean=baoCunBeanDao.get(123456);
                        mCompareThres = baoCunBean.getShibieFaZhi();
                    }else {
                        //登录失败
                        EventBus.getDefault().post("登录失败");
                    }
                }catch (Exception e){
                    EventBus.getDefault().post("登录失败"+e.getMessage());
                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                }

            }
        });
    }

    //上传状态
    private void link_updataState(String deviceId){
        if (baoCunBean.getHoutaiDiZhi()==null || baoCunBean.getHoutaiDiZhi().equals("")){
            EventBus.getDefault().post("后台地址不正确");
            return;
        }
        String sdk_version=null;
        if (paAccessControl!=null){
            sdk_version=paAccessControl.getVersion();
        }else {
            sdk_version="1.0.0";
        }
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        try {
            object.put("device_sn", deviceId);
            object.put("token", baoCunBean.getXgToken());
            object.put("device_ip", FileUtil.getIPAddress(getApplicationContext()));
            object.put("device_name", baoCunBean.getDevice_name());
            object.put("device_status", "1");
            object.put("sdk_version",sdk_version );
            object.put("cpu_usage", GetCpuState.getRate());
            object.put("memory_size",GetCpuState.getTotalMemory(MianBanJiActivity3.this));
            object.put("memory_usage", GetCpuState.getAvailMemory(MianBanJiActivity3.this));
            object.put("disk_size", GetCpuState.getTotalInternalMemorySize()+"");
            object.put("disk_usage",(Float.valueOf(GetCpuState.getAvailableInternalMemorySize())/Float.valueOf(GetCpuState.getTotalInternalMemorySize()))+"");//磁盘使用率
            object.put("disk_free_size", GetCpuState.getAvailableInternalMemorySize()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MianBanJiActivity3", object.toString());
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi()+"/device-notify");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
                EventBus.getDefault().post("网络请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("MianBanJiActivity3", "token:"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    TimeStateBean logingBean = gson.fromJson(jsonObject, TimeStateBean.class);
                    if (logingBean.getCode()==0){
                        //提交状态成功
                        Log.d("MianBanJiActivity3", "成功提交状态");
                    }else if (logingBean.getCode()==40004){
                        //登录失败
                       link_loging_40004(baoCunBean.getTuisongDiZhi());
                    }else {
                        EventBus.getDefault().post("提交状态失败");
                    }
                }catch (Exception e){
                    EventBus.getDefault().post("提交状态失败"+e.getMessage());
                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                }

            }
        });
    }

    //新增人员
    private void link_add(AddFacesBean deviceId,int type){
        if (getPerosnPath==null){
            EventBus.getDefault().post("后台地址不正确");
            return;
        }
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        JSONObject objectParams = new JSONObject();
        JSONObject object_put = new JSONObject();
        try {
            object.put("device_sn", baoCunBean.getTuisongDiZhi()+"");
            object.put("token", baoCunBean.getXgToken()+"");
            object.put("Hid", deviceId.getParams().getHid());
            JSONArray array=new JSONArray();
            for (String s:deviceId.getParams().getIds()){
                array.put(s);
            }
            object.put("Ids",array);

            object_put.put("Hid", deviceId.getParams().getHid());
            object_put.put("Ids",array);
            objectParams.put("params",object_put);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MianBanJiActivity3", "objectParams:" + objectParams);
        Log.d("AllConnects", deviceId.getPath()+"\n"+object.toString());
        if (type==2){
            ZhiLingBean logingBean = new ZhiLingBean();
            logingBean.setJsonObject(objectParams);
            logingBean.setCode(-99999);
            linkedBlockingQueue.offer(logingBean);
            return;
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(getPerosnPath);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
                EventBus.getDefault().post("网络请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("AllConnects", "token:"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    ZhiLingBean logingBean = gson.fromJson(jsonObject, ZhiLingBean.class);
                    logingBean.setJsonObject(objectParams);
                    if (logingBean.getCode()==0 && logingBean.isSuccess()){
                        linkedBlockingQueue.offer(logingBean);

                    }else {
                        //登录失败
                        EventBus.getDefault().post("新增人员失败");
                    }
                }catch (Exception e){
                    EventBus.getDefault().post("新增人员失败"+e.getMessage());
                }

            }
        });
    }


    //同步人员
    private void link_sync_person(List<String> successfulList,List<FailedPersonBean> failedPersonBeanList,JSONObject jsonObject){
        if (perosnNotifyPath==null){
            EventBus.getDefault().post("后台地址不正确");
            return;
        }
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        try {
            object.put("device_sn", baoCunBean.getTuisongDiZhi()+"");
            object.put("token", baoCunBean.getXgToken()+"");
           // object.put("Hid", deviceId.getParams().getHid());
            JSONArray s_array=new JSONArray();
            for (String s:successfulList){
                s_array.put(s);
            }
            object.put("successful",s_array);
            ///////////////////////////////////////////////
            JSONArray f_array=new JSONArray();
            for (FailedPersonBean f:failedPersonBeanList){
                JSONObject fo = new JSONObject();
                fo.put("person_id",f.getPerson_id());
                fo.put("reason",f.getReason());
                f_array.put(fo);
            }
            object.put("failed",f_array);
            object.put("params",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("提交新增人员状态", perosnNotifyPath+"\n"+object.toString());
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(perosnNotifyPath);
        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
                EventBus.getDefault().post("网络请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("AllConnects", "人员同步:"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    LogingBean logingBean = gson.fromJson(jsonObject, LogingBean.class);
                    if (logingBean.getCode()==0 && logingBean.isSuccess()){
                        //登录成功

                    }else {
                        //登录失败
                        EventBus.getDefault().post("同步失败");
                    }

                }catch (Exception e){
                    EventBus.getDefault().post("同步失败"+e.getMessage());
                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                }

            }
        });
    }
}
