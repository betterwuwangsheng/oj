package cn.little.prince.onlinejudgebackendmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author 349807102
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {
    private static final long serialVersionUID = 8716633273738212104L;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;
}