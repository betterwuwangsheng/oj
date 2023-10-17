package cn.little.prince.onlinejudgebackendquestionservice.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.little.prince.onlinejudgebackendcommon.annotation.AuthCheck;
import cn.little.prince.onlinejudgebackendcommon.common.BaseResponse;
import cn.little.prince.onlinejudgebackendcommon.common.DeleteRequest;
import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;
import cn.little.prince.onlinejudgebackendcommon.constant.UserConstant;
import cn.little.prince.onlinejudgebackendcommon.exception.BusinessException;
import cn.little.prince.onlinejudgebackendcommon.utils.CopyUtils;
import cn.little.prince.onlinejudgebackendcommon.utils.ResultUtils;
import cn.little.prince.onlinejudgebackendcommon.utils.ThrowUtils;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.JudgeInfo;
import cn.little.prince.onlinejudgebackendmodel.model.dto.question.*;
import cn.little.prince.onlinejudgebackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import cn.little.prince.onlinejudgebackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.QuestionSubmit;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.enums.QuestionSubmitLanguageEnum;
import cn.little.prince.onlinejudgebackendmodel.model.enums.QuestionSubmitStatusEnum;
import cn.little.prince.onlinejudgebackendmodel.model.vo.JudgeVO;
import cn.little.prince.onlinejudgebackendmodel.model.vo.QuestionSubmitVO;
import cn.little.prince.onlinejudgebackendmodel.model.vo.QuestionVO;
import cn.little.prince.onlinejudgebackendquestionservice.manage.RedissonLimiter;
import cn.little.prince.onlinejudgebackendquestionservice.service.QuestionService;
import cn.little.prince.onlinejudgebackendquestionservice.service.QuestionSubmitService;
import cn.little.prince.onlinejudgebackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 349807102
 */
@RequestMapping("/")
@RestController
@Slf4j
public class QuestionController {

    @Resource
    QuestionService questionService;

    @Resource
    UserFeignClient userFeignClient;

    @Resource
    private RedissonLimiter redissonLimiter;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 新建 Gson 对象 -> 不用每个地方都 new Gson
     */
    private static final Gson GSON = new Gson();

    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        // 前端参数校验
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 新建问题对象
        Question question = new Question();

        // 复制属性
        BeanUtils.copyProperties(questionAddRequest, question);

        // 获取问题的标签
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            // 转换为 json
            question.setTags(GSON.toJson(tags));
        }

        // 获取问题的用例
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }

        // 获取问题的题目配置
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }

        questionService.validQuestion(question, true);

        // 获取当前用户 id
        User loginUser = userFeignClient.getLoginUser(request);
        Long currentUserId = loginUser.getId();
        question.setUserId(currentUserId);
        question.setFavourNum(0);
        question.setThumbNum(0);

        Long id = questionService.addQuestion(question);
        return ResultUtils.success(id);
    }

    /**
     * 删除题目
     *
     * @param deleteRequest 删除问题请求
     * @param request       掐你的那请求头
     * @return 是否删除
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        // 前端参数校验
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        // 获取当前登录用户
        User loginUser = userFeignClient.getLoginUser(request);

        // 获取题目 id
        long id = deleteRequest.getId();

        // 判断题目是否存在
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅本人或管理员可以删除
        boolean conditions = !question.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser);
        ThrowUtils.throwIf(conditions, ErrorCode.NO_AUTH_ERROR);

        // 删除
        boolean result = questionService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 更改题目[仅限管理员]
     *
     * @param questionUpdateRequest 更新问题请求
     * @return 是否更新
     */
    @PostMapping("/update")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        // 前端参数校验
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);

        // 获取标签
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }

        // 获取用例
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }

        // 获取配置
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }

        // 参数校验
        questionService.validQuestion(question, false);

        // 获取 id
        long id = questionUpdateRequest.getId();

        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取[当前用户或管理员]
     *
     * @param id 问题 id
     * @return 根据 id 后去到的问题
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        // 前端参数校验
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);

        // 查询数据库中的信息
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        // 获取当前登录用户
        User loginUser = userFeignClient.getLoginUser(request);

        // 不是本人或管理员，不能直接获取所有信息
        boolean conditions = !question.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser);
        ThrowUtils.throwIf(conditions, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(question);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id 题目 id
     * @return 根据 id 获取到的题目
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页问题获取列表（封装类）
     *
     * @param questionQueryRequest 问题查询请求
     * @param request              前端请求体
     * @return 分页问题封装对象
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest 问题查询请求
     * @param request              前端请求体
     * @return 分页问题封装对象
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionQueryRequest 问题查询请求
     * @return 分页问题封装对象
     */
    @PostMapping("/list/page")
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 编辑（用户）
     *
     * @param questionEditRequest 问题查询请求
     * @param request             前端请求体
     * @return 是否编辑成功
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 提交问题
     *
     * @param questionSubmitAddRequest 问题提交请求
     * @param request                  前端请求体
     * @return 提交记录的 id
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userFeignClient.getLoginUser(request);

        // 限流
        boolean rateLimit = redissonLimiter.doRateLimit(loginUser.getId().toString());
        if (!rateLimit) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "提交过于频繁,请稍后重试");
        }

        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest 用户提交查询请求
     * @param request                    前端请求体
     * @return 问题提交封装对象（分页）
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userFeignClient.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 根据提交的 id 查询判题信息
     *
     * @param questionSubmitId 用户提交的问题 id
     * @return 判题信息
     */
    // @GetMapping("/question_submit/get/id")
    // public BaseResponse<JudgeVO> getJudgeResult(Long questionSubmitId) {
    //     // 查询问题
    //     QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
    //     if (ObjectUtil.isEmpty(questionSubmit)) {
    //         throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据为空");
    //     }
    //     Integer status = questionSubmit.getStatus();
    //     if (QuestionSubmitStatusEnum.WAITING.getValue().equals(status) || QuestionSubmitStatusEnum.RUNNING.getValue().equals(status)) {
    //         throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "判题中");
    //     }
    //     JudgeVO judgeVO = CopyUtils.copy(questionSubmit, JudgeVO.class);
    //     JudgeInfo judgeInfo = JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class);
    //     judgeVO.setTime(ObjectUtil.defaultIfNull(judgeInfo.getTime(), 0) + "ms");
    //     judgeVO.setMessage(judgeInfo.getMessage());
    //     return ResultUtils.success(judgeVO);
    // }

    /**
     * 获取编程语言（预先设定）
     *
     * @return 语言
     */
    @GetMapping("/get/language")
    public BaseResponse<List<String>> getCodeLanguage() {
        return ResultUtils.success(QuestionSubmitLanguageEnum.getValues());
    }
}