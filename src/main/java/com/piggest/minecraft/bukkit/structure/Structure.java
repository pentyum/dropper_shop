package com.piggest.minecraft.bukkit.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Chunk_location;

public abstract class Structure {
	protected String world_name = null;
	protected int x;
	protected int y;
	protected int z;

	public void set_from_save(Map<?, ?> save) {
		this.x = (int) save.get("x");
		this.y = (int) save.get("y");
		this.z = (int) save.get("z");
		this.world_name = (String) save.get("world");
		if (this instanceof Ownable) {
			Ownable ownable = (Ownable) this;
			ownable.set_owner((String) save.get("owner"));
		}
		if (this instanceof Capacity_upgradable) {
			Capacity_upgradable upgradable = (Capacity_upgradable) this;
			upgradable.set_capacity_level((int) save.get("structure-level"));
		}
	}

	public void set_location(Location loc) {
		this.set_location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public void set_location(String world_name, int x, int y, int z) {
		this.world_name = world_name;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location get_location() {
		return new Location(Bukkit.getWorld(this.world_name), this.x, this.y, this.z);
	}

	public int get_chunk_x() {
		return this.x / 16;
	}

	public int get_chunk_z() {
		return this.y / 16;
	}

	public Chunk_location get_chunk_location() {
		return new Chunk_location(this.world_name, this.x / 16, this.z / 16);
	}

	/**
	 * 判断结构所在区块是否被加载
	 */
	public boolean is_loaded() {
		return this.get_chunk_location().is_loaded();
	}

	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("world", this.world_name);
		save.put("x", this.x);
		save.put("y", this.y);
		save.put("z", this.z);
		if (this instanceof Ownable) {
			Ownable ownable = (Ownable) this;
			save.put("owner", ownable.get_owner_name());
		}
		if (this instanceof Capacity_upgradable) {
			Capacity_upgradable upgradable = (Capacity_upgradable) this;
			save.put("structure-level", upgradable.get_capacity_level());
		}
		return save;
	}

	public Structure_manager get_manager() {
		return Dropper_shop_plugin.instance.get_structure_manager().get(this.getClass());
	}

	/**
	 * 创建前调用的函数，返回true表明创建成功。
	 */
	public abstract boolean create_condition(Player player);

	public void remove() {
		this.get_manager().remove(this);
	}

	/**
	 * 破坏方块时调用的函数，返回true表面允许破坏。
	 */
	protected abstract boolean on_break(Player player);

	@Override
	public String toString() {
		return this.getClass().getName() + "(" + this.world_name + "," + this.x + "," + this.y + "," + this.z + ")";
	}
}
