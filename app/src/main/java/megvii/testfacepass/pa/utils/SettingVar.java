package megvii.testfacepass.pa.utils;






/**
 * Created by wangzhiqiang on 2017/11/22.
 */

public class SettingVar {
    public static boolean cameraFacingFront = true;
    public static int faceRotation = 0;
    public static int faceRotation2 = 0;
    public static boolean isSettingAvailable = true;
    public static int cameraPreviewRotation = 0;
    public static int cameraPreviewRotation2 = 0;
    public static int msrBitmapRotation = 0;
    public static boolean isCross = false;
    public static String SharedPrefrence = "user";
    public static int mHeight;
    public static int mWidth;
    public static int cameraId=0;
    public static boolean cameraSettingOk = true;
    public static boolean iscameraNeedConfig = true;
    public static boolean isButtonInvisible = true;

    public  static int getCaneraID(){

        return cameraFacingFront?0:1;
    }

}
