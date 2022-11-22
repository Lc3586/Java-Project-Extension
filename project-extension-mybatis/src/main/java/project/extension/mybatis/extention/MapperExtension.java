package project.extension.mybatis.extention;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.model.ExecutorParameter;
import project.extension.mybatis.model.Pagination;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

/**
 * 映射接口拓展方法
 *
 * @author LCTR
 * @date 2022-03-25
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public class MapperExtension {
    /**
     * 使用分页设置进行分页
     * <p>会自动获取参数中的分页设置</p>
     *
     * @param func      查询方法的委托
     * @param parameter 查询方法的参数
     * @param <P>       参数类型
     * @param <R>       返回值类型
     */
    public static <P, R> R pagination(Function<P, R> func,
                                      P parameter,
                                      Class<P> parameterType)
            throws
            Throwable {
        Field paginationField = CollectionsExtension.tryGet(
                ExecutorExtension.getParameters(
                        parameterType,
                        ExecutorParameter.分页设置),
                ExecutorParameter.分页设置).b;

        if (paginationField != null) {
            paginationField.setAccessible(true);
            Pagination pagination = (Pagination) paginationField.get(parameter);

            return pagination(
                    pagination,
                    func,
                    parameter);
        }

        return func.apply(parameter);
    }

    /**
     * 使用分页设置进行分页
     *
     * @param pagination 分页设置
     * @param func       查询方法的委托
     * @param parameter  查询方法的参数
     * @param <P>        参数类型
     * @param <R>        返回值类型
     * @return
     */
    public static <P, R> R pagination(Pagination pagination,
                                      Function<P, R> func,
                                      P parameter) {
        if (pagination != null && !pagination.getNope() && pagination.getUserPageHelper()) {
            Page<List> page = PageHelper.startPage(
                                                pagination.getPageNum(),
                                                pagination.getPageSize())
                                        .doSelectPage(() -> func.apply(parameter));
            pagination.setRecordCount(page.getTotal());
            return (R) page.getResult();
        }

        return func.apply(parameter);
    }
}
