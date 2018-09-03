package com.rdypda.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.rdypda.R;
import com.rdypda.presenter.CsllwxcbPresentor;
import com.rdypda.view.viewinterface.ICsllwxcbView;
import com.rdypda.view.widget.PowerButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 厂商来料外箱拆包
 */
public class CsllwxcbActivity extends BaseActivity2 implements ICsllwxcbView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tmbh)
    EditText tmbh;
    @BindView(R.id.ok)
    PowerButton ok;
    private CsllwxcbPresentor presentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csllwxcb);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("厂商来料外箱拆包");
        actionBar.setDisplayHomeAsUpEnabled(true);
        presentor = new CsllwxcbPresentor(this,this);
        //tmbh.setText("");
    }

    @OnClick(R.id.ok)
    public void onViewClicked() {
        presentor.isValidCode(tmbh.getText().toString());
    }

    @Override
    public void setTmEd(String tmxh2) {
        tmbh.setText(tmxh2);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presentor != null){
            presentor.onDestroy();
        }
    }
}
