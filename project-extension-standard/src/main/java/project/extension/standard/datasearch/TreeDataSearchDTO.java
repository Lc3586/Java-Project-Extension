package project.extension.standard.datasearch;

import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.standard.datasearch.DataSearchDTO;

/**
 * 树状数据搜索参数
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class TreeDataSearchDTO extends DataSearchDTO {
    /**
     * 父Id
     */
    @OpenApiDescription("父Id")
    private String parentId;

    /**
     * 获取所有层级的数据
     * <p>默认值 true</p>
     */
    @OpenApiDescription("获取所有层级的数据")
    @OpenApiSchema("true")
    private Boolean allLevel = true;

    /**
     * 获取指定层级数的数据
     * <p>从parentId对应的数据开始计算层级</p>
     * <p>此值为2，则代表只向下获取2层数据</p>
     */
    @OpenApiDescription("获取指定层级数的数据 \r从parentId对应的数据开始计算层级 \r此值为2，则代表只向下获取2层数据")
    private Integer rank;

    /**
     * 父Id
     */
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取所有层级的数据
     */
    public Boolean getAllLevel() {
        return allLevel;
    }

    public void setAllLevel(Boolean allLevel) {
        this.allLevel = allLevel;
    }

    /**
     * 获取指定层级数的数据
     * <p>从parentId对应的数据开始计算层级</p>
     * <p>此值为2，则代表只向下获取2层数据</p>
     */
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
