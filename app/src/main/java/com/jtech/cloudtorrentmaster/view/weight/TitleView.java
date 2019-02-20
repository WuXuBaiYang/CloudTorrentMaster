package com.jtech.cloudtorrentmaster.view.weight;

import android.view.View;

import com.cy.translucentparent.StatusNavUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.jtech.cloudtorrentmaster.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 标题栏视图
 */
public class TitleView {
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AppCompatActivity activity;

    /**
     * 兼容appcompat的主构造
     *
     * @param activity
     */
    public TitleView(@NonNull AppCompatActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        activity.setSupportActionBar(toolbar);
        StatusNavUtils.setStatusBarColor(activity, 0x00000000);
    }

    /**
     * 构造对象
     *
     * @param activity
     * @return
     */
    public static TitleView build(AppCompatActivity activity) {
        return new TitleView(activity);
    }

    /**
     * 设置标题
     *
     * @param resId
     * @return
     */
    public TitleView setTitle(@StringRes int resId) {
        return setTitle(activity.getString(resId));
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public TitleView setTitle(@NonNull CharSequence title) {
        if (null != toolbar) {
            toolbar.setTitle(title);
        }
        return this;
    }

    /**
     * 设置子标题
     *
     * @param resId
     * @return
     */
    public TitleView setSubTitle(@StringRes int resId) {
        return setSubTitle(activity.getString(resId));
    }

    /**
     * 设置子标题
     *
     * @param subTitle
     * @return
     */
    public TitleView setSubTitle(@NonNull CharSequence subTitle) {
        if (null != toolbar) {
            toolbar.setSubtitle(subTitle);
        }
        return this;
    }

    /**
     * 设置左侧按钮
     *
     * @param resId    按钮图标
     * @param listener 按钮点击事件
     * @return
     */
    public TitleView setLeftButton(@DrawableRes int resId, View.OnClickListener listener) {
        if (null != toolbar) {
            toolbar.setNavigationIcon(resId);
            toolbar.setNavigationOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置菜单
     *
     * @param resId
     * @return
     */
    public TitleView setUpMenu(@MenuRes int resId) {
        if (null != toolbar) {
            toolbar.inflateMenu(resId);
        }
        return this;
    }

    /**
     * 设置菜单的点击事件
     *
     * @param listener
     * @return
     */
    public TitleView setMenuClickListener(Toolbar.OnMenuItemClickListener listener) {
        if (null != toolbar) {
            toolbar.setOnMenuItemClickListener(listener);
        }
        return this;
    }

    /**
     * 获取标题栏对象
     *
     * @return
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 获取appbar对象
     *
     * @return
     */
    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }
}