package top.hugo.common.annotation;

import java.lang.annotation.*;

/**
 * 字典格式化
 *
 * @author kuanghua
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelDictFormat {

    /**
     * 如果是字典类型，请设置字典的type值 (如: sys_user_sex)
     */
    String dictType() default "";
}
