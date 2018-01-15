package com.rdypda.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rdypda.R;
import com.rdypda.presenter.FirstPresenter;
import com.rdypda.view.viewinterface.IFirstView;

public class FirstActivity extends BaseActivity implements IFirstView {
    private FirstPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        presenter=new FirstPresenter(this,this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void showToastMsg(String msg) {
        Toast.makeText(FirstActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
