package com.xbsd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//java操作FTP，实现文件上传下载删除操作
public class FtpUtil {
	private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	private static ParamUtil paramUtil= new ParamUtil();
	/**
	 * FTP登录
	 */
	public static FTPClient login() {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {

			// 连接
			ftpClient.connect(paramUtil.getParam("ftp.ip"), Integer
					.parseInt(paramUtil.getParam("ftp.port")));
			// 检测连接是否成功
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				closeConn(ftpClient);
				logger.error("FTP拒绝访问。");
			}
			flag = ftpClient.login(paramUtil.getParam("ftp.user"), paramUtil
					.getParam("ftp.password"));
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(BINARY_FILE_TYPE);// 设置文件类型
			ftpClient.enterLocalPassiveMode(); // 连接模式被动连接
			// ftpClient.setConnectTimeout(1000*60);
		} catch (IOException ex) {
			logger.error("FTP登录错误", ex);
			// 关闭
			closeConn(ftpClient);
		}
		return ftpClient;
	}

	/**
	 * 断开FTP连接
	 */
	public static void closeConn(FTPClient ftpClient) {
		if (ftpClient != null) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					logger.error("断开FTP出现异常", e);
				}
			}
		}

	}

	/**
	 * 在FTP上创建文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean makDir(FTPClient ftpClient, String dir) {
		try {
			return ftpClient.makeDirectory(dir);
		} catch (IOException e) {
			logger.error("FTP文件夹创建失败", e);
		}
		return false;
	}

	public static String upload(InputStream in, String fileExt)
			throws IOException {
		FTPClient ftpClient = FtpUtil.login();
		boolean flag = false;
		//FileInputStream in = null;
		String url = "";
		try {
			//in = new InputStream();
			// 构造文件名
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = df1.format(new Date()) + "_"
					+ new Random().nextInt(1000) + "." + fileExt;
			// 构造存储目录
			SimpleDateFormat df2 = new SimpleDateFormat("yyyyMM");
			String dir = df2.format(new Date());
			if (null != ftpClient) {

				// 设置上传目录
				if (!ftpClient.changeWorkingDirectory(dir)) {
					makDir(ftpClient, dir);
					ftpClient.changeWorkingDirectory(dir);
				}
				// 上传
				flag = ftpClient.storeFile(fileName, in);
				if (flag) {
					url = dir + "/" + fileName;
				}
			}
		} catch (IOException e) {
			logger.error("上传文件异常：{}", e);
			throw new IOException("上传失败:" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(in);
		}
		return url;
	}

	public static InputStream downLoad( String filePath) {
		FTPClient ftpClient = FtpUtil.login();
		InputStream in = null;
		try {
			//String dir[]=filePath.split("/");
			//ftpClient.changeWorkingDirectory(dir[0]);
			return ftpClient.retrieveFileStream(filePath);
		} catch (IOException e) {
			logger.error("下载文件失败！", e);
		}
		return in;
	}

	public static boolean delete(String filePath) {
		FTPClient ftpClient = FtpUtil.login();
		Boolean b = false;
		try {
			return ftpClient.deleteFile(filePath);
		} catch (IOException e) {
			logger.error("删除文件异常：{}", e);
		} finally {
			FtpUtil.closeConn(ftpClient);
		}
		return b;
	}
	
}
