package com.rdypda.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.rdypda.model.network.WebService;
import com.rdypda.util.PrinterUtil;
import com.rdypda.util.QrCodeUtil;
import com.rdypda.util.ScanUtil;
import com.rdypda.view.viewinterface.ICgrktmdyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Chen on 2018-07-18.
 */

public class CgrktmdyPresentor extends BasePresenter {
    private static final String TAG = PddyPresenter.class.getSimpleName();
    private ICgrktmdyView view;
    private ScanUtil scanUtil;
    public CgrktmdyPresentor(Context context, ICgrktmdyView view) {
        super(context);
        this.view=view;
        getKwData();
        /*scanUtil=new ScanUtil(context);
        scanUtil.open();
        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void onSuccess(String result) {
                barcodeQuery(new QrCodeUtil(result).getTmxh());
            }

            @Override
            public void onFail(String error) {

            }
        });*/
    }

    //查询条码
    /*public void barcodeQuery(String tmxh){
        if (tmxh.equals("")){
            view.setShowMsgDialogEnable("请先输入条码序号");
            return;
        }
        view.setTmxxMsg(null);
        view.refreshKcsw(new ArrayList<Map<String, String>>());
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_BarcodeQuery('%s')",tmxh);
        WebService.doQuerySqlCommandResultJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray arrayTmxx=value.getJSONArray("Table2");
                    JSONArray arrayKcsw=value.getJSONArray("Table3");
                    Map<String,String>tmxxMap=new HashMap<>();
                    tmxxMap.put("wjbh",arrayTmxx.getJSONObject(0).getString("brp_DocNo"));
                    tmxxMap.put("wldm",arrayTmxx.getJSONObject(0).getString("brp_wldm"));
                    tmxxMap.put("ph",arrayTmxx.getJSONObject(0).getString("brp_LotNo"));
                    tmxxMap.put("sl",arrayTmxx.getJSONObject(0).getString("brp_Qty")
                            +arrayTmxx.getJSONObject(0).getString("brp_Unit"));
                    tmxxMap.put("pmgg",arrayTmxx.getJSONObject(0).getString("brp_pmgg"));
                    tmxxMap.put("scrq",arrayTmxx.getJSONObject(0).getString("brp_Prd_Date"));
                    tmxxMap.put("gc",arrayTmxx.getJSONObject(0).getString("brp_FtyId"));
                    tmxxMap.put("kcdd",arrayTmxx.getJSONObject(0).getString("stk_stkmc"));
                    tmxxMap.put("dycs",arrayTmxx.getJSONObject(0).getString("brp_dycs"));
                    tmxxMap.put("qrcode",arrayTmxx.getJSONObject(0).getString("brp_QrCode"));
                    view.setTmxxMsg(tmxxMap);
                    List<Map<String,String>>kcswData=new ArrayList<>();
                    for (int i=0;i<arrayKcsw.length();i++){
                        Map<String,String>map=new HashMap<>();
                        map.put("swm",arrayKcsw.getJSONObject(i).getString("trx_yydm"));
                        map.put("swdh",arrayKcsw.getJSONObject(i).getString("trx_swdh"));
                        map.put("rq",arrayKcsw.getJSONObject(i).getString("trx_swrq"));
                        map.put("wldm",arrayKcsw.getJSONObject(i).getString("trx_wldm"));
                        map.put("ph",arrayKcsw.getJSONObject(i).getString("trx_LotNo"));
                        map.put("kcdd",arrayKcsw.getJSONObject(i).getString("trx_stkId"));
                        map.put("kw",arrayKcsw.getJSONObject(i).getString("trx_kwdm"));
                        map.put("cw",arrayKcsw.getJSONObject(i).getString("trx_cwdm"));
                        map.put("swqsl",arrayKcsw.getJSONObject(i).getString("trx_kcsl"));
                        map.put("swsl",arrayKcsw.getJSONObject(i).getString("trx_swsl"));
                        map.put("jysl",arrayKcsw.getJSONObject(i).getString("trx_sysl"));
                        map.put("czry",arrayKcsw.getJSONObject(i).getString("trx_jlrymc"));
                        map.put("czrq",arrayKcsw.getJSONObject(i).getString("trx_jlrq"));
                        map.put("dw",arrayTmxx.getJSONObject(0).getString("brp_Unit"));
                        kcswData.add(map);
                    }
                    view.refreshKcsw(kcswData);

                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }*/


    public void closeScan(){
        scanUtil.close();
    }

    public void queryWlbh(String wlbh) {
        if (wlbh.equals("")){
            view.setShowMsgDialogEnable("请先输入物料编号");
            return;
        }
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_Get_Item('%s','','')",wlbh);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                try {
                    List<Map<String,String>> wlbhData = new ArrayList<>();

                    JSONArray arrayWlxx=value.getJSONArray("Table1");
                    for (int i = 0; i<arrayWlxx.length(); i++){
                        Map<String,String> map = new HashMap<>();
                        JSONObject jsonObject = (JSONObject) arrayWlxx.get(i);
                        //物料编号
                        map.put("itm_wldm",jsonObject.getString("itm_wldm"));
                        //单位
                        map.put("itm_unit",jsonObject.getString("itm_unit"));
                        //物料规格
                        map.put("itm_wlgg",jsonObject.getString("itm_wlgg"));
                        //物料品名
                        map.put("itm_wlpm",jsonObject.getString("itm_wlpm"));
                        //物料英文品名
                        map.put("itm_ywwlpm",jsonObject.getString("itm_ywwlpm"));
                        wlbhData.add(map);
                    }
                    String wldmArr[] = new String[wlbhData.size()];
                    String dwArr[] = new String[wlbhData.size()];
                    String wlpmArr[] = new String[wlbhData.size()];
                    for (int i=0; i<wlbhData.size(); i++){
                        wldmArr[i] = wlbhData.get(i).get("itm_wldm");
                    }
                    view.onQueryWlbhSucceed(wldmArr,wlbhData);
                    view.setShowProgressDialogEnable(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取库位
     */
    public void getKwData() {
        view.setShowProgressDialogEnable(true);
        String sql="Call Proc_PDA_GetKwmList();";
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                try {
                    JSONArray array=value.getJSONArray("Table1");
                    List<Map<String,String>> data=new ArrayList<>();
                    List<String>dataMc=new ArrayList<>();
                    dataMc.add("");
                    for (int i=0;i<array.length();i++){
                        Map<String,String> map = new HashMap<>();
                        JSONObject item = array.getJSONObject(i);
                        map.put("kwm_ftyid", item.getString("kwm_ftyid"));
                        map.put("kwm_stkId", item.getString("kwm_stkId"));
                        map.put("kwm_kwdm", item.getString("kwm_kwdm"));
                        map.put("kwm_cwdm", item.getString("kwm_cwdm"));
                        data.add(map);
                        dataMc.add(item.getString("kwm_kwmc")+","+ item.getString("kwm_cwdm"));
                    }
                    view.onGetKwdataSucceed(dataMc,data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取条码
     * @param wlbh
     * @param scpc
     * @param bzsl
     * @param strDw
     * @param wlbh2
     * @param s
     * @param bzsl1
     * @param mapKw
     */


    /**
     *
     * @param qrCode 二维码
     * @param wlpmChinese 物料品名 wlgg
     * @param ddbh 订单编号
     * @param scpc 生产批次
     * @param beizhu 备注
     */
    public void printEvent(final String qrCode, final String wlpmChinese, final String ddbh, final String scpc, final String beizhu) {

        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()){
            view.showBlueToothAddressDialog();
            return;
        }
        if (preferenUtil.getString("blueToothAddress").equals("")){
            view.showBlueToothAddressDialog();
            return;
        }

        if (ddbh == null || "".equals(ddbh)){
            view.setShowMsgDialogEnable("请输入订单编号");
            return;
        }
        if (wlpmChinese == null || ",".equals(wlpmChinese)){
            view.setShowMsgDialogEnable("请先验证物料编号");
            return;
        }
        if (scpc == null || "".equals(scpc)){
            view.setShowMsgDialogEnable("请输入生产批次");
            return;
        }

        if (qrCode == null || "".equals(qrCode)){
            view.setShowMsgDialogEnable("请先获取条码序号");
            return;
        }

        view.setShowProgressDialogEnable(true);
        final PrinterUtil util=new PrinterUtil(context);
        final QrCodeUtil qrCodeUtil = new QrCodeUtil(qrCode);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String address=preferenUtil.getString("blueToothAddress");
                int startX = 30;
                int distance = 37;
                int startY = 15;
                util.openPort2(address);
                util.printFont("物料编号:"+qrCodeUtil.getWlbh(),startX,(startY+distance*0));
                util.printFont("供应商代码:"+qrCodeUtil.getGysdm(),300,(startY+distance*0));
                util.printFont("物料规格:"+wlpmChinese+"",startX,(startY+distance*1));
                //util.printFont(wlgg+" ",15,140);
                util.printFont("订单编号:"+qrCodeUtil.getDdbh(),startX,(startY+distance*2));
                util.printFont("生产批次:"+scpc,startX,(startY+distance*3));
                util.printFont("生产日期:"+qrCodeUtil.getScrq(),startX,(startY+distance*4));
                util.printFont("包装数量:"+qrCodeUtil.getBzsl()+qrCodeUtil.getDw(),startX,(startY+distance*5));
                util.printFont("条码编号:"+qrCodeUtil.getTmxh(),startX,(startY+distance*6));
                //util.printFont("备注:"+beizhu,startX,(startY+distance*7));
                util.printQRCode(qrCode,380,(startY+distance*3),4);
                util.startPrint();
                Log.e("printMsg",qrCode);
                e.onNext("");
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                //view.showMessage("打印完成");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable("打印出错！");
            }
            @Override
            public void onComplete() {
                view.setShowProgressDialogEnable(false);
            }
        });
    }

    //入库
    public void rk(String tmbh,Map<String,String> mapKw) {
        if ("".equals(tmbh)){
            view.setShowMsgDialogEnable("请获取条码");
            return;
        }
        view.setShowProgressDialogEnable(true);
        String sql = String.format("Call Proc_PDA_IsValidCode('%s','%s', '%s;%s;%s;%s' ,'%s');",
                tmbh,"GNSH",mapKw.get("kwm_ftyid").trim(),mapKw.get("kwm_stkId").trim(),mapKw.get("kwm_kwdm").trim(),mapKw.get("kwm_cwdm").trim().trim(),preferenUtil.getString("userId"));
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
                    if (array.length() > 0) {
                        mapScan.put("tmbh", array.getJSONObject(0).getString("brp_Sn"));
                        view.onRkSucceed();
                    }
                    /*mapScan.put("sl",array.getJSONObject(0).getString("brp_Qty"));
                    mapScan.put("wlbh",array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("sl",array.getJSONObject(0).getString("brp_Qty"));
                    mapZs.put("wlbh",array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("wlgg",array.getJSONObject(0).getString("brp_pmgg"));
                    view.addScanData(mapScan);
                    view.addZsData(mapZs);
                    view.setTmEd("");*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onComplete() {

            }
        });
    }


    public void checkGysdm(String gysdm) {
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_Get_Supp('%s')",gysdm);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray item=value.getJSONArray("Table1");
                    if (item.length() > 0){
                        String supp_no = item.getJSONObject(0).getString("supp_no");
                        view.onCheckGysdmSucceed(true,supp_no);
                    }else {
                        view.setShowMsgDialogEnable("供应商验证失败,请重新输入");
                        view.onCheckGysdmSucceed(false,"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });
    }

    public void checkDdbh(String ddbh) {
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_Get_mom_Info('%s')",ddbh);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray item=value.getJSONArray("Table1");
                    if (item.length() > 0){
                        String mom_zzdh = item.getJSONObject(0).getString("mom_zzdh");
                        view.onCheckDdbhmSucceed(true,mom_zzdh);
                    }else {
                        view.setShowMsgDialogEnable("订单编号验证失败,请重新输入");
                        view.onCheckDdbhmSucceed(false,"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });
    }

    public void checkShdh(String shdh) {
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_PDA_Get_Grn_Info('%s')",shdh);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(JSONObject value) {
                try {
                    JSONArray item=value.getJSONArray("Table1");
                    if (item.length() > 0){
                        String grdNo = item.getJSONObject(0).getString("GrdNo");
                        view.onCheckShdhmSucceed(true,grdNo);
                    }else {
                        view.setShowMsgDialogEnable("收货单号验证失败,请重新输入");
                        view.onCheckShdhmSucceed(false,"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });
    }

    public void getBarCode(String gysdm2, String shdh, String ddbh, String bz, String wlbh, String scpc, String bzsl, Map<String, String> mapKw) {
        if (gysdm2.equals("")){
            view.setShowMsgDialogEnable("请先输入供应商代码");
            return;
        }
        if (shdh.equals("")){
            view.setShowMsgDialogEnable("请先输入收货单号");
            return;
        }
        if (ddbh.equals("")){
            view.setShowMsgDialogEnable("请先输入订单编号");
            return;
        }
        if (wlbh.equals("")){
            view.setShowMsgDialogEnable("请先输入物料编号");
            return;
        }
        if (scpc.equals("")){
            view.setShowMsgDialogEnable("请先输入生产批次");
            return;
        }
        if (bzsl.equals("")){
            view.setShowMsgDialogEnable("请先输入包装数量");
            return;
        }
        if (mapKw == null){
            view.setShowMsgDialogEnable("请先选择原料库位");
            return;
        }
        view.setShowProgressDialogEnable(true);
        String sql=String.format("Call Proc_GenQrcode5 ('BRP','GN', '%s', '%s', '%s', '%s', '%s', 'KG','%s', '%s', '', '', '', '%s', '', '%s');"
                ,ddbh,shdh,wlbh,scpc,bzsl,gysdm2,mapKw.get("kwm_ftyid"),preferenUtil.getString("usr_yhdm"),bz);
        WebService.querySqlCommandJson(sql,preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(JSONObject value) {
                try {
                    String[] item=value.getJSONArray("Table1").getJSONObject(0).getString("cRetMsg").split(":");
                    if (item.length>1){
                        String[]itemItem=item[1].split(";");
                        if (itemItem.length>1){
                            view.onGetBarCodeSucceed(itemItem[0],itemItem[1]);
                        }else {
                            view.setShowMsgDialogEnable(item[1]);
                        }
                    }else {
                        view.setShowMsgDialogEnable("数据解析出错");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.setShowMsgDialogEnable("Json数据解析出错");
                }finally {
                    view.setShowProgressDialogEnable(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.setShowProgressDialogEnable(false);
                view.setShowMsgDialogEnable(e.getMessage());
            }
            @Override
            public void onComplete() {

            }
        });
    }
}
