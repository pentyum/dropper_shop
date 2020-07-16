package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gas_bottle {
	public static String name = "§r气体瓶";
	public static int max_capacity = 1000;
	public static NamespacedKey namespace = new NamespacedKey(Dropper_shop_plugin.instance, "gas_bottle");

	public static boolean is_gas_bottle(ItemStack item) {
		if (item == null) {
			return false;
		}
		return Material_ext.get_id_name(item).equals("gas_bottle");
	}

	public static void init_gas_bottle() {
		ItemStack item = new ItemStack(Material.GLASS_BOTTLE);
		Grinder.set_item_name(item, name);
		clean_contents(item);
		Material_ext.register("gas_bottle", item);
	}

	public static int get_contents(ItemStack item, Gas gastype) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		for (String string : lore) {
			String pattern = "§r(.*?): (-?\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(string);
			if (m.find()) {
				if (m.group(1).equals(gastype.name())) {
					return Integer.parseInt(m.group(2));
				}
			}
		}
		return 0;
	}

	public static void clean_contents(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r总容量: 0/" + max_capacity + "单位");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static int set_contents(ItemStack item, Gas gastype, int quantity) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int i = 0;
		int fail = 0;
		int current_capacity = Gas_bottle.calc_capacity(item);
		if (quantity + current_capacity > Gas_bottle.max_capacity) {
			fail = quantity + current_capacity - Gas_bottle.max_capacity;
			quantity = Gas_bottle.max_capacity - current_capacity;
		}
		for (String string : lore) {
			String pattern = "§r(.*?): (-?\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(string);
			if (m.find()) {
				if (m.group(1).equals(gastype.name())) {
					if (quantity != 0) {
						lore.set(i, "§r" + gastype.name() + ": " + quantity);
						meta.setLore(lore);
						item.setItemMeta(meta);
						update_capacity(item);
						return fail;
					} else {
						lore.remove(i);
						meta.setLore(lore);
						item.setItemMeta(meta);
						update_capacity(item);
						return fail;
					}
				}
			}
			i++;
		}
		lore.add("§r" + gastype.name() + ": " + quantity);
		meta.setLore(lore);
		item.setItemMeta(meta);
		update_capacity(item);
		return fail;
	}

	public static int calc_capacity(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int capacity = 0;
		for (String string : lore) {
			String pattern = "§r(.*?): (-?\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(string);
			if (m.find()) {
				if (Gas.get_gas(m.group(1)) != null) {
					capacity += Integer.parseInt(m.group(2));
				}
			}
		}
		return capacity;
	}

	public static void update_capacity(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§r总容量: " + Gas_bottle.calc_capacity(item) + "/" + max_capacity + "单位");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(namespace, Material_ext.new_item("gas_bottle", 1));
		sr1.shape("aia", "ibi", "aia");
		sr1.setIngredient('a', Material.AIR);
		sr1.setIngredient('b', Material.GLASS_BOTTLE);
		sr1.setIngredient('i', Material.IRON_INGOT);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("气体瓶合成表已添加");
	}

	public static HashMap<Gas, Integer> get_gas_map(ItemStack item) {
		HashMap<Gas, Integer> gas_map = new HashMap<Gas, Integer>();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int i = 1;
		for (i = 1; i < lore.size(); i++) {
			String string = lore.get(i);
			String pattern = "§r(.*?): (-?\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(string);
			if (m.find()) {
				Gas gas = Gas.get_gas(m.group(1));
				if (gas != null) {
					gas_map.put(gas, Integer.parseInt(m.group(2)));
				}
			}
		}
		return gas_map;
	}

	public static ItemStack get_new_empty_bottle() {
		return Material_ext.new_item("gas_bottle", 1);
	}
}
