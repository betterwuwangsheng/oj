package cn.little.prince.oj.judge.codesandbox.impl;

import cn.little.prince.oj.judge.codesandbox.CodeSandbox;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeResponse;
import cn.little.prince.oj.judge.codesandbox.model.JudgeInfo;
import cn.little.prince.oj.model.enums.JudgeInfoMessageEnum;
import cn.little.prince.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 *
 * @author 349807102
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
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
