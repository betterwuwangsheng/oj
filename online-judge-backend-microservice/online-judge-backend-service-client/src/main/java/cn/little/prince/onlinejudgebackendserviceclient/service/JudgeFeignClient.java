package cn.little.prince.onlinejudgebackendserviceclient.service;

import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 公共判题服务接口
 *
 * @author 349807102
 */
@FeignClient(name = "online-judge-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     *
     * @param questionSubmitId 问题提交 id
     * @return {@link Question}
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
