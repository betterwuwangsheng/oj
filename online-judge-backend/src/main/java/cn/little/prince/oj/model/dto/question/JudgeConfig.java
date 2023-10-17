package cn.little.prince.oj.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目配置
 *
 * @author 349807102
 */
@Data
public class JudgeConfig implements Serializable {
    private static final long serialVersionUID = -54950308842147039L;

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;
}
