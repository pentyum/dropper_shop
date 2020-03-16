package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Ownable;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Depository extends Multi_block_with_gui implements Ownable, HasRunner, Auto_io {
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
			if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
				if (Upgrade_component.get_process(item) == 100) {
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
			if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
				if (Upgrade_component.get_process(item) == 100) {
					capacity += Depository.capacity_level[Upgrade_component.get_level(item) - 1];
				}
			}
		}
		return capacity;
	}

	public int get_price() {
		int price = 5;
		for (ItemStack item : this.getInventory().getContents()) {
			if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
				price = price + Depository.price_level[Upgrade_component.get_level(item) - 1];
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

	public synchronized boolean add(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Damageable) {
			if (((Damageable) meta).hasDamage()) {
				return false;
			}
		}
		Integer current_num = this.contents.get(Material_ext.get_full_name(item));
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
			this.contents.put(Material_ext.get_full_name(item), current_num + add_num);
			item.setAmount(item.getAmount() - add_num);
			return true;
		} else {
			return false;
		}
	}

	public synchronized ItemStack remove(String type) {
		return this.remove(type, 1);
	}

	public synchronized ItemStack remove(String name, int num) {
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
			ItemStack item = Material_ext.new_item_full_name(name, num);
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
	protected void set_from_save(Map<String, Object> shop_save) {
		super.set_from_save(shop_save);
		this.owner = (String) shop_save.get("owner");
		this.contents = (HashMap<String, Integer>) shop_save.get("contents");
		ArrayList<ArrayList<Integer>> item_list = (ArrayList<ArrayList<Integer>>) shop_save.get("levels");
		for (ArrayList<Integer> item_info : item_list) {
			ItemStack item = Upgrade_component.component_item[item_info.get(0) - 1];
			Upgrade_component.set_process(item, item_info.get(1));
			this.getInventory().addItem(item);
		}
	}

	@Override
	protected HashMap<String, Object> get_save() {
		ArrayList<int[]> levels = new ArrayList<int[]>();
		HashMap<String, Object> save = super.get_save();
		save.put("contents", this.contents);
		for (ItemStack item : this.getInventory().getContents()) {
			if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
				int[] item_info = { Upgrade_component.get_level(item), Upgrade_component.get_process(item) };
				levels.add(item_info);
			}
		}
		save.put("levels", levels);
		return save;
	}

	public Set<Entry<String, Integer>> get_contents_entry() {
		return this.contents.entrySet();
	}

	public synchronized int get_material_num(String material_name) {
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

	@Override
	protected boolean on_break(Player player) {
		if (player.getName().equalsIgnoreCase(this.owner)) {
			player.sendMessage("使用/depository remove移除存储器后才能破坏存储器方块(所有物品都会消失!)");
		} else {
			player.sendMessage("你不能破坏别人的存储器");
		}
		return false;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		return true;
	}

	@Override
	protected void init_after_set_location() {
		return;
	}

	@Override
	protected void on_right_click(Player player) {
		if (this.get_owner_name().equalsIgnoreCase(player.getName())) {
			super.on_right_click(player);
		}
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		return true;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}
	
	@Nonnull
    public static Depository deserialize(@Nonnull Map<String, Object> args) {
		Depository structure = new Depository();
		structure.set_from_save(args);
		return structure;
    }
}
