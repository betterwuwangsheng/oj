package cn.little.prince.onlinejudgebackendmodel.model.enums;


import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author 349807102
 */
public enum UserRoleEnum {
    /**
     * 用户角色
     */
    USER("用户", "user"),

    /**
     * 管理员角色
     */
    ADMIN("管理员", "admin"),

    /**
     * 被封号角色
     */
    BAN("被封号", "ban");

    /**
     * 角色信息
     */
    private final String text;

    /**
     * 角色对应的值
     */
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 角色 value
     * @return 角色 value 对应的枚举值
     */
    public static UserRoleEnum getEnumByValue(String value) {
        // 值为空
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        // 查找
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
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
