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
	 * 设定结果为成功
	 *
	 * @param message 消息
	 */
	public void setResultSuccess(String message) {
		this.message = message;
		this.success = true;
		this.data = null;
	}

	/**
	 * 设定结果为成功
	 *
	 * @param message 消息
	 * @param data    数据体
	 */
	public void setResultSuccess(String message, T data) {
		this.message = message;
		this.success = true;
		this.data = data;
	}

	/**
	 * 设定结果为失败
	 *
	 * @param message 消息
	 */
	public void setResultFailed(String message) {
		this.message = message;
		this.success = false;
		this.data = null;
	}

}