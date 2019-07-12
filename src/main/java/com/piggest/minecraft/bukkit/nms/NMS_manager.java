package com.piggest.minecraft.bukkit.nms;

public class NMS_manager {
	public static Ext_id ext_id_provider = null;
	
	public NMS_manager(String version) {
		ext_id_provider = new Ext_id_1_14();
	}
}
