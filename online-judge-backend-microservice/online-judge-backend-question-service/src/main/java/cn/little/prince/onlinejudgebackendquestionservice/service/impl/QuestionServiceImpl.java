package cn.little.prince.onlinejudgebackendquestionservice.service.impl;

import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;
import cn.little.prince.onlinejudgebackendcommon.constant.CommonConstant;
import cn.little.prince.onlinejudgebackendcommon.utils.SqlUtils;
import cn.little.prince.onlinejudgebackendcommon.utils.ThrowUtils;
import cn.little.prince.onlinejudgebackendmodel.model.dto.question.QuestionQueryRequest;
import cn.little.prince.onlinejudgebackendmodel.model.entity.Question;
import cn.little.prince.onlinejudgebackendmodel.model.entity.User;
import cn.little.prince.onlinejudgebackendmodel.model.vo.QuestionVO;
import cn.little.prince.onlinejudgebackendmodel.model.vo.UserVO;
import cn.little.prince.onlinejudgebackendquestionservice.mapper.QuestionMapper;
import cn.little.prince.onlinejudgebackendquestionservice.service.QuestionService;
import cn.little.prince.onlinejudgebackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 针对表【question(题目)】的数据库操作 Service 实现
 *
 * @author 349807102
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    UserFeignClient userFeignClient;

    /**
     * 标题最大长度
     */
    private static final int TITLE_MAX_LEN = 80;

    /**
     * 内容最大长度
     */
    private static final int CONTENT_MAX_LEN = 8192;

    /**
     * GSON
     */
    private static final Gson GSON = new Gson();

    @Override
    public Long addQuestion(Question question) {
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long id = question.getId();
        ThrowUtils.throwIf(id < 0, ErrorCode.OPERATION_ERROR);
        return id;
    }

    @Override
    public void validQuestion(Question question, boolean add) {
        // 创建时字段是否可以空
        // 修改时字段是否可以为空
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);

        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        // 创建时，参数不能为空
        ThrowUtils.throwIf(add && StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);

        // 有参数则校验
        ThrowUtils.throwIf(StringUtils.isNotBlank(title) && title.length() > TITLE_MAX_LEN, ErrorCode.PARAMS_ERROR, "标题过长!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(content) && content.length() > CONTENT_MAX_LEN, ErrorCode.PARAMS_ERROR, "内容过长!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(answer) && answer.length() > CONTENT_MAX_LEN, ErrorCode.PARAMS_ERROR, "答案过长!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(judgeCase) && judgeCase.length() > CONTENT_MAX_LEN, ErrorCode.PARAMS_ERROR, "判题用例过长!");
        ThrowUtils.throwIf(StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > CONTENT_MAX_LEN, ErrorCode.PARAMS_ERROR, "判题配置过长!");
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        // 业务参数校验
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        // 问题对象转换为问题封装对象
        QuestionVO questionVO = QuestionVO.objToVo(question);

        // 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }

        // 获取用户脱敏信息
        UserVO userVO = userFeignClient.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userFeignClient.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (!CollectionUtils.isEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}




