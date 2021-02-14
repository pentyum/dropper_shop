package com.piggest.minecraft.bukkit.nms;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.nms.difficulty.Local_difficulty;
import com.piggest.minecraft.bukkit.nms.difficulty.Local_difficulty_1_16;
import com.piggest.minecraft.bukkit.nms.enchant.Enchant_manager;
import com.piggest.minecraft.bukkit.nms.enchant.Enchant_manager_1_16;
import com.piggest.minecraft.bukkit.nms.map.Map_editor;
import com.piggest.minecraft.bukkit.nms.map.Map_editor_1_16;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id_1_16;

public class NMS_manager {
	public static Ext_id ext_id_provider = null;
	public static Raid_trigger raid_provider = null;
	//public static Biome_modifier biome_modifier = null;
	//public static Packet_map_chunk_listener_1_16 packet_map_chunk_listener = null;
	public static Enchant_manager enchant_manager = null;
	public static Local_difficulty local_difficulty = null;
	public static Map_editor map_editor = null;

	public NMS_manager(String version) {
		Dropper_shop_plugin.instance.getLogger().info("当前NMS:" + version);
		NMS_version nms_version = NMS_version.parse_version(version);
		switch (nms_version) {
			case v1_16:
				Dropper_shop_plugin.instance.getLogger().info("已适配NMS:" + version);
				ext_id_provider = new Ext_id_1_16();
				raid_provider = new Raid_trigger_1_16();
				//biome_modifier = new Biome_modifier_1_16();
				//packet_map_chunk_listener = new Packet_map_chunk_listener_1_16();
				enchant_manager = new Enchant_manager_1_16();
				local_difficulty = new Local_difficulty_1_16();
				map_editor = new Map_editor_1_16();
				break;
			default:
				Dropper_shop_plugin.instance.getLogger().warning("NMS未能适配!");
				ext_id_provider = new Ext_id_1_16();
				raid_provider = new Raid_trigger_1_16();
				//biome_modifier = new Biome_modifier_1_16();
				//packet_map_chunk_listener = new Packet_map_chunk_listener_1_16();
				enchant_manager = new Enchant_manager_1_16();
				local_difficulty = new Local_difficulty_1_16();
				map_editor = new Map_editor_1_16();
				break;
		}
	}
}
