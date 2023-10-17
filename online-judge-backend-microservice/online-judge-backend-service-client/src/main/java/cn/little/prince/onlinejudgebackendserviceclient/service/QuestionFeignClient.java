package cn.little.prince.onlinejudgebackendserviceclient.service;

import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 公共题目服务接口
 *
 * @author 349807102
 */
@FeignClient(name = "online-judge-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * 根据 id 获取题目信息
     *
     * @param questionId 题目 id
     * @return {@link Question}
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据 id 获取到提交题目信息
     *
     * @param questionSubmitId 问题提交 id
     * @return {@link QuestionSubmit}
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    /**
     * 根据 id 更新提交题目信息
     *
     * @param questionSubmit 问题提交对象
     * @return {@link boolean}
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);


    /**
     * 保存数据
     *
     * @param question 问题对象
     * @return {@link boolean}
     */
    @PostMapping("/question/save")
    boolean updateQuestion(@RequestBody Question question);
}
