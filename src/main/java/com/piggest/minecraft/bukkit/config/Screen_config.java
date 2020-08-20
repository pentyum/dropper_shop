package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.custom_map.Gif_screen;
import com.piggest.minecraft.bukkit.custom_map.Screen;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Screen_config extends Ext_config {
	private final HashMap<Integer, Screen> screen_map = new HashMap<>();
	private final ScheduledExecutorService service;

	public Screen_config() {
		super("screen.yml");
		ConfigurationSerialization.registerClass(Gif_screen.class);
		int threads = Runtime.getRuntime().availableProcessors();
		this.service = Executors.newScheduledThreadPool(threads);
		Dropper_shop_plugin.instance.getLogger().info("[屏幕渲染器]启动" + threads + "线程刷新");
	}

	public Screen get_screen(int id) {
		return screen_map.get(id);
	}

	public synchronized void add_screen(Screen screen) {
		if (screen.get_id() == 0) {
			screen.set_id(this.get_next_id());
		}
		screen_map.put(screen.get_id(), screen);
		service.scheduleAtFixedRate(screen, 1000, screen.get_refresh_interval() * 50, TimeUnit.MILLISECONDS);
	}

	@Override
	public void load() {
		super.load();
		List<?> screen_list = this.get_config().getList("screen-list");
		if (screen_list == null) {
			screen_list = new ArrayList<>();
		}
		for (Object screen : screen_list) {
			if (screen instanceof Screen) {
				this.add_screen((Screen) screen);
			}
		}
	}

	private int get_next_id() {
		return Collections.max(this.screen_map.keySet()) + 1;
	}

	public void stop_refresh() {
		this.service.shutdownNow();
	}
}
