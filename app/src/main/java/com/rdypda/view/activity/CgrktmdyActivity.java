package com.rdypda.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rdypda.R;
import com.rdypda.presenter.CgrktmdyPresentor;
import com.rdypda.view.viewinterface.ICgrktmdyView;
import com.rdypda.view.widget.PowerButton;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//采购入库条码打印
public class CgrktmdyActivity extends BaseActivity2 implements ICgrktmdyView {

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
    @BindView(R.id.et_scrq_cgrktmdyActivity)
    EditText etScrq;

    private CgrktmdyPresentor presentor;
    private Map<String, String> mapKw;
    private String strDw = "";
    private String wlgg = "";
    private String qrCode = "";
    private String wlpmChinese = "";
    private boolean isCheckGysdm = false;
    private String TAG = "CgrktmdyActivity";
    private boolean isCheckShdh = false;
    private boolean isCheckDdbh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgrktmdy);
        ButterKnife.bind(this);
        presentor = new CgrktmdyPresentor(this, this);
        initView();
    }

    @Override
    protected void initView() {
        edGysdm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isCheckGysdm = false;
                } else if (!hasFocus) {
                    //如果失去了焦点
                    String gysdm = edGysdm.getText().toString();
                    presentor.checkGysdm(gysdm);
                }
            }
        });

        etShdh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "etShdh: " + hasFocus);
                if (!isCheckGysdm) {//如果未验证供应商代码
                    return;
                } else if (hasFocus) {
                    isCheckShdh = false;
                } else if (!hasFocus) {
                    //如果失去了焦点
                    String shdh = etShdh.getText().toString();
                    presentor.checkShdh(shdh);
                }
            }
        });

        etDdbh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "etDdbh: " + hasFocus);
                if (!isCheckShdh) {//如果未验证收货单号
                    return;
                } else if (!hasFocus) {
                    //如果失去了焦点
                    String ddbh = etDdbh.getText().toString();
                    presentor.checkDdbh(ddbh);
                }
            }
        });
        etScrq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar ca = Calendar.getInstance();
                int year = ca.get(Calendar.YEAR);
                int month = ca.get(Calendar.MONTH);
                int day = ca.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CgrktmdyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etScrq.setText(year+"-"+month+"-"+dayOfMonth);
                    }
                },year,month,day);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        presentor.checkScrq(etScrq.getText().toString());
                    }
                });
                dialog.show();
            }
        });
        /*etScrq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "etDdbh: " + hasFocus);
                if (!isCheckDdbh) {//如果未验证订单编号
                    return;
                } else if (!hasFocus) {
                    //如果失去了焦点
                    String scrq = etScrq.getText().toString();
                    presentor.checkScrq(scrq);
                }
            }
        });*/
    }

    @Override
    public void onCheckGysdmSucceed(boolean isCheckGysdm, String gysdm) {
        this.isCheckGysdm = isCheckGysdm;
        if (isCheckGysdm) {
            edGysdm.setText(gysdm);
        } else {
            edGysdm.setText("");
            edGysdm.requestFocus();
        }
    }

    @Override
    public void onCheckShdhmSucceed(boolean b, String grdNo) {
        this.isCheckShdh = b;
        if (b) {
            etShdh.setText(grdNo);
        } else {
            etShdh.setText("");
            etShdh.requestFocus();
        }
    }

    @Override
    public void onCheckScrqSucceed(boolean b) {
        if (b) {
            //etScrq.setText(mom_zzdh);
        } else {
            etScrq.setText("");
            etScrq.requestFocus();
        }
    }

    @Override
    public void onCheckDdbhmSucceed(boolean b, String mom_zzdh) {
        isCheckDdbh = b;
        if (b) {
            etDdbh.setText(mom_zzdh);
        } else {
            etDdbh.setText("");
            etDdbh.requestFocus();
        }
    }


    @OnClick({R.id.btn_query_wlbh_cgrktmdyActivity, R.id.btn_getbarcode_cgrktmdyActivity, R.id.btn_print_cgrktmdyActivity, R.id.btn_rk_cgrktmdyActivity,R.id.et_scrq_cgrktmdyActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {

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
                String wlbh2 = edWlbh.getText().toString();
                String gysdm2 = edGysdm.getText().toString();
                String ddbh = etDdbh.getText().toString();
                String shdh = etShdh.getText().toString();
                String bz = etBz.getText().toString();
                String scpc = etScpc.getText().toString();
                String bzsl = etBzsl.getText().toString();
                String scrq = etScrq.getText().toString();
                presentor.getBarCode(gysdm2, shdh, ddbh, bz, wlbh2, scpc, bzsl, mapKw, scrq);
                break;
            //打印
            case R.id.btn_print_cgrktmdyActivity:
                String qrcode = qrCode;
                presentor.printEvent(qrcode, wlpmChinese.trim() + "," + wlgg, etDdbh.getText().toString(), etScpc.getText().toString(), etBz.getText().toString());
                break;
            //入库
            case R.id.btn_rk_cgrktmdyActivity:
                String tmbh = tvTmbh.getText().toString();
                presentor.rk(tmbh, mapKw);
                break;
        }
    }

    @Override
    public void onRkSucceed() {
        qrCode = "";
        tvTmbh.setText("");
    }



    @Override
    public void onQueryWlbhSucceed(final String[] wldmArr, final List<Map<String, String>> wlbhData) {
        if (wlbhData.size() == 1) {
            edWlbh.setText(wldmArr[0]);
            //Log.d(TAG, "onClick: "+wlbhData.get(which).get("itm_wlgg"));
            tvWlgg.setText(wlbhData.get(0).get("itm_wlgg"));
            strDw = wlbhData.get(0).get("itm_unit");
            wlpmChinese = wlbhData.get(0).get("itm_wlpm");
            /*wlpmEnlight = wlbhData.get(0).get("itm_ywwlpm");*/
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
                wlpmChinese = wlbhData.get(which).get("itm_wlpm");
                /*wlpmEnlight = wlbhData.get(which).get("itm_ywwlpm");*/
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
                if (position == 0) {
                    mapKw = null;
                } else {
                    mapKw = data.get(position - 1);
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
