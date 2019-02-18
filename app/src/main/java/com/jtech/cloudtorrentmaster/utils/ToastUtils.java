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
    public static Snackbar showShort(@NonNull View view, @StringRes int resId) {
        return showShort(view, view.getResources().getString(resId));
    }

    /**
     * 短时toast
     *
     * @param view
     * @param text
     */
    public static Snackbar showShort(@NonNull View view, @NonNull CharSequence text) {
        return show(view, text, Snackbar.LENGTH_SHORT);
    }

    /**
     * 长时toast
     *
     * @param view
     * @param resId
     */
    public static Snackbar showLong(@NonNull View view, @StringRes int resId) {
        return showLong(view, view.getResources().getString(resId));
    }

    /**
     * 长时toast
     *
     * @param view
     * @param text
     */
    public static Snackbar showLong(@NonNull View view, @NonNull CharSequence text) {
        return show(view, text, Snackbar.LENGTH_LONG);
    }

    /**
     * 展示toast
     *
     * @param view
     * @param text
     * @param duration
     */
    public static Snackbar show(@NonNull View view, @NonNull CharSequence text, int duration) {
        return Snackbar.make(view, text, duration);
    }

}