package top.hugo.db.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.hugo.db.annotation.DataColumn;
import top.hugo.db.annotation.DataPermission;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限拦截器
 *
 * @author Lion Li
 * @version 3.5.0
 */
public class PlusDataPermissionInterceptor extends JsqlParserSupport implements InnerInterceptor {


    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 检查忽略注解 (@InterceptorIgnore(dataPermission = "true"))
        if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
            return;
        }
        // 解析 sql 分配对应方法 如：select路由到processSelect
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        System.out.println("beforePrepare");
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        //获取注解数值
        DataColumn[] dataColumns = findAnnotation((String) obj);
        //将sql解析出来
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        //拼接where语句
        try {
            Expression expression = CCJSqlParserUtil.parseExpression("1=1");
            if (ObjectUtil.isNotEmpty(dataColumns)) {
                selectBody.setWhere(expression);
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        System.out.println("processUpdate");
    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        System.out.println("processDelete");
    }

    private DataColumn[] findAnnotation(String mappedStatementId) {
        //mappedStatementId：com.ruoyi.system.mapper.SysUserMapper.selectPageUserList
        StringBuilder sb = new StringBuilder(mappedStatementId);
        int index = sb.lastIndexOf(".");
        String clazzName = sb.substring(0, index);
        String methodName = sb.substring(index + 1, sb.length());
        //clazzName：com.ruoyi.system.mapper.SysUserMapper
        Class<?> clazz = ClassUtil.loadClass(clazzName);

        List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
                .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        Method method = methods.get(1);
        DataPermission dataPermission;
        if (AnnotationUtil.hasAnnotation(method, DataPermission.class)) {
            dataPermission = AnnotationUtil.getAnnotation(method, DataPermission.class);
            return dataPermission.value();
        }
        return null;
    }
}

