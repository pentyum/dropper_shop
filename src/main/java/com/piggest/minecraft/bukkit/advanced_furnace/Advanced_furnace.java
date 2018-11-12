package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Abstract_structure;

public class Advanced_furnace extends Abstract_structure{
	private int temperature = 0;
	public void set_temperature(int T) {
		this.temperature = T;
	}
	public int get_temperature() {
		return this.temperature;
	}
	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_location(Location loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_location(String world_name, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location get_location() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> get_save() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int completed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean in_structure(Location loc) {
		// TODO Auto-generated method stub
		return false;
	}

}
