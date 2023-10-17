package cn.little.prince.onlinejudgebackenduserservice.controller;

import cn.little.prince.onlinejudgebackendcommon.annotation.AuthCheck;
import cn.little.prince.onlinejudgebackendcommon.common.BaseResponse;
import cn.little.prince.onlinejudgebackendcommon.common.DeleteRequest;
import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;
import cn.little.prince.onlinejudgebackendcommon.constant.UserConstant;
import cn.little.prince.onlinejudgebackendcommon.utils.ResultUtils;
import cn.little.prince.onlinejudgebackendcommon.utils.ThrowUtils;
import cn.little.prince.onlinejudgebackendmodel.model.dto.user.*;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.enums.UserRoleEnum;
import cn.little.prince.onlinejudgebackendmodel.model.vo.LoginUserVO;
import cn.little.prince.onlinejudgebackendmodel.model.vo.UserVO;
import cn.little.prince.onlinejudgebackenduserservice.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 *
 * @author 349807102
 */
@RequestMapping("/")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 用户 id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 前端参数校验
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);

        // 获取注册信息
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        // 参数校验
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword), ErrorCode.PARAMS_ERROR, "用户名、密码、确认密码不能为空!");

        // 调用 service 层代码
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登陆请求对象
     * @param request          前端请求体
     * @return 登录用户脱敏信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 前端参数校验
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);

        // 获取登录信息
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 校验
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR, "用户名或密码为空!");

        // 调用 service 层代码
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request 前端请求体
     * @return 是否注销成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        // 前端参数校验
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request 前端请求体
     * @return 当前登录用户信息(脱敏)
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        // 前端参数校验
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        // 调用 service 层代码
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 创建用户
     *
     * @param userAddRequest 创建用户请求
     * @return 用户 id
     */
    @PostMapping("/add")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        // 前端参数校验
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);

        // 新建用户对象
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        Long result = userService.addUser(user);
        return ResultUtils.success(result);
    }

    /**
     * 删除用户
     *
     * @param deleteRequest 删除用户请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null || userUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id 用户 id
     * @return 根据 id 查询到的用户信息
     */
    @GetMapping("/get")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id 用户 id
     * @return 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest 用户更新个人信息请求
     * @param request             前端请求头
     * @return 是否更新成功
     */
    @PostMapping("/update/mine")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMineRequest userUpdateMyRequest, HttpServletRequest request) {
        // 参数校验
        ThrowUtils.throwIf(userUpdateMyRequest == null, ErrorCode.PARAMS_ERROR);

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 新建用户对象
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);

        // 设置 id l
        user.setId(loginUser.getId());

        // 更新
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest 用户查询请求
     * @return 分页用户信息
     */
    @PostMapping("/list/page")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);

        // 获取当前页号
        long current = userQueryRequest.getCurrent();

        // 获取页面大小
        long size = userQueryRequest.getPageSize();

        // 分页查询
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }
}
