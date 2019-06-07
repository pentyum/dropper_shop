package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Advanced_furnace extends Multi_block_with_gui implements HasRunner {
	private Reaction_container reaction_container = new Reaction_container();
	private double power = 0;
	private Advanced_furnace_temp_runner temp_runner = new Advanced_furnace_temp_runner(this);
	private Advanced_furnace_reaction_runner reaction_runner = new Advanced_furnace_reaction_runner(this);
	private Advanced_furnace_io_runner io_runner = new Advanced_furnace_io_runner(this);
	private Fuel fuel;
	public int fuel_ticks = 0;
	private int money = 0;
	private int money_limit = 10000;

	public static double get_block_temperature(Block block) {
		double base_temp = block.getTemperature() * 20 + 270;
		double height_temp = (64 - block.getY()) / 30.0;
		double env_temp = 0;
		double light_temp = 0;
		if (block.getWorld().getEnvironment() == Environment.NETHER) {
			env_temp = 50;
		} else if (block.getWorld().getEnvironment() == Environment.THE_END) {
			env_temp = -10;
		}
		light_temp = -8 + block.getLightLevel();
		// Dropper_shop_plugin.instance.getLogger().info("温度=" + base_temp + "+" +
		// height_temp + "+" + env_temp + "+" + light_temp);
		return base_temp + height_temp + env_temp + light_temp;
	}

	public Advanced_furnace() {
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r温度: 0 K");
		lore.add("§r燃料: null");
		lore.add("§r燃料功率: " + 0 + " K/tick");
		lore.add("§r剩余燃烧时间: " + 0 + " s");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.gui.setItem(26, temp_info);
		this.set_auto_product(true);
		this.set_open(false);
		this.set_make_money(false);
		this.set_money(0);
	}

	public double get_base_temperature() {
		return Advanced_furnace.get_block_temperature(this.get_location().add(0, 1, 0).getBlock());
	}

	public void set_temperature(double temperature) {
		this.reaction_container.set_temperature(temperature);
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		if (temp_info_meta.hasLore()) {
			List<String> lore = temp_info_meta.getLore();
			lore.set(0, "§r温度: " + String.format("%.1f", temperature) + " K");
			temp_info_meta.setLore(lore);
			temp_info.setItemMeta(temp_info_meta);
		}
	}

	public double get_temperature() {
		return this.reaction_container.get_temperature();
	}

	public ItemStack get_gui_item(int i) {
		return this.gui.getItem(i);
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.set_temperature((Double) shop_save.get("temperature"));
		this.fuel_ticks = ((Integer) shop_save.get("fuel-ticks"));
		String fuel_type = (String) shop_save.get("fuel-type");
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> contents = (HashMap<String, Integer>) shop_save.get("contents");
		if (fuel_type.equals("null")) {
			this.set_fuel(null);
		} else {
			this.set_fuel(Fuel.valueOf(fuel_type));
		}
		if (shop_save.get("fuel-slot") != null) {
			ItemStack fuel_slot_item = Material_ext.new_item((String) shop_save.get("fuel-slot"),
					(Integer) shop_save.get("fuel-slot-num"));
			this.set_fuel_slot(fuel_slot_item);
		}
		if (shop_save.get("solid-product-slot") != null) {
			ItemStack solid_product_slot_item = Material_ext.new_item((String) shop_save.get("solid-product-slot"),
					(Integer) shop_save.get("solid-product-slot-num"));
			this.gui.setItem(this.get_solid_product_slot(), solid_product_slot_item);
		}
		if (shop_save.get("solid-reactant-slot") != null) {
			ItemStack solid_reactant_slot_item = Material_ext.new_item((String) shop_save.get("solid-reactant-slot"),
					(Integer) shop_save.get("solid-reactant-slot-num"));
			this.set_solid_reactant_slot(solid_reactant_slot_item);
		}
		for (Entry<String, Integer> entry : contents.entrySet()) {
			String name = entry.getKey();
			Integer unit = entry.getValue();
			this.reaction_container.set_unit(Chemical.get_chemical(name), unit);
		}
		this.set_open((boolean) shop_save.get("open"));
		this.set_auto_product((boolean) shop_save.get("auto-product"));
		this.set_make_money((boolean) shop_save.get("make-money"));
		this.set_money((int) shop_save.get("money"));
	}

	public void set_solid_reactant_slot(ItemStack slot_item) {
		this.gui.setItem(9, slot_item);
	}

	public void set_fuel_slot(ItemStack slot_item) {
		this.gui.setItem(17, slot_item);
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		HashMap<String, Integer> contents = new HashMap<String, Integer>();
		save.put("temperature", this.get_temperature());
		save.put("fuel-ticks", this.fuel_ticks);
		if (this.fuel != null) {
			save.put("fuel-type", this.fuel.name());
		} else {
			save.put("fuel-type", "null");
		}
		ItemStack fuel_slot = this.gui.getItem(get_fuel_slot());
		ItemStack solid_product_slot = this.gui.getItem(this.get_solid_product_slot());
		ItemStack solid_reactant_slot = this.gui.getItem(this.get_solid_reactant_slot());
		if (!Grinder.is_empty(fuel_slot)) {
			save.put("fuel-slot", Material_ext.get_id_name(fuel_slot));
			save.put("fuel-slot-num", fuel_slot.getAmount());
		}
		if (!Grinder.is_empty(solid_product_slot)) {
			save.put("solid-product-slot", Material_ext.get_id_name(solid_product_slot));
			save.put("solid-product-slot-num", solid_product_slot.getAmount());
		}
		if (!Grinder.is_empty(solid_reactant_slot)) {
			save.put("solid-reactant-slot", Material_ext.get_id_name(solid_reactant_slot));
			save.put("solid-reactant-slot-num", solid_reactant_slot.getAmount());
		}
		for (Entry<Chemical, Integer> entry : this.get_reaction_container().get_all_chemical().entrySet()) {
			Chemical chemical = entry.getKey();
			Integer unit = entry.getValue();
			contents.put(chemical.get_name(), unit);
		}
		save.put("contents", contents);
		save.put("auto-product", this.get_auto_product());
		save.put("open", this.is_open());
		save.put("make-money", this.get_make_money());
		save.put("money", this.money);
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
					if (x == 0 && y == 0 && z == 0 && material != Material.FURNACE) {
						Bukkit.getLogger().info("熔炉不对");
						return 0;
					}
					if (Math.abs(x) == 1 && Math.abs(y) == 1 && Math.abs(z) == 1 && material != Material.GOLD_BLOCK) {
						Bukkit.getLogger().info("金块不对");
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

	public Furnace get_center_furnace() {
		return (Furnace) this.get_location().getBlock().getState();
	}

	public void set_last_sec(int last_sec) {
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		if (temp_info_meta.hasLore()) {
			List<String> lore = temp_info_meta.getLore();
			lore.set(3, "§r剩余燃烧时间: " + last_sec + " s");
			temp_info_meta.setLore(lore);
			temp_info.setItemMeta(temp_info_meta);
		}
	}

	public void set_power(double power) {
		this.power = power;
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		lore.set(2, "§r燃料功率: " + String.format("%.2f", power) + " K/tick");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
	}

	public double get_power() { // 产热功率 K/tick
		return this.power;
	}

	public Fuel get_fuel() {
		return this.fuel;
	}

	public void set_fuel(Fuel fuel) {
		this.fuel = fuel;
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		double power = 0;
		if (fuel != null) {
			lore.set(1, "§r燃料: " + fuel.name());
			power = fuel.get_power();
		} else {
			lore.set(1, "§r燃料: null");
		}
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.set_power(power);
	}

	public double get_k() { // 热传导率 K/(deltaK tick)
		return 0.0015;
	}

	public double get_power_loss() {
		return this.get_k() * (this.reaction_container.get_temperature() - this.get_base_temperature());
	}

	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.temp_runner, this.reaction_runner, this.io_runner };
	}

	public Reaction_container get_reaction_container() {
		return this.reaction_container;
	}

	public void set_reactor_info(List<String> lore) {
		ItemStack item = this.gui.getItem(0);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void solid_to_product(Solid solid, Iterator<Entry<Chemical, Integer>> iterator) {
		ItemStack item = solid.get_item_stack();
		int item_unit = solid.get_unit();
		int move_num = this.reaction_container.get_unit(solid) / item_unit;
		int max_stack_num = item.getMaxStackSize();
		if (Grinder.is_empty(this.gui.getItem(this.get_solid_product_slot()))) {
			if (move_num > max_stack_num) {
				move_num = max_stack_num;
			}
			item.setAmount(move_num);
			this.gui.setItem(this.get_solid_product_slot(), item);
		} else {
			ItemStack current_item = this.gui.getItem(get_solid_product_slot());
			if (current_item.isSimilar(item)) {
				int current_num = current_item.getAmount();
				if (current_num + move_num > max_stack_num) {
					move_num = max_stack_num - current_num;
				}
				current_item.setAmount(current_num + move_num);
			} else {
				move_num = 0;
			}
		}
		int last = this.reaction_container.get_unit(solid) - move_num * item_unit;
		if (last != 0) {
			this.reaction_container.set_unit(solid, last);
		} else {
			iterator.remove();
		}
	}

	private void set_switch(int i, boolean value) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if (meta.hasLore() == false) {
			lore = new ArrayList<String>();
			lore.add(value ? "§r开启" : "§r关闭");
		} else {
			lore = meta.getLore();
			lore.set(0, value ? "§r开启" : "§r关闭");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	private boolean get_switch(int i) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String info = lore.get(0);
		if (info.equals("§r开启")) {
			return true;
		} else {
			return false;
		}
	}

	public void set_auto_product(boolean auto_product) {
		set_switch(2, auto_product);
	}

	public boolean get_auto_product() {
		return this.get_switch(2);
	}

	public void set_make_money(boolean auto_product) {
		set_switch(8, auto_product);
	}

	public boolean get_make_money() {
		return this.get_switch(8);
	}

	public boolean is_open() {
		return this.get_switch(5);
	}

	public void set_open(boolean open) {
		set_switch(5, open);
	}

	private boolean add_a_item_to_slot(ItemStack src_item, int i) {
		if (!Grinder.is_empty(src_item)) {
			if (Grinder.is_empty(this.gui.getItem(i))) {
				this.gui.setItem(i, src_item.clone());
				this.gui.getItem(i).setAmount(1);
				src_item.setAmount(src_item.getAmount() - 1);
				return true;
			} else if (src_item.getType() == this.gui.getItem(i).getType()) {
				int new_num = 1 + this.gui.getItem(i).getAmount();
				if (new_num <= src_item.getMaxStackSize()) {
					this.gui.getItem(i).setAmount(new_num);
					src_item.setAmount(src_item.getAmount() - 1);
					return true;
				}
			}
		}
		return false;
	}

	public boolean add_a_solid(ItemStack src_item) {
		return this.add_a_item_to_slot(src_item, this.get_solid_reactant_slot());
	}

	public boolean add_a_fuel(ItemStack src_item) {
		return this.add_a_item_to_slot(src_item, this.get_fuel_slot());
	}

	public int get_solid_reactant_slot() {
		return 9;
	}

	public int get_gas_reactant_slot() {
		return 11;
	}

	public int get_fuel_slot() {
		return 17;
	}

	public int get_solid_product_slot() {
		return 18;
	}

	public int get_gas_product_slot() {
		return 20;
	}

	public void set_gas_product_slot(ItemStack item) {
		this.gui.setItem(20, item);
	}

	public int get_make_money_rate() { // 生产金币的速率(1分钟)
		int rate = 0;
		if (this.get_temperature() > 1200) {
			double d_temp = this.get_temperature() - this.get_base_temperature();
			rate = (int) (d_temp / 120);
		}
		return rate;
	}

	public int add_money(int money) {
		if (this.money + money > this.money_limit) {
			money = this.money_limit - this.money;
		}
		this.set_money(this.money + money);
		return this.money;
	}

	public int get_money() {
		return this.money;
	}

	public void set_money(int money) {
		this.money = money;
		ItemStack item = this.gui.getItem(8);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String string = "§r储存: " + money + "/" + this.money_limit;
		try {
			lore.set(1, string);
		} catch (IndexOutOfBoundsException e) {
			lore.add(string);
		}
		String string2 = "§r产钱率: " + this.get_make_money_rate() * 2 + "/min";
		try {
			lore.set(2, string2);
		} catch (IndexOutOfBoundsException e) {
			lore.add(string2);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public int get_money_limit() {
		return this.money_limit;
	}

	@Override
	public boolean create_condition(Player player) {
		if (!player.hasPermission("adv_furnace.make")) {
			player.sendMessage("你没有建立高级熔炉的权限");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		HashMap<Chemical, Integer> all_chemical = this.reaction_container.get_all_chemical();
		if (slot == 3) { // 取出固体
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					Solid solid = (Solid) chemical;
					this.solid_to_product(solid, iterator);
				}

			}
		} else if (slot == 4) { // 清除固体
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					iterator.remove();
				}
			}
		} else if (slot == 6) { // 清除气体
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Gas) {
					iterator.remove();
				}
			}
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}
}