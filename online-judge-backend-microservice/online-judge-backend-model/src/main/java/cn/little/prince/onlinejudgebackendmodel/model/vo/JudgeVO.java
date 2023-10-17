package cn.little.prince.onlinejudgebackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题 VO
 *
 * @author 349807102
 */
@Data
public class JudgeVO implements Serializable {
    private static final long serialVersionUID = -7515889409822792280L;
    private String language;
    private String code;
    private String message;
    private String time;
}
