package com.gitee.swsk33.gitdocument.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后端返回结果
 *
 * @param <T> 结果数据类型
 */
@Getter
@Setter
@NoArgsConstructor
public class Result<T> implements Serializable {

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 成功状态
	 */
	private boolean success;

	/**
	 * 数据
	 */
	private T data;

	/**
	 * 失败结果构造器
	 *
	 * @param message 消息
	 */
	public Result(String message) {
		this.message = message;
		this.success = false;
		this.data = null;
	}

	/**
	 * 成功结果构造器
	 *
	 * @param message 消息
	 * @param data    数据体
	 */
	public Result(String message, T data) {
		this.message = message;
		this.success = true;
		this.data = data;
	}

}