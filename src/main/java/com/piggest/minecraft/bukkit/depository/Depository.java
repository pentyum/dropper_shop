package com.piggest.minecraft.bukkit.depository;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.piggest.minecraft.bukkit.Structure.Abstract_structure;
import com.piggest.minecraft.bukkit.Structure.Ownable;

public class Depository extends Abstract_structure implements Ownable {

	public void set_owner(String owner) {
		// TODO Auto-generated method stub
		
	}

	public OfflinePlayer get_owner() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_owner_name() {
		// TODO Auto-generated method stub
		return null;
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
