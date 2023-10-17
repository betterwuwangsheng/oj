package cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.JudgeInfo;
import cn.little.prince.onlinejudgebackendmodel.model.dto.question.JudgeCase;
import cn.little.prince.onlinejudgebackendmodel.model.dto.question.JudgeConfig;
import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * Java 判题策略
 *
 * @author 349807102
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    /**
     * Java 程序本身需要额外执行 10 秒钟
     */
    private static final long JAVA_PROGRAM_TIME_COST = 5000L;

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        // 获取判题信息中的输出列表等信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = ObjectUtil.defaultIfNull(judgeInfo.getMemory(), 0L);
        Long time = ObjectUtil.defaultIfNull(judgeInfo.getTime(), 0L);

        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();

        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            // 获取判题用例
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfoResponse;
            }
        }

        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
