package cn.little.prince.onlinejudgebackendserviceclient.service;

import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;
import cn.little.prince.onlinejudgebackendcommon.exception.BusinessException;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.enums.UserRoleEnum;
import cn.little.prince.onlinejudgebackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static cn.little.prince.onlinejudgebackendcommon.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 公共用户服务接口
 *
 * @author 349807102
 */
@FeignClient(name = "online-judge-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据 id 获取用户信息
     *
     * @param userId 用户 id
     * @return {@link  User}
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * 根据 id 获取用户列表
     *
     * @param idList id 列表
     * @return 用户列表
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);

    /**
     * 获取当前登录用户
     *
     * @param request 前端请求
     * @return {@link User}
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user 用户对象
     * @return {@link boolean}
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户对象
     * @return {@link UserVO}
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}