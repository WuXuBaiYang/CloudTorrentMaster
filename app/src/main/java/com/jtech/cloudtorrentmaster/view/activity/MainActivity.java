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
import com.jtech.cloudtorrentmaster.manager.ParamsCacheManager;
import com.jtech.cloudtorrentmaster.model.ServerDownloadsModel;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.model.ServerStatsModel;
import com.jtech.cloudtorrentmaster.model.ServerTorrentModel;
import com.jtech.cloudtorrentmaster.model.ServerUserModel;
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.utils.ToastUtils;
import com.jtech.cloudtorrentmaster.utils.Utils;
import com.jtech.cloudtorrentmaster.view.weight.AddTaskSheet;
import com.jtech.cloudtorrentmaster.view.weight.LoadingDialog;
import com.jtech.cloudtorrentmaster.view.weight.ServerSelectPopup;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
    @BindView(R.id.cardview_main_content_stats)
    CardView cardViewStats;
    @BindView(R.id.cardview_main_content_downloads)
    CardView cardViewDownloads;
    @BindView(R.id.cardview_main_content_torrents)
    CardView cardViewTorrents;

    private DownloadsCardViewHolder downloadsCardViewHolder;
    private TorrentsCardViewHolder torrentsCardViewHolder;
    private StatsCardViewHolder statsCardViewHolder;
    private NavigationHeaderViewHolder viewHolder;
    private AddTaskSheet addTaskSheet;
    private TitleView titleView;

    private boolean clipMagnetFlag = false;//剪切板中的磁力链读取标记
    private String clipMagnet;//剪切板中的磁力链，用做缓存

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new MainPresenter(getActivity(), this, bundle);
        //获取剪切板中的磁力链记录
        this.clipMagnet = ParamsCacheManager.get(getActivity())
                .getClipMagnet();
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
        //实例化卡片持有对象
        downloadsCardViewHolder = new DownloadsCardViewHolder(cardViewDownloads);
        torrentsCardViewHolder = new TorrentsCardViewHolder(cardViewTorrents);
        statsCardViewHolder = new StatsCardViewHolder(cardViewStats);
    }

    @Override
    protected void loadData() {
        //初始化服务器
        presenter.initServer();
    }

    /**
     * 服务器状态变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerStatsEvent(ServerStatsEvent event) {
        if (null != statsCardViewHolder) {
            statsCardViewHolder.updateStats(event.getModel());
        }
    }

    /**
     * 服务器用户状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerUsersEvent(ServerUsersEvent event) {
        if (null != statsCardViewHolder) {
            statsCardViewHolder.updateUsers(event.getModels());
        }
    }

    /**
     * 服务器下载状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerDownloadsEvent(ServerDownloadsEvent event) {
        if (null != downloadsCardViewHolder) {
            downloadsCardViewHolder.update(event.getModel());
        }
    }

    /**
     * 服务器种子状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerTorrentsEvent(ServerTorrentsEvent event) {
        if (null != torrentsCardViewHolder) {
            torrentsCardViewHolder.update(event.getModels());
        }
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
                            presenter.addMagnetTask(magnet);
                            LoadingDialog.showProgressDialog(getActivity(),
                                    getString(R.string.add_task_magnet_loading));
                        }

                        @Override
                        public void addTorrentFileTask(File file) {
                            presenter.addTorrentTask(file);
                            LoadingDialog.showProgressDialog(getActivity(),
                                    getString(R.string.add_task_torrent_loading));
                        }

                        @Override
                        public void pickTorrentFile() {
                            //选择种子文件
                            AddTaskSheet.pickTorrent(getActivity());
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
     * 显示剪切板中的磁力链
     *
     * @param magnet
     */
    private void showClipMagnetDialog(@NonNull String magnet) {
        this.clipMagnet = magnet;
        ParamsCacheManager.get(getActivity()).setClipMagnet(magnet);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_find_magnet)
                .setMessage(magnet)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_add_task, (dialog, which) -> {
                    LoadingDialog.showProgressDialog(getActivity(),
                            getString(R.string.add_task_magnet_loading));
                    presenter.addMagnetTask(magnet);
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //已经读取过剪切板则不需要重复读取
        if (clipMagnetFlag) return;
        this.clipMagnetFlag = true;
        for (String magnet : Utils.findMagnetInClipBoard(getActivity())) {
            if (!magnet.equals(clipMagnet)) {
                showClipMagnetDialog(magnet);
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.clipMagnetFlag = false;
    }

    @Override
    public void addTaskSuccess() {
        LoadingDialog.dismissProgressDialog();
        ToastUtils.makeShort(titleView.getToolbar(), R.string.add_task_success).show();
    }

    @Override
    public void addTaskFail(String error) {
        LoadingDialog.dismissProgressDialog();
        ToastUtils.makeShort(titleView.getToolbar(), error).show();
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
        getAddTaskSheet().handlePickTorrentResult(requestCode, resultCode, data);
    }

    /**
     * 状态卡片试图持有
     */
    class StatsCardViewHolder {
        StatsCardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        /**
         * 更新服务器状态
         *
         * @param model
         */
        void updateStats(@NonNull ServerStatsModel model) {
            // TODO: 2019/2/22
        }

        /**
         * 更新已连接的用户列表
         *
         * @param models
         */
        void updateUsers(@NonNull List<ServerUserModel> models) {
            // TODO: 2019/2/22
        }
    }

    /**
     * 已下载文件试图持有
     */
    class DownloadsCardViewHolder {
        DownloadsCardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        /**
         * 更新服务器已下载文件信息
         *
         * @param model
         */
        void update(@NonNull ServerDownloadsModel model) {
            // TODO: 2019/2/22
        }
    }

    /**
     * 服务器下载任务卡片试图持有
     */
    class TorrentsCardViewHolder {
        TorrentsCardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        /**
         * 更新服务器下载任务列表
         *
         * @param models
         */
        void update(@NonNull List<ServerTorrentModel> models) {
            // TODO: 2019/2/22
        }
    }
}