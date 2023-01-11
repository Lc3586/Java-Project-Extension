package project.extension.mybatis.edge.common;

import project.extension.standard.exception.ModuleException;

/**
 * 异常类扩展方法
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class ExceptionExtension {
    /**
     * 输出
     *
     * @param exception 异常信息
     */
    public static void output(Throwable exception) {
        if (exception.getClass()
                     .equals(ModuleException.class)) {
            ModuleException moduleException = (ModuleException) exception;
            if (moduleException.getInnerException() != null && moduleException.getInnerException()
                                                                              .getClass()
                                                                              .equals(ModuleException.class)) {
                output(moduleException.getInnerException());
            }
        }

        System.out.println("\r\n" + exception.getMessage());
    }
}
