package cn.little.prince.onlinejudgebackendmodel.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 创建问题请求体
 *
 * @author 349807102
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionAddRequest extends QuestionBaseRequest implements Serializable {
    private static final long serialVersionUID = 5166963731857304740L;
}
