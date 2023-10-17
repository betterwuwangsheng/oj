package cn.little.prince.onlinejudgebackendcommon.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 349807102
 */
public class CopyUtils {

    /**
     * 单体复制
     *
     * @param source 源
     * @param clazz  clazz
     * @return {@link T}
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T obj;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    /**
     * 列表复制
     *
     * @param source 源
     * @param clazz  clazz
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> copyList(List<?> source, Class<T> clazz) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        return source.stream().map(c -> copy(c, clazz)).collect(Collectors.toList());
    }
}
