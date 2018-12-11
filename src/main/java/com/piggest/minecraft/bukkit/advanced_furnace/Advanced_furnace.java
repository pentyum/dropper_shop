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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Advanced_furnace extends Multi_block_structure implements InventoryHolder, HasRunner {
	private Reaction_container reaction_container = new Reaction_container();
	private double power = 0;
	private Inventory gui = Bukkit.createInventory(this, 27, "高级熔炉");
	private Advanced_furnace_temp_runner temp_runner = new Advanced_furnace_temp_runner(this);
	private Advanced_furnace_reaction_runner reaction_runner = new Advanced_furnace_reaction_runner(this);
	private Advanced_furnace_io_runner io_runner = new Advanced_furnace_io_runner(this);
	private Fuel fuel;
	public int fuel_ticks = 0;

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
		ItemStack raw_solid_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack raw_gas_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack fuel_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack product_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack temp_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack info_workbench = new ItemStack(Material.CRAFTING_TABLE);
		ItemStack auto_product = new ItemStack(Material.HOPPER_MINECART);
		ItemStack to_product = new ItemStack(Material.CHEST_MINECART);
		ItemStack clean_solid = new ItemStack(Material.MINECART);
		ItemStack clean_gas = new ItemStack(Material.GLASS_BOTTLE);
		ItemStack auto_gas_discharge = new ItemStack(Material.DISPENSER);
		ItemStack make_money = new ItemStack(Material.CHEST);

		Grinder.set_item_name(raw_solid_sign, "§r左边放固体原料");
		Grinder.set_item_name(raw_gas_sign, "§r左边放气体原料");
		Grinder.set_item_name(fuel_sign, "§r右边放燃料");
		Grinder.set_item_name(product_sign, "§r左边为产品");
		Grinder.set_item_name(temp_sign, "§r右边为温度");
		Grinder.set_item_name(info_workbench, "§e内部信息");
		Grinder.set_item_name(auto_product, "§e固体产品自动提取");
		Grinder.set_item_name(to_product, "§r立刻取出固体");
		Grinder.set_item_name(clean_solid, "§r清除全部固体");
		Grinder.set_item_name(auto_gas_discharge, "§e气体自动排放");
		Grinder.set_item_name(clean_gas, "§r清除全部气体");
		Grinder.set_item_name(make_money, "§e金币制造");

		this.gui.setItem(10, raw_solid_sign);
		this.gui.setItem(12, raw_gas_sign);
		this.gui.setItem(14, raw_solid_sign.clone());
		this.gui.setItem(16, fuel_sign);
		this.gui.setItem(19, product_sign);
		this.gui.setItem(21, product_sign.clone());
		this.gui.setItem(23, product_sign.clone());
		this.gui.setItem(25, temp_sign);
		this.gui.setItem(0, info_workbench);
		this.gui.setItem(2, auto_product);
		this.gui.setItem(3, to_product);
		this.gui.setItem(4, clean_solid);
		this.gui.setItem(5, auto_gas_discharge);
		this.gui.setItem(6, clean_gas);
		this.gui.setItem(8, make_money);

		ItemStack temp_info = new ItemStack(Material.FURNACE);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		temp_info_meta.setDisplayName("§e信息");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r温度: 0 K");
		lore.add("§r燃料: null");
		lore.add("§r燃料功率: " + 0 + " K/tick");
		lore.add("§r剩余燃烧时间: " + 0 + " s");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.gui.setItem(26, temp_info);
		this.set_auto_product(true);
		this.set_gas_discharge(false);
		// this.set_fuel(Fuel.coal);
		// this.reaction_container.get_all_chemical().put(Solid.iron_powder, 10000);
	}

	public double get_base_temperature() {
		return Advanced_furnace.get_block_temperature(this.get_location().add(0, 1, 0).getBlock());
	}

	public void set_temperature(double temperature) {
		this.reaction_container.set_temperature(temperature);
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		lore.set(0, "§r温度: " + String.format("%.1f", temperature) + " K");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
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
		this.set_auto_product((boolean) shop_save.get("auto-product"));
		this.set_gas_discharge((boolean) shop_save.get("auto-gas-discharge"));
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
		save.put("auto-gas-discharge", this.get_gas_discharge());
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

	public Inventory getInventory() {
		return this.gui;
	}

	public Furnace get_center_furnace() {
		return (Furnace) this.get_location().getBlock().getState();
	}

	public void set_last_sec(int last_sec) {
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		lore.set(3, "§r剩余燃烧时间: " + last_sec + " s");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
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

	public BukkitRunnable[] get_runner() {
		return new BukkitRunnable[] { this.temp_runner, this.reaction_runner, this.io_runner };
	}

	public int[] get_runner_cycle() {
		return new int[] { this.temp_runner.get_cycle(), this.reaction_runner.get_cycle(), 2 };
	}

	public int[] get_runner_delay() {
		return new int[] { 10, 10, 10 };
	}

	public int get_fuel_slot() {
		return 17;
	}

	public int get_solid_product_slot() {
		return 18;
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

	public int get_solid_reactant_slot() {
		return 9;
	}

	private void set_switch(int i, boolean value) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(value ? "§r开启" : "§r关闭");
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

	public void set_gas_discharge(boolean auto_product) {
		set_switch(5, auto_product);
	}

	public boolean get_gas_discharge() {
		return this.get_switch(5);
	}

	public void set_make_money(boolean auto_product) {
		set_switch(8, auto_product);
	}

	public boolean get_make_money() {
		return this.get_switch(8);
	}

	private void unpress_button(int i) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(null);
		item.setItemMeta(meta);
	}

	private boolean pressed_button(int i) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		return meta.hasLore();
	}

	public void unpress_to_product() {
		unpress_button(3);
	}

	public boolean pressed_to_product() {
		return pressed_button(3);
	}

	public void unpress_clean_solid() {
		unpress_button(4);
	}

	public boolean pressed_clean_solid() {
		return pressed_button(4);
	}

	public void unpress_clean_gas() {
		unpress_button(6);
	}

	public boolean pressed_clean_gas() {
		return pressed_button(6);
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

	public int get_gas_reactant_slot() {
		return 11;
	}

	public int get_gas_product_slot() {
		return 20;
	}

	public void set_gas_product_slot(ItemStack item) {
		this.gui.setItem(20, item);
	}
}