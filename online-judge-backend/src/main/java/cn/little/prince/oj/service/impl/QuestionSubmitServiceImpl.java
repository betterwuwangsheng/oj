package cn.little.prince.oj.service.impl;

import cn.little.prince.oj.common.ErrorCode;
import cn.little.prince.oj.constant.CommonConstant;
import cn.little.prince.oj.exception.BusinessException;
import cn.little.prince.oj.judge.JudgeService;
import cn.little.prince.oj.mapper.QuestionSubmitMapper;
import cn.little.prince.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import cn.little.prince.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import cn.little.prince.oj.model.entity.Question;
import cn.little.prince.oj.model.entity.QuestionSubmit;
import cn.little.prince.oj.model.entity.User;
import cn.little.prince.oj.model.enums.QuestionSubmitLanguageEnum;
import cn.little.prince.oj.model.enums.QuestionSubmitStatusEnum;
import cn.little.prince.oj.model.vo.QuestionSubmitVO;
import cn.little.prince.oj.service.QuestionService;
import cn.little.prince.oj.service.QuestionSubmitService;
import cn.little.prince.oj.service.UserService;
import cn.little.prince.oj.utils.SqlUtils;
import cn.little.prince.oj.utils.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 针对表【question_submit(题目提交)】的数据库操作 Service 实现
 *
 * @author 349807102
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 请求包装类
     * @param loginUser                登录用户
     * @return 提交题目 id
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String submitLanguage = questionSubmitAddRequest.getLanguage();
        String submitCode = questionSubmitAddRequest.getCode();
        // 校验编程语言是否正确
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(submitLanguage);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 设置提交数
        Question updateQuestion = questionService.getById(questionId);
        synchronized (question.getSubmitNum()) {
            int submitNum = question.getSubmitNum() + 1;
            updateQuestion.setSubmitNum(submitNum);
            boolean save = questionService.updateById(updateQuestion);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据保存失败");
            }
        }

        // 是否已提交题目
        long userId = loginUser.getId();

        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(submitCode);
        questionSubmit.setLanguage(submitLanguage);

        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR, "数据保存失败");

        Long questionSubmitId = questionSubmit.getId();
        // // 生产者发送消息
        // codeMqProducer.sendMessage(CODE_EXCHANGE_NAME, CODE_ROUTING_KEY, String.valueOf(questionSubmitId));
        // // 执行判题服务
        // // CompletableFuture.runAsync(() -> {
        // //     judgeService.doJudge(questionSubmitId);
        // // });

        // 执行判题服务
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {


        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String submitLanguage = questionSubmitQueryRequest.getLanguage();
        Integer submitState = questionSubmitQueryRequest.getStatus();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(submitLanguage), "submitLanguage", submitLanguage);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(submitState) != null, "submitState", submitState);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 获取查询封装类（单个）
     *
     * @param questionSubmit 问题提交对象
     * @param loginUser      当前登录用户对象
     * @return 脱敏后的问题提交对象
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        // long userId = loginUser.getId();
        // // 处理脱敏
        // if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
        //     questionSubmitVO.setSubmitCode(null);
        // }
        return questionSubmitVO;
    }

    /**
     * 获取查询脱敏信息
     *
     * @param questionSubmitPage 题目提交分页
     * @param loginUser          直接获取到用户信息，减少查询数据库
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




