package project.extension.openapi.model.ApiData;

/**
 * 分页设置
 *
 * @author LCTR
 * @date 2022-03-26
 */
public interface IPagination {
    /**
     * 页码
     */
    Integer getPageNum();

    /**
     * 页面数据量
     */
    Integer getPageSize();

    /**
     * 总记录数
     */
    Long getRecordCount();

    /**
     * 总页数
     */
    Long getPageCount();

    /**
     * 获取计时器当前耗时
     *
     * @param stop 同时关闭计时器
     * @return 耗时，单位ms
     */
    Long getWatchTime(boolean stop);
}
