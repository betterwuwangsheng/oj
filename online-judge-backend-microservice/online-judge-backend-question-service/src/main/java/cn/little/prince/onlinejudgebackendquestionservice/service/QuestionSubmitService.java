package cn.little.prince.onlinejudgebackendquestionservice.service;


import cn.little.prince.onlinejudgebackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import cn.little.prince.onlinejudgebackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.vo.QuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 针对表【question_submit(题目提交)】的数据库操作 Service
 *
 * @author 349807102
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交添加对象
     * @param loginUser                当前登录用户
     * @return 提交记录的 id
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest 问题提交查询请求
     * @return QueryWrapper<QuestionSubmit>
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit 问题提交对象
     * @param loginUser      登录用户
     * @return {@link QuestionSubmitVO}
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage 问题提交分页
     * @param loginUser          登录用户
     * @return Page<QuestionSubmitVO>
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}

