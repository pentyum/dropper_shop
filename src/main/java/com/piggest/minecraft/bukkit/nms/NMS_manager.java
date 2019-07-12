package com.piggest.minecraft.bukkit.nms;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class NMS_manager {
	public static Ext_id ext_id_provider = null;

	public NMS_manager(String version) {
		Dropper_shop_plugin.instance.getLogger().info("当前NMS:" + version);
		ext_id_provider = new Ext_id_1_14();
	}
}
