package net.koodar.suite.common.support.datatracer;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.exception.ServiceException;
import net.koodar.suite.common.support.datatracer.annoation.DataTracerFieldLabel;
import net.koodar.suite.common.support.datatracer.domain.DataTracer;
import net.koodar.suite.common.support.datatracer.domain.DataTracerContentBO;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据变动记录业务
 *
 * @author liyc
 */
@Slf4j
@Component
public class DataTracerService {

	private final DataTracerRepository dataTracerRepository;

	/**
	 * 字段名缓存 key: 字段, value: 字段名
	 */
	private final ConcurrentHashMap<String, String> fieldSummaryMap = new ConcurrentHashMap<>();

	/**
	 * 字段缓存 key: 类, value: 字段列表
	 */
	private final ConcurrentHashMap<Class, List<Field>> fieldMap = new ConcurrentHashMap<>();

	public DataTracerService(DataTracerRepository dataTracerRepository) {
		this.dataTracerRepository = dataTracerRepository;
	}

	/**
	 * 获取对象所有字段
	 * @param obj 记录对象
	 * @return 字段列表
	 */
	private List<Field> getFields(Object obj) {
		// 从缓存中查询
		Class tClass = obj.getClass();
		if (fieldMap.contains(tClass)) {
			return fieldMap.get(tClass);
		}

		// 获取对象所有字段
		List<Field> fieldList = new ArrayList<>();
		Class tempClass = tClass;
		while (tempClass != null) {
			fieldList.addAll(Arrays.stream(tempClass.getDeclaredFields()).toList());
			tempClass = tempClass.getSuperclass();
		}
		fieldMap.put(tClass, fieldList);
		return fieldList;
	}

	/**
	 * 获取字段显示名称，解析字段注解@DataTracerFieldLabel
	 * @param field 字段
	 * @return 字段显示名称
	 */
	private String getFieldSummary(Field field) {
		String summary = fieldSummaryMap.get(field.getName());
		if (summary != null) {
			return summary;
		}
		DataTracerFieldLabel annotation = field.getAnnotation(DataTracerFieldLabel.class);
		if (annotation == null) {
			return null;
		}
		summary = annotation.value();
		fieldSummaryMap.put(field.getName(), summary);
		return summary;
	}

	/**
	 * 解析字段
	 * @param fieldList 字段列表
	 * @param obj 记录对象
	 * @return 解析结果
	 */
	private List<DataTracerContentBO> parseField(List<Field> fieldList, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
		Class tClass = obj.getClass();
		List<DataTracerContentBO> dataTracerContentBOList = new ArrayList<>();

		// 解析字段
		for (Field field : fieldList) {
			// 获取字段显示名称
			String fieldSummary = getFieldSummary(field);
			if (fieldSummary == null) {
				continue;
			}
			// 获取字段值
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
			Method method = pd.getReadMethod();
			Object fieldValue = method.invoke(obj);

			// 字段值处理
			String fieldContent;
			if (fieldValue instanceof String) {
				fieldContent = (String) fieldValue;
			} else {
				fieldContent = JsonUtils.objectToJson(fieldValue);
			}

			DataTracerContentBO dataTracerContentBO = new DataTracerContentBO();
			dataTracerContentBO.setField(field);
			dataTracerContentBO.setFieldSummary(fieldSummary);
			dataTracerContentBO.setFieldValue(fieldValue);
			dataTracerContentBO.setFieldContent(fieldContent);
			dataTracerContentBOList.add(dataTracerContentBO);
		}
		return dataTracerContentBOList;
	}

	private String dataTracerContentConvertString(List<DataTracerContentBO> dataTracerContentBOList) {
		List<String> contentList = new ArrayList<>();
		for (DataTracerContentBO dataTracerContentBO : dataTracerContentBOList) {
			String filedDesc = dataTracerContentBO.getFieldSummary();
			boolean jsonFlag = JSONUtil.isTypeJSON(dataTracerContentBO.getFieldContent());
			if (jsonFlag) {
				contentList.add(filedDesc + "(请进入详情查看)");
			} else {
				contentList.add(dataTracerContentBO.getFieldSummary() + ":" + dataTracerContentBO.getFieldContent());
			}
		}
		return StringUtils.join(contentList, "<br/>");
	}

	public String getChangeContent(Object object) {
		List<Field> fields = getFields(object);
		try {
			List<DataTracerContentBO> dataTracerContentBOList = parseField(fields, object);
			String content = dataTracerContentConvertString(dataTracerContentBOList);
			if (StringUtils.isEmpty(content)) {
				return "";
			}
			return content;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void save(DataTracer dataTracer) {
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dataTracer.setUserId(userDetails.getUserId());
		dataTracer.setUsername(userDetails.getUsername());
		dataTracer.setIp(userDetails.getIp());
		dataTracer.setUserAgent(userDetails.getUserAgent());
		dataTracerRepository.save(dataTracer);
	}

}
