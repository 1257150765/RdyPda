package com.rdypda.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DengJf on 2018/1/26.
 */

public class QrCodeUtil {
    String qrcode;
    JSONObject object;
    String cpxh;//产品型号
    String scpc;//生产批次
    String tmxh;//条码序号
    String bzsl;//包装数量
    String dw;//单位

    public QrCodeUtil(String qrcode) {
        this.qrcode = qrcode;
        object=new JSONObject();
        String[] items=qrcode.split("\\*");
        for (int i=0;i<items.length;i++){
            if (items[i].length()<2)
                break;
            String key=items[i].substring(0,2);
            String values=items[i].substring(2,items[i].length());
            try {
                object.put(key,values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 物料编号
     * @return
     */
    public String getWlbh() {
        try {
            return object.getString("PN");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 生产批次
     * @return
     */
    public String getScpc() {
        try {
            return object.getString("LT");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 条码号
     * @return
     */
    public String getTmxh() {
        try {
            return object.getString("BR");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 包装数量
     * @return
     */
    public String getBzsl() {
        try {
            return object.getString("QY");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 单位
     * @return
     */
    public String getDw() {
        try {
            return object.getString("UT");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 供应商代码
     * @return
     */
    public String getGysdm() {
        try {
            return object.getString("SP");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }
    /**
     * 收货单号
     * @return
     */
    public String getShdh() {
        try {
            return object.getString("GN");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getScrq() {
        try {
            return object.getString("PD");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getDdbh() {
        try {
            return object.getString("SO");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Deprecated
    public String getGDH() {
        try {
            return object.getString("SO");
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
