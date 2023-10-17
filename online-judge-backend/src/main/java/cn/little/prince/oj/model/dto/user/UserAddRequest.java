package cn.little.prince.oj.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建用户请求
 *
 * @author 349807102
 */
@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 3749920700580833412L;


    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 性别 男 女
     */
    private String gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private String userState;

    /**
     * 用户角色: user, admin
     */
    private String userRole;
}
