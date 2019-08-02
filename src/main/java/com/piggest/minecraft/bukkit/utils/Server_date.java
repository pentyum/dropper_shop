package com.piggest.minecraft.bukkit.utils;

import java.util.Calendar;

import org.bukkit.World;

public class Server_date {
	public static int get_world_day(World world) {
		return (int) (world.getFullTime() / 24000);
	}

	public static int get_world_min(World world) {
		return (int) (world.getFullTime() / 1000 * 60);
	}

	public static Calendar get_world_date(World world) {
		Calendar start_date = Calendar.getInstance();
		start_date.set(1000, 1, 1, 6, get_world_min(world), 0);
		return start_date;
	}

}
