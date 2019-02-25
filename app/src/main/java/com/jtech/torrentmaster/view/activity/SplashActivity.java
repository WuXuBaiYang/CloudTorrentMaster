package com.jtech.torrentmaster.view.activity;

import android.os.Bundle;

import com.jtech.torrentmaster.R;
import com.jtech.torrentmaster.manager.ActivityGoManager;
import com.jtech.torrentmaster.manager.PermissionManager;
import com.jtech.torrentmaster.mvp.contract.SplashContract;
import com.jtech.torrentmaster.mvp.presenter.SplashPresenter;
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