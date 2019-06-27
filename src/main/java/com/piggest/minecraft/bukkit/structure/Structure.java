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
	private boolean loaded = false;

	public void set_from_save(Map<?, ?> save) {
		this.x = (int) save.get("x");
		this.y = (int) save.get("y");
		this.z = (int) save.get("z");
		this.world_name = (String) save.get("world");
		if (this instanceof Ownable) {
			Ownable ownable = (Ownable) this;
			ownable.set_owner((String) save.get("owner"));
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

	public boolean is_loaded() {
		return this.loaded;
	}

	public void set_loaded(boolean loaded) {
		Dropper_shop_plugin.instance.getLogger().info(this.toString() + "激活状态:" + loaded);
		this.loaded = loaded;
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
		return save;
	}

	public Structure_manager get_manager() {
		return Dropper_shop_plugin.instance.get_structure_manager().get(this.getClass());
	}

	public abstract boolean create_condition(Player player);

	public void remove() {
		this.get_manager().remove(this);
	}

	protected abstract boolean on_break(Player player);

	@Override
	public String toString() {
		return this.getClass().getName() + "(" + this.world_name + "," + this.x + "," + this.y + "," + this.z + ")";
	}
}
