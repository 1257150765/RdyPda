package com.rdypda.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.liangmutian.airrecyclerview.swipetoloadlayout.BaseRecyclerAdapter;
import com.rdypda.R;
import com.rdypda.adapter.SbljAdapter;
import com.rdypda.presenter.SbljPresenter;
import com.rdypda.view.viewinterface.ISbljView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SbljActivity extends BaseActivity implements ISbljView ,com.rdypda.view.viewinterface.OnItemClickListener {
    private SbljPresenter presenter;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    @BindView(R.id.sblj_list)
    RecyclerView sbljList;
    @BindView(R.id.jt_radio)
    RadioButton jtRadio;
    @BindView(R.id.kl_radio)
    RadioButton klRadio;
    @BindView(R.id.jtbh_ed)
    EditText jtbhEd;
    @BindView(R.id.klbh_ed)
    EditText klbhEd;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sblj);
        ButterKnife.bind(this);
        initView();
        presenter=new SbljPresenter(this,this);
    }

    @Override
    protected void initView() {
        dialog=new AlertDialog.Builder(this).setTitle("提示")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        jtRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    presenter.setType(presenter.SCAN_TYPE_JT);
                }else {
                    presenter.setType(presenter.SCAN_TYPE_KL);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.jt_sure_btn,R.id.kl_sure_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.jt_sure_btn:
                presenter.getConnectedDevice(jtbhEd.getText().toString());
                break;
            case R.id.kl_sure_btn:
                showConnectDialog(jtbhEd.getText().toString(),klbhEd.getText().toString());
                break;
        }
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
    public void showMsgDialog(String msg) {
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    public void refreshSblj(List<Map<String, String>> data) {
        SbljAdapter adapter=new SbljAdapter(SbljActivity.this,R.layout.item_sblj,data);
        sbljList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        sbljList.setLayoutManager(new GridLayoutManager(SbljActivity.this,1));
    }

    @Override
    public void showConnectDialog(final String jtbh, final String klbh) {
        if (jtbh.equals("")){
            showMsgDialog("请先输入机台编号");
            return;
        }
        if (klbh.equals("")){
           showMsgDialog("请先输入烤炉编号");
            return;
        }
        AlertDialog cnDialog=new AlertDialog.Builder(SbljActivity.this).setTitle("提示")
                .setCancelable(false)
                .setMessage("确认"+jtbh+"连接到"+klbh+"?")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.connectDevice(jtbh,klbh);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        cnDialog.show();
    }

    @Override
    public void setJtKlText(String jtbh, String klbh) {
        jtbhEd.setText(jtbh);
        klbhEd.setText(klbh);
    }

    @Override
    public String getJtbhText() {
        return jtbhEd.getText().toString();
    }

    @Override
    public String getklbhText() {
        return klbhEd.getText().toString();
    }

    @Override
    protected void onDestroy() {
        presenter.closeScanUtil();
        super.onDestroy();
    }



    @Override
    public void onItemClick(int position, Map<String, String> map, BaseRecyclerAdapter.BaseRecyclerViewHolder holder) {
        //ybdkl,已绑定烤炉
        final android.app.AlertDialog.Builder disConnectDeviceBuilder = new android.app.AlertDialog.Builder(this, 3);
        android.app.AlertDialog.Builder showConnectDeviceBuilder = new android.app.AlertDialog.Builder(this, 3);

        final String jtbh = map.get("jtbh");
        final String jtmc = map.get("jtmc");
        final String ybdklStr = map.get("ybdkl");
        if ("".equals(ybdklStr)){
            return;
        }
        final String[] ybdkls = ybdklStr.split(",");
        if (ybdkls.length == 1){
            disConnectDeviceBuilder.setTitle("警告");
            disConnectDeviceBuilder.setMessage("你是否要解除"+jtmc+"与"+ybdklStr+"的连接");
            disConnectDeviceBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    presenter.disConnectDevice(jtbh, ybdklStr);
                    dialog.dismiss();
                }
            });
            disConnectDeviceBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            disConnectDeviceBuilder.create().show();
        }else {
            showConnectDeviceBuilder.setTitle("请选择需要解除的烤炉");
            showConnectDeviceBuilder.setItems(ybdkls, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    disConnectDeviceBuilder.setTitle("警告");
                    disConnectDeviceBuilder.setMessage("你是否要解除"+jtmc+"与"+ybdkls[which]+"的连接");
                    disConnectDeviceBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which2) {
                            presenter.disConnectDevice(jtbh,ybdkls[which]);
                            dialog.dismiss();
                        }
                    });
                    disConnectDeviceBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                    disConnectDeviceBuilder.create().show();
                }
            });
            showConnectDeviceBuilder.create().show();
        }


    }
}
