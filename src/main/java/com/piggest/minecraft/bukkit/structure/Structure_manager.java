package com.piggest.minecraft.bukkit.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public abstract class Structure_manager {
	protected Class<? extends Structure> structure_class = null;
	protected HashMap<Location, Structure> structure_map = new HashMap<Location, Structure>();

	public Structure_manager(Class<? extends Structure> structure_class) {
		this.structure_class = structure_class;
	}

	public Structure get(Location loc) {
		return this.structure_map.get(loc);
	}

	public void add(Structure new_structure) {
		if (new_structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) new_structure;
			BukkitRunnable[] runnable_list = new_HasRunner.get_runner();
			int i = 0;
			for (i = 0; i < runnable_list.length; i++) {
				Dropper_shop_plugin.instance.getLogger().info("已启动" + runnable_list[i].getClass().getName());
				runnable_list[i].runTaskTimerAsynchronously(Dropper_shop_plugin.instance,
						new_HasRunner.get_runner_delay()[i], new_HasRunner.get_runner_cycle()[i]);
			}
		}
		this.structure_map.put(new_structure.get_location(), new_structure);
	}

	public void remove(Structure structure) {
		if (structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) structure;
			for (BukkitRunnable runnable : new_HasRunner.get_runner()) {
				runnable.cancel();
			}
		}
		this.structure_map.remove(structure.get_location());
	}

	public void load_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		List<Map<?, ?>> map_list = Dropper_shop_plugin.instance.get_shop_config().getMapList(structure_name);
		for (Map<?, ?> shop_save : map_list) {
			try {
				Structure shop = structure_class.newInstance();
				shop.set_from_save(shop_save);
				if (shop instanceof Multi_block_structure) {
					Multi_block_structure multi_block_struct = (Multi_block_structure) shop;
					if (multi_block_struct.completed() > 0) {
						this.add(multi_block_struct);
					}
				} else {
					this.add(shop);
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void save_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		ArrayList<HashMap<String, Object>> shop_list = new ArrayList<HashMap<String, Object>>();
		for (Entry<Location, Structure> entry : structure_map.entrySet()) {
			shop_list.add(entry.getValue().get_save());
		}
		Dropper_shop_plugin.instance.get_shop_config().set(structure_name, shop_list);
	}

	public abstract Structure find(String player_name, Location loc, boolean new_structure);
}
