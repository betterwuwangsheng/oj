package cn.little.prince.oj.judge;

import cn.little.prince.oj.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 * @author 349807102
 */
public interface JudgeService {
    /**
     * 判题
     * 1. 根据传入的 id 获取对应的问题信息
     * 2. 调用代码沙箱，获取到执行结果
     * 3.更具沙箱的执行结果，设置题目的判题状态和信息
     *
     * @param questionSubmitId 问题 id
     * @return 问题提交对象
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
