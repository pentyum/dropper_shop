package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.config.Structure_config;
import com.piggest.minecraft.bukkit.utils.Chunk_location;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Structure_manager<T extends Structure> {
	protected Class<T> structure_class = null;
	protected Constructor<T> constructor = null;
	protected HashMap<World, Structure_config> config_map = new HashMap<>();
	protected HashMap<Chunk_location, HashSet<T>> chunk_structure_map = new HashMap<>();
	protected HashMap<World, HashMap<Location, T>> structure_map = new HashMap<>();
	protected Structure_manager_runner structure_manager_runner;
	protected Structure_runner<T>[] structure_runners = null;
	protected Structure_completeness_checker structure_completeness_checker = new Structure_completeness_checker(this);
	protected Structure_manager_logger logger = new Structure_manager_logger(this);

	public Structure_manager(Class<T> structure_class) {
		this.structure_class = structure_class;
		ConfigurationSerialization.registerClass(this.structure_class);
		try {
			this.constructor = structure_class.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		for (World world : Bukkit.getServer().getWorlds()) {
			this.load_config_from_world(world);
		}
		if (this instanceof Has_runner) {
			this.structure_runners = ((Has_runner) this).init_runners();
		}
	}

	public void load_config_from_world(World world) {
		Structure_config config = new Structure_config(world, this.get_permission_head());
		this.config_map.put(world, config);
		//this.logger.info(world.getName() + "的" + this.get_permission_head() + "的配置文件已读取");
	}

	public T get(Location loc) {
		return this.structure_map.get(loc.getWorld()).get(loc);
	}

	private void add_to_chunk_structure_map(T new_structure) {
		Chunk_location chunk_location = new_structure.get_chunk_location();
		HashSet<T> structures_in_chunk = this.chunk_structure_map.get(chunk_location);
		if (structures_in_chunk == null) {
			HashSet<T> new_list = new HashSet<>();
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
		this.add_to_chunk_structure_map((T) new_structure);
		Location loc = new_structure.get_location();
		this.structure_map.get(loc.getWorld()).put(new_structure.get_location(), (T) new_structure);
	}

	@SuppressWarnings("unchecked")
	public void remove(Structure structure) {
		this.remove_from_chunk_structure_map((T) structure);
		this.structure_map.get(structure.get_world()).remove(structure.get_location());
	}

	/**
	 * 检查该管理器下全部结构的完整性，不完整的将被删除。
	 *
	 * @return 被移除的结构数量
	 */
	public int check_structures_completeness() {
		ArrayList<T> to_remove = new ArrayList<>();
		for (World world : Bukkit.getWorlds()) {
			Collection<T> structures = this.get_all_structures_in_world(world);
			for (T structure : structures) {
				if (!structure.completed()) {
					to_remove.add(structure);
				}
			}
		}
		to_remove.forEach(this::remove);
		return to_remove.size();
	}

	public void load_world_structures(World world) {
		Structure_config config = this.config_map.get(world);
		List<Structure> list = config.getList();
		this.structure_map.put(world, new HashMap<>());
		if (list == null) {
			this.logger.info(world.getName() + "已加载0个" + this.structure_class.getSimpleName());
			return;
		}
		for (Structure structure : list) {
			if (structure instanceof Multi_block_structure) {
				Multi_block_structure multi_block_struct = (Multi_block_structure) structure;
				if (multi_block_struct.completed() == true) {
					this.add(structure);
				}
			} else {
				this.add(structure);
			}
		}
		this.logger.info(world.getName() + "已加载" + list.size() + "个" + this.structure_class.getSimpleName());
	}

	public void start_structure_manager_runner() {
		if (this.structure_manager_runner != null) {
			this.structure_manager_runner.start();
		}
		this.structure_completeness_checker.start();
		if (this instanceof Has_runner) {
			for (Structure_runner<T> runner : this.structure_runners) {
				runner.start();
			}
		}
	}

	public void unload_world_structures(World world) {
		HashMap<Location, T> world_structures_map = this.structure_map.get(world);
		synchronized (world_structures_map) {
			Collection<T> structures = world_structures_map.values();
			Structure[] structures_to_remove = new Structure[structures.size()];
			structures_to_remove = structures.toArray(structures_to_remove);
			for (Structure structure : structures_to_remove) {
				structure.remove();
			}
			this.logger.info(world.getName() + "已卸载" + structures_to_remove.length + "个" + this.structure_class.getSimpleName());
			world_structures_map.remove(world);
		}
	}

	public void save_world_structures(World world) {
		HashMap<Location, T> map = this.structure_map.get(world);
		if (map == null) {
			return;
		}
		Collection<T> structures = map.values();
		Structure_config config = this.config_map.get(world);
		config.set(new ArrayList<>(structures));
		config.save();
	}

	@Nullable
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

	@Nullable
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
							structure = this.constructor.newInstance();
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException e) {
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

	@Nonnull
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

	public void stop_runner() {
		if (this.structure_manager_runner != null) {
			this.structure_manager_runner.cancel();
		}
		for (Structure_runner runner : this.structure_runners) {
			runner.cancel();
		}
	}

	/**
	 * 获取该结构管理器下某世界中的全部结构
	 *
	 * @param world
	 * @return 全部结构的集合，该集合不是副本。
	 */
	public Collection<T> get_all_structures_in_world(@Nonnull World world) {
		return this.structure_map.get(world).values();
	}

	public void load_instance_from_config() {
		for (Structure_config config : this.config_map.values()) {
			config.load();
		}
	}

	public void load_instance_from_world_config(World world) {
		Structure_config config = this.config_map.get(world);
		if (config == null) {
			this.logger.warning(world.getName() + "的" + this.get_permission_head() + "的配置文件未读取，请先读取后再实例化！");
			return;
		}
		config.load();
	}

	public void backup_config() {
		for (Structure_config config : this.config_map.values()) {
			config.backup();
		}
	}

	public Structure_manager_logger get_logger() {
		return this.logger;
	}

	@Nullable
	public abstract String get_gui_name();
}
