package com.rdypda.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chen on 2018/4/25.
 */

public class SharePreferenceUtil {
    public static final String DATA_TYPE_WL = "wl";
    public static final String DATA_TYPE_WL_TMBH = "tmbh";
    public static final String DATA_TYPE_WL_BZSL = "bzsl";
    public static final String DATA_TYPE_WL_TMSL = "tmsl";
    private Context context;

    public SharePreferenceUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取物料信息
     * @return
     */
    public Map<String,String> getWl(){
        Map<String,String> map = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences(DATA_TYPE_WL, Context.MODE_PRIVATE);
        map.put(DATA_TYPE_WL_TMBH,preferences.getString(DATA_TYPE_WL_TMBH,""));
        map.put(DATA_TYPE_WL_BZSL,preferences.getString(DATA_TYPE_WL_BZSL,""));
        map.put(DATA_TYPE_WL_TMSL,preferences.getString(DATA_TYPE_WL_TMSL,""));
        if (map.get(DATA_TYPE_WL_BZSL).equals("")){
            map = null;
        }
        return map;
    }

    /**
     * 保存物料信息
     * @param wl
     */
    public void saveWl(Map<String,String> wl){
        SharedPreferences preferences = context.getSharedPreferences(DATA_TYPE_WL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATA_TYPE_WL_TMBH,wl.get(DATA_TYPE_WL_TMBH));
        editor.putString(DATA_TYPE_WL_BZSL,wl.get(DATA_TYPE_WL_BZSL));
        editor.putString(DATA_TYPE_WL_TMSL,wl.get(DATA_TYPE_WL_TMSL));
        editor.commit();
    }

    /**
     * 清除
     */
    public void clear() {
        SharedPreferences preferences = context.getSharedPreferences(DATA_TYPE_WL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
