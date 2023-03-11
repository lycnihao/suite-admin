package net.koodar.suite.common.support.datatracer.domain;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * 变动内容
 *
 */
@Data
public class DataTracerContentBO {

    /**
     * 变动字段
     */
    private Field field;

    /**
     * 变动字段的值
     */
    private Object fieldValue;

    /**
     * 变动字段描述
     */
    private String fieldSummary;

    /**
     * 变动内容
     */
    private String fieldContent;

}
