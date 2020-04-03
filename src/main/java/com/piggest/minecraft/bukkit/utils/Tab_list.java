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
	public static final ArrayList<String> color_list = new ArrayList<String>(Color_utils.string_color_map.keySet());
	public static final List<String> time_format = new ArrayList<String>();

	public static void init() {
		true_false_list.add("true");
		true_false_list.add("false");
		for (World world : Bukkit.getWorlds()) {
			world_name_list.add(world.getName());
		}
		time_format.add("yyyy年");
		time_format.add("yyyy-MM-dd");
		time_format.add("MM-dd");
		time_format.add("MM月dd日");
		time_format.add("HH:mm:ss");
		time_format.add("HH:mm");
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
