package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.custom_map.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Screen_config extends Ext_config {
	private final HashMap<Integer, Screen> screen_map = new HashMap<>();

	public Screen_config() {
		super("screen.yml");
	}

	public Screen get_screen(int id) {
		return screen_map.get(id);
	}

	private void add_screen(Screen screen) {
		screen_map.put(screen.get_id(), screen);
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
}
