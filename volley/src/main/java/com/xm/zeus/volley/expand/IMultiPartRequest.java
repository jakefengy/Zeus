package com.xm.zeus.volley.expand;

import java.io.File;
import java.util.Map;

/**
 * 文件上传请求中，文件和参数操作入口
 * 
 */
public interface IMultiPartRequest {

	/**
	 * 添加文件
	 * 
	 * @param param
	 *            文件标识，可以为文件名
	 * @param file
	 *            文件
	 */
	public void addFileUpload(String param, File file);

	/**
	 * 添加参数
	 * 
	 * @param param
	 *            参数名
	 * @param content
	 *            参数值
	 */
	public void addStringUpload(String param, String content);

	/**
	 * 获得文件
	 * 
	 * @return 文件集合key-value
	 */
	public Map<String, File> getFileUploads();

	/**
	 * 获得参数
	 * 
	 * @return 参数集合key-value
	 */
	public Map<String, String> getStringUploads();
}