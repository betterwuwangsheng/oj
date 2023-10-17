package cn.little.prince.oj.judge;

import cn.hutool.json.JSONUtil;
import cn.little.prince.oj.common.ErrorCode;
import cn.little.prince.oj.exception.BusinessException;
import cn.little.prince.oj.judge.codesandbox.CodeSandbox;
import cn.little.prince.oj.judge.codesandbox.CodeSandboxFactory;
import cn.little.prince.oj.judge.codesandbox.CodeSandboxProxy;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeResponse;
import cn.little.prince.oj.judge.codesandbox.model.JudgeInfo;
import cn.little.prince.oj.judge.strategy.JudgeContext;
import cn.little.prince.oj.model.dto.question.JudgeCase;
import cn.little.prince.oj.model.entity.Question;
import cn.little.prince.oj.model.entity.QuestionSubmit;
import cn.little.prince.oj.model.enums.QuestionSubmitLanguageEnum;
import cn.little.prince.oj.model.enums.QuestionSubmitStatusEnum;
import cn.little.prince.oj.service.QuestionService;
import cn.little.prince.oj.service.QuestionSubmitService;
import cn.little.prince.oj.utils.ThrowUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现类
 *
 * @author 349807102
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManger judgeManger;

    @Value("${codesandbox.type:example}")
    private String judgeType;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1、根据题目的提交 id，获取到对应的题目、提交信息（包含提交的代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");

        // 通过提交的信息中的题目 id 获取到题目的全部信息
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(questionId == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        // 2、如果题目提交状态不为等待中 0 - 待判题、1 - 判题中、2 - 成功、3 - 失败
        boolean conditions = !questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue());
        ThrowUtils.throwIf(conditions, ErrorCode.OPERATION_ERROR, "题目正在判题中");

        // 3、更改判题（题目提交）的状态为 "判题中"，防止重复执行，也能让用户即时看到状态
        // 设置提交 id 和判题状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());

        // 更新数据中的信息
        boolean updateState = questionSubmitService.updateById(questionSubmitUpdate);
        ThrowUtils.throwIf(!updateState, ErrorCode.SYSTEM_ERROR, "题目状态更新失败");

        // 4、调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(judgeType);
        codeSandbox = new CodeSandboxProxy(codeSandbox);

        // 获取语言和代码
        QuestionSubmitLanguageEnum languageType = QuestionSubmitLanguageEnum.getEnumByValue(questionSubmit.getLanguage());
        String submitCode = questionSubmit.getCode();

        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCasesList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);

        // 通过 Lambda 表达式获取到每个题目的输入用例
        List<String> inputList = judgeCasesList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        // 调用沙箱
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().code(submitCode).language(languageType).inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();

        // 5、根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCasesList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        // 进入到代码沙箱，执行程序，返回执行结果
        JudgeInfo judgeInfo = judgeManger.doJudge(judgeContext);

        // 6、修改判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        updateState = questionSubmitService.updateById(questionSubmitUpdate);

        ThrowUtils.throwIf(!updateState, ErrorCode.SYSTEM_ERROR, "题目状态更新失败");

        // 再次查询数据库，返回最新提交信息
        return questionSubmitService.getById(questionId);
    }
}