package com.piggest.minecraft.bukkit.custom_map;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.config.Map_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Map_init_listener implements Listener {
	@EventHandler
	public void on_map_init(MapInitializeEvent event) {
		MapView map = event.getMap();
		int id = map.getId();
		Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
		MapRenderer render = (MapRenderer) map_config.get_config().get("map_" + id);
		if (render != null) {
			Dropper_shop_plugin.instance.getLogger().info("map_" + id + ": " + render.getClass().getSimpleName());
			List<MapRenderer> renders = map.getRenderers();
			for (MapRenderer old_render : renders) {
				map.removeRenderer(old_render);
			}
			map.addRenderer(render);
		}
	}
}
