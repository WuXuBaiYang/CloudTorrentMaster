package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.view.activity.BaseActivity;

import androidx.appcompat.widget.Toolbar;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements MainContract.View,
        Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public final static String KEY_SERVER_INFO = "keyServerInfo";

    private MainContract.Presenter presenter;

    private TitleView titleView;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new MainPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        //初始化标题栏
        this.titleView = TitleView.build(getActivity())
                .setTitle(presenter.getServerName())
                .setSubTitle(presenter.getIPAddress())
                .setMenuClickListener(this)
                .setLeftButton(R.drawable.ic_title_menu, this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default://默认为菜单
                // TODO: 2019/2/19 弹出菜单
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO: 2019/2/19 实现右侧菜单的事件响应
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        titleView.setUpMenu(R.menu.main_menu);
        return super.onCreateOptionsMenu(menu);
    }
}