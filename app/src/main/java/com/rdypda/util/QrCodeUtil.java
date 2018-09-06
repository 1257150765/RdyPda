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
            //厂商来料的格式：名称>值 瑞多益格式：名称值（没有隔开）
            String values=items[i].substring(2,items[i].length()).replaceAll(">","");
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
            return "";
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
            return "";
        }
    }

    /**
     * 条码号
     * @return
     */
    public String getTmxh() {
        try {
            String br = object.getString("BR");
            if (br != null && !"".equals(br)){
                String gysdm = getGysdm();
                //如果供应商条码不为空  并且条码编号不以供应商条码开头 并且不是pda厂商来料打印的条码（GN） 则在条码前加上供应商代码
                if (!"".equals(gysdm) && !br.startsWith(gysdm) && !br.startsWith("GN")){
                    br = gysdm +br;
                }
            }
            return br;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
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
            return "";
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
            return "";
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
            return "";
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
            return "";
        }
    }

    public String getScrq() {
        try {
            return object.getString("PD");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取订单编号 瑞多益定义的订单编号是so开头，厂商来料的是PO开头
     * @return
     */
    public String getDdbh() {
        String so;
        String po;
        try {
            so = object.getString("SO");
            if (so == null|| "".equals(so)){
                 po = object.getString("PO");
                 return po;
            }else {
                return so;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Deprecated
    public String getGDH() {
        try {
            return object.getString("SO");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
