package com.rdypda.view.viewinterface;

/**
 * Created by DengJf on 2018/1/15.
 */

public interface IBaseView2 extends IBaseView{
    void showBlueToothAddressDialog();
    void setShowMsgDialogEnable(String msg);
    void setShowProgressDialogEnable(boolean enable);
    void finish();
}
