//package top.hugo.db.handler;
//
//
//import cn.hutool.core.collection.ConcurrentHashSet;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jsqlparser.expression.Expression;
//import top.hugo.db.annotation.DataColumn;
//
//import java.util.Set;
//
///**
// * 数据权限过滤
// *
// * @author Lion Li
// * @version 3.5.0
// */
//@Slf4j
//public class PlusDataPermissionHandler {
//
//    /**
//     * 无效注解方法缓存用于快速返回
//     */
//    private final Set<String> invalidCacheSet = new ConcurrentHashSet<>();
//
//    /**
//     * 是否为无效方法 无数据权限
//     */
//    public boolean isInvalid(String mappedStatementId) {
//        return invalidCacheSet.contains(mappedStatementId);
//    }
//
//    public Expression getSqlSegment(Expression where, String mappedStatementId, boolean b) {
//        DataColumn[] dataColumns = findAnnotation(mappedStatementId);
//    }
//
//    private DataColumn[] findAnnotation(String mappedStatementId) {
//        return null;
//    }
//}