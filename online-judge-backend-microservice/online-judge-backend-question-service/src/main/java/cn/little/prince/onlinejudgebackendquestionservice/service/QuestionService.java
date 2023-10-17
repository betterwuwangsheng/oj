package cn.little.prince.onlinejudgebackendquestionservice.service;

import cn.little.prince.onlinejudgebackendmodel.model.dto.question.QuestionQueryRequest;
import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 针对表【question(题目)】的数据库操作 Service
 *
 * @author 349807102
 */
public interface QuestionService extends IService<Question> {


    /**
     * 添加问题
     *
     * @param question 问题
     * @return 问题 id
     */
    Long addQuestion(Question question);

    /**
     * 校验题目 -> 问题是否合法
     *
     * @param question 问题对象
     * @param add      是否添加
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取问题封装
     *
     * @param question 问题
     * @param request  前端请求头
     * @return 题目封装类
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题问题封装对象
     *
     * @param questionPage 问题分页对象
     * @param request      前端请求头
     * @return 分页题问题封装对象
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 题目查询请求
     * @return {@link QueryWrapper}<{@link Question}>
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);
}
