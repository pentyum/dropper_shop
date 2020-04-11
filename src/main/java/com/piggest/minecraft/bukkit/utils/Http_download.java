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

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Http_download {
	private int cache = 10 * 1024;
	private File save_dir;

	public Http_download(File save_dir) {
		if (!save_dir.isDirectory()) {
			save_dir.mkdir();
		}
		this.save_dir = save_dir;
	}

	public static String get_file_name(URL url) {
		String path = url.getPath();
		String[] paths = path.split("/");
		if (paths.length == 0) {
			return path;
		}
		return paths[paths.length - 1];
	}

	public void download(CommandSender sender, String url) {
		final CommandSender real_sender = sender == null ? Bukkit.getConsoleSender() : sender;
		real_sender.sendMessage("开始下载" + url);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					String file_name = Http_download.this.download(url);
					real_sender.sendMessage(file_name + "已完成下载");
				} catch (IOException | InterruptedException | URISyntaxException e) {
					real_sender.sendMessage("下载发生错误");
					real_sender.sendMessage(e.toString());
				}
			}
		}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 1);

	}

	private String download(String url) throws IOException, InterruptedException, URISyntaxException {
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
		URL url_obj = new URL(url);
		URI uri = url_obj.toURI();

		String file_name = get_file_name(url_obj);

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

}
