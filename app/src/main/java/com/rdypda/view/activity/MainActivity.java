package com.rdypda.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rdypda.R;
import com.rdypda.presenter.MainPresenter;
import com.rdypda.view.viewinterface.IMainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class MainActivity extends BaseActivity implements IMainView{

    private MainPresenter presenter;
    private String arrayStr;
    private AlertDialog dialog,downloadDialog;
    private ProgressDialog progressDialog;
    private ProgressDialog downloadProgressDialog;

    //侧滑菜单
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    FrameLayout rkBtn;
    @BindView(R.id.switch_layout)
    LinearLayout switchLayout;
    @BindView(R.id.exit_layout)
    LinearLayout exitLayout;
    @BindView(R.id.banner)
    BGABanner banner;

    //功能按钮
    @BindView(R.id.tmdy)
    LinearLayout tmdy_btn;
    @BindView(R.id.fl)
    LinearLayout fl_btn;
    @BindView(R.id.yljs)
    LinearLayout yljs_btn;
    @BindView(R.id.yltl)
    LinearLayout yltl_btn;
    @BindView(R.id.hl)
    LinearLayout hl_btn;
    @BindView(R.id.aldfl)
    LinearLayout aldfl_btn;
    @BindView(R.id.tl)
    LinearLayout tl_btn;
    @BindView(R.id.gdtldylz)
    LinearLayout gdtldylz_btn;
    @BindView(R.id.cpsmrk)
    LinearLayout cpsmrk_btn;
    @BindView(R.id.ykll)
    LinearLayout ykll_btn;
    @BindView(R.id.adfl)
    LinearLayout adfl_btn;
    @BindView(R.id.adtl)
    LinearLayout adtl_btn;
    @BindView(R.id.yktldck)
    LinearLayout yctldck_btn;
    @BindView(R.id.tmcf)
    LinearLayout tmcf_btn;
    @BindView(R.id.tmbd)
    LinearLayout tmbd_btn;
    @BindView(R.id.kcpd)
    LinearLayout kcpd_btn;
    @BindView(R.id.tmcx)
    LinearLayout tmcx_btn;
    @BindView(R.id.user_name)
    TextView userNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        presenter=new MainPresenter(this,this);
        presenter.initPermissionList(arrayStr);
    }

    @Override
    protected void initView(){
        dialog=new AlertDialog.Builder(this).setTitle("提示").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true)
                .create();


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在检查更新...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home);

        arrayStr=getIntent().getStringExtra("permissionList");


        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.banner_2));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.banner_3));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.banner_4));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.banner_1));
        banner.setData(views);

        downloadProgressDialog=new ProgressDialog(this);
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.setCancelable(false);
        downloadProgressDialog.setCanceledOnTouchOutside(false);
        downloadProgressDialog.setTitle("下载中");
        downloadProgressDialog.setMax(100);
    }


    @OnClick({R.id.switch_layout,R.id.exit_layout,R.id.tmdy,R.id.fl,R.id.yljs,
            R.id.yltl,R.id.hl,R.id.aldfl,R.id.tl,R.id.gdtldylz,R.id.cpsmrk,R.id.ykll,
            R.id.adfl,R.id.adtl,R.id.yktldck,R.id.tmcf,R.id.tmbd,R.id.kcpd,R.id.tmcx,
            R.id.hlbz,R.id.sbtl,R.id.sblj,R.id.sbxl})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tmdy:
                presenter.goToLlddr(MainPresenter.TMDY);
                break;
            case R.id.switch_layout:
                presenter.goToLogin();
                break;
            case R.id.exit_layout:
                finish();
                break;
            case R.id.fl:
                presenter.goToLlddr(MainPresenter.FL);
                break;
            case R.id.yljs:
                presenter.goToYljs();
                break;
            case R.id.yltl:
                presenter.goToYltl();
                break;
            case R.id.hl:
                presenter.goToHl();
                break;
            case R.id.aldfl:
                presenter.goToAldfl();
                break;
            case R.id.tl:
                presenter.goToTl();
                break;
            case R.id.gdtldylz:
                presenter.goToYlzck(YlzckActivity.START_TYPE_GDTLDYLZ);
                break;
            case R.id.cpsmrk:
                presenter.goToYlzck(YlzckActivity.STRAT_TYPE_CPSMCR);
                break;
            case R.id.ykll:
                presenter.goToYlzck(YlzckActivity.START_TYPE_YKLL);
                break;
            case R.id.adfl:
                presenter.goToAdfl();
                break;
            case R.id.adtl:
                presenter.goToAdtl();
                break;
            case R.id.yktldck:
                presenter.goToYktldck();
                break;
            case R.id.tmcf:
                presenter.goToTmcf();
                break;
            case R.id.tmbd:
                presenter.goToTmbd();
                break;
            case R.id.kcpd:
                presenter.goToKcpd();
                break;
            case R.id.tmcx:
                presenter.goToTmcx();
                break;
            case R.id.hlbz:
                presenter.goTohlbz();
                break;
            case R.id.sbtl:
                presenter.goToSbtl();
                break;
            case R.id.sblj:
                presenter.goToSblj();
                break;
            case R.id.sbxl:
                presenter.goTosbxl();
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.about:
                presenter.checkToUpdate(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserName(String userName) {
        userNameText.setText(userName);
    }

    @Override
    public void showMsgDialog(String msg) {
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    public void setShowProgressDialogEnable(boolean enable) {
        if (enable){
            progressDialog.show();
        }else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showDownloadDialog(final String url) {
        downloadDialog=new AlertDialog.Builder(this).setTitle("提示").setMessage("发现新的版本，是否现在下载更新").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.downloadInstallApk(url);
                //Toast.makeText(MainActivity.this,"已创建下载任务",Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        downloadDialog.show();
    }

    @Override
    public void showToastMsg(String msg) {
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setShowDownloadProgressDialogEnable(boolean enable) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.closeTimer();
    }
}
