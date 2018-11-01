package com.piggest.minecraft.bukkit.dropper_shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;

public class Dropper_shop_manager {
	private Dropper_shop_plugin plugin = null;
	private HashMap<Location, Dropper_shop> shops_map = new HashMap<Location, Dropper_shop>();

	public Dropper_shop_manager(Dropper_shop_plugin plugin) {
		this.plugin = plugin;
	}

	public void load_shops() {
		List<Map<?, ?>> map_list = plugin.get_shop_config().getMapList("shops");
		for (Map<?, ?> shop_save : map_list) {
			String world_name = (String) shop_save.get("world");
			int x = (Integer) shop_save.get("x");
			int y = (Integer) shop_save.get("y");
			int z = (Integer) shop_save.get("z");
			String owner = (String) shop_save.get("owner");
			Material item = Material.getMaterial((String) shop_save.get("item"));
			Dropper_shop shop = new Dropper_shop(plugin, world_name, x, y, z, owner);
			shop.set_selling_item(item);
			this.shops_map.put(shop.get_location(), shop);
		}
	}

	public void save_shops() {
		ArrayList<HashMap<String, Object>> shop_list = new ArrayList<HashMap<String, Object>>();
		for (Entry<Location, Dropper_shop> entry : shops_map.entrySet()) {
			shop_list.add(entry.getValue().get_save());
		}
		plugin.get_shop_config().set("shops", shop_list);
	}

	public Dropper_shop get_dropper_shop(Location loc) {
		return this.shops_map.get(loc);
	}

	public void add(Dropper_shop new_shop) {
		this.shops_map.put(new_shop.get_location(), new_shop);
	}

	public void remove(Dropper_shop shop) {
		this.shops_map.remove(shop.get_location());
	}

}
