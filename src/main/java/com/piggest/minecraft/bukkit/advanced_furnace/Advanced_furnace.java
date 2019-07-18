package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Capacity_upgradable;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Advanced_furnace extends Multi_block_with_gui implements HasRunner, Capacity_upgradable {
	public static final int solid_reactant_slot = 9;
	public static final int gas_reactant_slot = 11;
	public static final int fuel_slot = 17;
	public static final int fuel_product_slot = 35;
	public static final int solid_product_slot = 18;
	public static final int gas_product_slot = 20;
	public static final int upgrade_component_slot = 33;

	private static final int solid_reactant_hopper_check_list[][] = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 1, -2 },
			{ -2, 1, 0 } }; // 注入固体
	private static final int fuel_hopper_check_list[][] = { { 0, -1, 2 }, { 2, -1, 0 }, { 0, -1, -2 }, { -2, -1, 0 } }; // 注入固体

	private Reaction_container reaction_container = new Reaction_container();
	private double power = 0;
	private Advanced_furnace_temp_runner temp_runner = new Advanced_furnace_temp_runner(this);
	private Advanced_furnace_reaction_runner reaction_runner = new Advanced_furnace_reaction_runner(this);
	private Advanced_furnace_io_runner io_runner = new Advanced_furnace_io_runner(this);
	private Advanced_furnace_upgrade_runner upgrade_runner = new Advanced_furnace_upgrade_runner(this);

	private int heat_keeping_value = 0;
	private Fuel fuel;
	public int fuel_ticks = 0;
	private int money = 0;
	private int money_limit = 9000;
	private int structure_level = 1;
	private boolean heat_keeping_upgrade = false;
	private int overload_upgrade = 0;
	private int time_upgrade = 0;
	private double e = 1;

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
			this.set_fuel_product_slot((ItemStack) shop_save.get("solid-product-slot"));
		}
		if (shop_save.get("solid-reactant-slot") != null) {
			this.set_fuel_product_slot((ItemStack) shop_save.get("solid-reactant-slot"));
		}
		if (shop_save.get("fuel-product-slot") != null) {
			this.set_fuel_product_slot((ItemStack) shop_save.get("fuel-product-slot"));
		}
		if (shop_save.get("upgrade-component-slot") != null) {
			this.gui.setItem(Advanced_furnace.upgrade_component_slot,
					(ItemStack) shop_save.get("upgrade-component-slot"));
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

	public void set_solid_reactant_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.solid_reactant_slot, slot_item);
	}

	public void set_fuel_slot(ItemStack slot_item) {
		this.gui.setItem(Advanced_furnace.fuel_slot, slot_item);
	}

	public void set_fuel_product_slot(ItemStack item) {
		this.gui.setItem(Advanced_furnace.fuel_product_slot, item);
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
		ItemStack fuel_slot = this.gui.getItem(Advanced_furnace.fuel_slot);
		ItemStack solid_product_slot = this.gui.getItem(Advanced_furnace.solid_product_slot);
		ItemStack solid_reactant_slot = this.gui.getItem(Advanced_furnace.solid_reactant_slot);
		ItemStack fuel_product_slot = this.gui.getItem(Advanced_furnace.fuel_product_slot);
		ItemStack upgrade_component_slot = this.gui.getItem(Advanced_furnace.upgrade_component_slot);

		if (!Grinder.is_empty(fuel_slot)) {
			save.put("fuel-slot", Material_ext.get_id_name(fuel_slot));
			save.put("fuel-slot-num", fuel_slot.getAmount());
		}
		if (!Grinder.is_empty(solid_product_slot)) {
			save.put("solid-product-slot", solid_product_slot);
		}
		if (!Grinder.is_empty(solid_reactant_slot)) {
			save.put("solid-reactant-slot", solid_reactant_slot);
		}
		if (!Grinder.is_empty(fuel_product_slot)) {
			save.put("fuel-product-slot", fuel_product_slot);
		}
		if (!Grinder.is_empty(upgrade_component_slot)) {
			save.put("upgrade-component-slot", upgrade_component_slot);
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
		lore.set(2, "§r燃料功率: " + String.format("%.2f", this.get_power()) + " K/tick");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
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
			lore.set(1, "§r燃料: 无");
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

	private boolean add_a_item_to_slot(ItemStack src_item, int i) {
		if (!Grinder.is_empty(src_item)) {
			if (Grinder.is_empty(this.gui.getItem(i))) {
				this.gui.setItem(i, src_item.clone());
				this.gui.getItem(i).setAmount(1);
				src_item.setAmount(src_item.getAmount() - 1);
				return true;
			} else if (src_item.isSimilar(this.gui.getItem(i))) {
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
		return this.add_a_item_to_slot(src_item, Advanced_furnace.solid_reactant_slot);
	}

	public boolean add_a_fuel(ItemStack src_item) {
		return this.add_a_item_to_slot(src_item, Advanced_furnace.fuel_slot);
	}

	public void set_gas_product_slot(ItemStack item) {
		this.gui.setItem(20, item);
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
		return true;
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
			player.sendMessage("已经升级至满级");
			return false;
		}
		int need_price = Exp_saver.get_upgrade_price(current_level);
		if (Dropper_shop_plugin.instance.cost_player_money(need_price, player)) {
			this.set_capacity_level(current_level + 1);
			player.sendMessage("消耗了" + need_price + "金币把高级熔炉升级至" + (current_level + 1) + "级");
			return true;
		} else {
			player.sendMessage("你的钱不够，高级熔炉由" + current_level + "升级至" + (current_level + 1) + "级需要" + need_price + "金币");
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

	private synchronized Hopper get_hopper(int[][] check_list) {
		for (int[] relative_coord : check_list) {
			BlockState block = this.get_block(relative_coord[0], relative_coord[1], relative_coord[2]).getState();
			if (block instanceof Hopper) {
				org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
						.getBlockData();
				Vector vec = hopper_data.getFacing().getDirection().multiply(2)
						.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
				if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
					if (block.getBlock().isBlockPowered()) {
						continue;
					}
					return (Hopper) block;
				}
			}
		}
		return null;
	}

	public synchronized Hopper get_solid_reactant_hopper() {
		return this.get_hopper(solid_reactant_hopper_check_list);
	}

	public synchronized Hopper get_fuel_hopper() {
		return this.get_hopper(fuel_hopper_check_list);
	}

	@Override
	public void init_after_set_location() {
		this.set_temperature(this.get_base_temperature());
		this.set_money(0);
	}
}