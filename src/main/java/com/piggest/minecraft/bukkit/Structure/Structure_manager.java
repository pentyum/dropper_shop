package com.piggest.minecraft.bukkit.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public abstract class Structure_manager<T extends Abstract_structure> {
	public static Dropper_shop_plugin plugin = null;
	protected Class<? extends Abstract_structure> structure_class = null;
	protected HashMap<Location, T> structure_map = new HashMap<Location, T>();

	public Structure_manager(Dropper_shop_plugin plugin, Class<? extends Abstract_structure> structure_class) {
		Structure_manager.plugin = plugin;
		try {
			this.structure_class = (Class<? extends Abstract_structure>) structure_class;
		} catch (Exception e) {

		}
	}

	public T get(Location loc) {
		return this.structure_map.get(loc);
	}

	public void add(T new_shop) {
		this.structure_map.put(new_shop.get_location(), new_shop);
	}

	public void remove(T shop) {
		this.structure_map.remove(shop.get_location());
	}

	@SuppressWarnings("unchecked")
	public void load_shops() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		List<Map<?, ?>> map_list = plugin.get_shop_config().getMapList(structure_name);
		for (Map<?, ?> shop_save : map_list) {
			try {
				T shop = (T) structure_class.newInstance();
				shop.set_from_save(shop_save);
				this.structure_map.put(shop.get_location(), shop);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void save_shops() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		ArrayList<HashMap<String, Object>> shop_list = new ArrayList<HashMap<String, Object>>();
		for (Entry<Location, T> entry : structure_map.entrySet()) {
			shop_list.add(entry.getValue().get_save());
		}
		plugin.get_shop_config().set(structure_name, shop_list);
	}
}
