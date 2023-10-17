package cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.impl;


import cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.CodeSandbox;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeRequest;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeResponse;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.JudgeInfo;
import cn.little.prince.onlinejudgebackendmodel.model.enums.JudgeInfoMessageEnum;
import cn.little.prince.onlinejudgebackendmodel.model.enums.QuestionSubmitStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 *
 * @author 349807102
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ExampleCodeSandbox");
        // 获取请求信息
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);

        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
