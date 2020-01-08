package megvii.testfacepass.pa.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class GetCpuState {
    private static final String TAG = "GetCpuState";
    private static final String MEM_INFO_PATH = "/proc/meminfo";
    public static final String MEMTOTAL = "MemTotal";
    public static final String MEMFREE = "MemFree";



    public static float getRate(){
        Map<String,String> map1 = getMap();//采样第一次CPU信息快照
        long totalTime1 = Long.parseLong(map1.get("user")) + Long.parseLong(map1.get("nice"))
                + Long.parseLong(map1.get("system")) + Long.parseLong(map1.get("idle"))
                + Long.parseLong(map1.get("iowait")) + Long.parseLong(map1.get("irq"))
                + Long.parseLong(map1.get("softirq"));//获取totalTime1
        long idleTime1 = Long.parseLong(map1.get("idle"));//获取idleTime1
        try{
            Thread.sleep(360);//等待360ms
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String,String> map2 = getMap();//采样第二次CPU快照
        long totalTime2 = Long.parseLong(map2.get("user")) + Long.parseLong(map2.get("nice"))
                + Long.parseLong(map2.get("system")) + Long.parseLong(map2.get("idle"))
                + Long.parseLong(map2.get("iowait")) + Long.parseLong(map2.get("irq"))
                + Long.parseLong(map2.get("softirq"));//获取totalTime2
        long idleTime2 = Long.parseLong(map2.get("idle"));//获取idleTime2
        float cpuRate = 100*((totalTime2-totalTime1)-(idleTime2-idleTime1))/(totalTime2-totalTime1);
        return cpuRate;
    }

    //采样CPU信息快照的函数，返回Map类型
    public static Map<String,String> getMap(){
        String[] cpuInfos = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")));//读取CPU信息文件
            String load = br.readLine();
            br.close();
            cpuInfos = load.split(" ");
        }catch (IOException e){
            e.printStackTrace();
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("user",cpuInfos[2]);
        map.put("nice",cpuInfos[3]);
        map.put("system",cpuInfos[4]);
        map.put("idle",cpuInfos[5]);
        map.put("iowait",cpuInfos[6]);
        map.put("irq",cpuInfos[7]);
        map.put("softirq",cpuInfos[8]);
        return map;
    }

    /**
     *   * 获取android当前可用运行内存大小
     *   * @param context
     *   *
     */
    public static Float getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
// mi.availMem; 当前系统的可用内存
        return Float.valueOf(Formatter.formatFileSize(context, mi.availMem).replace("GB",""));// 将获取的内存大小规格化
    }




    /**
     *   * 获取android总运行内存大小
     *   * @param context
     *   *
     */
    public static Float getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            //int值乘以1024转换为long类型
            initial_memory = new Long((long)i*1024);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return Float.valueOf(Formatter.formatFileSize(context, initial_memory).replace("GB",""));// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        DecimalFormat decimalFormat =new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足
        return decimalFormat.format((availableBlocks * blockSize)/1024.0/1024.0/1024.0);

    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        DecimalFormat decimalFormat =new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足
        return decimalFormat.format((totalBlocks * blockSize)/1024.0/1024.0/1024.0);
    }

}
