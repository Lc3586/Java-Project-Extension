package project.extension.mybatis.edge.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.TableSetting;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSubTag;

import java.util.Date;

/**
 * 快捷输入
 *
 * @author LCTR
 * @date 2022-01-09
 */
@TableSetting
@JSONType(ignores = "serialVersionUID")
public class CommonQuickInput {
    /**
     * 快捷输入Id
     */
    @OpenApiDescription("个人文件Id")
    @OpenApiSubTag({"OptionList",
                    "Create"})
    @ColumnSetting(primaryKey = true,
                   length = 36)
    private String id;

    /**
     * 分类
     */
    @OpenApiDescription("分类")
    @OpenApiSubTag({"OptionList",
                    "Create"})
    private String category;

    /**
     * 关键词
     */
    @OpenApiDescription("关键词")
    @OpenApiSubTag({"OptionList",
                    "Create"})
    private String keyword;

    /**
     * 内容
     */
    @OpenApiDescription("内容")
    @OpenApiSubTag({"OptionList",
                    "Create"})
    private String content;

    /**
     * 公用
     */
    @OpenApiDescription("公用")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting("PUBLIC")
    private Boolean public_;

    /**
     * 创建者
     */
    @OpenApiDescription("创建者")
    @OpenApiSubTag({"List"})
    private String createBy;

    /**
     * 创建时间
     */
    @OpenApiDescription("创建时间")
    @OpenApiSubTag({"List"})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPublic_(Boolean public_) {
        this.public_ = public_;
    }

    public Boolean getPublic_() {
        return public_;
    }

    /**
     * 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,
                                   ToStringStyle.MULTI_LINE_STYLE)
                .append("id",
                        getId())
                .append("category",
                        getCategory())
                .append("keyword",
                        getKeyword())
                .append("content",
                        getContent())
                .append("public",
                        getPublic_())
                .append("createBy",
                        getCreateBy())
                .append("createTime",
                        getCreateTime())
                .toString();
    }
}
