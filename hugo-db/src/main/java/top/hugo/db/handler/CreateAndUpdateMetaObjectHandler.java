package top.hugo.db.handler;


import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import top.hugo.common.exception.ServiceException;
import top.hugo.satoken.helper.LoginHelper;

import java.util.Date;

/**
 * MP注入处理器
 *
 * @author kuanghua
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        try {

            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
            this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());

            this.strictInsertFill(metaObject, "createBy", String.class, LoginHelper.getUsername());
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
            this.strictInsertFill(metaObject, "updateBy", String.class, LoginHelper.getUsername());
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    /**
     * 获取登录用户名
     */
//    private String getLoginUsername() {
//        LoginUserInfo user;
//        try {
//            user = JsonUtils.parseObject(StpUtil.getExtra("user"), LoginUserInfo.class);
//        } catch (Exception e) {
//            log.warn("自动注入警告 => 用户未登录");
//            return null;
//        }
//        return user.getUsername();
//    }

}
