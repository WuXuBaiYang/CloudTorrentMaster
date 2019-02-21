package com.jtech.cloudtorrentmaster.view.weight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.utils.ToastUtils;
import com.jtech.cloudtorrentmaster.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 添加下载任务的sheet
 */
public class AddTaskSheet extends BottomSheetDialog implements DialogInterface.OnDismissListener,
        View.OnClickListener {
    private final static int REQUEST_CODE_PICK_TORRENT = 0x001;

    @BindView(R.id.textinputlayout_add_task_name)
    TextInputLayout textInputLayoutName;
    @BindView(R.id.textinputlayout_add_task_hash)
    TextInputLayout textInputLayoutHash;
    @BindView(R.id.textinputlayout_add_task_tracker)
    TextInputLayout textInputLayoutTracker;
    @BindView(R.id.linearlayout_add_task_trackers)
    LinearLayout linearLayoutTrackers;
    @BindView(R.id.textview_add_task_add_torrent)
    TextView textViewAddTorrent;
    @BindView(R.id.linearlayout_add_task_magnet_info)
    LinearLayout linearLayoutMagnetInfo;
    @BindView(R.id.linearlayout_add_task_torrent_info)
    LinearLayout linearLayoutTorrentInfo;
    @BindView(R.id.textview_add_task_torrent_name)
    TextView textViewTorrentName;

    private OnAddTaskListener listener;
    private Activity activity;
    private File torrentFile;

    private AddTaskSheet(@NonNull Activity context, int theme) {
        super(context, theme);
        this.activity = context;
        //设置dismiss监听
        setOnDismissListener(this);
        //设置主视图
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(context)
                .inflate(R.layout.view_add_task_sheet, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        //添加内容输入监听
        Objects.requireNonNull(textInputLayoutHash.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutHash));
    }

    /**
     * 构造对象
     *
     * @param context
     * @return
     */
    public static AddTaskSheet build(@NonNull Activity context) {
        return new AddTaskSheet(context, R.style.BottomSheetDialog);
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public AddTaskSheet setListener(OnAddTaskListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 选择种子文件
     *
     * @return
     */
    private void pickTorrent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/x-bittorrent");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_TORRENT);
    }

    /**
     * 处理种子文件选择返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public boolean handlePickTorrentResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == REQUEST_CODE_PICK_TORRENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (null != uri) {
                String filePath = Utils.getPath(getContext(), uri);
                if (!TextUtils.isEmpty(filePath)) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        setTorrentFileInfo(file);
                    } else {
                        ToastUtils.makeShort(linearLayoutTrackers,
                                R.string.add_task_sheet_not_found_torrent).show();
                    }
                } else {
                    ToastUtils.makeShort(linearLayoutTrackers,
                            R.string.add_task_sheet_torrent_unexists).show();
                }
            } else {
                ToastUtils.makeShort(linearLayoutTrackers,
                        R.string.add_task_sheet_system_error).show();
            }
            return true;
        }
        return false;
    }

    /**
     * 设置种子文件信息
     *
     * @param file
     */
    private void setTorrentFileInfo(@NonNull File file) {
        this.torrentFile = file;
        textViewAddTorrent.setText(R.string.add_task_sheet_replace_torrent);
        //显示种子信息隐藏磁力链信息
        linearLayoutTorrentInfo.setVisibility(View.VISIBLE);
        linearLayoutMagnetInfo.setVisibility(View.GONE);
        //设置种子名称
        textViewTorrentName.setText(file.getName());
        //设置删除种子操作
        // TODO: 2019/2/21
    }

    /**
     * 重置页面
     */
    private void resetSheet() {
        //清空所有填写的内容
        Objects.requireNonNull(textInputLayoutName
                .getEditText()).setText("");
        textInputLayoutName.clearFocus();
        Objects.requireNonNull(textInputLayoutHash
                .getEditText()).setText("");
        textInputLayoutHash.clearFocus();
        Objects.requireNonNull(textInputLayoutTracker
                .getEditText()).setText("");
        textInputLayoutTracker.clearFocus();
        //清空trackers的子视图
        linearLayoutTrackers.removeAllViews();
        //隐藏种子信息
        textViewAddTorrent.setText(R.string.add_task_sheet_add_torrent);
        linearLayoutMagnetInfo.setVisibility(View.VISIBLE);
        linearLayoutTorrentInfo.setVisibility(View.GONE);
    }

    /**
     * 提交操作
     */
    private boolean commitTask() {
        if (null == listener) return false;
        //种子文件不为空则提交种子
        if (null != torrentFile) {
            listener.addTorrentFileTask(torrentFile);
            return true;
        }
        //判断hash值是否填写
        String hash = Objects.requireNonNull(textInputLayoutHash
                .getEditText()).getText().toString();
        if (TextUtils.isEmpty(hash)) {
            textInputLayoutHash.setError(getContext()
                    .getString(R.string.input_error_empty));
            return false;
        }
        //获取名称
        String name = Objects.requireNonNull(textInputLayoutName
                .getEditText()).getText().toString();
        //获取固定tracker
        List<String> trackers = new ArrayList<>();
        trackers.add(Objects.requireNonNull(textInputLayoutTracker
                .getEditText()).getText().toString());
        //获取其他tracker
        for (int i = 0; i < linearLayoutTrackers.getChildCount(); i++) {
            TextInputLayout child = linearLayoutTrackers.getChildAt(i)
                    .findViewById(R.id.textinputlayout_add_task_sheet_tracker_item);
            trackers.add(Objects.requireNonNull(
                    child.getEditText()).getText().toString());
        }
        //拼接磁力链
        String magnet = Utils.jointMagnet(hash, name,
                trackers.toArray(new String[]{}));
        listener.addMagnetTask(magnet);
        return true;
    }

    /**
     * 添加tracker服务器空位
     */
    private void addTrackerItem() {
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_add_task_sheet_tracker_item, null);
        TextInputLayout textInputLayout = itemView.findViewById(
                R.id.textinputlayout_add_task_sheet_tracker_item);
        //设置粘贴按钮事件
        itemView.findViewById(R.id.imageview_add_task_sheet_tracker_item_paste)
                .setOnClickListener(v -> setPasteWithInput(
                        Objects.requireNonNull(textInputLayout.getEditText())));
        //设置移除按钮事件
        itemView.findViewById(R.id.imageview_add_task_sheet_tracker_item_remove)
                .setOnClickListener(v -> linearLayoutTrackers.removeView(itemView));
        //将视图添加到tracker容器中
        linearLayoutTrackers.addView(itemView);
    }

    /**
     * 选择剪切板内容并赋值
     *
     * @param editText
     */
    private void setPasteWithInput(@NonNull EditText editText) {
        //显示剪切板选择popup，并将选择后的内容粘贴到目标输入框中，会替换原有内容
        ClipSelectPopup.build(getContext())
                .setListener(editText::setText)
                .show(editText);
    }

    @OnClick({R.id.imageview_add_task_sheet_close,
            R.id.imageview_add_task_sheet_done,
            R.id.textview_add_task_add_torrent,
            R.id.textview_add_task_add_tracker,
            R.id.imageview_add_task_name_paste,
            R.id.imageview_add_task_hash_paste,
            R.id.imageview_add_task_tracker_paste})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_add_task_add_torrent://种子添加
                pickTorrent();
                break;
            case R.id.textview_add_task_add_tracker://添加tracker服务器
                addTrackerItem();
                break;
            case R.id.imageview_add_task_sheet_close://关闭按钮
                dismiss();
                break;
            case R.id.imageview_add_task_sheet_done://完成按钮
                if (commitTask()) dismiss();
                break;
            case R.id.imageview_add_task_name_paste://粘贴-名字
                setPasteWithInput(Objects.requireNonNull(
                        textInputLayoutName.getEditText()));
                break;
            case R.id.imageview_add_task_hash_paste://粘贴-hash值
                setPasteWithInput(Objects.requireNonNull(
                        textInputLayoutHash.getEditText()));
                break;
            case R.id.imageview_add_task_tracker_paste://粘贴-tracker
                setPasteWithInput(Objects.requireNonNull(
                        textInputLayoutTracker.getEditText()));
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        resetSheet();
    }

    /**
     * 添加下载任务监听
     */
    public interface OnAddTaskListener {
        void addMagnetTask(String magnet);

        void addTorrentFileTask(File file);
    }

    /**
     * 输入变化回调
     */
    private class OnTextWatcher implements TextWatcher {
        private TextInputLayout textInputLayout;

        public OnTextWatcher(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textInputLayout.setError("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}