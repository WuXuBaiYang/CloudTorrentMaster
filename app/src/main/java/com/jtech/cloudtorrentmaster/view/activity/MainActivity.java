package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtechlib2.view.activity.BaseActivity;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements MainContract.View {
    public final static String KEY_SERVER_INFO = "keyServerInfo";

    private MainContract.Presenter presenter;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new MainPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void loadData() {
    }
}