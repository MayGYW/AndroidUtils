package com.may.xcyl;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


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



}
