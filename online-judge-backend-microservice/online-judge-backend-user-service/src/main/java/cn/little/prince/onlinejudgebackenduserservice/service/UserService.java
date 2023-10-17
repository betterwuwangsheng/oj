package cn.little.prince.onlinejudgebackenduserservice.service;

import cn.little.prince.onlinejudgebackendmodel.model.dto.user.UserQueryRequest;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.vo.LoginUserVO;
import cn.little.prince.onlinejudgebackendmodel.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 349807102
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-09-11 21:26:56
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 注册成功的用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户注销
     *
     * @param request 前端请求头
     * @return 是否注销成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      前端请求头
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 用户对象
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取登录用户信息
     *
     * @param request 前端请求头
     * @return 脱敏后的用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 用户 id
     */
    Long addUser(User user);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求
     * @return queryWrapper 对象
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 判断用户[session对象中]是否为管理员
     *
     * @param request 前端请求体
     * @return 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断用户[Use 对象]是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员
     */
    boolean isAdmin(User user);

    /**
     * 将用户对象信息转换为脱敏后的用户信息
     *
     * @param user 用户对象
     * @return 脱敏后的用户信息
     */
    UserVO getUserVO(User user);
}
