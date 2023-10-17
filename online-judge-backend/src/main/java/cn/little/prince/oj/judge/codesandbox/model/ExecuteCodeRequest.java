package cn.little.prince.oj.judge.codesandbox.model;


import cn.little.prince.oj.model.enums.QuestionSubmitLanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码请求
 *
 * @author 349807102
 * [@Builder]可以使用链式的方式更方便地给对象赋值
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {
    /**
     * 接收问题输入
     */
    private List<String> inputList;

    /**
     * 接收执行的代码
     */
    private String code;

    /**
     * 执行代码的编程语言
     */
    private QuestionSubmitLanguageEnum language;
}
