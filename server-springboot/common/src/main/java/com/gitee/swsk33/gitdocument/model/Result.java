package com.gitee.swsk33.gitdocument.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端返回结果
 *
 * @param <T> 结果数据类型
 */
@Data
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
	 * 返回成功结果
	 *
	 * @param message 消息
	 * @return 成功结果
	 */
	public static <T> Result<T> resultSuccess(String message) {
		Result<T> result = new Result<>();
		result.setSuccess(true);
		result.setMessage(message);
		result.setData(null);
		return result;
	}

	/**
	 * 返回成功结果
	 *
	 * @param message 消息
	 * @param data    数据体
	 * @return 成功结果
	 */
	public static <T> Result<T> resultSuccess(String message, T data) {
		Result<T> result = new Result<>();
		result.setSuccess(true);
		result.setMessage(message);
		result.setData(data);
		return result;
	}

	/**
	 * 返回失败结果
	 *
	 * @param message 消息
	 * @return 失败结果
	 */
	public static <T> Result<T> resultFailed(String message) {
		Result<T> result = new Result<>();
		result.setSuccess(false);
		result.setMessage(message);
		result.setData(null);
		return result;
	}

}