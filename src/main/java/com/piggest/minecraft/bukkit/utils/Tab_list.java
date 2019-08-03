package com.piggest.minecraft.bukkit.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Tab_list {
	public static final ArrayList<String> true_false_list = new ArrayList<String>();
	public static final ArrayList<String> world_name_list = new ArrayList<String>();

	public static void init() {
		true_false_list.add("true");
		true_false_list.add("false");
		for (World world : Bukkit.getWorlds()) {
			world_name_list.add(world.getName());
		}
	}
}
