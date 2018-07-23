package com.rdypda.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rdypda.R;
import com.rdypda.presenter.CgrktmdyPresentor;
import com.rdypda.view.viewinterface.ICgrktmdyView;
import com.rdypda.view.widget.PowerButton;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//采购入库条码打印
public class CgrktmdyActivity extends BaseActivity2 implements ICgrktmdyView{

    @BindView(R.id.ed_gysdm_cgrktmdyActivity)
    EditText edGysdm;
    @BindView(R.id.et_shdh_cgrktmdyActivity)
    EditText etShdh;
    @BindView(R.id.et_ddbh_cgrktmdyActivity)
    EditText etDdbh;
    @BindView(R.id.et_bz_cgrktmdyActivity)
    EditText etBz;
    @BindView(R.id.ed_wlbh_cgrktmdyActivity)
    EditText edWlbh;
    @BindView(R.id.tv_wlgg_cgrktmdyActivity)
    TextView tvWlgg;
    @BindView(R.id.et_scpc_cgrktmdyActivity)
    EditText etScpc;
    @BindView(R.id.et_bzsl_cgrktmdyActivity)
    EditText etBzsl;
    @BindView(R.id.sp_ylkw_cgrktmdyActivity)
    Spinner spYlkw;
    @BindView(R.id.tv_tmbh_cgrktmdyActivity)
    TextView tvTmbh;
    @BindView(R.id.btn_getbarcode_cgrktmdyActivity)
    Button btnGetbarcode;
    @BindView(R.id.btn_print_cgrktmdyActivity)
    Button btnPrint;
    @BindView(R.id.btn_rk_cgrktmdyActivity)
    Button btnRk;
    @BindView(R.id.btn_query_gysdm_cgrktmdyActivity)
    PowerButton btnQueryGysdm;
    @BindView(R.id.btn_query_wlbh_cgrktmdyActivity)
    PowerButton btnQueryWlbh;
    private CgrktmdyPresentor presentor;
    private Map<String, String> mapKw;
    private String strDw = "";
    private String wlgg = "";
    private String qrCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgrktmdy);
        ButterKnife.bind(this);
        presentor = new CgrktmdyPresentor(this,this);
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.btn_query_gysdm_cgrktmdyActivity, R.id.btn_query_wlbh_cgrktmdyActivity,R.id.btn_getbarcode_cgrktmdyActivity, R.id.btn_print_cgrktmdyActivity, R.id.btn_rk_cgrktmdyActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //供应商代码查询
            case R.id.btn_query_gysdm_cgrktmdyActivity:
                String gysdm = edGysdm.getText().toString();
                if ("".equals(gysdm)){
                    setShowMsgDialogEnable("请输入物料编码");
                    return;
                }
                presentor.queryGysdm(gysdm);
                break;
            //物料代码
            case R.id.btn_query_wlbh_cgrktmdyActivity:
                String wlbh = edWlbh.getText().toString();
                /*if ("".equals(wlbh)){
                    setShowMsgDialogEnable("请输入物料编码");
                    return;
                }*/
                presentor.queryWlbh(wlbh);
                break;
            //获取条码
            case R.id.btn_getbarcode_cgrktmdyActivity:
                //presentor.getBarCode();
                break;
            //打印
            case R.id.btn_print_cgrktmdyActivity:
                //presentor.printEvent();
                break;
            //入库
            case R.id.btn_rk_cgrktmdyActivity:
                presentor.rk();
                break;
        }
    }




    @Override
    public void onQueryWlbhSucceed(final String[] wldmArr, final List<Map<String, String>> wlbhData) {
        if (wlbhData.size() == 1){
            edWlbh.setText(wldmArr[0]);
            //Log.d(TAG, "onClick: "+wlbhData.get(which).get("itm_wlgg"));
            tvWlgg.setText(wlbhData.get(0).get("itm_wlgg"));
            strDw = wlbhData.get(0).get("itm_unit");
            /*wlpmChinese = wlbhData.get(0).get("itm_wlpm");
            wlpmEnlight = wlbhData.get(0).get("itm_ywwlpm");*/
            wlgg = wlbhData.get(0).get("itm_wlgg");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setItems(wldmArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edWlbh.setText(wldmArr[which]);
                //Log.d(TAG, "onClick: "+wlbhData.get(which).get("itm_wlgg"));
                tvWlgg.setText(wlbhData.get(which).get("itm_wlgg"));
                strDw = wlbhData.get(which).get("itm_unit");
                /*wlpmChinese = wlbhData.get(which).get("itm_wlpm");
                wlpmEnlight = wlbhData.get(which).get("itm_ywwlpm");*/
                wlgg = wlbhData.get(0).get("itm_wlgg");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onQueryGysdmSucceed(String gysdm) {

    }

    @Override
    public void onGetKwdataSucceed(List<String> dataMc, final List<Map<String, String>> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataMc);
        spYlkw.setAdapter(adapter);
        spYlkw.setSelection(0);
        spYlkw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    mapKw = null;
                }else {
                    mapKw = data.get(position-1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mapKw = null;
            }
        });
    }

    @Override
    public void onGetBarCodeSucceed(String barCode, String qrCode) {
        tvTmbh.setText(barCode);
        this.qrCode = qrCode;
    }
}
