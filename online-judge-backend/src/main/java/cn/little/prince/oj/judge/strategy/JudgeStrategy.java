package cn.little.prince.oj.judge.strategy;


import cn.little.prince.oj.judge.codesandbox.model.JudgeInfo;

/**
 * 判题策略接口
 *
 * @author 349807102
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return 判题信息
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}