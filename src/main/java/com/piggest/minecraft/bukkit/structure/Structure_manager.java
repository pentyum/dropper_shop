package com.piggest.minecraft.bukkit.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Chunk_location;

public abstract class Structure_manager {
	protected Class<? extends Structure> structure_class = null;
	protected HashMap<Chunk_location, HashSet<Structure>> chunk_structure_map = new HashMap<Chunk_location, HashSet<Structure>>();
	protected HashMap<Location, Structure> structure_map = new HashMap<Location, Structure>();

	public Structure_manager(Class<? extends Structure> structure_class) {
		this.structure_class = structure_class;
	}

	public Structure get(Location loc) {
		return this.structure_map.get(loc);
	}

	private void add_to_chunk_structure_map(Structure new_structure) {
		Chunk_location chunk_location = new_structure.get_chunk_location();
		HashSet<Structure> structures_in_chunk = this.chunk_structure_map.get(chunk_location);
		if (structures_in_chunk == null) {
			HashSet<Structure> new_list = new HashSet<Structure>();
			new_list.add(new_structure);
			this.chunk_structure_map.put(chunk_location, new_list);
		} else {
			structures_in_chunk.add(new_structure);
		}
	}

	private void remove_from_chunk_structure_map(Structure structure) {
		Chunk_location chunk_location = structure.get_chunk_location();
		HashSet<Structure> structures_in_chunk = this.chunk_structure_map.get(chunk_location);
		structures_in_chunk.remove(structure);
		if (structures_in_chunk.size() == 0) {
			this.chunk_structure_map.remove(chunk_location);
		}
	}

	public void add(Structure new_structure) {
		if (new_structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) new_structure;
			Structure_runner[] runnable_list = new_HasRunner.get_runner();
			for (Structure_runner runner : runnable_list) {
				Dropper_shop_plugin.instance.getLogger().info("已启动" + runner.getClass().getName());
				if (runner.is_asynchronously() == true) {
					runner.runTaskTimerAsynchronously(Dropper_shop_plugin.instance, runner.get_delay(),
							runner.get_cycle());
				} else {
					runner.runTaskTimer(Dropper_shop_plugin.instance, runner.get_delay(), runner.get_cycle());
				}
			}
		}
		this.add_to_chunk_structure_map(new_structure);
		this.structure_map.put(new_structure.get_location(), new_structure);
	}

	public void remove(Structure structure) {
		if (structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) structure;
			for (BukkitRunnable runnable : new_HasRunner.get_runner()) {
				runnable.cancel();
			}
		}
		this.remove_from_chunk_structure_map(structure);
		this.structure_map.remove(structure.get_location());
	}

	public void load_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		List<Map<?, ?>> save_list = Dropper_shop_plugin.instance.get_shop_config().getMapList(structure_name);
		for (Map<?, ?> shop_save : save_list) {
			try {
				Structure shop = structure_class.newInstance();
				shop.set_from_save(shop_save);
				if (shop instanceof Multi_block_structure) {
					Multi_block_structure multi_block_struct = (Multi_block_structure) shop;
					if (multi_block_struct.completed() == true) {
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

	public abstract String get_permission_head();

	public HashSet<Structure> get_all_structures_in_chunk(Chunk_location chunk_location) {
		return this.chunk_structure_map.get(chunk_location);
	}
	
	public abstract Material[][][] get_model(); //model[y][z][x]
	public abstract int[] get_center();
}
