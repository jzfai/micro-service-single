package top.hugo.common.annotation;


import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项)
 *
 * @author kuanghua
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

    /**
     * col index
     */
    int index() default -1;

}
