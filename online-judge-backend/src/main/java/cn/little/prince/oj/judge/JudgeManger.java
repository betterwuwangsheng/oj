package cn.little.prince.oj.judge;

import cn.little.prince.oj.judge.codesandbox.model.JudgeInfo;
import cn.little.prince.oj.judge.strategy.DefaultJudgeStrategy;
import cn.little.prince.oj.judge.strategy.JavaJudgeStrategy;
import cn.little.prince.oj.judge.strategy.JudgeContext;
import cn.little.prince.oj.judge.strategy.JudgeStrategy;
import cn.little.prince.oj.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 判题管理,策略的选择（简化调用）
 *
 * @author 349807102
 */
@Service
public class JudgeManger {
    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return 判题信息
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        // 获取语言
        QuestionSubmitLanguageEnum languageType = judgeContext.getLanguageType();

        JudgeStrategy judgeStrategy;
        if (QuestionSubmitLanguageEnum.JAVA.equals(languageType)) {
            judgeStrategy = new JavaJudgeStrategy();
        } else {
            judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
