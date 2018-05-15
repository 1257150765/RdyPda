package com.rdypda.view.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rdypda.R;
import com.rdypda.presenter.FirstPresenter;
import com.rdypda.util.DownloadService;
import com.rdypda.view.viewinterface.IFirstView;

import java.io.File;

//第一个活动，
public class FirstActivity extends BaseActivity implements IFirstView {
    private FirstPresenter presenter;
    private ProgressDialog downloadProgressDialog;
    private AlertDialog dialog,downloadDialog;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        presenter=new FirstPresenter(this,this);
        initView();
    }

    @Override
    protected void initView() {
        downloadProgressDialog=new ProgressDialog(this);
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.setCancelable(false);
        downloadProgressDialog.setCanceledOnTouchOutside(false);
        downloadProgressDialog.setTitle("下载中");
        downloadProgressDialog.setMax(100);

        dialog=new AlertDialog.Builder(this,R.style.AppTheme).setTitle("提示").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true)
                .create();
    }

    @Override
    public void showToastMsg(String msg) {
        Toast.makeText(FirstActivity.this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setShowDownloadProgressDialogEnable(boolean enable) {
        if (isDestroyed())
            return;
        if (enable){
            downloadProgressDialog.show();
        }else {
            downloadProgressDialog.dismiss();
        }
    }

    @Override
    public void setProgressDownloadProgressDialog(int size) {
        downloadProgressDialog.setProgress(size);
    }

    @Override
    public void showDownloadDialog(final String url) {
        downloadDialog=new AlertDialog.Builder(FirstActivity.this,R.style.DialogTheme)
                .setTitle("提示")
                .setMessage("发现新的版本，是否现在下载更新")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long data = intent.getLongExtra(DownloadService.EXTENDED_DATA_STATUS,0L);
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/RdyPDA.apk")),
                                "application/vnd.android.package-archive");
                        context.startActivity(intent);
                        finish();
                    }
                };
                LocalBroadcastManager.getInstance(FirstActivity.this).registerReceiver(receiver, new IntentFilter(DownloadService.BROADCAST_ACTION));
                //FirstActivity.this.registerReceiver(receiver,new IntentFilter(DownloadService.BROADCAST_ACTION));
                //presenter.downloadInstallApk(url);
                Intent serviceIntent = new Intent(FirstActivity.this,DownloadService.class);
                //将下载地址url放入intent中
                serviceIntent.setData(Uri.parse(url));
                startService(serviceIntent);
                //Toast.makeText(MainActivity.this,"已创建下载任务",Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.goToLogin();
            }
        }).create();
        if (!FirstActivity.this.isDestroyed())
        downloadDialog.show();
    }

    @Override
    public void showMsgDialog(String msg) {
        if (!FirstActivity.this.isDestroyed()){
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
