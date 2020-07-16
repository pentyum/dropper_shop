package com.piggest.minecraft.bukkit.nms.map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.minecraft.server.v1_16_R1.WorldMap;
import org.bukkit.craftbukkit.v1_16_R1.map.CraftMapView;
import org.bukkit.map.MapView;

import java.lang.reflect.Field;

public class Map_editor_1_16 implements Map_editor {
	Field worldmap_field;

	public Map_editor_1_16() {
		try {
			worldmap_field = CraftMapView.class.getDeclaredField("worldMap");
			worldmap_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void set_map_color(MapView map, byte[] colors) {
		if (colors.length != 128 * 128) {
			Dropper_shop_plugin.instance.getLogger().warning("颜色数组长度不是128*128");
			return;
		}
		CraftMapView craft_map = (CraftMapView) map;
		if (craft_map.isLocked() == false) {
			Dropper_shop_plugin.instance.getLogger().warning("不能编辑未锁定地图");
			return;
		}
		WorldMap world_map = null;
		try {
			world_map = (WorldMap) worldmap_field.get(craft_map);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (world_map != null) {
			world_map.colors = colors;
		}
	}
}
