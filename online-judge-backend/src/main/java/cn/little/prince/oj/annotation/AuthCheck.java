package cn.little.prince.oj.annotation;

import cn.little.prince.oj.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *
 * @author 349807102
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须要有的角色
     *
     * @return {@link UserRoleEnum}
     */
    UserRoleEnum[] role() default {UserRoleEnum.USER, UserRoleEnum.ADMIN};
}
