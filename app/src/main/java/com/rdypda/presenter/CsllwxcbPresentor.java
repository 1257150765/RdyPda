package com.rdypda.presenter;

import android.content.Context;

import com.rdypda.model.network.WebService;
import com.rdypda.util.QrCodeUtil;
import com.rdypda.util.ScanUtil;
import com.rdypda.view.activity.WydrckActivity;
import com.rdypda.view.viewinterface.ICsllwxcbView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chen on 2018-09-03.
 */

public class CsllwxcbPresentor extends BasePresenter {
    private ICsllwxcbView view;
    private final ScanUtil scanUtil;
    public CsllwxcbPresentor(Context context, ICsllwxcbView view) {
        super(context);
        this.view = view;
        scanUtil=new ScanUtil(context);
        scanUtil.open();
        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void onSuccess(String result) {
                isValidCode(result);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public void isValidCode(String qrcode) {
        QrCodeUtil qrCodeUtil = new QrCodeUtil(qrcode);
        String sql = String.format("Call Proc_PDA_IsValidCode2('%s','%s', '%s;%s;%s;%s' ,'%s','%s','%s');",
                qrCodeUtil.getTmxh(), "WX_SPLIT", "", "", "", "", preferenUtil.getString("userId"), "", qrcode);


        WebService.doQuerySqlCommandResultJson(sql, preferenUtil.getString("usr_Token")).subscribe(new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject value) {
                view.setShowProgressDialogEnable(false);
                try {
                    JSONArray array = value.getJSONArray("Table2");
                    Map<String, String> mapScan = new HashMap<>();
                    Map<String, String> mapZs = new HashMap<>();
                    mapScan.put("tmbh", array.getJSONObject(0).getString("brp_Sn"));
                    mapScan.put("sl", array.getJSONObject(0).getString("brp_Qty"));
                    mapScan.put("wlbh", array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("sl", array.getJSONObject(0).getString("brp_Qty"));
                    mapZs.put("wlbh", array.getJSONObject(0).getString("brp_wldm"));
                    mapZs.put("wlgg", array.getJSONObject(0).getString("brp_pmgg"));
                    view.setTmEd(mapScan.get("tmbh"));
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
    public void onDestroy(){
        if (scanUtil != null){
            scanUtil.close();
        }
    }

}
