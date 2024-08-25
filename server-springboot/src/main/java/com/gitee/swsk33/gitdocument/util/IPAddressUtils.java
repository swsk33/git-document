package com.gitee.swsk33.gitdocument.util;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于IP地址的实用类
 */
public class IPAddressUtils {

	/**
	 * 从请求对象解析IP地址
	 *
	 * @param request 请求对象
	 * @return IP地址字符串
	 */
	public static String resolveIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (!StrUtil.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
			// X-Forwarded-For可能包含多个IP地址，第一个是客户端的真实IP
			return ip.split(",")[0];
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (!StrUtil.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (!StrUtil.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
			return ip;
		}
		ip = request.getHeader("HTTP_CLIENT_IP");
		if (!StrUtil.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
			return ip;
		}
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (!StrUtil.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 将ip2region结果解析为字符串形式
	 *
	 * @param ip2regionResult ip2region结果
	 * @return 字符串形式，例如：中国-湖北-宜昌-电信
	 */
	public static String resolveIPLocation(String ip2regionResult) {
		String[] resultArray = ip2regionResult.split("\\|");
		List<String> resultList = new ArrayList<>();
		for (String region : resultArray) {
			region = region.trim();
			if (region.equals("0")) {
				continue;
			}
			resultList.add(region);
		}
		return String.join("-", ArrayUtil.toArray(resultList, String.class));
	}

}