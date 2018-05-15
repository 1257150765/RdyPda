package com.rdypda.util;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;

/**
 * Created by Chen on 2018/5/15.
 */

public class DownloadService extends IntentService {
    private String TAG = "DownloadService";
    public static final String BROADCAST_ACTION =
            "com.example.android.threadsample.BROADCAST";
    public static final String EXTENDED_DATA_STATUS =
            "com.example.android.threadsample.STATUS";

    private LocalBroadcastManager mLocalBroadcastManager;

    public DownloadService() {
        super("DownloadService");
    }
    private void regist() {
        IntentFilter intentFilter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //regist();
        //获取下载地址
        String url = intent.getDataString();
        Log.i(TAG,url);
        String[] split = url.split(":");
        url = split[0].toLowerCase()+":"+split[1].toLowerCase()+":"+split[2];
        //获取DownloadManager对象
        Uri uri = Uri.parse(url);
        //Log.d(TAG, "onHandleIntent: uri"+uri);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //指定APK缓存路径和应用名称，可在SD卡/Android/data/包名/file/Download文件夹中查看
        //request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "RdyPDA.apk");
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/RdyPDA.apk");
        if (file.exists()){
            file.delete();
        }
        request.setDestinationUri(Uri.fromFile(file));
        //设置网络下载环境为wifi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置显示通知栏，下载完成后通知栏自动消失
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //设置通知栏标题
        request.setTitle("RdyPDA.apk");
        request.setDescription("RdyPDA.apk");
        request.setAllowedOverRoaming(false);
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        //获得唯一下载id
        long requestId = downloadManager.enqueue(request);
        //将id放进Intent
        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS,requestId);
        //查询下载信息
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(requestId);
        try{
            boolean isGoging=true;
            while(isGoging){
                Cursor cursor = downloadManager.query(query);
                Thread.sleep(1000);
                if (cursor != null && cursor.moveToFirst()) {
                    /*int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    Log.d(TAG, "onHandleIntent: "+status);
                    switch(status){
                        //如果下载状态为成功
                        case DownloadManager.STATUS_SUCCESSFUL:
                            isGoging=false;
                            //调用LocalBroadcastManager.sendBroadcast将intent传递回去
                            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                            mLocalBroadcastManager.sendBroadcast(localIntent);
                            stopSelf();
                            break;*/
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    int reason = cursor.getInt(columnReason);
                    Log.d(TAG, "onHandleIntent:status "+status);
                    Log.d(TAG, "onHandleIntent: reason"+reason);
                    switch(status){
                        case DownloadManager.STATUS_FAILED:
                            switch(reason){
                                case DownloadManager.ERROR_CANNOT_RESUME:
                                    //some possibly transient error occurred but we can't resume the download
                                    break;
                                case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                    //no external storage device was found. Typically, this is because the SD card is not mounted
                                    break;
                                case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                    //the requested destination file already exists (the download manager will not overwrite an existing file)
                                    break;
                                case DownloadManager.ERROR_FILE_ERROR:
                                    //a storage issue arises which doesn't fit under any other error code
                                    break;
                                case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                    //an error receiving or processing data occurred at the HTTP level
                                    break;
                                case DownloadManager.ERROR_INSUFFICIENT_SPACE://sd卡满了
                                    //here was insufficient storage space. Typically, this is because the SD card is full
                                    break;
                                case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                    //there were too many redirects
                                    break;
                                case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                    //an HTTP code was received that download manager can't handle
                                    break;
                                case DownloadManager.ERROR_UNKNOWN:
                                    //he download has completed with an error that doesn't fit under any other error code
                                    break;
                            }
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            switch(reason){
                                case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                    //the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed
                                    break;
                                case DownloadManager.PAUSED_UNKNOWN:
                                    //the download is paused for some other reason
                                    break;
                                case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                    //the download is waiting for network connectivity to proceed
                                    break;
                                case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                    //the download is paused because some network error occurred and the download manager is waiting before retrying the request
                                    break;
                            }
                            break;
                        case DownloadManager.STATUS_PENDING:
                            //the download is waiting to start
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            //the download is currently running
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            //the download has successfully completed
                            isGoging=false;
                            //调用LocalBroadcastManager.sendBroadcast将intent传递回去
                            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                            mLocalBroadcastManager.sendBroadcast(localIntent);
                            stopSelf();
                            break;
                    }
                }

                if(cursor!=null){
                    cursor.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
