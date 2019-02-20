package com.jtech.cloudtorrentmaster.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * toast通知
 */
public class ToastUtils {
    /**
     * 短时toast
     *
     * @param view
     * @param resId
     */
    public static Snackbar makeShort(@NonNull View view, @StringRes int resId) {
        return makeShort(view, view.getResources().getString(resId));
    }

    /**
     * 短时toast
     *
     * @param view
     * @param text
     */
    public static Snackbar makeShort(@NonNull View view, @NonNull CharSequence text) {
        return make(view, text, Snackbar.LENGTH_SHORT);
    }

    /**
     * 长时toast
     *
     * @param view
     * @param resId
     */
    public static Snackbar makeLong(@NonNull View view, @StringRes int resId) {
        return makeLong(view, view.getResources().getString(resId));
    }

    /**
     * 长时toast
     *
     * @param view
     * @param text
     */
    public static Snackbar makeLong(@NonNull View view, @NonNull CharSequence text) {
        return make(view, text, Snackbar.LENGTH_LONG);
    }

    /**
     * 展示toast
     *
     * @param view
     * @param text
     * @param duration
     */
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {
        return Snackbar.make(view, text, duration);
    }

}