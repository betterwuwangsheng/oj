package cn.little.prince.onlinejudgebackendmodel.model.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息消息枚举
 *
 * @author 349807102
 */

public enum JudgeInfoMessageEnum {
    /**
     * 成功
     */
    ACCEPTED("Accepted", "成功"),

    /**
     * 答案错误
     */
    WRONG_ANSWER("Wrong Answer", "答案错误"),

    /**
     * Compile Error
     */
    COMPILE_ERROR("Compile Error", "编译错误"),

    /**
     * 内存溢出
     */
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeed", "内存溢出"),

    /**
     * 超时
     */
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"),

    /**
     * 展示错误
     */
    PRESENTATION_ERROR("Presentation Error", "展示错误"),

    /**
     * 等待中
     */
    WAITING("Waiting", "编译中"),

    /**
     * 输出溢出
     */
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出溢出"),

    /**
     * 危险操作
     */
    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"),

    /**
     * 运行错误
     */
    RUNTIME_ERROR("Runtime Error", "运行错误"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR("System Error", "系统错误");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 枚举值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value value 值
     * @return 根据 value 获取到的枚举
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
