package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ActivityGoManager;
import com.jtech.cloudtorrentmaster.manager.PermissionManager;
import com.jtech.cloudtorrentmaster.mvp.contract.SplashContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.SplashPresenter;
import com.jtechlib2.view.activity.BaseActivity;

/**
 * 起始页
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashContract.Presenter presenter;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new SplashPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void loadData() {
        PermissionManager.checkMust(getActivity(), allGranted -> {
            ActivityGoManager.goServerSelectPage(getActivity());
            onBackPressed();
        });
    }
}