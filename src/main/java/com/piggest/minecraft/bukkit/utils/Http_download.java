package com.piggest.minecraft.bukkit.utils;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Http_download {
	private int cache = 10 * 1024;
	private File root;

	public Http_download(File folder) {
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		this.root = folder;
	}

	/**
	 * 根据url下载文件，文件名从response header头中获取
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String download(String url) throws IOException {
		return download(url, null);
	}

	/**
	 * 根据url下载文件，保存到filepath中
	 *
	 * @param url
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public String download(String url, String filepath) throws IOException {

		HttpClient client = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();// 设置请求和传输超时时间
		httpget.setConfig(requestConfig);
		HttpResponse response = client.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		if (filepath == null) {
			filepath = getFilePath(response);
		}

		File file = new File(filepath);
		file.getParentFile().mkdirs();
		FileOutputStream fileout = new FileOutputStream(file);
		/**
		 * 根据实际运行效果 设置缓冲区大小
		 */
		byte[] buffer = new byte[cache];
		int ch = 0;
		while ((ch = is.read(buffer)) != -1) {
			fileout.write(buffer, 0, ch);
		}
		is.close();
		fileout.flush();
		fileout.close();

		return file.getName();
	}

	/**
	 * 获取response要下载的文件的默认路径
	 *
	 * @param response
	 * @return
	 */
	private String getFilePath(HttpResponse response) {
		String filepath = root.getAbsolutePath() + "/";
		String filename = getFileName(response);

		if (filename != null) {
			filepath += filename;
		} else {
			filepath += getRandomFileName();
		}
		Header content_type = response.getFirstHeader("Content-Type");
		if (content_type != null) {
			String type = content_type.getValue();
			filepath += Mime_type.mime_map.get(type);
		}
		return filepath;
	}

	/**
	 * 获取response header中Content-Disposition中的filename值
	 *
	 * @param response
	 * @return
	 */
	private String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						// filename = new String(param.getValue().toString().getBytes(), "utf-8");
						// filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}

	/**
	 * 获取随机文件名
	 *
	 * @return
	 */
	private String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}

}