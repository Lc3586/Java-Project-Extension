package project.extension.mybatis.edge.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.TableSetting;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSubTag;

/**
 * 测试自增主键
 *
 * @author LCTR
 * @date 2023-03-01
 */
@TableSetting
//新增此属性防止mapper的xml文件报错
@Alias("TestIdentityEntity")
public class TestIdentityEntity {
    /**
     * 自增主键
     */
    @OpenApiDescription("自增主键")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(isPrimaryKey = true,
                   isIdentity = true,
                   //使用Oracle时需要指定自增主键的序列
                   oracleIdentitySequence = "ISEQ$$_73261")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 编号
     */
    @OpenApiDescription("编号")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(length = 36)
    private String no;

    /**
     * 自增主键
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 编号
     */
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,
                                   ToStringStyle.MULTI_LINE_STYLE)
                .append("id",
                        getId())
                .append("number",
                        getNo())
                .toString();
    }
}
