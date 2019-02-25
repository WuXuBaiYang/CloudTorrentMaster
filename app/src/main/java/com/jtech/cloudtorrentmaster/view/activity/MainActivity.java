package com.jtech.cloudtorrentmaster.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.jtech.cloudtorrentmaster.model.event.ServerConnectEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.utils.ToastUtils;
import com.jtech.cloudtorrentmaster.utils.Utils;
import com.jtech.cloudtorrentmaster.view.adapter.ServerStatsUserAdapter;
import com.jtech.cloudtorrentmaster.view.adapter.TorrentsAdapter;
import com.jtech.cloudtorrentmaster.view.weight.AddTaskSheet;
import com.jtech.cloudtorrentmaster.view.weight.LoadingDialog;
import com.jtech.cloudtorrentmaster.view.weight.ServerSelectPopup;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.activity.BaseActivity;
import com.jtechlib2.view.recycler.JRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
     * 链接状态变化监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectStatsEvent(ServerConnectEvent event) {
        if (null != statsCardViewHolder) {
            statsCardViewHolder.setConnectStats(event.getConnectStats());
        }
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
        @BindView(R.id.imageview_main_content_stats_arrow)
        ImageView imageViewArrow;
        @BindView(R.id.textview_main_content_stats_preview_title)
        TextView textViewPreviewTitle;
        @BindView(R.id.textview_main_content_stats_preview_disk)
        TextView textViewPreviewDisk;
        @BindView(R.id.textview_main_content_stats_preview_memory)
        TextView textViewPreviewMemory;
        @BindView(R.id.textview_main_content_stats_preview_cpu)
        TextView textViewPreviewCPU;

        @BindView(R.id.linearlayout_main_content_stats)
        LinearLayout linearLayoutStats;
        @BindView(R.id.textview_main_content_stats_title)
        TextView textViewTitle;
        @BindView(R.id.textview_main_content_stats_disk)
        TextView textViewDisk;
        @BindView(R.id.textview_main_content_stats_memory)
        TextView textViewMemory;
        @BindView(R.id.textview_main_content_stats_cpu)
        TextView textViewCPU;
        @BindView(R.id.textview_main_content_stats_runtime)
        TextView textViewRunTime;
        @BindView(R.id.textview_main_content_stats_go_memory)
        TextView textViewGoMemory;
        @BindView(R.id.textview_main_content_stats_go_routines)
        TextView textViewGoRoutines;
        @BindView(R.id.textview_main_content_stats_uptime)
        TextView textViewUptime;
        @BindView(R.id.textview_main_content_stats_users)
        TextView textViewUsers;
        @BindView(R.id.jrecyclerview_main_content_stats_users)
        JRecyclerView jRecyclerViewUsers;
        @BindView(R.id.imageview_main_content_stats)
        ImageView imageViewStats;

        private ServerStatsUserAdapter userAdapter;

        StatsCardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            //设置已连接用户列表
            jRecyclerViewUsers.setLayoutManager(
                    new GridLayoutManager(getActivity(), 2));
            userAdapter = new ServerStatsUserAdapter(getActivity());
            jRecyclerViewUsers.setAdapter(userAdapter);
            //设置默认值
            updateStats(new ServerStatsModel());
            //默认状态为关闭
            imageViewStats.setEnabled(false);
        }

        /**
         * 计算比例
         *
         * @param used
         * @param total
         * @return
         */
        private String getRatio(long used, long total) {
            if (total <= 0) return 0 + "%";
            return BigDecimal.valueOf(used)
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.DOWN)
                    .doubleValue() + "%";
        }

        /**
         * 更新服务器状态
         *
         * @param model
         */
        void updateStats(@NonNull ServerStatsModel model) {
            ServerStatsModel.SystemModel systemModel = model.getSystem();
            //设置服务器名称
            textViewPreviewTitle.setText(model.getTitle());
            textViewTitle.setText(String.format(getString(
                    R.string.server_stats_title), model.getTitle(), model.getVersion()));
            //设置磁盘使用率
            String diskUsed = Formatter.formatFileSize(
                    getActivity(), systemModel.getDiskUsed());
            String diskTotal = Formatter.formatFileSize(
                    getActivity(), systemModel.getDiskTotal());
            String diskRatio = getRatio(
                    systemModel.getDiskUsed(), systemModel.getDiskTotal());
            textViewPreviewDisk.setText(String.format(
                    getString(R.string.server_stats_preview_disk), String.valueOf(diskRatio)));
            textViewDisk.setText(String.format(
                    getString(R.string.server_stats_disk), diskUsed, diskTotal, diskRatio));
            //设置内存使用率
            String memoryUsed = Formatter.formatFileSize(
                    getActivity(), systemModel.getMemoryUsed());
            String memoryTotal = Formatter.formatFileSize(
                    getActivity(), systemModel.getMemoryTotal());
            String memoryRatio = getRatio(systemModel.getMemoryUsed(),
                    systemModel.getMemoryTotal());
            textViewPreviewMemory.setText(String.format(
                    getString(R.string.server_stats_preview_memory), memoryRatio));
            textViewMemory.setText(String.format(
                    getString(R.string.server_stats_memory), memoryUsed, memoryTotal, memoryRatio));
            //设置cpu使用率
            String cpuRatio = BigDecimal.valueOf(systemModel.getCpu())
                    .setScale(2, RoundingMode.DOWN).doubleValue() + "%";
            textViewPreviewCPU.setText(String.format(
                    getString(R.string.server_stats_cpu), cpuRatio));
            textViewCPU.setText(String.format(
                    getString(R.string.server_stats_cpu), cpuRatio));
            //设置运行时环境版本号
            textViewRunTime.setText(String.format(
                    getString(R.string.server_stats_runtime), model.getRuntime()));
            //设置GoMemory
            textViewGoMemory.setText(String.format(getString(R.string.server_stats_go_memory),
                    Formatter.formatFileSize(getActivity(), systemModel.getGoMemory())));
            //设置go并发
            textViewGoRoutines.setText(String.format(getString(
                    R.string.server_stats_go_routines), systemModel.getGoRoutines()));
            //设置更新时间
            textViewUptime.setText(String.format(getString(
                    R.string.server_stats_uptime), model.getUptime()));
        }

        void setConnectStats(int connectStats) {
            switch (connectStats) {
                case -1://未连接
                    imageViewStats.setEnabled(false);
                    break;
                case 0://连接中
                case 1://已连接
                    imageViewStats.setEnabled(true);
                    break;
            }
        }

        /**
         * 更新已连接的用户列表
         *
         * @param models
         */
        void updateUsers(@NonNull List<ServerUserModel> models) {
            textViewUsers.setText(String.format(
                    getString(R.string.server_stats_users), models.size()));
            userAdapter.setDatas(models);
        }

        /**
         * 是否展开发片
         *
         * @param isExpand
         */
        void expandCard(boolean isExpand) {
            linearLayoutStats.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        }

        /**
         * 切换卡片状态
         *
         * @param v
         */
        void switchCard(View v) {
            v.setSelected(!v.isSelected());
            expandCard(v.isSelected());
            rotationArrow(v.isSelected());
            downloadsCardViewHolder.expandCard(false);
            torrentsCardViewHolder.expandCard(false);
        }

        /**
         * 旋转箭头
         *
         * @param isUp
         */
        private void rotationArrow(boolean isUp) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    isUp ? R.anim.arrow_rotation_up : R.anim.arrow_rotation_down);
            imageViewArrow.startAnimation(animation);
        }

        @OnClick(R.id.cardview_main_content_stats)
        void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardview_main_content_stats://状态卡片展开或收起
                    switchCard(v);
                    break;
            }
        }
    }

    /**
     * 服务器下载任务卡片视图持有
     */
    class TorrentsCardViewHolder {
        @BindView(R.id.textview_main_content_torrents_preview_title)
        TextView textViewPreviewTitle;
        @BindView(R.id.progressbar_main_content_total)
        ProgressBar progressBarTotal;
        @BindView(R.id.imageview_main_content_torrents_arrow)
        ImageView imageViewArrow;

        @BindView(R.id.linearlayout_main_content_torrents)
        LinearLayout linearLayoutTorrents;
        @BindView(R.id.textview_main_content_torrents_title)
        TextView textViewTitle;
        @BindView(R.id.jrecyclerview_main_content_torrents)
        JRecyclerView jRecyclerViewTorrents;

        private TorrentsAdapter torrentsAdapter;

        TorrentsCardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            //设置种子下载任务列表
            jRecyclerViewTorrents.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.torrentsAdapter = new TorrentsAdapter(getActivity());
            jRecyclerViewTorrents.setAdapter(torrentsAdapter);
            //设置默认值
            update(new ArrayList<>());
        }

        /**
         * 更新服务器下载任务列表
         *
         * @param models
         */
        void update(@NonNull List<ServerTorrentModel> models) {
            //设置标题
            BigDecimal totalProgress = getTotalProgress(models);
            String taskCount = String.valueOf(models.size());
            textViewPreviewTitle.setText(
                    String.format(getString(R.string.server_torrents_preview_title),
                            taskCount, totalProgress.doubleValue() + "%"));
            textViewTitle.setText(String.format(getString(R.string.server_torrents_title), taskCount));
            //设置预览的总进度条
            progressBarTotal.setProgress(totalProgress.intValue());
            //设置下载列表适配器
            torrentsAdapter.setDatas(models);
        }

        /**
         * 计算总进度
         *
         * @param models
         * @return
         */
        private BigDecimal getTotalProgress(List<ServerTorrentModel> models) {
            if (models.size() <= 0) return BigDecimal.ZERO;
            BigDecimal totalProgress = BigDecimal.ZERO;
            for (ServerTorrentModel model : models) {
                totalProgress = totalProgress.add(
                        BigDecimal.valueOf(model.getPercent()));
            }
            totalProgress = totalProgress
                    .divide(BigDecimal.valueOf(models.size())
                            .multiply(BigDecimal.valueOf(100)), 4, RoundingMode.DOWN)
                    .multiply(BigDecimal.valueOf(100));
            return totalProgress;
        }

        /**
         * 是否展开发片
         *
         * @param isExpand
         */
        void expandCard(boolean isExpand) {
            linearLayoutTorrents.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        }

        /**
         * 切换卡片状态
         *
         * @param v
         */
        void switchCard(View v) {
            v.setSelected(!v.isSelected());
            expandCard(v.isSelected());
            rotationArrow(v.isSelected());
            statsCardViewHolder.expandCard(false);
            downloadsCardViewHolder.expandCard(false);
        }

        /**
         * 旋转箭头
         *
         * @param isUp
         */
        private void rotationArrow(boolean isUp) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    isUp ? R.anim.arrow_rotation_up : R.anim.arrow_rotation_down);
            imageViewArrow.startAnimation(animation);
        }

        @OnClick(R.id.cardview_main_content_torrents)
        void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardview_main_content_torrents://卡片展开或收起
                    if (torrentsAdapter.getItemCount() > 0 ||
                            (torrentsAdapter.getItemCount() <= 0 && !v.isSelected())) {
                        switchCard(v);
                    } else {
                        ToastUtils.makeShort(linearLayoutTorrents,
                                getString(R.string.server_torrents_empty));
                    }
                    break;
            }
        }
    }

    /**
     * 已下载文件视图持有
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

        /**
         * 是否展开发片
         *
         * @param isExpand
         */
        void expandCard(boolean isExpand) {
//            linearLayoutStats.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        }

        /**
         * 切换卡片状态
         *
         * @param v
         */
        void switchCard(View v) {
            v.setSelected(!v.isSelected());
            expandCard(v.isSelected());
            rotationArrow(v.isSelected());
            statsCardViewHolder.expandCard(false);
            torrentsCardViewHolder.expandCard(false);
        }

        /**
         * 旋转箭头
         *
         * @param isUp
         */
        private void rotationArrow(boolean isUp) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    isUp ? R.anim.arrow_rotation_up : R.anim.arrow_rotation_down);
//            imageViewArrow.startAnimation(animation);
        }

//        @OnClick(R.id.cardview_main_content_stats)
//        void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.cardview_main_content_stats://状态卡片展开或收起
//                    switchCard(v);
//                    break;
//            }
//        }
    }
}