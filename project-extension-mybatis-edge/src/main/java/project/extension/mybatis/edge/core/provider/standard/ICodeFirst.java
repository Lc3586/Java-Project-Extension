package project.extension.mybatis.edge.core.provider.standard;

import project.extension.standard.exception.ModuleException;

/**
 * CodeFirst 开发模式相关功能接口类
 *
 * @author LCTR
 * @date 2022-06-10
 */
public interface ICodeFirst {
    /**
     * 创建或更新数据库方法
     */
    void createOrReplaceFunctions()
            throws
            ModuleException;
}
