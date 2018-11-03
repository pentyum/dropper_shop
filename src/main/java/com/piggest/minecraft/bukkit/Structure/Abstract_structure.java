package com.piggest.minecraft.bukkit.Structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public abstract class Abstract_structure {

	public abstract void set_from_save(Map<?, ?> shop_save);

	public abstract void set_location(Location loc);

	public abstract void set_location(String world_name, int x, int y, int z);

	public abstract Location get_location();

	public abstract HashMap<String, Object> get_save();
	
	public abstract int completed();
	
	public abstract boolean in_structure(Location loc);

}
