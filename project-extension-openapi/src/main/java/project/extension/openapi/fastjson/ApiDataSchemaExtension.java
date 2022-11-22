package project.extension.openapi.fastjson;

import project.extension.openapi.model.ApiData.*;

import java.util.List;

/**
 * 接口数据结构方案相关拓展方法
 *
 * @author LCTR
 * @date 2022-03-25
 */
public class ApiDataSchemaExtension {
    /**
     * 构建集合数据返回对象
     *
     * @param schema     接口数据结构方案
     * @param data       数据集合
     * @param pagination 分页设置
     * @param <T>        元素类型
     * @return 构建好的数据
     */
    public static <T> IApiData<T> buildResult(ApiDataSchema schema, List<T> data, IPagination pagination) {
        return buildResult(schema, data, pagination, true, schema == ApiDataSchema.Default || schema == ApiDataSchema.Ruoyi ? 200 : 0, null);
    }

    /**
     * 构建集合数据返回对象
     *
     * @param schema     接口数据结构方案
     * @param data       数据集合
     * @param pagination 分页设置
     * @param success    成功与否
     * @param code       状态码
     * @param <T>        元素类型
     * @return 构建好的数据
     */
    public static <T> IApiData<T> buildResult(ApiDataSchema schema, List<T> data, IPagination pagination, boolean success, Integer code) {
        return buildResult(schema, data, pagination, success, code, null);
    }

    /**
     * 构建集合数据返回对象
     *
     * @param schema     接口数据结构方案
     * @param data       数据集合
     * @param pagination 分页设置
     * @param success    成功与否
     * @param code       状态码
     * @param message    消息
     * @param <T>        元素类型
     * @return 构建好的数据
     */
    public static <T> IApiData<T> buildResult(ApiDataSchema schema, List<T> data, IPagination pagination, boolean success, Integer code, String message) {
        switch (schema) {
            case Layui:
                LayuiResult<T> layuiResult = new LayuiResult<>();
                layuiResult.setCode(code);
                layuiResult.setMsg(message);
                layuiResult.setCount_(pagination.getRecordCount());
                layuiResult.setCosttime_(pagination.getWatchTime(true));
                layuiResult.setData(data);
                return layuiResult;
            case JqGrid:
                JqGridResult<T> jqGridResult = new JqGridResult<>();
                jqGridResult.setTotal_(pagination.getPageCount());
                jqGridResult.setPage(pagination.getPageNum());
                jqGridResult.setRecords_(pagination.getRecordCount());
                jqGridResult.setCosttime_(pagination.getWatchTime(true));
                jqGridResult.setRows(data);
                return jqGridResult;
            case BootstrapTable:
            case Easyui:
                EasyuiResult<T> easyuiResult = new EasyuiResult<>();
                easyuiResult.setTotal_(pagination.getRecordCount());
                easyuiResult.setCurrentPage(pagination.getPageNum());
                easyuiResult.setPageSize(pagination.getPageSize());
                easyuiResult.setCosttime_(pagination.getWatchTime(true));
                easyuiResult.setRows(data);
                return easyuiResult;
            case AntdVue:
                AntdVueResult<T> antdVueResult = new AntdVueResult<>();
                antdVueResult.setMessage(message);
                antdVueResult.setSuccess(success);
                antdVueResult.setTotal_(pagination.getRecordCount());
                antdVueResult.setData(data);
                return antdVueResult;
            case ElementVue:
                ElementVueResult<T> elementVueResult = new ElementVueResult<>();
                elementVueResult.setSuccess(success);
                elementVueResult.setErrorCode(code);
                elementVueResult.setMessage(message);
                ElementVueResultData<T> elementVueResultData = new ElementVueResultData<>();
                elementVueResult.setData(elementVueResultData);
                elementVueResultData.setPageTotal_(pagination.getPageCount());
                elementVueResultData.setTotal_(pagination.getRecordCount());
                elementVueResultData.setPageIndex(pagination.getPageNum());
                elementVueResultData.setPageSize(pagination.getPageSize());
                elementVueResultData.setList(data);
                return elementVueResult;
            default:
            case Default:
            case Ruoyi:
                RuoyiResult<T> ruoyiResult = new RuoyiResult<>();
                ruoyiResult.setTotal_(pagination.getRecordCount());
                ruoyiResult.setCode(code);
                ruoyiResult.setMsg(message);
                ruoyiResult.setRows(data);
                return ruoyiResult;
        }
    }
}
