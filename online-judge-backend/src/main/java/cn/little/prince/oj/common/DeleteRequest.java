package cn.little.prince.oj.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author 349807102
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 2632549830571629343L;

    /**
     * id
     */
    private Long id;
}