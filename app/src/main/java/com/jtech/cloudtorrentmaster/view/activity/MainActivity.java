package com.jtech.cloudtorrentmaster.view.activity;

import android.annotation.SuppressLint;
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
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.MainPresenter;
import com.jtech.cloudtorrentmaster.view.weight.ServerSelectPopup;
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
        // TODO: 2019/2/19 实现侧滑菜单点击事件
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
                                //重新启动本页
                                ActivityGoManager.goMainPage(getActivity(), model);
                                finish();
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
        if (viewHolder.dismissPopup()) return;
        if (closeNavigation()) return;
        super.onBackPressed();
    }
}