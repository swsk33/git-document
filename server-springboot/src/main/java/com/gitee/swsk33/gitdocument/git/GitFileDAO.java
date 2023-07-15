package com.gitee.swsk33.gitdocument.git;

import com.gitee.swsk33.gitdocument.annotation.GitRepository;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

/**
 * 用于读取Git裸仓库中文件信息(blob)的接口层
 */
@GitRepository
public interface GitFileDAO {

	/**
	 * 获取指定的commit中的文件列表
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @param commitId     指定的commit的id，传入HEAD可以指定获取最新的文件列表
	 * @return 文件列表
	 */
	List<String> getFileList(String gitDirectory, String commitId);

	/**
	 * 获取一个git工作区的最新的所有markdown文件列表
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @return 最新版本的版本库中所有文件列表
	 */
	List<String> getLatestFileList(String gitDirectory);

	/**
	 * 获取Git仓库中指定提交的版本中某个文件的二进制字节内容
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @param filePath     文件路径（相对于工作区下的路径）
	 * @param commitId     指定的commit的id，传入HEAD可以指定获取最新的文件列表
	 * @return 文件内容，为字节数组形式，仓库为空返回null
	 */
	byte[] getFileBytes(String gitDirectory, String filePath, String commitId);

	/**
	 * 获取Git仓库中最新提交的版本中某个文件的二进制字节内容
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @param filePath     文件路径（相对于工作区下的路径）
	 * @return 文件内容，为字节数组形式，仓库为空返回null
	 */
	byte[] getFileBytesInLatestCommit(String gitDirectory, String filePath);

	/**
	 * 获取Git仓库中最新提交的版本中某个文件的文本内容
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @param filePath     文件路径（相对于工作区下的路径）
	 * @return 文件内容，为字符串形式
	 */
	String getFileTextContentInLatestCommit(String gitDirectory, String filePath);

	/**
	 * 比较两次文集仓库的文件变化
	 *
	 * @param gitDirectory 仓库位置（绝对路径）
	 * @param oldCommitId  旧的提交Id
	 * @param newCommitId  新的提交Id
	 * @return 变化对象，只返回增加、删除、内容更改和修改文件的文件名这几种变化
	 */
	List<DiffEntry> compareDiffBetweenTwoCommits(String gitDirectory, String oldCommitId, String newCommitId);

}