package com.jtech.cloudtorrentmaster.net;

import com.jtech.cloudtorrentmaster.model.MagnetModel;
import com.jtech.cloudtorrentmaster.model.request.ServerConfigureRequest;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 种子离线服务接口
 */
public interface CloudTorrentApi {
    /**
     * 资源列表搜索
     *
     * @param site      搜索地址
     * @param pageIndex 当前页码
     * @param query     搜索内容
     * @return
     */
    @GET("search/{site}")
    Observable<List<MagnetModel>> search(
            @Path("site") String site,
            @Query("page") int pageIndex,
            @Query("query") String query
    );

    /**
     * 搜索二级列表寻找信息
     *
     * @param site 搜索地址
     * @param item 二级页面地址
     * @return
     */
    @GET("search/{site}")
    Observable<List<MagnetModel>> searchItem(
            @Path("site") String site,
            @Query("item") String item
    );

    /**
     * 添加一个磁力链任务
     *
     * @param body application/json;charset=UTF-8
     * @return
     */
    @POST("api/magnet")
    Observable<String> addMagnetTask(
            @Body RequestBody body
    );

    /**
     * 添加种子任务
     *
     * @param body application/json;charset=UTF-8
     * @return
     */
    @POST("api/torrentfile")
    Observable<String> addTorrentTask(
            @Body RequestBody body
    );

    /**
     * 修改任务状态 start/stop/delete:种子hash值
     *
     * @param body application/json;charset=UTF-8
     * @return
     */
    @POST("api/torrent")
    Observable<String> modifyTask(
            @Body RequestBody body
    );

    /**
     * 修改服务器配置参数
     *
     * @param request
     * @return
     */
    @POST("api/configure")
    Observable<String> modifyServerConfigure(
            @Body ServerConfigureRequest request
    );

    /**
     * 删除离线文件
     *
     * @param filePath 文件路径
     * @return
     */
    @DELETE("{filePath}")
    Observable<String> deleteFile(
            @Path("filePath") String filePath
    );
}