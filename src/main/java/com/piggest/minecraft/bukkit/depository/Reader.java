package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Reader {
	public static String name = "§r存储读取器";
	public static ItemStack reader_item = null;

	public static Location lore_parse_loction(List<String> lore) {
		String world_name = null;
		int x = 0;
		int y = 0;
		int z = 0;
		String line = lore.get(0);
		String pattern = "§r存储器位置: (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			world_name = m.group(1);
		}
		line = lore.get(1);
		pattern = "§rx: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			x = Integer.parseInt(m.group(1));
		}
		line = lore.get(2);
		pattern = "§ry: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			y = Integer.parseInt(m.group(1));
		}
		line = lore.get(3);
		pattern = "§rz: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			z = Integer.parseInt(m.group(1));
		}
		return new Location(Bukkit.getWorld(world_name), x, y, z);
	}

	public static Material lore_parse_material(List<String> lore) {
		String material_name;
		String line = lore.get(4);
		String pattern = "§r物品: (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			material_name = m.group(1);
			// Bukkit.getLogger().info(material_name);
			return Material.getMaterial(material_name);
		}
		return null;
	}

	public static int lore_parse_num(List<String> lore) {
		int num;
		String line = lore.get(5);
		String pattern = "§r数量: ([1-9]\\d*|0)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			num = Integer.parseInt(m.group(1));
			return num;
		}
		return 0;
	}

	public static ArrayList<String> get_lore(Location loc, String material, int num) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r存储器位置: " + loc.getWorld().getName());
		lore.add("§rx: " + loc.getBlockX());
		lore.add("§ry: " + loc.getBlockY());
		lore.add("§rz: " + loc.getBlockZ());
		lore.add("§r物品: " + material);
		lore.add("§r数量: " + num);
		return lore;
	}

	public static List<String> lore_update(List<String> lore) {
		Location loc = lore_parse_loction(lore);
		Material material = lore_parse_material(lore);
		Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
		if (depository == null) {
			lore.set(0, "§r存储器位置: null");
			lore.set(1, "§rx: null");
			lore.set(2, "§ry: null");
			lore.set(3, "§rz: null");
			lore.set(4, "§r物品: null");
			lore.set(5, "§r数量: 0");
		}
		// Bukkit.getLogger().info("new num" +
		// depository.get_material_num(material.name()));
		lore.set(5, "§r数量: " + depository.get_material_num(material.name()));
		return lore;
	}

	public static void item_lore_update(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore_update(meta.getLore()));
		item.setItemMeta(meta);
	}

	public static boolean is_reader(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getItemMeta().hasDisplayName() == false) {
			return false;
		}
		return item.getItemMeta().getDisplayName().equals(Reader.name);
	}

	public static ItemStack init_reader_item() {
		ItemStack reader_item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = reader_item.getItemMeta();
		meta.setDisplayName(Reader.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r存储器位置: null");
		lore.add("§rx: null");
		lore.add("§ry: null");
		lore.add("§rz: null");
		lore.add("§r物品: null");
		lore.add("§r数量: 0");
		meta.setLore(lore);
		reader_item.setItemMeta(meta);
		Reader.reader_item = reader_item;
		return reader_item;
	}

	public static void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(Dropper_shop_plugin.instance.namespace, Reader.reader_item);
		sr1.shape("rsr", "scs", "rsr");
		sr1.setIngredient('s', Material.NETHER_STAR);
		sr1.setIngredient('c', Material.ENDER_CHEST);
		sr1.setIngredient('r', Material.END_CRYSTAL);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr1);
		Dropper_shop_plugin.instance.get_sr().add(sr1);
		Dropper_shop_plugin.instance.getLogger().info("存储读取器合成表已添加");
	}
}
