package project.extension.document.excel;

import java.lang.annotation.*;

/**
 * 导出时忽略
 *
 * @author LCTR
 * @date 2023-06-25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExportIgnore {

}
