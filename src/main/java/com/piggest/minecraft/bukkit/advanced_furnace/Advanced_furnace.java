package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Advanced_furnace extends Multi_block_structure implements InventoryHolder, HasRunner {
	private double temperature = 0;
	private double power = 0;
	private Inventory gui = Bukkit.createInventory(this, 27, "高级熔炉");
	private Advanced_furnace_temp_runner temp_runner = new Advanced_furnace_temp_runner(this);
	private Fuel fuel;

	public static double get_block_temperature(Block block) {
		double base_temp = block.getTemperature() * 20 + 270;
		double height_temp = (64 - block.getY()) / 30.0;
		double env_temp = 0;
		double light_temp = 0;
		if (block.getWorld().getEnvironment() == Environment.NETHER) {
			env_temp = 50;
		}
		light_temp = -8 + block.getLightLevel();
		// Dropper_shop_plugin.instance.getLogger().info("温度=" + base_temp + "+" +
		// height_temp + "+" + env_temp + "+" + light_temp);
		return base_temp + height_temp + env_temp + light_temp;
	}

	public Advanced_furnace() {
		ItemStack raw_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack fuel_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack product_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemStack temp_sign = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		Grinder.set_item_name(raw_sign, "§r左边放原料");
		Grinder.set_item_name(fuel_sign, "§r右边放燃料");
		Grinder.set_item_name(product_sign, "§r左边为产品");
		Grinder.set_item_name(temp_sign, "§r右边为温度");
		this.gui.setItem(10, raw_sign);
		this.gui.setItem(12, raw_sign.clone());
		this.gui.setItem(14, raw_sign.clone());
		this.gui.setItem(16, fuel_sign);
		this.gui.setItem(19, product_sign);
		this.gui.setItem(21, product_sign.clone());
		this.gui.setItem(23, product_sign.clone());
		this.gui.setItem(25, temp_sign);
		ItemStack temp_info = new ItemStack(Material.FURNACE);
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		temp_info_meta.setDisplayName("§e信息");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r温度: 0 K");
		lore.add("§r燃料: ");
		lore.add("§r燃料功率: " + 0 + " K/tick");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
		this.gui.setItem(26, temp_info);
		
		this.set_fuel(Fuel.coal);
		this.set_power(0.1);
	}

	public double get_base_temperature() {
		return Advanced_furnace.get_block_temperature(this.get_location().add(0, 1, 0).getBlock());
	}

	public void set_temperature(double temperature) {
		this.temperature = temperature;
		ItemStack temp_info = this.gui.getContents()[26];
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		lore.set(0, "§r温度: " + String.format("%.1f", temperature) + " K");
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
	}

	public double get_temperature() {
		return this.temperature;
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.set_temperature((Double) shop_save.get("temperature"));
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("temperature", this.get_temperature());
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

	public void set_power(double power) {
		this.power = power;
		ItemStack temp_info = this.gui.getContents()[26];
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
		ItemStack temp_info = this.gui.getContents()[26];
		ItemMeta temp_info_meta = temp_info.getItemMeta();
		List<String> lore = temp_info_meta.getLore();
		lore.set(1, "§r燃料: " + fuel.name());
		temp_info_meta.setLore(lore);
		temp_info.setItemMeta(temp_info_meta);
	}

	public double get_k() { // 热传导率 K/(deltaK tick)
		return 0.001;
	}

	public double get_power_loss() {
		return this.get_k() * (this.temperature - this.get_base_temperature());
	}

	public Advanced_furnace_temp_runner get_runner() {
		return this.temp_runner;
	}

	public int get_runner_cycle() {
		return 1;
	}

	public int get_runner_delay() {
		return 10;
	}

}