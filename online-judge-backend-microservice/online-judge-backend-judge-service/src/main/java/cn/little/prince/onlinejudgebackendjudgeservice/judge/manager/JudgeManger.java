package cn.little.prince.onlinejudgebackendjudgeservice.judge.manager;

import cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy.JavaJudgeStrategy;
import cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy.JudgeContext;
import cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy.JudgeStrategy;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.JudgeInfo;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendmodel.model.enums.QuestionSubmitLanguageEnum;
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
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        // 获取语言
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy;
        if (QuestionSubmitLanguageEnum.JAVA.equals(QuestionSubmitLanguageEnum.getEnumByValue(language))) {
            judgeStrategy = new JavaJudgeStrategy();
        } else {
            judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
