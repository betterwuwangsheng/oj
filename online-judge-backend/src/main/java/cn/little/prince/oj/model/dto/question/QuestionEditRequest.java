package cn.little.prince.oj.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 编辑问题请求
 *
 * @author 349807102
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionEditRequest extends QuestionBaseRequest implements Serializable {
    private static final long serialVersionUID = 733315434585119094L;

    /**
     * id
     */
    private Long id;
}
