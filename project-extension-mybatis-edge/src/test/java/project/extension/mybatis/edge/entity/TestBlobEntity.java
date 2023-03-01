package project.extension.mybatis.edge.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.TableSetting;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSubTag;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 测试读写文件数据
 *
 * @author LCTR
 * @date 2023-03-01
 */
@TableSetting
//新增此属性防止mapper的xml文件报错
@Alias("TestBlobEntity")
public class TestBlobEntity {
    /**
     * 主键
     */
    @OpenApiDescription("主键")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(isPrimaryKey = true,
                   length = 36)
    private String id;

    /**
     * 文件数据
     */
    @OpenApiDescription("文件数据")
    @OpenApiSubTag({"List",
                    "Create"})
    private byte[] bytes;

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
     * 文件数据
     */
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,
                                   ToStringStyle.MULTI_LINE_STYLE)
                .append("id",
                        getId())
                .append("bytes",
                        new String(Base64.getEncoder()
                                         .encode(getBytes()),
                                   StandardCharsets.UTF_8))
                .toString();
    }
}
