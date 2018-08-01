package com.rdypda.view.viewinterface;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 2018-07-18.
 */

public interface ICgrktmdyView extends IBaseView2{


    void onQueryWlbhSucceed(String[] wldmArr, List<Map<String, String>> wlbhData);

    void onQueryGysdmSucceed(String gysdm);

    void onGetKwdataSucceed(List<String> dataMc, List<Map<String,String>> data);

    void onGetBarCodeSucceed(String barCode, String qrCode);

    void onCheckGysdmSucceed(boolean b, String s);

    void onCheckDdbhmSucceed(boolean b, String mom_zzdh);

    void onCheckShdhmSucceed(boolean b, String grdNo);

    void onRkSucceed();
}
