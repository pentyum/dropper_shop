package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.custom_map.*;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_background_map_render;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_screen;
import com.piggest.minecraft.bukkit.custom_map.clock.Digital_clock_screen;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import javax.lang.model.type.ArrayType;
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
	private List<Screen> screen_list = new ArrayList<>();

	public Screen_config() {
		super("screen.yml");
		ConfigurationSerialization.registerClass(Analog_clock_background_map_render.class);
		ConfigurationSerialization.registerClass(Background_map_render.class);

		ConfigurationSerialization.registerClass(Gif_screen.class);
		ConfigurationSerialization.registerClass(Local_image_screen.class);
		ConfigurationSerialization.registerClass(Qr_code_screen.class);
		ConfigurationSerialization.registerClass(Character_screen.class);
		ConfigurationSerialization.registerClass(Digital_clock_screen.class);
		ConfigurationSerialization.registerClass(Analog_clock_screen.class);
		ConfigurationSerialization.registerClass(Rolling_subtitle_screen.class);
		ConfigurationSerialization.registerClass(Stock_subtitle_screen.class);
		ConfigurationSerialization.registerClass(Stock_panel_screen.class);

		int threads = Runtime.getRuntime().availableProcessors();
		this.service = Executors.newScheduledThreadPool(threads);
		Dropper_shop_plugin.instance.getLogger().info("[屏幕渲染器]启动" + threads + "线程刷新");
	}

	public Screen get_screen(int id) {
		return screen_map.get(id);
	}

	public synchronized void add_screen(Screen screen) {
		if (screen.get_id() == 0) {//新添加的screen
			screen.set_id(this.get_next_id());
			this.screen_list.add(screen);
			this.set("screen-list", this.screen_list);
		}
		screen_map.put(screen.get_id(), screen);
		if (screen.get_refresh_interval() > 0) {
			service.scheduleAtFixedRate(screen, 1000, screen.get_refresh_interval() * 50, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void load() {
		super.load();
		List<Screen> screen_list = (List<Screen>) this.get_config().getList("screen-list");
		if (screen_list == null) {
			screen_list = new ArrayList<>();
		}
		for (Screen screen : screen_list) {
			this.add_screen(screen);
		}
	}

	private int get_next_id() {
		if (this.screen_map.size() == 0) {
			return 0;
		}
		return Collections.max(this.screen_map.keySet()) + 1;
	}

	public void stop_refresh() {
		this.service.shutdownNow();
	}

	public void reload() {
	}
}
