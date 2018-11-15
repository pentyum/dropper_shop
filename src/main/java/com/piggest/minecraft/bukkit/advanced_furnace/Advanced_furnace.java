package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Advanced_furnace extends Multi_block_structure implements InventoryHolder {
	private int temperature = 0;
	private Inventory gui = Bukkit.createInventory(this, InventoryType.CHEST, "高级熔炉");
	
	public void set_temperature(int T) {
		this.temperature = T;
	}

	public int get_temperature() {
		return this.temperature;
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		return save;
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

	public Inventory getInventory() {
		return this.gui;
	}

}
