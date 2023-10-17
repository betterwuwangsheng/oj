package cn.little.prince.oj.sandbox;

import cn.little.prince.oj.sandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.sandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口【方便切换 -> 提高通用性】
 * 之后的项目代码只调用接口不调用具体的实现类
 * 这样在使用其他代码沙箱实现类时，不需要修改名称
 * 便于扩展
 *
 * @author 349807102
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行代码的响应
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
