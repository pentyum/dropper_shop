package com.piggest.minecraft.bukkit.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Tab_list {
	public static final ArrayList<String> true_false_list = new ArrayList<String>();
	public static final ArrayList<String> world_name_list = new ArrayList<String>();
	public static final ArrayList<String> color_list = new ArrayList<String>();

	public static void init() {
		true_false_list.add("true");
		true_false_list.add("false");
		for (World world : Bukkit.getWorlds()) {
			world_name_list.add(world.getName());
		}
		color_list.add("BLACK");
		color_list.add("WHITE");
		color_list.add("GREEN");
		color_list.add("BLUE");
		color_list.add("YELLOW");
	}

	@Nonnull
	public static List<String> contains(@Nonnull List<String> string_list, @Nullable String str) {
		ArrayList<String> result = new ArrayList<String>();
		if (str == null) {
			return string_list;
		}
		if (str.length() == 0) {
			return string_list;
		}
		for (String s : string_list) {
			if (s.contains(str)) {
				result.add(s);
			}
		}
		return result;
	}
}
