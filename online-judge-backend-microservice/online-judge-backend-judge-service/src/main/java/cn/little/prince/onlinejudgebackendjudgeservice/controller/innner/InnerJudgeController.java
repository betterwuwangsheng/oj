package cn.little.prince.onlinejudgebackendjudgeservice.controller.innner;

import cn.little.prince.onlinejudgebackendjudgeservice.judge.service.JudgeService;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 判题控制器（仅内部调用）
 *
 * @author 349807102
 */
@RestController
@RequestMapping("/inner")
public class InnerJudgeController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     *
     * @param questionSubmitId 问题提交 id
     * @return {@link QuestionSubmit}
     */
    @PostMapping("/do")
    @Override
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
