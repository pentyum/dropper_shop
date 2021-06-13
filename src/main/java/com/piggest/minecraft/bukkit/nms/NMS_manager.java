package com.piggest.minecraft.bukkit.nms;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.nms.difficulty.Local_difficulty;
import com.piggest.minecraft.bukkit.nms.difficulty.Local_difficulty_1_17;
import com.piggest.minecraft.bukkit.nms.map.Map_editor;
import com.piggest.minecraft.bukkit.nms.map.Map_editor_1_17;

public class NMS_manager {
	public static Raid_trigger raid_provider = null;
	public static Local_difficulty local_difficulty = null;
	public static Map_editor map_editor = null;

	public NMS_manager(String version) {
		Dropper_shop_plugin.instance.getLogger().info("当前NMS:" + version);
		NMS_version nms_version = NMS_version.parse_version(version);
		switch (nms_version) {
			case v1_16:
				Dropper_shop_plugin.instance.getLogger().info("已适配NMS:" + version);
				raid_provider = new Raid_trigger_1_17();
				local_difficulty = new Local_difficulty_1_17();
				map_editor = new Map_editor_1_17();
				break;
			default:
				Dropper_shop_plugin.instance.getLogger().warning("NMS未能适配!");
				raid_provider = new Raid_trigger_1_17();
				local_difficulty = new Local_difficulty_1_17();
				map_editor = new Map_editor_1_17();
				break;
		}
	}
}
