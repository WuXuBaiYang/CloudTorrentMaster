package com.jtech.cloudtorrentmaster.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ActivityGoManager;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.model.event.ServerConnectEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.view.weight.AddTaskSheet;
import com.jtech.cloudtorrentmaster.view.weight.ServerSelectPopup;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    private AddTaskSheet addTaskSheet;
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
                .setLeftButton(R.drawable.ic_title_menu, this)
                .setMenuClickListener(this);
        //设置侧滑菜单的点击事件
        navigationView.setNavigationItemSelectedListener(this);
        //实例化viewHolder
        this.viewHolder = new NavigationHeaderViewHolder(
                navigationView.getHeaderView(0));
        //设置服务器信息
        setupServerInfo(presenter.getServerInfo());
    }

    @Override
    protected void loadData() {
    }

    /**
     * 服务器状态变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerStatsEvent(ServerStatsEvent event) {
        // TODO: 2019/2/20
    }

    /**
     * 服务器用户状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerUsersEvent(ServerUsersEvent event) {
        // TODO: 2019/2/20
    }

    /**
     * 服务器链接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerConnect(ServerConnectEvent event) {
        // TODO: 2019/2/20
    }

    /**
     * 服务器下载状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerDownloadsEvent(ServerDownloadsEvent event) {
        // TODO: 2019/2/20
    }

    /**
     * 服务器种子状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerTorrentsEvent(ServerTorrentsEvent event) {
        // TODO: 2019/2/20
    }

    /**
     * 获取添加任务的sheet
     *
     * @return
     */
    private AddTaskSheet getAddTaskSheet() {
        if (null == addTaskSheet) {
            this.addTaskSheet = AddTaskSheet.build(getActivity())
                    .setListener(new AddTaskSheet.OnAddTaskListener() {
                        @Override
                        public void addMagnetTask(String magnet) {
                            // TODO: 2019/2/21  
                        }

                        @Override
                        public void addTorrentFileTask(File file) {
                            // TODO: 2019/2/21
                        }
                    });
        }
        return addTaskSheet;
    }

    /**
     * 设置服务器信息
     *
     * @param model
     */
    private void setupServerInfo(ServerInfoModel model) {
        if (null == model) return;
        titleView.setTitle(model.getLabel())
                .setSubTitle(model.getIpAddress());
        //设置侧滑菜单
        ImageUtils.showImage(getActivity(), model.getIconUri(), viewHolder.imageViewHeaderLogo);
        viewHolder.textViewHeaderIPAddress.setText(model.getIpAddress());
        viewHolder.textViewHeaderLabel.setText(model.getLabel());
    }

    /**
     * 启动新的页面
     *
     * @param model
     */
    private void switchNewServer(ServerInfoModel model) {
        String message = String.format(getString(R.string.server_switch_message),
                model.getLabel(), model.getIpAddress());
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .setPositiveButton(getString(R.string.dialog_server_switch), (dialog, which) -> {
                    //重新启动本页
                    ActivityGoManager.goMainPage(getActivity(), model);
                    //关闭当前页面
                    MainActivity.super.onBackPressed();
                }).show();
    }

    /**
     * 关闭navigation
     *
     * @return
     */
    @SuppressLint("RtlHardcoded")
    private boolean closeNavigation() {
        if (null != drawerLayout && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_menu_navigation_setting://设置
                // TODO: 2019/2/21 跳转到服务器设置页面
                break;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_download://下载按钮
                // TODO: 2019/2/20  
                break;
            case R.id.main_menu_search://搜索按钮
                // TODO: 2019/2/20
                break;
        }
        return false;
    }

    @OnClick({R.id.fab_main_add_task})
    @Override
    @SuppressLint("RtlHardcoded")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main_add_task://添加任务
                getAddTaskSheet().show();
                break;
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
     * 持有侧滑菜单的视图
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

        private ServerSelectPopup serverSelectPopup;

        NavigationHeaderViewHolder(View headerView) {
            ButterKnife.bind(this, headerView);
        }

        /**
         * 服务器信息点击事件
         */
        @OnClick(R.id.linearlayout_main_navigation_header)
        void onServerItemClick(View v) {
            imageViewHeaderArrow.setEnabled(
                    !imageViewHeaderArrow.isEnabled());
            rotationArrow(!imageViewHeaderArrow.isEnabled());
            if (null == serverSelectPopup) {
                serverSelectPopup = ServerSelectPopup.build(getActivity())
                        .setDatas(presenter.loadServerInfoList(true))
                        .setListener(new ServerSelectPopup.OnServerSelectPopupListener() {
                            @Override
                            public void onItemSelect(ServerInfoModel model) {
                                switchNewServer(model);
                            }

                            @Override
                            public void onDismiss() {
                                rotationArrow(false);
                                imageViewHeaderArrow.setEnabled(true);
                            }
                        });
            }
            if (!imageViewHeaderArrow.isEnabled()) {
                serverSelectPopup.show(v);
            } else {
                serverSelectPopup.dismiss();
            }
        }

        /**
         * 旋转箭头
         *
         * @param isUp
         */
        private void rotationArrow(boolean isUp) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    isUp ? R.anim.arrow_rotation_up : R.anim.arrow_rotation_down);
            imageViewHeaderArrow.startAnimation(animation);
        }

        /**
         * 取消popup
         *
         * @return
         */
        boolean dismissPopup() {
            if (null != serverSelectPopup && serverSelectPopup.isShowing()) {
                serverSelectPopup.dismiss();
                return true;
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //判断服务器切换列表是否展示
        if (viewHolder.dismissPopup()) return;
        //判断侧滑菜单是否展示
        if (closeNavigation()) return;
        //判断添加任务sheet是否展示
        if (getAddTaskSheet().isShowing()) {
            getAddTaskSheet().dismiss();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理种子文件选择返回值
        getAddTaskSheet().handlePickTorrentResult(requestCode,
                resultCode, Objects.requireNonNull(data));
    }
}