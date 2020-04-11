package com.piggest.minecraft.bukkit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class Http_download {
	private int cache = 10 * 1024;
	private File save_dir;
	
	public Http_download(File save_dir) {
		if (!save_dir.isDirectory()) {
			save_dir.mkdir();
		}
		this.save_dir = save_dir;
	}
	
	public String download(String url) throws IOException, InterruptedException, URISyntaxException {
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
		URL url_obj = new URL(url);
		URI uri = url_obj.toURI();

		String file_name = url_obj.getFile();
		
		HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

		HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
		InputStream is = response.body();

		File file = new File(this.save_dir, file_name);
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
	
	private String get_file_name(HttpResponse<?> response) {
		String file_name = null;
		if (file_name != null) {
			return file_name;
		} else {
			file_name = getRandomFileName();
			Optional<String> content_type = response.headers().firstValue("Content-Type");
			if (content_type.isPresent()) {
				String type = content_type.get();
				file_name += Mime_type.mime_map.get(type);
			}
		}
		return file_name;
	}
	
	
	private String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}
}
