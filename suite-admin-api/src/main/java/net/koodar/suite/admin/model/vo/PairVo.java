package net.koodar.suite.admin.model.vo;

import lombok.Data;

/**
 * Pair Vo
 *
 * @author liyc
 */
@Data
public class PairVo<T> {

	private String label;

	private T value;

	public PairVo(String label, T value) {
		this.label = label;
		this.value = value;
	}

	public static <T>PairVo<T> of(String label, T value) {
		return new PairVo<>(label, value);
	}

}
