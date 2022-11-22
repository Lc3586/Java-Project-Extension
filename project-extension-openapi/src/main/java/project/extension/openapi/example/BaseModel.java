package project.extension.openapi.example;

import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSubTag;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.Date;

/**
 * 示例代码 基础模型
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class BaseModel {
    @OpenApiSubTag({"List", "_Edit", "Detail"})
    private String id;

    @OpenApiSubTag({"List", "Create", "Edit", "Detail"})
    private String name;

    @OpenApiSubTag({"List", "Create", "Edit", "Detail"})
    private int maxInt;

    @OpenApiSubTag({"_List", "_Create", "_Edit", "_Detail"})
    private long maxLong;

    @OpenApiSubTag({"Create", "Edit", "Detail"})
    private String content;

    private String creatorId;

    @OpenApiSubTag({"List", "Detail"})
    private String creatorName;

    @OpenApiSubTag({"List", "Detail"})
    @OpenApiSchema(type = OpenApiSchemaType.string, format = OpenApiSchemaFormat.string_datetime)
    private Date createTime;

    @OpenApiSubTag({"Detail"})
    @OpenApiSchema(type = OpenApiSchemaType.string, format = OpenApiSchemaFormat.string_datetime)
    private Date updateTime;

    /**
     * 主键
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * int类型
     */
    public int getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(int maxInt) {
        this.maxInt = maxInt;
    }

    /**
     * long类型
     */
    public long getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(long maxLong) {
        this.maxLong = maxLong;
    }

    /**
     * 内容
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 创建者
     */
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 创建者名称
     */
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 最近更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
