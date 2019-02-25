package com.jtech.torrentmaster.view.weight;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 剪切板内容选择popup
 */
public class ClipSelectPopup extends ListPopupWindow implements AdapterView.OnItemClickListener {
    private OnClipSelectPopupListener listener;
    private SimpleAdapter adapter;

    public ClipSelectPopup(@NonNull Context context) {
        super(context);
        //设置item点击事件
        setOnItemClickListener(this);
        //获取剪切板内容列表
        ClipboardManager clipboardManager = (ClipboardManager)
                context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < Objects
                .requireNonNull(clipData).getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            Map<String, String> map = new HashMap<>();
            map.put("text", item.getText().toString());
            list.add(map);
        }
        //设置适配器
        this.adapter = new SimpleAdapter(context, list, android.R.layout.simple_list_item_1,
                new String[]{"text"}, new int[]{android.R.id.text1});
        setAdapter(adapter);
    }

    /**
     * 构造对象
     *
     * @param context
     * @return
     */
    public static ClipSelectPopup build(Context context) {
        return new ClipSelectPopup(context);
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public ClipSelectPopup setListener(OnClipSelectPopupListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 展示并设置anchor视图
     *
     * @param anchorView
     * @return
     */
    public void show(@NonNull View anchorView) {
        setAnchorView(anchorView);
        show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener) {
            Map<String, String> map = (Map<String, String>) adapter.getItem(position);
            listener.onItemSelect(map.get("text"));
        }
        dismiss();
    }

    /**
     * 剪切板选择popup监听
     */
    public interface OnClipSelectPopupListener {
        void onItemSelect(String pasteText);
    }
}