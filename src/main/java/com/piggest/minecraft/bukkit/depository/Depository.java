package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Ownable;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Depository extends Multi_block_with_gui implements Ownable, HasRunner {
	public static int[] price_level = { 5, 10, 20, 30, 40 };
	public static int[] capacity_level = { 5000, 10000, 20000, 30000, 50000 };
	private Depository_runner runner = new Depository_runner(this);
	private Depository_item_importer importer = new Depository_item_importer(this);
	private boolean accessible = true;
	private HashMap<String, Integer> contents = new HashMap<String, Integer>();
	private String owner = null;

	public String get_info() {
		String msg = "存储器结构信息";
		msg += "\n拥有者: " + this.get_owner_name();
		msg += "  消耗: " + this.get_price();
		msg += "\n物品种类: " + this.get_type() + "/" + this.get_max_type();
		msg += "  容量: " + this.get_capacity() + "/" + this.get_max_capacity();
		msg += "\n--------------------";
		for (Entry<String, Integer> entry : this.get_contents_entry()) {
			msg += "\n" + entry.getKey() + ": " + entry.getValue();
		}
		return msg;
	}

	public void set_accessible(boolean accessible) {
		this.accessible = accessible;
	}

	public boolean is_accessible() {
		return this.accessible;
	}

	public int get_max_type() {
		int type = 2;
		for (ItemStack item : this.getInventory().getContents()) {
			if (Update_component.is_component(item)) {
				if (Update_component.get_process(item) == 100) {
					type++;
				}
			}
		}
		return type;
	}

	public int get_type() {
		return this.contents.size();
	}

	public int get_max_capacity() {
		int capacity = 5000;
		for (ItemStack item : this.getInventory().getContents()) {
			if (Update_component.is_component(item)) {
				if (Update_component.get_process(item) == 100) {
					capacity += Depository.capacity_level[Update_component.get_level(item)];
				}
			}
		}
		return capacity;
	}

	public int get_price() {
		int price = 5;
		for (ItemStack item : this.getInventory().getContents()) {
			if (Update_component.is_component(item)) {
				price = price + Depository.price_level[Update_component.get_level(item)];
			}
		}
		return price;
	}

	public int get_capacity() {
		int capacity = 0;
		for (Entry<String, Integer> entry : this.contents.entrySet()) {
			capacity = capacity + entry.getValue();
		}
		return capacity;
	}

	public boolean add(ItemStack item) {
		Integer current_num = this.contents.get(Material_ext.get_id_name(item));
		if (current_num == null) { // 存储器没有这种物品
			if (this.get_max_type() == this.get_type()) { // 超出种类限制
				return false;
			} else { // 没有超出种类限制
				current_num = 0;
			}
		}
		int add_num = item.getAmount();
		if (this.get_capacity() + add_num > this.get_max_capacity()) {
			add_num = this.get_max_capacity() - this.get_capacity();
		}
		if (current_num + add_num > 0) { // 大于0才添加内容
			this.contents.put(Material_ext.get_id_name(item), current_num + add_num);
			item.setAmount(item.getAmount() - add_num);
			return true;
		} else {
			return false;
		}
	}

	public ItemStack remove(String type) {
		return this.remove(type, 1);
	}

	public ItemStack remove(String name, int num) {
		Integer current_num = this.contents.get(name);
		if (current_num == null) {
			return null;
		}
		if (current_num - num <= 0) {
			num = current_num;
			this.contents.remove(name);
		} else {
			this.contents.put(name, current_num - num);
		}
		if (num > 0) {
			ItemStack item = Material_ext.new_item(name, num);
			return item;
		} else {
			return null;
		}
	}

	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.runner, this.importer };
	}

	public Depository_item_importer get_importer() {
		return this.importer;
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

	@SuppressWarnings("unchecked")
	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.owner = (String) shop_save.get("owner");
		this.contents = (HashMap<String, Integer>) shop_save.get("contents");
		ArrayList<ArrayList<Integer>> item_list = (ArrayList<ArrayList<Integer>>) shop_save.get("levels");
		for (ArrayList<Integer> item_info : item_list) {
			ItemStack item = Update_component.component_item[item_info.get(0)];
			Update_component.set_process(item, item_info.get(1));
			this.getInventory().addItem(item);
		}
	}

	@Override
	public HashMap<String, Object> get_save() {
		ArrayList<int[]> levels = new ArrayList<int[]>();
		HashMap<String, Object> save = super.get_save();
		save.put("contents", this.contents);
		for (ItemStack item : this.getInventory().getContents()) {
			if (Update_component.is_component(item)) {
				int[] item_info = { Update_component.get_level(item), Update_component.get_process(item) };
				levels.add(item_info);
			}
		}
		save.put("levels", levels);
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
						Bukkit.getLogger().info("末地烛不对");
						return 0;
					}
					if (Math.abs(x) == 1 && Math.abs(y) == 1 && Math.abs(z) == 1 && material != Material.LAPIS_BLOCK) {
						Bukkit.getLogger().info("青金石不对");
						return 0;
					}
					if (Math.abs(x) + Math.abs(z) + Math.abs(y) == 2 && material != Material.IRON_BLOCK) {
						Bukkit.getLogger().info("铁块不对");
						return 0;
					}
					if (Math.abs(x) + Math.abs(z) == 1 & Math.abs(y) == 0 && material != Material.IRON_BARS) {
						Bukkit.getLogger().info("铁栏杆不对");
						return 0;
					}
					if (Math.abs(x) == 0 && Math.abs(z) == 0 & Math.abs(y) == 1 && material != Material.DIAMOND_BLOCK) {
						Bukkit.getLogger().info("钻石块不对");
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

	public Set<Entry<String, Integer>> get_contents_entry() {
		return this.contents.entrySet();
	}

	public int get_material_num(String material_name) {
		Integer num = this.contents.get(material_name);
		if (num == null) {
			return 0;
		} else {
			return num;
		}
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		// TODO Auto-generated method stub

	}
}
