package com.jtech.torrentmaster.manager;

import android.app.Activity;
import android.os.Bundle;

import com.jtech.torrentmaster.model.ServerInfoModel;
import com.jtech.torrentmaster.utils.ActivityGo;
import com.jtech.torrentmaster.view.activity.MainActivity;
import com.jtech.torrentmaster.view.activity.ServerSelectActivity;

/**
 * 页面跳转管理
 */
public class ActivityGoManager {
    /**
     * 跳转到服务器选择页面
     *
     * @param activity
     */
    public static void goServerSelectPage(Activity activity) {
        ActivityGo.build(activity, ServerSelectActivity.class)
                .go();
    }

    /**
     * 跳转到主页
     *
     * @param activity
     */
    public static void goMainPage(Activity activity, ServerInfoModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.KEY_SERVER_INFO, model);
        ActivityGo.build(activity, MainActivity.class)
                .addBundle(bundle)
                .go();
    }
}