package cn.little.prince.onlinejudgebackendjudgeservice.judge.strategy;

import cn.little.prince.onlinejudgebackendmodel.codesandbox.JudgeInfo;
import cn.little.prince.onlinejudgebackendmodel.model.dto.question.JudgeCase;
import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendmodel.model.enums.QuestionSubmitLanguageEnum;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文（用于定义在策略中传递的参数）
 *
 * @author 349807102
 */
@Data
public class JudgeContext {

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 输入
     */
    private List<String> inputList;

    /**
     * 输出
     */
    private List<String> outputList;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 问题提交信息
     */
    private QuestionSubmit questionSubmit;

    /**
     * 问题信息
     */
    private Question question;
    //
    //
    // private List<String> outputListResult;
    //
    // /**
    //  * 语言类型
    //  */
    // private QuestionSubmitLanguageEnum languageType;
}
