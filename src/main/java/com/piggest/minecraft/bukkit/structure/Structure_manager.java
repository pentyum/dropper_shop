package com.piggest.minecraft.bukkit.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.config.World_structure_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Chunk_location;

public abstract class Structure_manager<T extends Structure> {
	protected Class<T> structure_class = null;
	protected HashMap<Chunk_location, HashSet<T>> chunk_structure_map = new HashMap<Chunk_location, HashSet<T>>();
	protected HashMap<Location, T> structure_map = new HashMap<Location, T>();

	public Structure_manager(Class<T> structure_class) {
		this.structure_class = structure_class;
	}

	public T get(Location loc) {
		return this.structure_map.get(loc);
	}

	private void add_to_chunk_structure_map(T new_structure) {
		Chunk_location chunk_location = new_structure.get_chunk_location();
		HashSet<T> structures_in_chunk = this.chunk_structure_map.get(chunk_location);
		if (structures_in_chunk == null) {
			HashSet<T> new_list = new HashSet<T>();
			new_list.add(new_structure);
			this.chunk_structure_map.put(chunk_location, new_list);
		} else {
			structures_in_chunk.add(new_structure);
		}
	}

	private void remove_from_chunk_structure_map(T structure) {
		Chunk_location chunk_location = structure.get_chunk_location();
		HashSet<T> structures_in_chunk = this.chunk_structure_map.get(chunk_location);
		structures_in_chunk.remove(structure);
		if (structures_in_chunk.size() == 0) {
			this.chunk_structure_map.remove(chunk_location);
		}
	}

	@SuppressWarnings("unchecked")
	public void add(Structure new_structure) {
		if (new_structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) new_structure;
			Structure_runner[] runnable_list = new_HasRunner.get_runner();
			for (Structure_runner runner : runnable_list) {
				Dropper_shop_plugin.instance.getLogger().info("[结构管理器]已启动" + runner.getClass().getSimpleName());
				if (runner.is_asynchronously() == true) {
					runner.runTaskTimerAsynchronously(Dropper_shop_plugin.instance, runner.get_delay(),
							runner.get_cycle());
				} else {
					runner.runTaskTimer(Dropper_shop_plugin.instance, runner.get_delay(), runner.get_cycle());
				}
			}
		}
		this.add_to_chunk_structure_map((T) new_structure);
		this.structure_map.put(new_structure.get_location(), (T) new_structure);
	}

	@SuppressWarnings("unchecked")
	public void remove(Structure structure) {
		if (structure instanceof HasRunner) {
			HasRunner new_HasRunner = (HasRunner) structure;
			for (BukkitRunnable runnable : new_HasRunner.get_runner()) {
				runnable.cancel();
			}
		}
		this.remove_from_chunk_structure_map((T) structure);
		this.structure_map.remove(structure.get_location());
	}

	public void load_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		ArrayList<Map<?, ?>> save_list = new ArrayList<Map<?, ?>>();
		for (World_structure_config config : Dropper_shop_plugin.instance.get_shop_config().values()) {
			save_list.addAll(config.getMapList(structure_name));
		}
		// List<Map<?, ?>> save_list =
		// Dropper_shop_plugin.instance.get_shop_config().getMapList(structure_name);
		for (Map<?, ?> shop_save : save_list) {
			try {
				T shop = structure_class.newInstance();
				shop.set_from_save(shop_save);
				if (shop instanceof Multi_block_structure) {
					Multi_block_structure multi_block_struct = (Multi_block_structure) shop;
					if (multi_block_struct.get_location().getWorld() != null) {
						if (multi_block_struct.completed() == true) {
							this.add(shop);
						}
					}
				} else {
					this.add(shop);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void save_structures() {
		String structure_name = this.structure_class.getName().replace('.', '-');
		HashMap<String, ArrayList<HashMap<String,Object>>> world_structure_list = new HashMap<>();
		for (World world : Dropper_shop_plugin.instance.getServer().getWorlds()) {
			world_structure_list.put(world.toString(), new ArrayList<HashMap<String, Object>>());
		}
		//ArrayList<HashMap<String, Object>> structure_list = new ArrayList<HashMap<String, Object>>();
		for (Entry<Location, T> entry : structure_map.entrySet()) {
			Structure structure = entry.getValue();
			synchronized (structure) {
				ArrayList<HashMap<String, Object>> structure_list = world_structure_list.get(structure.get_world_name());
				structure_list.add(structure.get_save());
			}
		}
		for(Entry<String,ArrayList<HashMap<String,Object>>> entry:world_structure_list.entrySet()) {
			Dropper_shop_plugin.instance.get_shop_config().get(entry.getKey()).set(structure_list);
		}
		Dropper_shop_plugin.instance.get_shop_config().set(structure_name, structure_list);
	}

	public T find_existed(Location loc) {
		int x;
		int y;
		int z;
		int[] center = this.get_center();
		Material[][][] model = this.get_model();
		int y_size = model.length;
		int z_size = model[0].length;
		int x_size = model[0][0].length;
		T structure;
		for (x = center[0] - x_size + 1; x <= center[0]; x++) {
			for (y = center[1] - y_size + 1; y <= center[1]; y++) {
				for (z = center[2] - z_size + 1; z <= center[2]; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == model[center[1]][center[2]][center[0]]) {
						structure = this.get(check_loc);
						if (structure != null) {
							return structure;
						}
					}
				}
			}
		}
		return null;
	}

	public T find_and_make(Player player, Location loc) {
		int x;
		int y;
		int z;
		int[] center = this.get_center();
		Material[][][] model = this.get_model();
		int y_size = model.length;
		int z_size = model[0].length;
		int x_size = model[0][0].length;
		T structure = null;
		for (x = center[0] - x_size + 1; x <= center[0]; x++) {
			for (y = center[1] - y_size + 1; y <= center[1]; y++) {
				for (z = center[2] - z_size + 1; z <= center[2]; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == model[center[1]][center[2]][center[0]]) {
						try {
							structure = structure_class.newInstance();
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
						structure.set_location(check_loc);
						if (structure.completed() == true) {
							if (structure instanceof Ownable) {
								((Ownable) structure).set_owner(player.getName());
							}
							structure.init_after_set_location();
							return structure;
						}
					}
				}
			}
		}
		return null;
	}

	// public abstract T find(String player_name, Location loc, boolean
	// new_structure);

	public abstract String get_permission_head();

	public HashSet<T> get_all_structures_in_chunk(Chunk_location chunk_location) {
		return this.chunk_structure_map.get(chunk_location);
	}

	public HashSet<T> get_all_structures_around_chunk(Chunk_location chunk_location, int r) {
		HashSet<T> result = new HashSet<T>();
		int center_x = chunk_location.get_x();
		int center_z = chunk_location.get_z();
		int x, z;
		for (x = center_x - r; x <= center_x + r; x++) {
			for (z = center_z - r; z <= center_z + r; z++) {
				Chunk_location check_chunk = new Chunk_location(chunk_location.get_world_name(), x, z);
				HashSet<T> find = this.get_all_structures_in_chunk(check_chunk);
				if (find != null) {
					result.addAll(find);
				}
			}
		}
		return result;
	}

	public abstract Material[][][] get_model(); // model[y][z][x]

	public abstract int[] get_center();

}
