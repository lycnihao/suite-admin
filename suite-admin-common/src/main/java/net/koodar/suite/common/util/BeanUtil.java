package net.koodar.suite.common.util;

import org.springframework.beans.BeanUtils;

/**
 * bean相关工具类
 *
 * @author liyc
 */
public class BeanUtil {

	/**
	 * 复制对象
	 *
	 * @param source 源 要复制的对象
	 * @param target 目标 复制到此对象
	 * @param <T>
	 * @return
	 */
	public static <T> T copy(Object source, Class<T> target) {
		if (source == null || target == null) {
			return null;
		}
		try {
			T newInstance = target.newInstance();
			BeanUtils.copyProperties(source, newInstance);
			return newInstance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 复制对象
	 *
	 * @param source 源 要复制的对象
	 * @param target 目标 复制到此对象
	 * @param <T>
	 * @return
	 */
	public static <T> T copy(Object source, T target) {
		if (source == null || target == null) {
			return null;
		}
		try {
			BeanUtils.copyProperties(source, target);
			return target;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
