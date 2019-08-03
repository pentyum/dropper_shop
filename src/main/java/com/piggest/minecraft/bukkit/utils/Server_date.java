package com.piggest.minecraft.bukkit.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.World;

public class Server_date {
	public static int get_world_day(World world) {
		return (int) (world.getFullTime() / 24000);
	}

	public static int get_world_min(World world) {
		return (int) (world.getFullTime() * 60 / 1000);
	}

	public static Calendar get_world_date(World world) {
		Calendar start_date = Calendar.getInstance();
		start_date.set(2000, 1, 1, 6, get_world_min(world), 0);
		return start_date;
	}

	public static String formatDate(Calendar date) {
		SimpleDateFormat timeFt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String timeStr = (date == null ? "" : timeFt.format(date.getTime()));
		return timeStr;
	}

	public static double get_solar_dec(World world) {
		Calendar date = get_world_date(world);
		int day = date.get(Calendar.DAY_OF_YEAR);
		return Math.asin(0.39795 * Math.cos(0.98563 * (day - 173) / 180 * Math.PI));
	}

	public static long real_time_to_full_time() {
		Calendar now = Calendar.getInstance();

		Calendar start = Calendar.getInstance();
		start.set(2000, 1, 1, 6, 0, 0);
		long milli = now.getTime().getTime() - start.getTime().getTime();
		return milli / 3600;
	}
}
