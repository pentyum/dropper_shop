package com.piggest.minecraft.bukkit.nms;

public enum NMS_version {
	v1_14, v1_13, v1_12, v1_11, v1_10, v1_9, v1_8, v1_7, v1_6;
	public static NMS_version parse_version(String version) {
		String[] splitted1 = version.split("-");
		String[] splitted2 = splitted1[0].split("\\.");
		String name = "v" + splitted2[0] + "_" + splitted2[1];
		return NMS_version.valueOf(name);
	}
}
