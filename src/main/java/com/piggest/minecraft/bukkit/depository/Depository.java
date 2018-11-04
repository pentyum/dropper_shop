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

public class Depository extends Abstract_structure implements Ownable {
	public static int[] price_level = { 5, 10, 20, 30, 40 };
	public static int[] capacity_level = { 5000, 10000, 20000, 30000, 50000 };
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
