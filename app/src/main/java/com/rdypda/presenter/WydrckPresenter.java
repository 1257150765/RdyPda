package com.rdypda.presenter;

import android.content.Context;

import com.rdypda.model.network.WebService;
import com.rdypda.util.QrCodeUtil;
import com.rdypda.util.ScanUtil;
import com.rdypda.view.activity.WydrckActivity;
import com.rdypda.view.viewinterface.IWydrckView;

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
 * Created by DengJf on 2018/3/15.
 */

public class WydrckPresenter extends BasePresenter {
    public static final int SCAN_TYPE_GDH = 1;
    public static final int SCAN_TYPE_TMBH = 2;
    private static final String TAG = WydrckActivity.class.getSimpleName();
    private String gdh = "";
    private IWydrckView view;
    private ScanUtil scanUtil;
    private String ftyIdAndstkId=";";
    private int startType=0;
    private int scanType = 0;
    private String sh = "";//送货单号（来料扫描有此参数）


    public WydrckPresenter(Context context, final IWydrckView view) {
        super(context);
        this.view=view;
        scanUtil=new ScanUtil(context);
        scanUtil.open();
        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void onSuccess(String result) {
                if (startType==WydrckActivity.START_TYPE_WYDRK |startType==WydrckActivity.START_TYPE_WYDCK || startType == WydrckActivity.START_TYPE_LLCKSM|| startType == WydrckActivity.START_TYPE_LLRKSM){
                    String tmbh=new QrCodeUtil(result).getTmxh();
                    view.setTmEd(tmbh);
                    isValidCode(tmbh,result);
                }else if (startType==WydrckActivity.START_TYPE_GDTH || startType == WydrckActivity.START_TYPE_GDSH){
                    if (scanType == SCAN_TYPE_TMBH) {
                        String tmbh = new QrCodeUtil(result).getTmxh();
                        //view.setTmEd(tmbh);
                        isValidCode(tmbh,result);
                    }else if (scanType == SCAN_TYPE_GDH){
                        //String gdh = new QrCodeUtil(result).getGDH();
                        //view.setGdhEd(gdh);
                        isValidGDH(result);
                    }
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        getKc();
    }

    //验证工单号
    public void isValidGDH(final String gdh) {
        if (gdh.equals("")){
            view.showMsgDialog("请先输入工单号");
            return;
        }
        /*//如果是工单收货才需要接收库位
        if (startType == WydrckActivity.START_TYPE_GDSH) {
            if (ftyIdAndstkId.equals(";")) {
                view.showMsgDialog("请先选择接收库位");
                return;
            }
        }
        String[]kw=ftyIdAndstkId.split(";");
        if (kw.length<2){
            view.showMsgDialog("接收库位解析失败");
            return;
        }*/
        view.setShowProgressDialogEnable(true);

        String sql=String.format("Call Proc_PDA_SoValid('%s');", gdh);
        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                try {
                    String result = value.getJSONArray("Table1").getJSONObject(0).getString("cRetMsg");
                    if (result.equals("OK")){
                        WydrckPresenter.this.gdh = gdh;
                        view.onQueryGdhSucceed(gdh);
                        //view.showMsgDialog("工单号验证成功");
                    }
                    //Log.d(TAG, "onNext: result"+result);
                    //String .put("sl",value.getJSONObject(0).getString("brp_Qty"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.showMsgDialog(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                view.setShowProgressDialogEnable(false);
                view.showMsgDialog(e.getMessage());
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取库存地点
     */
    public void getKc(){
        view.setShowProgressDialogEnable(true);
        String sql="Proc_PDA_GetKwmList();";
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                try {
                    JSONArray array=value.getJSONArray("Table1");
                    List<String> data=new ArrayList<>();
                    List<String>dataMc=new ArrayList<>();
                    data.add(";");
                    dataMc.add("");
                    for (int i=0;i<array.length();i++){
                        data.add(array.getJSONObject(i).getString("kwm_ftyid")+" ; "+
                                array.getJSONObject(i).getString("kwm_stkId")+" ; "+
                                array.getJSONObject(i).getString("kwm_kwdm")+" ; "+
                                array.getJSONObject(i).getString("kwm_cwdm"));
                        dataMc.add(array.getJSONObject(i).getString("kwm_kwmc")+","+
                                array.getJSONObject(i).getString("kwm_cwdm"));
                    }
                    view.refreshJskwSp(dataMc,data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.showMsgDialog(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.showMsgDialog(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 验证条码编号
     * @param tmbh
     *
     */
    public void isValidCode(String tmbh, String qrcode){
        if (startType == WydrckActivity.START_TYPE_LLRKSM){
            if ("".equals(sh)){
                view.showMsgDialog("请先输入送货单号");
                return;
            }
        }
        //如果是工单退货，工单退货 应该先验证工单号
        //20181129取消必填
        /*if (startType == WydrckActivity.START_TYPE_GDTH ||startType == WydrckActivity.START_TYPE_GDSH) {
            if (gdh.equals("")) {
                view.showMsgDialog("请先输入工单号");
                return;
            }
        }*/
        if (tmbh.equals("")){
            view.showMsgDialog("请先输入条码编号");
            return;
        }
        //工单收货 无源单入库 来料入库 都需要选择库位
        if ((startType == WydrckActivity.START_TYPE_WYDRK ||startType == WydrckActivity.START_TYPE_GDSH || startType == WydrckActivity.START_TYPE_LLRKSM )&ftyIdAndstkId.equals(";")){
            view.showMsgDialog("请先选择接收库位");
            return;
        }
        //工单收货 无源单入库 来料入库 都需要选择库位
        String[] kw = ftyIdAndstkId.split(";");
        if ((startType == WydrckActivity.START_TYPE_WYDRK ||startType == WydrckActivity.START_TYPE_GDSH ||startType == WydrckActivity.START_TYPE_LLRKSM)&kw.length<4){
            view.showMsgDialog("接收库位解析失败");
            return;
        }
        view.setShowProgressDialogEnable(true);
        String type = "";
        if (startType== WydrckActivity.START_TYPE_GDSH){//工单收货
            type = "GDSH";
        } else if (startType == WydrckActivity.START_TYPE_GDTH){//工单退货
            type = "GDTH";
        }else if (startType == WydrckActivity.START_TYPE_WYDCK){//无源单出库
            type = "CHWY";
        }else if (startType == WydrckActivity.START_TYPE_WYDRK){//无源单入库
            type = "RKWY";
        }else if (startType == WydrckActivity.START_TYPE_LLRKSM){//来料入库扫描
            type = "GNSH";
        }else if (startType == WydrckActivity.START_TYPE_LLCKSM){//来料出库扫描
            type = "GNTH";
        }
        String sql = "";
        //下面这么多接口  重点是type参数  接口都可以使用同一个,(Proc_PDA_IsValidCode2),这个是最新的接口，以前的不做调整
        //工单退货
        if (startType == WydrckActivity.START_TYPE_GDTH){
            sql = String.format("Call Proc_PDA_IsValidCode('%s','%s','%s','%s');",
                    tmbh,type,gdh,preferenUtil.getString("userId"));
            //工单收货 无源单入库  来料入库
        }else if ( startType == WydrckActivity.START_TYPE_WYDRK){
            sql = String.format("Call Proc_PDA_IsValidCode('%s','%s', '%s;%s;%s;%s' ,'%s');",
                    tmbh,type,kw[0].trim(),kw[1].trim(),kw[2].trim(),kw[3].trim(),preferenUtil.getString("userId"));
        //无源单出库  来料出库
        }else if (startType==WydrckActivity.START_TYPE_WYDCK || startType == WydrckActivity.START_TYPE_LLCKSM){
            sql = String.format("Call Proc_PDA_IsValidCode('%s','%s', '' ,'%s');",
                    tmbh,type,preferenUtil.getString("userId"));
        }else if (startType == WydrckActivity.START_TYPE_GDSH){
            sql = String.format("Call Proc_PDA_IsValidCode2('%s','%s', '%s;%s;%s;%s' ,'%s','%s','%s');",
                    tmbh,type,kw[0].trim(),kw[1].trim(),kw[2].trim(),kw[3].trim(),preferenUtil.getString("userId"),gdh,qrcode);
        }else if (startType == WydrckActivity.START_TYPE_LLRKSM){
            sql = String.format("Call Proc_PDA_IsValidCode2('%s','%s', '%s;%s;%s;%s' ,'%s','%s','%s');",
                    tmbh,type,kw[0].trim(),kw[1].trim(),kw[2].trim(),kw[3].trim(),preferenUtil.getString("userId"),sh,qrcode);
        }

        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                try {
                    JSONArray array=value.getJSONArray("Table2");
                    Map<String,String>mapScan=new HashMap<>();
                    Map<String,String>mapZs=new HashMap<>();
                    mapScan.put("tmbh",array.getJSONObject(0).getString("brp_Sn"));
                    mapScan.put("sl",array.getJSONObject(0).getString("brp_Qty"));
                    mapScan.put("wlbh",array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("sl",array.getJSONObject(0).getString("brp_Qty"));
                    mapZs.put("wlbh",array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("wlgg",array.getJSONObject(0).getString("brp_pmgg"));
                    view.addScanData(mapScan);
                    view.addZsData(mapZs);
                    view.setTmEd("");
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.showMsgDialog(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                view.setShowProgressDialogEnable(false);
                view.showMsgDialog(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onComplete() {

            }
        });
    }

    public void cancelScan(final String tmxh, final String wlbh, final String tmsl){
        view.setShowProgressDialogEnable(true);
        String type="";
        if (startType==WydrckActivity.START_TYPE_GDSH){//工单收货
            type = "GDSH";
        }else if (startType == WydrckActivity.START_TYPE_GDTH){//工单退货
            type = "GDTH";
        }else if (startType == WydrckActivity.START_TYPE_WYDCK){//无源单出库
            type = "CHWY";
        }else if (startType == WydrckActivity.START_TYPE_WYDRK){//无源单入库
            type = "RKWY";
        }else if (startType == WydrckActivity.START_TYPE_LLRKSM){//来料入库
            type = "GNSH";
        }else if (startType == WydrckActivity.START_TYPE_LLCKSM){//来料出库
            type = "GNTH";
        }
        String sql=String.format("Call Proc_PDA_CancelScan('%s', '%s', '%s');",type,tmxh,preferenUtil.getString("userId"));
        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                view.removeScanData(tmxh);
                view.removeZsData(wlbh,tmsl);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.showMsgDialog(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void closeScanUtil(){
        scanUtil.close();
    }

    public void setFtyIdAndstkId(String ftyIdAndstkId) {
        this.ftyIdAndstkId = ftyIdAndstkId;
    }

    /**
     * 设置是无源单出库，无源单入库，工单收货  工单退货  来料入库  来料出库
     * @param startType
     */
    public void setStartType(int startType) {
        this.startType = startType;
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }
}
