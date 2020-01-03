package megvii.testfacepass.pa.ui;

import android.app.Activity;


import android.content.Context;


import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;



import android.graphics.Bitmap;
import android.graphics.Typeface;


import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;




import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.FrameLayout;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.ai.access.entiry.YuvInfo;

import com.pingan.ai.access.manager.PaAccessControl;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.objectbox.Box;

import megvii.testfacepass.pa.MyApplication;
import megvii.testfacepass.pa.R;
import megvii.testfacepass.pa.beans.BaoCunBean;
import megvii.testfacepass.pa.beans.Subject;

import megvii.testfacepass.pa.camera.CameraManager;
import megvii.testfacepass.pa.camera.CameraManager2;
import megvii.testfacepass.pa.camera.CameraPreview;
import megvii.testfacepass.pa.camera.CameraPreview2;
import megvii.testfacepass.pa.camera.CameraPreviewData;
import megvii.testfacepass.pa.camera.CameraPreviewData2;

import megvii.testfacepass.pa.dialog.RegisterDialog;


import megvii.testfacepass.pa.utils.NV21ToBitmap;
import megvii.testfacepass.pa.utils.SettingVar;



public class YuLanActivity extends Activity implements CameraManager.CameraListener ,CameraManager2.CameraListener2{

    private enum FacePassSDKMode {
        MODE_ONLINE,
        MODE_OFFLINE
    }
    private int pp = 0;
    private static FacePassSDKMode SDK_MODE = FacePassSDKMode.MODE_OFFLINE;
    private static final String DEBUG_TAG = "FacePassDemo";
    private static final int MSG_SHOW_TOAST = 1;
    private static final int DELAY_MILLION_SHOW_TOAST = 2000;
    /* 人脸识别Group */
    private static final String group_name = "facepasstestx";
    RegisterDialog registerDialog;

  /* SDK 实例对象 */
  //  FacePassHandler mFacePassHandler;

    /* 相机实例 */
    private CameraManager manager;

    /* 显示人脸位置角度信息 */
    private TextView faceBeginTextView;

    /* 显示faceId */
    private TextView faceEndTextView;

    /* 相机预览界面 */
    private CameraPreview cameraView;

    private boolean isLocalGroupExist = true;
    private Bitmap msrBitmap = null;
    /* 在预览界面圈出人脸 */
   // private FaceView faceView;
    private static String faceId = "";
    private long feature2 = -1;
    private NV21ToBitmap nv21ToBitmap;
    private ScrollView scrollView;

    /* 相机是否使用前置摄像头 */
    private static boolean cameraFacingFront = true;
    /* 相机图片旋转角度，请根据实际情况来设置
     * 对于标准设备，可以如下计算旋转角度rotation
     * int windowRotation = ((WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
     * Camera.CameraInfo info = new Camera.CameraInfo();
     * Camera.getCameraInfo(cameraFacingFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK, info);
     * int cameraOrientation = info.orientation;
     * int rotation;
     * if (cameraFacingFront) {
     *     rotation = (720 - cameraOrientation - windowRotation) % 360;
     * } else {
     *     rotation = (windowRotation - cameraOrientation + 360) % 360;
     * }
     */
    private int cameraRotation;

    private static final int cameraWidth = 1280;
    private static final int cameraHeight = 720;


    private int heightPixels;
    private int widthPixels;

    int screenState = 0;// 0 横 1 竖

    /* 网络请求队列*/
   // RequestQueue requestQueue;


    Button visible;
    LinearLayout ll;
    FrameLayout frameLayout;
    private int buttonFlag = 0;
    /*Toast 队列*/
    LinkedBlockingQueue<Toast> mToastBlockQueue;
    /*DetectResult queue*/
    ArrayBlockingQueue<byte[]> mDetectResultQueue;
   // ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;
    /*recognize thread*/
  //  RecognizeThread mRecognizeThread;

   // FeedFrameThread mFeedFrameThread;
    /*底库同步*/
    private Button mSyncGroupBtn;
   // private AlertDialog mSyncGroupDialog;

    private Button mFaceOperationBtn;
    /*图片缓存*/

    private Handler mAndroidHandler;
    private PaAccessControl paAccessControl;
    private Box<BaoCunBean> baoCunBeanDao = null;
    private BaoCunBean baoCunBean = null;
    private Box<Subject> subjectBox = null;
    private String tupianFaceId=null;
    private String shipinFaceId=null;
    private static boolean isLink=false;
    private CameraManager2 manager2;
   // private PaAccessControl paFacePass;
    private CameraPreview2 cameraView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToastBlockQueue = new LinkedBlockingQueue<>();
        mDetectResultQueue = new ArrayBlockingQueue<byte[]>(5);
       // mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);
        //initAndroidHandler();
        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
        baoCunBean = baoCunBeanDao.get(123456L);
        subjectBox=MyApplication.myApplication.getSubjectBox();
      //  mFacePassHandler=MyApplication.myApplication.getFacePassHandler();
        nv21ToBitmap = new NV21ToBitmap(YuLanActivity.this);
        isLink=false;
        /* 初始化界面 */
        initView();

        manager.open(getWindowManager(), SettingVar.cameraId, cameraWidth, cameraHeight);//前置是1
        if (SettingVar.cameraId==1){
            manager2.open(getWindowManager(), 0, cameraWidth, cameraHeight, SettingVar.cameraPreviewRotation2);//最后一个参数是红外预览方向
        }else {
            manager2.open(getWindowManager(), 1, cameraWidth, cameraHeight, SettingVar.cameraPreviewRotation2);//最后一个参数是红外预览方向
        }
    }



    @Override
    protected void onResume() {
        //checkGroup();
       // initToast();
        /* 打开相机 */
       // manager.open(getWindowManager(), SettingVar.cameraId, cameraWidth, cameraHeight);
       // paFacePass.startFrameDetect();
     //   adaptFrameLayout();
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();

    }



    private YuvInfo rgb, ir;
    /* 相机回调函数 */
    @Override
    public void onPictureTaken(final CameraPreviewData cameraPreviewData) {


    }


    private void initView() {

        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
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



        Log.i("orientation", String.valueOf(windowRotation));
        final int mCurrentOrientation = getResources().getConfiguration().orientation;

        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }
        setContentView(R.layout.activity_yulan);

        mSyncGroupBtn = findViewById(R.id.btn_group_name);


        mFaceOperationBtn = findViewById(R.id.btn_face_operation);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
        /* 初始化界面 */
       // faceEndTextView = (TextView) this.findViewById(R.id.tv_meg2);
       // faceEndTextView.setTypeface(tf);
       Button fanhui = findViewById(R.id.fanhui);
       fanhui.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        SettingVar.cameraSettingOk = false;

        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
       // frameLayout = (FrameLayout) findViewById(R.id.frame);
        /* 注册相机回调函数 */
        manager.setListener(this);

        manager2 = new CameraManager2();
        cameraView2 = findViewById(R.id.preview2);
        manager2.setPreviewDisplay(cameraView2);
        /* 注册相机回调函数 */
        manager2.setListener(this);
    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onRestart() {
       // faceView.clear();
       // faceView.invalidate();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {

        if (manager2 != null) {
            manager2.release();
        }
        if (manager != null) {
            manager.release();
        }

        super.onDestroy();
    }


    /* 相机回调函数 */
    @Override
    public void onPictureTaken2(CameraPreviewData2 cameraPreviewData) {
        /* 如果SDK实例还未创建，则跳过 */
        // Log.d("MianBanJiActivity3", "cameraPreviewData2.rotation:" + cameraPreviewData.front);
//        if (paAccessControl == null) {
//            return;
//        }
//        //  paAccessControl.offerFrameBuffer(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height,SettingVar.faceRotation, SettingVar.getCaneraID());
//        try {
//            ir = new YuvInfo(cameraPreviewData.nv21Data, cameraPreviewData.front, 270, cameraPreviewData.width, cameraPreviewData.height);
//            if (rgb == null || !baoCunBean.isHuoTi())
//                return;
//            int result = paAccessControl.offerIrFrameBuffer(rgb, ir);//提供数据到队列
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Log.d("MianBanJiActivity3", "cameraPreviewData.result:" + result);
        /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
    }





}
