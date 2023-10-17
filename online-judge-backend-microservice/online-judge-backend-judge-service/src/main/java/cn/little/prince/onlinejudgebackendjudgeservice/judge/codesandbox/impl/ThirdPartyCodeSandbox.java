package cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.impl;


import cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.CodeSandbox;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeRequest;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 *
 * @author 349807102
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
