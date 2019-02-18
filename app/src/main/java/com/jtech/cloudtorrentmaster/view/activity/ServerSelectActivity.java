package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;
import android.view.View;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.ServerSelectPresenter;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.view.activity.BaseActivity;

/**
 * 服务器选择页面
 */
public class ServerSelectActivity extends BaseActivity implements ServerSelectContract.View,
        View.OnClickListener {
    private ServerSelectContract.Presenter presenter;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new ServerSelectPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_select);
        //设置标题栏
        TitleView.build(getActivity())
                .setTitle(R.string.server_select_title)
                .setLeftButton(R.drawable.ic_title_close, this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default://默认为关闭按钮        }
                onBackPressed();
                break;
        }
    }
}