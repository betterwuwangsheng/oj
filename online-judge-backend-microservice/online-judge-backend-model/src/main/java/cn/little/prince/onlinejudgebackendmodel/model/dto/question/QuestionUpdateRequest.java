package cn.little.prince.onlinejudgebackendmodel.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 更新问题请求
 *
 * @author 349807102
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionUpdateRequest extends QuestionBaseRequest implements Serializable {
    private static final long serialVersionUID = -7042383625197130315L;

    /**
     * id
     */
    private Long id;
}
