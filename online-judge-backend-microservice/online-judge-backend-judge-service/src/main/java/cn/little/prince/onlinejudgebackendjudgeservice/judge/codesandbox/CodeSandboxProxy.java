package cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox;

import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeRequest;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理代码沙箱接口[含日志输出]
 *
 * @author 349807102
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;


    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    /**
     * 代码沙箱执行代码接口
     * <p>
     * 在调用代码沙箱前，输出请求参数日志；
     * 在代码沙箱调用后，输出响应结果日志，便于管理员去分析。
     * <p>
     * 每个代码沙箱类都写一遍 log.info -> 每次调用代码沙箱前后都执行 log
     * 使用代理模式，提供一个 Proxy，来增强代码沙箱的能力（代理模式的作用就是增强能力）
     * <p>
     * 实现被代理的接口
     * 通过构造函数接受一个被代理的接口实现类
     * 调用被代理的接口实现类，在调用前后增加对应的操作
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行代码响应
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息: " + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息: " + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
