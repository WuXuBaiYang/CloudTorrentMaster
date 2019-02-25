package com.jtech.torrentmaster.model;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器状态对象
 */
public class ServerStatsModel extends BaseModel {
    @SerializedName("Title")
    private String title = "";
    @SerializedName("Version")
    private String version = "";
    @SerializedName("Runtime")
    private String runtime = "";
    @SerializedName("Uptime")
    private String uptime = "";
    @SerializedName("System")
    private SystemModel system = new SystemModel();

    public String getTitle() {
        return title;
    }

    public ServerStatsModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ServerStatsModel setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getRuntime() {
        return runtime;
    }

    public ServerStatsModel setRuntime(String runtime) {
        this.runtime = runtime;
        return this;
    }

    public String getUptime() {
        return uptime;
    }

    public ServerStatsModel setUptime(String uptime) {
        this.uptime = uptime;
        return this;
    }

    public SystemModel getSystem() {
        return system;
    }

    public ServerStatsModel setSystem(SystemModel system) {
        this.system = system;
        return this;
    }

    /**
     * 系统信息对象
     */
    public static class SystemModel extends BaseModel {
        @SerializedName("set")
        private boolean set = false;
        @SerializedName("cpu")
        private double cpu = 0;
        @SerializedName("diskUsed")
        private long diskUsed = 0;
        @SerializedName("diskTotal")
        private long diskTotal = 0;
        @SerializedName("memoryUsed")
        private long memoryUsed = 0;
        @SerializedName("memoryTotal")
        private long memoryTotal = 0;
        @SerializedName("goMemory")
        private long goMemory = 0;
        @SerializedName("goRoutines")
        private long goRoutines = 0;

        public boolean isSet() {
            return set;
        }

        public SystemModel setSet(boolean set) {
            this.set = set;
            return this;
        }

        public double getCpu() {
            return cpu;
        }

        public SystemModel setCpu(double cpu) {
            this.cpu = cpu;
            return this;
        }

        public long getDiskUsed() {
            return diskUsed;
        }

        public SystemModel setDiskUsed(long diskUsed) {
            this.diskUsed = diskUsed;
            return this;
        }

        public long getDiskTotal() {
            return diskTotal;
        }

        public SystemModel setDiskTotal(long diskTotal) {
            this.diskTotal = diskTotal;
            return this;
        }

        public long getMemoryUsed() {
            return memoryUsed;
        }

        public SystemModel setMemoryUsed(long memoryUsed) {
            this.memoryUsed = memoryUsed;
            return this;
        }

        public long getMemoryTotal() {
            return memoryTotal;
        }

        public SystemModel setMemoryTotal(long memoryTotal) {
            this.memoryTotal = memoryTotal;
            return this;
        }

        public long getGoMemory() {
            return goMemory;
        }

        public SystemModel setGoMemory(long goMemory) {
            this.goMemory = goMemory;
            return this;
        }

        public long getGoRoutines() {
            return goRoutines;
        }

        public SystemModel setGoRoutines(long goRoutines) {
            this.goRoutines = goRoutines;
            return this;
        }
    }
}