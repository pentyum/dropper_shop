package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gas_bottle {
	public static String name = "§r气体瓶";
	public static ItemStack item = null;
	public static int max_capacity = 1000;

	public static ItemStack init_gas_bottle() {
		item = new ItemStack(Material.GLASS_BOTTLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Gas_bottle.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r总容量: 0/" + max_capacity + "单位");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
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

	public static int set_contents(ItemStack item, Gas gastype, int quantity) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int i = 0;
		int fail = 0;
		int current_capacity = Gas_bottle.get_capacity(item);
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
						return fail;
					} else {
						lore.remove(i);
						return fail;
					}
				}
			}
			i++;
		}
		lore.add("§r" + gastype.name() + ": " + quantity);
		return fail;
	}

	public static int get_capacity(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int capacity = 0;
		for (String string : lore) {
			String pattern = "§r(.*?): (-?\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(string);
			if (m.find()) {
				if (Gas.valueOf(m.group(1)) != null) {
					capacity += Integer.parseInt(m.group(2));
				}
			}
		}
		return capacity;
	}

	public static void update_capacity(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§r总容量: " + Gas_bottle.get_capacity(item) + "/" + max_capacity + "单位");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
