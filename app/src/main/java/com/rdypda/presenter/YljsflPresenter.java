package com.rdypda.presenter;

import android.content.Context;

import com.rdypda.model.network.WebService;
import com.rdypda.util.QrCodeUtil;
import com.rdypda.util.ScanUtil;
import com.rdypda.view.viewinterface.IYljsflView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by DengJf on 2018/1/31.
 */

public class YljsflPresenter extends BasePresenter{
    private IYljsflView view;
    private ScanUtil scanUtil;
    private String djbh,wldm;
    private int startType=0;
    private String kcdd="";

    public YljsflPresenter(Context context,IYljsflView view) {
        super(context);
        this.view=view;
        scanUtil=new ScanUtil(context);
        scanUtil.open();
        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void onSuccess(String result) {
                if (startType==MainPresenter.YLTL){
                    isValidCode(new QrCodeUtil(result).getTmxh(),"MTR_OUT",kcdd);
                }else if (startType==MainPresenter.YLJS){
                    isValidCode(new QrCodeUtil(result).getTmxh(),"MTR_IN",kcdd);
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    //获取接收库位
    public void getKwmList(){
        view.setShowProgressDialogEnable(true);
        String sql="Call Proc_PDA_GetStkList();";
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray array=value.getJSONArray("Table1");
                    List<String>data=new ArrayList<>();
                    List<String>dataDm=new ArrayList<>();
                    dataDm.add("");
                    data.add("");
                    for (int i=0;i<array.length();i++){
                        String dh=array.getJSONObject(i).getString("stk_ftyId")+";"+
                                array.getJSONObject(i).getString("stk_stkId")+";"+
                                array.getJSONObject(i).getString("stk_stkId")+";";
                        String mc=array.getJSONObject(i).getString("stk_stkmc");
                        data.add(mc);
                        dataDm.add(dh);
                    }
                    view.refreshKcddSp(data,dataDm);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowDialogMsg("Json解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                view.setShowProgressDialogEnable(false);
                view.setShowDialogMsg(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //刷新已接收物料
    public void getLldDet(String djbh,String wldm){
        this.djbh=djbh;
        this.wldm=wldm;
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_Get_lld_det('%s','%s')",djbh,wldm);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray array=value.getJSONArray("Table1");
                    List<Map<String,String>>data=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        Map<String,String>map=new HashMap<>();
                        map.put("djbh",array.getJSONObject(i).getString("lld_djbh"));
                        map.put("llm_wldm",array.getJSONObject(i).getString("lld_llm_wldm"));
                        map.put("ftyId",array.getJSONObject(i).getString("lld_ftyId"));
                        map.put("stkId",array.getJSONObject(i).getString("lld_stkId"));
                        map.put("wldm",array.getJSONObject(i).getString("lld_wldm"));
                        map.put("wlpm",array.getJSONObject(i).getString("itm_wlpm"));
                        map.put("ywwlpm",array.getJSONObject(i).getString("itm_ywwlpm"));
                        map.put("qty",array.getJSONObject(i).getString("lld_nr_qty"));
                        map.put("unit",array.getJSONObject(i).getString("lld_unit"));
                        map.put("ni_qty",array.getJSONObject(i).getString("lll_qty_v2"));
                        data.add(map);
                    }
                    view.refreshWldRecycler(data);
                } catch (JSONException e) {
                    view.setShowDialogMsg("Json解析出错");
                    e.printStackTrace();
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowDialogMsg(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //条码验证，原料接收，原料退料
    public void isValidCode(String tmxh,String type,String kcdd){
        if (kcdd.equals("")&startType==MainPresenter.YLJS){
            view.setShowDialogMsg("请先选择库位");
            return;
        }
        if (tmxh.equals("")){
            view.setShowDialogMsg("条码序号有误，请重输");
            return;
        }
        view.setShowProgressDialogEnable(true);
        String sql;
        if (type.equals("MTR_OUT")){
            sql=String.format("Call Proc_PDA_IsValidCode ('%s','%s', '%s', '%s');",tmxh,type,djbh,preferenUtil.getString("userId"));
        }else {
            sql=String.format("Call Proc_PDA_IsValidCode ('%s','%s', '%s', '%s');",tmxh,type,kcdd,preferenUtil.getString("userId"));
        }
        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray array=value.getJSONArray("Table2");
                    Map<String,String>map=new HashMap<>();
                    map.put("wlbh",array.getJSONObject(0).getString("brp_wldm"));
                    map.put("wlgg",array.getJSONObject(0).getString("brp_pmgg"));
                    map.put("tmsl",array.getJSONObject(0).getString("brp_Qty"));
                    map.put("tmbh",array.getJSONObject(0).getString("brp_Sn"));
                    view.addYljstlRecyclerItem(map);
                    //退料
                    if (startType==MainPresenter.YLTL){
                        getLldDet(djbh,wldm);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowDialogMsg("Json解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowDialogMsg(e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //取消扫描记录
    public void cancelScan(final String tmxh){
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_CancelScan('MTR_IN', '%s', '%s');",tmxh,preferenUtil.getString("userId"));
        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.removeYljstlRecyclerItem(tmxh);
                view.setShowProgressDialogEnable(false);
            }

            @Override
            public void onError(Throwable e) {
                view.setShowProgressDialogEnable(false);
                e.printStackTrace();
                view.setShowDialogMsg(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void closeScan(){
        scanUtil.close();
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public void setKcdd(String kcdd) {
        this.kcdd = kcdd;
    }

    public String getKcdd() {
        return kcdd;
    }
}
