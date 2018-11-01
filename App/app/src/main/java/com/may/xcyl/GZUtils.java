package com.may.xcyl;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.regex.Pattern;


/**
 * ========================================================
 * User: GZ
 * Name: GZUtils
 * Date: 2018/10/30.
 * Info: 整合工具类
 * ========================================================
 */

public class GZUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "gz_save_file";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){
        String type = "";
        try {
            type = object.getClass().getSimpleName();
        }catch (Exception e){
            e.printStackTrace();
        }
           SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }else {
            String filename = key+".out";
            FileOutputStream fos=null;
            ObjectOutputStream os = null;
            try {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                os = new ObjectOutputStream(fos);
                os.writeObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(os!=null){
                    try {
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        editor.commit();
    }
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }else {
            String filename = key+".out";
            FileInputStream fis=null;
            ObjectInputStream oin=null;
            try {
                fis = context.openFileInput(filename);
                oin= new ObjectInputStream(fis);
                Object obj = oin.readObject();
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fis!=null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (oin!=null) {
                    try {
                        oin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return defaultObject;
    }

    /**
     * 格式化数字，超过10000显示1万
     * @param actualNumber//实际数目
     * @param Standard//标准单位
     * @return
     */
    public static String setPlanning(int actualNumber,String Standard){
        String Snumber = "";
        if (actualNumber <= 0) {
            Snumber = "0";
        } else if (actualNumber < 10000) {
            Snumber = actualNumber + "";
        } else {
            double d = (double) actualNumber;
            double num = d / 10000;//1.将数字转换成以万为单位的数字
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();//2.
            Snumber = f1 + Standard;
        }
        return  Snumber;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str null、“ ”、“null”都返回true
     */
    public static boolean isNullString(String str) {
        return (null == str || GZUtils.isBlank(str.trim()) || "null"
                .equals(str.trim().toLowerCase())) ? true : false;
    }
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断是否为数字
     */
    // 用JAVA自带的函数
    public static boolean isNumericC(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为数字
     */
    // 用正则表达式
    public static boolean isNumericP(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为数字
     */
    // 用ascii码
    public static boolean isNumericA(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    /**
     * 返回字符串长度
     */

    public static int getStrCharacterCount(String str) {
        int icount = 0;
        String chinese = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
        Pattern pattern = Pattern.compile(chinese);
        int ilenth = str.length();
        for (int i = 0; i < ilenth; i++) {
            String substr = str.substring(i, i + 1);
            boolean tf = pattern.matcher(substr).matches();
            if (tf == true) {
                icount += 2;
            } else {
                icount++;
            }
        }
        return icount;
    }

    /**
     * 手机号码隐藏中间4位
     */
    public static String getPhone(String s) {
        return s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    /**
     * 复制文本
     */
    public static void copyOnClipboard(Context context, String source) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", source);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context,"已复制",Toast.LENGTH_LONG).show();
    }

    /**
     * 获取通知栏颜色
     */
    public static int getNotificationColor(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        int layoutId = notification.contentView.getLayoutId();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
        if (viewGroup.findViewById(android.R.id.title) != null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }
    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context mContext)
    {
        try {
            ActivityManager am = (ActivityManager) mContext
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            return cn.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
