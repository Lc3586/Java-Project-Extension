package project.extension.openapi.model.ApiData;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 接口数据结构方案
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum ApiDataSchema {
    /**
     * 默认
     * <p>默认为偌依框架的数据结构</p>
     */
    @OpenApiDescription("Default")
    Default(0, "Default"),
    /**
     * Layui
     * <p>https://www.layui.com/doc/modules/table.html#response</p>
     */
    @OpenApiDescription("Layui")
    Layui(1, "Layui"),
    /**
     * JqGrid
     * <p>https://blog.mn886.net/jqGrid/api/jsondata/index.jsp</p>
     */
    @OpenApiDescription("JqGrid")
    JqGrid(2, "JqGrid"),
    /**
     * Easyui
     * <p>http://www.jeasyui.net/plugins/183.html</p>
     */
    @OpenApiDescription("Easyui")
    Easyui(3, "Easyui"),
    /**
     * BootstrapTable
     * <p>https://bootstrap-table.com/docs/api/table-options/</p>
     */
    @OpenApiDescription("BootstrapTable")
    BootstrapTable(4, "BootstrapTable"),
    /**
     * antdVue
     * <p>https://www.antdv.com/components/list-cn/#api</p>
     */
    @OpenApiDescription("AntdVue")
    AntdVue(5, "AntdVue"),
    /**
     * elementVue
     * <p>https://element-plus.org/#/zh-CN/component/pagination</p>
     */
    @OpenApiDescription("ElementVue")
    ElementVue(6, "ElementVue"),
    /**
     * ruoyi
     * <p></p>
     */
    @OpenApiDescription("Ruoyi")
    Ruoyi(7, "Ruoyi");

    /**
     * @param index 索引
     * @param value 值
     */
    ApiDataSchema(int index, String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    int index;

    /**
     * 值
     */
    String value;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取字符串
     *
     * @return 值
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static ApiDataSchema toEnum(String value) throws IllegalArgumentException {
        return ApiDataSchema.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ApiDataSchema toEnum(int index) throws IllegalArgumentException {
        for (ApiDataSchema value : ApiDataSchema.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
