package com.piggest.minecraft.bukkit.utils;

import org.bukkit.Location;

public class Radio {
	public static final int light_speed = 299792458;
	public static final double antenna_bandwidth = 0.2; // 天线半带宽
	public static final int max_channel_bandwidth = 5; // 频道带宽最大占用天线带宽的几分之一

	/*
	 * 香农公式，带宽(kHz)和信噪比得到比特率(kbit/s)
	 */
	public static int Shannon_equation(int channel_bandwidth, int ston) {
		return (int) (channel_bandwidth * Math.log(1 + ston) / Math.log(2));
	}

	public static double radiation_r(int n, int freq) {
		double central_freq = get_central_freq(n);
		double x = (double) freq / central_freq / 2;
		return 73.1296 + 430.849 * (x - 0.5) + 508.621 * Math.pow((x - 0.5), 2) - 1255.73 * Math.pow((x - 0.5), 3);
	}

	public static double joule_r(int n) {
		return 0.1 * n;
	}

	public static double x(int n, int freq) {
		double central_freq = get_central_freq(n);
		double x = (double) freq / central_freq;
		return (x - 1) * 100;
	}

	/*
	 * 获得阻抗，单位为欧姆
	 */
	public static double z(int n, int freq) {
		return Math.sqrt(Math.pow(radiation_r(n, freq) + joule_r(n), 2) + Math.pow(x(n, freq), 2));
	}

	/*
	 * 获得单位频率的发射功率W/kHz
	 */
	public static double get_radiant_power(int u, int n, int channel_bandwidth, int freq) {
		double z = z(n, freq);
		double i = u / z;
		double r = radiation_r(n, freq);
		return i * i * r / channel_bandwidth;
	}

	/*
	 * 获得总输入功率单位W
	 */
	public static double get_input_power(int u, int n, int channel_bandwidth, int freq) {
		double z = z(n, freq);
		double i = u / z;
		double r = radiation_r(n, freq) + joule_r(n);
		return i * i * r;
	}

	/*
	 * 获得不同天线的中心频率，单位kHz
	 */
	public static int get_central_freq(int n) {
		return 75000 / n;
	}

	/*
	 * 获得一定距离处的单位频率的信号功率
	 */
	public static double get_power_at(Location source_location, double radiant_power, int central_freq, Location loc) {
		double distance = 1;
		if (source_location.getWorld() != loc.getWorld()) {
			int x_distance = source_location.getBlockX() - loc.getBlockX();
			int y_distance = source_location.getBlockY() - loc.getBlockY();
			int z_distance = source_location.getBlockZ() - loc.getBlockZ();
			distance = 256 + Math.sqrt(x_distance * x_distance + y_distance * y_distance + z_distance * z_distance);
		} else {
			distance = source_location.distance(loc);
		}
		return radiant_power / distance / distance;
	}

	public static int get_common_bandwidth(int source_freq, int source_bandwidth, int receiver_freq,
			int receiver_bandwidth) {
		int source_min_freq = source_freq - source_bandwidth / 2;
		int source_max_freq = source_freq + source_bandwidth / 2;
		int receiver_min_freq = receiver_freq - receiver_bandwidth / 2;
		int receiver_max_freq = receiver_freq + receiver_bandwidth / 2;
		int min_freq = Math.max(source_min_freq, receiver_min_freq);
		int max_freq = Math.min(source_max_freq, receiver_max_freq);
		int bandwidth = max_freq - min_freq;
		if (bandwidth < 0) {
			return 0;
		} else {
			return bandwidth;
		}
	}

	public static double get_signal_at(Location source_location, double source_power, int source_freq,
			int source_bandwidth, Location receiver_location, int receiver_freq, int receiver_bandwidth) {
		double power_per_freq = get_power_at(source_location, source_power, source_freq, receiver_location);
		return power_per_freq * get_common_bandwidth(source_freq, source_bandwidth, receiver_freq, receiver_bandwidth);
	}

	public static boolean check_channel_vaild(int channel_freq, int channel_bandwidth, int n) {
		if (channel_bandwidth <= 0 || channel_freq <= 0) {
			return false;
		}
		int channel_max_freq = channel_freq + channel_bandwidth / 2;
		int channel_min_freq = channel_freq - channel_bandwidth / 2;
		int antenna_central_freq = get_central_freq(n);
		if (channel_max_freq > (1 + antenna_bandwidth) * antenna_central_freq
				|| channel_min_freq < (1 - antenna_bandwidth) * antenna_central_freq) {
			return false;
		}
		if (antenna_central_freq * 2 * antenna_bandwidth / channel_bandwidth < max_channel_bandwidth) {
			return false;
		}
		return true;
	}
}
