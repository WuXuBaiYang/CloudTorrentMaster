package com.jtech.cloudtorrentmaster.manager;

/**
 * 图标管理
 */
public class IconsManager {
    private static IconsManager manager;

    public static IconsManager get() {
        if (null == manager) {
            manager = new IconsManager();
        }
        return manager;
    }
}