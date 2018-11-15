package com.piggest.minecraft.bukkit.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public abstract class Structure_manager<T extends Abstract_structure> {
	protected Class<T> structure_class = null;
	protected HashMap<Location, T> structure_map = new HashMap<Location, T>();

	public Structure_manager(Class<T> structure_class) {
		this.structure_class = structure_class;
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

	public void load_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		List<Map<?, ?>> map_list = Dropper_shop_plugin.instance.get_shop_config().getMapList(structure_name);
		for (Map<?, ?> shop_save : map_list) {
			try {
				T shop = structure_class.newInstance();
				shop.set_from_save(shop_save);
				if (shop.completed() > 0) {
					this.add(shop);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void save_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		ArrayList<HashMap<String, Object>> shop_list = new ArrayList<HashMap<String, Object>>();
		for (Entry<Location, T> entry : structure_map.entrySet()) {
			shop_list.add(entry.getValue().get_save());
		}
		Dropper_shop_plugin.instance.get_shop_config().set(structure_name, shop_list);
	}
}
