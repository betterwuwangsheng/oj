package cn.little.prince.onlinejudgebackendquestionservice.controller.inner;

import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendquestionservice.service.QuestionService;
import cn.little.prince.onlinejudgebackendquestionservice.service.QuestionSubmitService;
import cn.little.prince.onlinejudgebackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 问题控制器（仅内部调用）
 *
 * @author 349807102
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据 id 获取题目信息
     *
     * @param questionId 问题 id
     * @return {@link Question}
     */
    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据 id 获取到提交题目信息
     *
     * @param questionSubmitId 问题提交 id
     * @return {@link QuestionSubmit}
     */
    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 根据id 更新提交题目信息
     *
     * @param questionSubmit 问题提交对象
     * @return {@link boolean}
     */
    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    /**
     * 保存数据
     *
     * @param question 问题对象
     * @return {@link boolean}
     */
    @PostMapping("/question/save")
    @Override
    public boolean updateQuestion(@RequestBody Question question) {
        return questionService.updateById(question);
    }
}
