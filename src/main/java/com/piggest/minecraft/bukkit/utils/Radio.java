package com.piggest.minecraft.bukkit.utils;

public class Radio {
	public static final int light_speed = 299792458;

	public static int Shannon_equation(int channel_bandwidth, int ston) { // 带宽(kHz)和信噪比得到比特率(kbit/s)
		return (int) (channel_bandwidth * Math.log(1 + ston) / Math.log(2));
	}

	public static int z(int length, int f, int a) {
		return 73;
	}

	public static int power(int i, int f) {
		return i * i * f * f * f * f;
	}
}
