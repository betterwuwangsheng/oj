package cn.little.prince.onlinejudgebackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录用户信息(脱敏对象-隐藏了某些字段)
 *
 * @author 349807102
 */
@Data
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = -1278123946932796874L;

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

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
     * 状态:正常/注销/封号
     */
    private String userState;


    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}