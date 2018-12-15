package com.piggest.minecraft.bukkit.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Structure {
	protected String world_name = null;
	protected int x;
	protected int y;
	protected int z;
	
	public void set_from_save(Map<?, ?> save) {
		this.x = (Integer) save.get("x");
		this.y = (Integer) save.get("y");
		this.z = (Integer) save.get("z");
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

	public abstract boolean create_condition(Player player);
}
