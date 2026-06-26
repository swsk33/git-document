package com.gitee.swsk33.gitdocument.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dataobject.Article;

import java.util.List;

/**
 * 用于文件名排序的实用类
 */
public class FileNameSortUtil {

	/**
	 * 根据文章在Git仓库的路径，对其文件名进行排序，排序以数字部分优先升序排列
	 *
	 * @param articleList 获取到的文章列表
	 */
	public static void sortArticlePath(List<Article> articleList) {
		articleList.sort((articleOne, articleTwo) -> {
			// 使用正则表达式，把数字和非数字部分拆分
			String regex = "(\\D+|\\d+)";
			List<String> partsOne = ReUtil.findAll(regex, articleOne.getFilePath(), 0);
			List<String> partsTwo = ReUtil.findAll(regex, articleTwo.getFilePath(), 0);
			// 逐个进行比较，并考虑文件名长度不同的情况
			int minPart = Math.min(partsOne.size(), partsTwo.size());
			// 每次遍历的部分
			String eachPartOne, eachPartTwo;
			for (int i = 0; i < minPart; i++) {
				eachPartOne = partsOne.get(i);
				eachPartTwo = partsTwo.get(i);
				// 判断两者是否是数字
				boolean isPartOneNumber = NumberUtil.isNumber(eachPartOne);
				boolean isPartTwoNumber = NumberUtil.isNumber(eachPartTwo);
				// 如果两者都是数字，则按照数字大小升序排列
				if (isPartOneNumber && isPartTwoNumber) {
					int numberPartOne = Integer.parseInt(eachPartOne);
					int numberPartTwo = Integer.parseInt(eachPartTwo);
					// 相等则比较下一片段
					if (numberPartOne == numberPartTwo) {
						continue;
					}
					// 不相等，则直接按照升序完成排序
					return numberPartOne - numberPartTwo;
				} else if (!isPartOneNumber && !isPartTwoNumber) {
					// 否则如果两者都不是数字，则按照字符串字符集默认方式排序
					// 字符串如果相等则比较下一片段
					if (StrUtil.equals(eachPartOne, eachPartTwo)) {
						continue;
					}
					// 否则进行比较
					return StrUtil.compare(eachPartOne, eachPartTwo, true);
				} else {
					// 否则，就是有两者其一不为数字，将为数字的部分的文件名放在前面
					int one = isPartOneNumber ? 1 : 0;
					int two = isPartTwoNumber ? 1 : 0;
					return two - one;
				}
			}
			// 否则，说明两个文件完全相等或者短文件名和长文件名的前半部分完全相等，将文件名长的放在后面
			return partsOne.size() - partsTwo.size();
		});
	}

}