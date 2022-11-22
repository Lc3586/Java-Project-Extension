package project.extension.mybatis.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.time.StopWatch;
import project.extension.openapi.annotations.*;
import project.extension.openapi.model.ApiData.IPagination;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * 分页设置
 *
 * @author LCTR
 * @date 2022-03-24
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"userPageHelper",
                     "watch"})
public class Pagination
        implements IPagination {
    public Pagination() {
        this.watch.start();
    }

    @OpenApiDescription("不分页")
    @OpenApiSchema("false")
    private Boolean nope = false;

    @OpenApiDescription("使用PageHelper插件进行分页")
    @OpenApiSchema("false")
    private Boolean userPageHelper = false;

    @OpenApiDescription("页码")
    @OpenApiSchema("1")
    private Integer pageNum = 1;

    @OpenApiDescription("每页数据量")
    @OpenApiSchema("50")
    private Integer pageSize = 50;

    @OpenApiDescription("总记录数")
    @OpenApiSchema(OpenApiSchemaType.string)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long recordCount;

    @OpenApiDescription("总页数")
    @OpenApiSchema(OpenApiSchemaType.string)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long pageCount;

    /**
     * 计时器
     */
    @OpenApiIgnore
    @JsonIgnore
    private final StopWatch watch = new StopWatch();

    /**
     * 关闭计时器
     */
    private void stopWatch() {
        this.watch.stop();
    }

    /**
     * 获取计时器当前耗时
     *
     * @param stop 同时关闭计时器
     * @return 耗时，单位ms
     */
    public Long getWatchTime(boolean stop) {
        if (stop) stopWatch();
        return this.watch.getTime();
    }

    /**
     * 是否不分页
     */
    public Boolean getNope() {
        return nope;
    }

    public void setNope(Boolean nope) {
        this.nope = nope;
    }

    /**
     * 使用PageHelper插件进行分页
     * <p>默认值 false</p>
     */
    public Boolean getUserPageHelper() {
        return userPageHelper;
    }

    public void setUserPageHelper(Boolean userPageHelper) {
        this.userPageHelper = userPageHelper;
    }

    /**
     * 页码
     * <p>默认值 1</p>
     */
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        this.pageNum = pageNum;
    }

    /**
     * 每页数据量
     * <p>默认值 50</p>
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) pageSize = 50;
        this.pageSize = pageSize;
        setPageCount();
    }

    /**
     * 总记录数
     */
    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
        setPageCount();

    }

    public void setRecordCountInteger(Integer recordCount) {
        this.recordCount = Long.parseLong(Integer.toString(recordCount));
    }

    /**
     * 总页数
     */
    public Long getPageCount() {
        return this.pageCount;
    }

    private void setPageCount() {
        if (this.recordCount == null || this.recordCount <= 0) this.pageCount = 0L;
        else {
            long pages = this.recordCount / this.pageSize;
            if (this.recordCount % this.pageSize != 0) pages += 1;
            this.pageCount = pages;
        }
    }
}
