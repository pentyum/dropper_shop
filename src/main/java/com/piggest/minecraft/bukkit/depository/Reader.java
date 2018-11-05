package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Reader {
	public static Location lore_parse_loction(ArrayList<String> lore) {
		String world_name;
		int x;
		int y;
		int z;
		String line = lore.get(0);
		String pattern = "\\((.+),(-?\\d+),(-?\\d+),(-?\\d+)\\)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			world_name = m.group(0);
			x = Integer.parseInt(m.group(1));
			y = Integer.parseInt(m.group(2));
			z = Integer.parseInt(m.group(3));
			return new Location(Bukkit.getWorld(world_name), x, y, z);
		}
		return null;
	}

	public static Material lore_parse_material(ArrayList<String> lore) {
		String material_name;
		String line = lore.get(1);
		String pattern = "§r物品: (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			material_name = m.group(0);
			return Material.getMaterial(material_name);
		}
		return null;
	}

	public static int lore_parse_num(ArrayList<String> lore) {
		int num;
		String line = lore.get(2);
		String pattern = "§r数量: ([1-9]\\d*|0)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			num = Integer.parseInt(m.group(0));
			return num;
		}
		return 0;
	}

	public static ArrayList<String> get_lore(Location loc, String material, int num) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r连接储存器: (" + loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + ","
				+ loc.getBlockZ());
		lore.add("§r物品: " + material);
		lore.add("§r数量: " + num);
		return lore;
	}
}
