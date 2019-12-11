package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.Capacity_upgradable;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Ownable;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Advanced_furnace extends Multi_block_with_gui implements HasRunner, Capacity_upgradable, Auto_io, Ownable {
	public static final int make_money_switch = 8;
	public static final int solid_reactant_slot = 9;
	public static final int liquid_reactant_slot = 13;
	public static final int gas_reactant_slot = 11;
	public static final int fuel_slot = 17;
	public static final int fuel_product_slot = 35;
	public static final int solid_product_slot = 18;
	public static final int liquid_product_slot = 22;
	public static final int gas_product_slot = 20;
	public static final int upgrade_component_slot = 33;

	private static final int[][] solid_reactant_hopper_check_list = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 1, -2 },
			{ -2, 1, 0 } }; // 注入固体
	private static final int[][] fuel_hopper_check_list = { { 0, -1, 2 }, { 2, -1, 0 }, { 0, -1, -2 }, { -2, -1, 0 } }; // 注入固体
	private static final int[][] solid_product_check_list = { { 1, -1, 2 }, { 2, -1, 1 }, { -1, -1, 2 }, { 2, -1, -1 },
			{ 1, -1, -2 }, { -2, -1, 1 }, { -2, -1, -1 }, { -1, -1, -2 } };

	private Reaction_container reaction_container = new Reaction_container();
	private double power = 0;
	private Advanced_furnace_temp_runner temp_runner = new Advanced_furnace_temp_runner(this);
	private Advanced_furnace_reaction_runner reaction_runner = new Advanced_furnace_reaction_runner(this);
	private Advanced_furnace_io_runner io_runner = new Advanced_furnace_io_runner(this);
	private Advanced_furnace_upgrade_runner upgrade_runner = new Advanced_furnace_upgrade_runner(this);
	private boolean is_litting = false;

	private int heat_keeping_value = 0;
	public Fuel_info fuel_info = new Fuel_info();
	// private Fuel fuel = null;
	// public int fuel_ticks = 0;
	// public int fuel_amount = 0;
	private int money = 0;
	private int money_limit = 9000;
	private int structure_level = 1;
	private boolean heat_keeping_upgrade = false;
	private int overload_upgrade = 0;
	private int time_upgrade = 0;
	private double e = 1;
	private boolean is_locked_temp = false;
	private double locked_temp = 290;
	private String owner;

	public static double get_block_temperature(Block block) {
		double base_temp = block.getTemperature() * 20 + 270;
		double height_temp = (64 - block.getY()) / 30.0;
		double env_temp = 0;
		double light_temp = 0;
		if (block.getWorld().getEnvironment() == Environment.NETHER) {
			env_temp = 50;
		} else if (block.getWorld().getEnvironment() == Environment.THE_END) {
			env_temp = -20;
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
		lore.add("§r燃料: 无");
		lore.add("§r燃料类型: 气态");
		lore.add("§r燃料功率: " + 0 + " K/tick");
		lore.add("§r剩余燃烧时间: " + 0 + " s");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.gui.setItem(26, temp_info);
		this.set_auto_product(true);
		this.set_open(false);
		this.set_make_money(false);
		this.set_capacity_level(1);
		this.set_heat_keeping_upgrade(false);
		this.set_overload_upgrade(0);
		this.set_time_upgrade(0);
	}

	public double get_base_temperature() {
		return Advanced_furnace.get_block_temperature(this.get_location().add(0, 1, 0).getBlock());
	}

	public void set_temperature(double temperature) {
		if (this.is_locked_temp == true) {
			temperature = this.locked_temp;
		}
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

	public void set_locked_temperature(double temp) {
		if (temp < 0) {
			this.is_locked_temp = false;
		} else {
			this.is_locked_temp = true;
			this.locked_temp = temp;
			this.set_temperature(temp);
		}
	}

	public double get_temperature() {
		return this.reaction_container.get_temperature();
	}

	public ItemStack get_gui_item(int i) {
		return this.gui.getItem(i);
	}

	@Override
	protected void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.set_temperature((double) shop_save.get("temperature"));
		this.fuel_info.fuel_ticks = ((int) shop_save.get("fuel-ticks"));
		String fuel_type = (String) shop_save.get("fuel-type");
		int fuel_amount = 0;
		if (shop_save.get("fuel-amount") != null) {
			fuel_amount = (int) shop_save.get("fuel-amount");
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> contents = (HashMap<String, Integer>) shop_save.get("contents");
		if (fuel_type.equals("null")) {
			this.set_fuel(null, 0);
		} else {
			this.set_fuel(Fuel.valueOf(fuel_type), fuel_amount);
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
		this.set_heat_keeping_upgrade((boolean) shop_save.get("heat-keeping-upgrade"));
		this.set_overload_upgrade((int) shop_save.get("overload-upgrade"));
		this.set_time_upgrade((int) shop_save.get("time-upgrade"));
	}

	public void set_solid_product_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.solid_product_slot, slot_item);
	}

	public void set_solid_reactant_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.solid_reactant_slot, slot_item);
	}

	public void set_liquid_product_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.liquid_product_slot, slot_item);
	}

	public void set_liquid_reactant_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.liquid_reactant_slot, slot_item);
	}

	public void set_gas_product_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.gas_product_slot, slot_item);
	}

	public void set_gas_reactant_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.gas_reactant_slot, slot_item);
	}

	public void set_fuel_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.fuel_slot, slot_item);
	}

	public void set_fuel_product_slot(ItemStack item) {
		this.gui.setItem(Advanced_furnace.fuel_product_slot, item);
	}

	@Override
	protected HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		HashMap<String, Integer> contents = new HashMap<String, Integer>();
		save.put("temperature", this.get_temperature());
		save.put("fuel-ticks", this.fuel_info.fuel_ticks);
		save.put("fuel-amount", this.fuel_info.fuel_amount);
		if (this.fuel_info.fuel != null) {
			save.put("fuel-type", this.fuel_info.fuel.name());
		} else {
			save.put("fuel-type", "null");
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
		save.put("heat-keeping-upgrade", this.heat_keeping_upgrade);
		save.put("overload-upgrade", this.overload_upgrade);
		save.put("time-upgrade", this.time_upgrade);
		return save;
	}

	public Furnace get_center_furnace() {
		return (Furnace) this.get_location().getBlock().getBlockData();
	}

	public void set_last_sec(int last_sec) {
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		if (temp_info_meta.hasLore()) {
			List<String> lore = temp_info_meta.getLore();
			lore.set(4, "§r剩余燃烧时间: " + last_sec + " s");
			temp_info_meta.setLore(lore);
			temp_info.setItemMeta(temp_info_meta);
		}
	}

	public void set_power(double power) {
		this.power = power;
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		if (temp_info_meta.hasLore()) {
			List<String> lore = temp_info_meta.getLore();
			lore.set(3, "§r燃料功率: " + String.format("%.2f", this.get_power()) + " K/tick");
			temp_info_meta.setLore(lore);
			temp_info.setItemMeta(temp_info_meta);
		}
	}

	public double get_power() { // 产热功率 K/tick
		return this.power * this.get_power_modify();
	}

	private double get_power_modify() {
		return (1 + 0.01 * this.get_manager().get_power_add_per_overload_upgrade() * this.overload_upgrade)
				* (1 - 0.01 * this.get_manager().get_power_loss_per_time_upgrade() * this.time_upgrade);
	}

	double get_time_modify() {
		return (1 + 0.01 * this.get_manager().get_time_add_per_time_upgrade() * this.time_upgrade)
				* (1 - 0.01 * this.get_manager().get_time_loss_per_overload_upgrade() * this.overload_upgrade);
	}

	public Fuel get_fuel() {
		return this.fuel_info.fuel;
	}

	public synchronized void set_fuel(Fuel fuel, int amount) {
		this.fuel_info.fuel = fuel;
		this.fuel_info.fuel_amount = amount;
		ItemStack temp_info = this.gui.getItem(26);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		double power = 0;
		String other_info = "";
		if (amount > 0) {
			other_info = " (" + amount + "单位)";
		}
		if (fuel != null) {
			lore.set(1, "§r燃料: " + fuel.name() + other_info);
			lore.set(2, "§r燃料类型: " + fuel.status.display_name);
			power = fuel.power;
		} else {
			lore.set(1, "§r燃料: 无");
			lore.set(2, "§r燃料类型: 气态");
		}
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.set_power(power);
	}

	public double get_k() { // 热传导率 K/(deltaK tick)
		return 0.0015 * (1 - 0.01 * this.get_heat_keeping_value());
	}

	public double get_e() { // 辐射系数
		return this.e;
	}

	public double get_power_loss() {
		double temp = this.get_temperature();
		double base_temp = this.get_base_temperature();
		return this.get_e() * 1e-14 * (Math.pow(temp, 4) - Math.pow(base_temp, 4)) + this.get_k() * (temp - base_temp);
	}

	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.temp_runner, this.reaction_runner, this.io_runner, this.upgrade_runner };
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
		if (Grinder.is_empty(this.gui.getItem(Advanced_furnace.solid_product_slot))) {
			if (move_num > max_stack_num) {
				move_num = max_stack_num;
			}
			item.setAmount(move_num);
			this.gui.setItem(Advanced_furnace.solid_product_slot, item);
		} else {
			ItemStack current_item = this.gui.getItem(Advanced_furnace.solid_product_slot);
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

	public boolean add_a_solid(ItemStack src_item) {
		return Inventory_io.move_item_to_slot(src_item, 1, this.gui, Advanced_furnace.solid_reactant_slot);
	}

	public boolean add_a_fuel(ItemStack src_item) {
		return Inventory_io.move_item_to_slot(src_item, 1, this.gui, Advanced_furnace.fuel_slot);
	}

	public int get_make_money_rate() { // 生产金币的速率(30秒)
		int rate = 0;
		double current_temp = this.get_temperature();
		double base_temp = this.get_base_temperature();
		if (this.get_temperature() > 1200) {
			double d_temp = current_temp - base_temp;
			double eta = 1 - base_temp / current_temp;
			rate = (int) (d_temp / 100 * eta);
		}
		return rate;
	}

	public int add_money(int money) {
		int money_limit = this.get_money_limit();
		if (this.money + money > money_limit) {
			money = money_limit - this.money;
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
		String string = "§r储存: " + money + "/" + this.get_money_limit();
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
		return this.money_limit + 1000 * this.structure_level;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_adv_furnace_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			player.sendMessage("[高级熔炉]已扣除" + price);
			return true;
		} else {
			player.sendMessage("[高级熔炉]建立高级熔炉所需的钱不够，需要" + price);
			return false;
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
		} else if (slot == 27) {
			this.capacity_upgrade_by(player);
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		switch (slot) {
		case Advanced_furnace.make_money_switch:
			if (player.hasPermission("adv_furnace.make_money")) {
				return true;
			} else {
				player.sendMessage("[高级熔炉]你没有启动火力发钱的权限");
				return false;
			}
		default:
			return true;
		}
	}

	public int get_heat_keeping_value() { // 热传导率降低率，单位百分比
		return this.heat_keeping_value;
	}

	public int get_capacity_level() {
		return this.structure_level;
	}

	public void set_capacity_level(int structure_level) {
		this.structure_level = structure_level;
		ItemStack upgrade_button = this.gui.getItem(27);
		ItemMeta meta = upgrade_button.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r当前等级: " + structure_level + " / "
				+ Dropper_shop_plugin.instance.get_exp_saver_max_structure_level());
		lore.add("§7升级所需金币: " + Exp_saver.get_upgrade_price(structure_level));
		lore.add("§7点击即可升级");
		meta.setLore(lore);
		upgrade_button.setItemMeta(meta);
	}

	@Override
	public boolean capacity_upgrade_by(Player player) {
		int current_level = this.get_capacity_level();
		if (current_level >= Dropper_shop_plugin.instance.get_exp_saver_max_structure_level()) {
			player.sendMessage("[高级熔炉]已经升级至满级");
			return false;
		}
		int need_price = Exp_saver.get_upgrade_price(current_level);
		if (Dropper_shop_plugin.instance.cost_player_money(need_price, player)) {
			this.set_capacity_level(current_level + 1);
			player.sendMessage("[高级熔炉]消耗了" + need_price + "金币把高级熔炉升级至" + (current_level + 1) + "级");
			return true;
		} else {
			player.sendMessage(
					"[高级熔炉]你的钱不够，高级熔炉由" + current_level + "升级至" + (current_level + 1) + "级需要" + need_price + "金币");
			return false;
		}
	}

	public void set_heat_keeping_upgrade(boolean upgrade) {
		this.heat_keeping_upgrade = upgrade;
		ItemStack icon = this.gui.getItem(28);
		ItemMeta meta = icon.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r状态: " + (upgrade ? "已升级" : "未升级"));
		lore.add("§7该升级可以允许你添加保温材料，减少热量损耗");
		lore.add("§7需要添加升级组件");
		meta.setLore(lore);
		icon.setItemMeta(meta);
	}

	public void set_overload_upgrade(int level) {
		this.overload_upgrade = level;
		ItemStack icon = this.gui.getItem(29);
		ItemMeta meta = icon.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r当前等级: " + level);
		lore.add("§7该升级可以增加燃料功率，");
		lore.add("§7但是会减少燃料燃烧时间，总产能下降");
		lore.add("§7需要添加升级组件");
		lore.add("§6每级提升" + this.get_manager().get_power_add_per_overload_upgrade() + "%功率，降低"
				+ this.get_manager().get_time_loss_per_overload_upgrade() + "%燃烧时间");
		meta.setLore(lore);
		icon.setItemMeta(meta);
		this.refresh_upgrade_info();
	}

	public void set_time_upgrade(int level) {
		this.time_upgrade = level;
		ItemStack icon = this.gui.getItem(30);
		ItemMeta meta = icon.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r当前等级: " + level);
		lore.add("§7该升级可以增加燃料燃烧时间，");
		lore.add("§7但是会减少燃料功率，总产能上升");
		lore.add("§7需要添加升级组件");
		lore.add("§6每级提升" + this.get_manager().get_time_add_per_time_upgrade() + "%燃烧时间，降低"
				+ this.get_manager().get_power_loss_per_time_upgrade() + "%功率");
		meta.setLore(lore);
		icon.setItemMeta(meta);
		this.refresh_upgrade_info();
	}

	private void refresh_upgrade_info() {
		ItemStack icon = this.gui.getItem(31);
		ItemMeta meta = icon.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		double power_modify = this.get_power_modify();
		double time_modify = this.get_time_modify();
		ChatColor power_color = ChatColor.GRAY;
		ChatColor time_color = ChatColor.GRAY;
		if (power_modify < 1) {
			power_color = ChatColor.RED;
		} else if (power_modify > 1) {
			power_color = ChatColor.GOLD;
		}
		if (time_modify < 1) {
			time_color = ChatColor.RED;
		} else if (time_modify > 1) {
			time_color = ChatColor.GOLD;
		}
		lore.add(power_color + "功率提升共计: " + String.format("%.2f", (power_modify - 1) * 100) + "%");
		lore.add(time_color + "时间提升共计: " + String.format("%.2f", (time_modify - 1) * 100) + "%");
		meta.setLore(lore);
		icon.setItemMeta(meta);
	}

	public int get_time_upgrade() {
		return this.time_upgrade;
	}

	public int get_overload_upgrade() {
		return this.overload_upgrade;
	}

	@Override
	public Advanced_furnace_manager get_manager() {
		return (Advanced_furnace_manager) super.get_manager();
	}

	public Hopper get_solid_reactant_hopper() {
		return this.get_hopper(solid_reactant_hopper_check_list, true);
	}

	public Hopper get_fuel_hopper() {
		return this.get_hopper(fuel_hopper_check_list, true);
	}

	public Chest get_chest() {
		return this.get_chest(solid_product_check_list);
	}

	@Override
	protected void init_after_set_location() {
		this.set_temperature(this.get_base_temperature());
		this.set_money(0);
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

	public boolean is_litting() {
		return this.is_litting;
	}

	public void set_litting(boolean litting) {
		if (this.is_litting != litting) {
			Furnace furnace = this.get_center_furnace();
			furnace.setLit(litting);
			this.is_litting = litting;
			this.get_location().getBlock().setBlockData(furnace);
		}
	}

	@Override
	public ItemStack[] get_drop_items() {
		ItemStack[] drop_items = new ItemStack[] { null, null, this.gui.getItem(fuel_slot),
				this.gui.getItem(fuel_product_slot), this.gui.getItem(solid_reactant_slot),
				this.gui.getItem(solid_product_slot), this.gui.getItem(liquid_reactant_slot),
				this.gui.getItem(liquid_product_slot), this.gui.getItem(gas_reactant_slot),
				this.gui.getItem(gas_product_slot) };
		if (this.get_overload_upgrade() > 0) {
			drop_items[0] = Upgrade_component.overload_component_item[this.get_overload_upgrade() - 1].clone();
		}
		if (this.get_time_upgrade() > 0) {
			drop_items[1] = Upgrade_component.time_component_item[this.get_time_upgrade() - 1].clone();
		}
		return drop_items;
	}

	@Override
	public void set_owner(String owner) {
		this.owner = owner;
	}

	@SuppressWarnings("deprecation")
	@Override
	public OfflinePlayer get_owner() {
		return Bukkit.getOfflinePlayer(this.owner);
	}

	@Override
	public String get_owner_name() {
		return this.owner;
	}
}