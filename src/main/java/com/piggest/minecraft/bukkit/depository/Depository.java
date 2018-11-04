package com.piggest.minecraft.bukkit.depository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.Structure.Abstract_structure;
import com.piggest.minecraft.bukkit.Structure.Ownable;
import com.piggest.minecraft.bukkit.Structure.Structure_manager;

public class Depository extends Abstract_structure implements Ownable {
	public static int[] price_level = { 5, 10, 20, 30, 40 };
	public static int[] capacity_level = { 5000, 10000, 20000, 30000, 50000 };
	private String world_name = null;
	private int x;
	private int y;
	private int z;
	private Depository_runner runner = new Depository_runner(this);
	private boolean accessible = true;
	private HashMap<Material, Integer> contents = new HashMap<Material, Integer>();
	private HashSet<Integer> levels = new HashSet<Integer>();
	private String owner = null;

	public void set_accessible(boolean accessible) {
		this.accessible = accessible;
	}

	public boolean is_accessible() {
		return this.accessible;
	}

	public int get_max_type() {
		return this.levels.size();
	}

	public int get_max_capacity() {
		int capacity = 0;
		for (int level : this.levels) {
			capacity = capacity + Depository.capacity_level[level];
		}
		return capacity;
	}

	public int get_price() {
		int price = 0;
		for (int level : this.levels) {
			price = price + Depository.price_level[level];
		}
		return price;
	}

	public int get_capacity() {
		int capacity = 0;
		for (Entry<Material, Integer> entry : this.contents.entrySet()) {
			capacity = capacity + entry.getValue();
		}
		return capacity;
	}

	public boolean add(ItemStack item) {
		Integer current_num = this.contents.get(item.getType());
		if (current_num == null) {
			return false;
		}
		int add_num = item.getAmount();
		if (this.get_capacity() + add_num > this.get_max_capacity()) {
			add_num = this.get_max_capacity() - this.get_capacity();
		}
		this.contents.put(item.getType(), current_num + add_num);
		item.setAmount(item.getAmount() - add_num);
		return true;
	}

	public ItemStack remove(Material type) {
		return this.remove(type, 1);
	}

	public ItemStack remove(Material type, int num) {
		Integer current_num = this.contents.get(type);
		if (current_num == null) {
			return null;
		}
		if (current_num == 0) {
			return null;
		}
		if (current_num - num < 0) {
			num = current_num;
		}
		ItemStack item = new ItemStack(type, num);
		this.contents.put(type, current_num - num);
		return item;
	}
	
	public Depository_runner get_runner() {
		return this.runner;
	}

	public void set_owner(String owner) {
		this.owner = owner;
	}

	@SuppressWarnings("deprecation")
	public OfflinePlayer get_owner() {
		return Bukkit.getOfflinePlayer(owner);
	}

	public String get_owner_name() {
		return this.owner;
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		this.x = (Integer) shop_save.get("x");
		this.y = (Integer) shop_save.get("y");
		this.z = (Integer) shop_save.get("z");
		this.owner = (String) shop_save.get("owner");
		this.world_name = (String) shop_save.get("world");
		Structure_manager.plugin.getLogger().info(shop_save.get("levels").getClass().getName());
		Structure_manager.plugin.getLogger().info(shop_save.get("contents").getClass().getName());
	}

	@Override
	public void set_location(Location loc) {
		this.set_location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	@Override
	public void set_location(String world_name, int x, int y, int z) {
		this.world_name = world_name;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Location get_location() {
		return new Location(Bukkit.getWorld(world_name), this.x, this.y, this.z);
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("world", this.world_name);
		save.put("owner", this.owner);
		save.put("x", this.x);
		save.put("y", this.y);
		save.put("z", this.z);
		save.put("levels", this.levels);
		save.put("contents", this.contents);
		return save;
	}

	@Override
	public int completed() {
		int x;
		int y;
		int z;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Material material = this.get_block(x, y, z).getType();
					if (x == 0 && y == 0 && z == 0 && material != Material.END_ROD) {
						return 0;
					}
					if (Math.abs(x) == 1 && Math.abs(y) == 1 && Math.abs(z) == 1 && material != Material.LAPIS_BLOCK) {
						return 0;
					}
					if (Math.abs(x) + Math.abs(z) + Math.abs(y) == 2 && material != Material.IRON_BLOCK) {
						return 0;
					}
					if (Math.abs(x) + Math.abs(z) == 1 & Math.abs(y) == 0 && material != Material.IRON_BARS) {
						return 0;
					}
					if (Math.abs(x) == 0 && Math.abs(z) == 1 & Math.abs(y) == 1 && material != Material.DIAMOND_BLOCK) {
						return 0;
					}
				}
			}
		}
		return 1;
	}

	@Override
	public boolean in_structure(Location loc) {
		int r_x = loc.getBlockX() - this.x;
		int r_y = loc.getBlockY() - this.y;
		int r_z = loc.getBlockZ() - this.z;
		if (Math.abs(r_x) <= 1 && Math.abs(r_y) <= 1 && Math.abs(r_z) <= 1) {
			return true;
		}
		return false;
	}

}
