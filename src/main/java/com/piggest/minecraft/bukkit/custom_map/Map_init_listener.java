package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.config.Map_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Map_init_listener implements Listener {
	@EventHandler
	public void on_map_init(MapInitializeEvent event) {
		MapView map = event.getMap();
		int id = map.getId();
		Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
		Custom_map_render edge_render = map_config.get_edge_from_config(id);
		Custom_map_render content_render = map_config.get_content_from_config(id);
		if (content_render != null) {
			Dropper_shop_plugin.instance.getLogger()
					.info("map_" + id + ": " + content_render.getClass().getSimpleName());
			Dropper_shop_plugin.instance.get_map_config().replace_render(map, content_render,
					edge_render);
		}
	}
}
