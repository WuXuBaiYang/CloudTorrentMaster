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
     * @param duration
     */
    public static void showShort(@NonNull View view, @StringRes int resId, int duration) {
        show(view, view.getResources().getString(resId), Snackbar.LENGTH_SHORT);
    }

    /**
     * 短时toast
     *
     * @param view
     * @param text
     * @param duration
     */
    public static void showShort(@NonNull View view, @NonNull CharSequence text, int duration) {
        show(view, text, Snackbar.LENGTH_SHORT);
    }

    /**
     * 长时toast
     *
     * @param view
     * @param resId
     */
    public static void showLong(@NonNull View view, @StringRes int resId) {
        show(view, view.getResources().getString(resId), Snackbar.LENGTH_LONG);
    }

    /**
     * 长时toast
     *
     * @param view
     * @param text
     */
    public static void showLong(@NonNull View view, @NonNull CharSequence text) {
        show(view, text, Snackbar.LENGTH_LONG);
    }

    /**
     * 展示toast
     *
     * @param view
     * @param text
     * @param duration
     */
    public static void show(@NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar.make(view, text, duration);
    }
}