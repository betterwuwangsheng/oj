package cn.little.prince.onlinejudgebackenduserservice.controller.inner;

import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendserviceclient.service.UserFeignClient;
import cn.little.prince.onlinejudgebackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户控制器（仅内部调用）
 *
 * @author 349807102
 */
@RestController
@RequestMapping("/inner")
public class InnerUserController implements UserFeignClient {
    @Resource
    private UserService userService;

    /**
     * 根据 id 获取用户信息
     *
     * @param userId 用户 id
     * @return {@link User}
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据 id 获取用户列表
     *
     * @param idList id 列表
     * @return 用户列表
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
