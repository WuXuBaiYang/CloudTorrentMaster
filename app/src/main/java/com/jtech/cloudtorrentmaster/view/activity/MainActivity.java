package com.jtech.cloudtorrentmaster.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.activity.BaseActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements MainContract.View,
        Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    public final static String KEY_SERVER_INFO = "keyServerInfo";

    private MainContract.Presenter presenter;

    @BindView(R.id.drawerlayout_main)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationview_main)
    NavigationView navigationView;

    private NavigationHeaderViewHolder viewHolder;
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
        ServerInfoModel model = presenter.getServerInfo();
        this.titleView = TitleView.build(getActivity())
                .setTitle(null != model ? model.getLabel() : "")
                .setSubTitle(null != model ? model.getIpAddress() : "")
                .setLeftButton(R.drawable.ic_title_menu, this)
                .setMenuClickListener(this);
        //设置服务器信息
        setupServerInfo(model);
        //设置侧滑菜单的点击事件
        navigationView.setNavigationItemSelectedListener(this);
        //实例化viewholder
        this.viewHolder = new NavigationHeaderViewHolder(
                navigationView.getHeaderView(0));
    }

    @Override
    protected void loadData() {
    }

    /**
     * 设置服务器信息
     *
     * @param model
     */
    private void setupServerInfo(ServerInfoModel model) {
        if (null == model) return;
        //设置侧滑菜单
        ImageUtils.showImage(getActivity(), model.getIconUri(), viewHolder.imageViewHeaderLogo);
        viewHolder.textViewHeaderIPAddress.setText(model.getIpAddress());
        viewHolder.textViewHeaderLabel.setText(model.getLabel());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // TODO: 2019/2/19 实现侧滑菜单点击事件
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO: 2019/2/19 实现右侧菜单的事件响应
        return false;
    }

    @Override
    @SuppressLint("RtlHardcoded")
    public void onClick(View v) {
        switch (v.getId()) {
            default://默认为侧滑菜单
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        titleView.setUpMenu(R.menu.main_menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     */
    class NavigationHeaderViewHolder {
        @BindView(R.id.imageview_main_navigation_header_logo)
        ImageView imageViewHeaderLogo;
        @BindView(R.id.textview_main_navigation_header_label)
        TextView textViewHeaderLabel;
        @BindView(R.id.textview_main_navigation_header_ip_address)
        TextView textViewHeaderIPAddress;
        @BindView(R.id.imageview_main_navigation_header_arrow)
        ImageView imageViewHeaderArrow;

        NavigationHeaderViewHolder(View headerView) {
            ButterKnife.bind(this, headerView);
        }

        /**
         * 服务器信息点击事件
         */
        @OnClick(R.id.linearlayout_main_navigation_header)
        void onServerItemClick() {
            // TODO: 2019/2/19 弹出服务器选择列表以及旋转arrow
        }
    }
}