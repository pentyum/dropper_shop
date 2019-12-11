package com.piggest.minecraft.bukkit.nms;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.nms.nbt.Element_type;
import com.piggest.minecraft.bukkit.nms.nbt.Element_type_1_15;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id_1_15;
import com.piggest.minecraft.bukkit.nms.nbt.Flying_time;
import com.piggest.minecraft.bukkit.nms.nbt.Flying_time_1_15;
import com.piggest.minecraft.bukkit.nms.nbt.No_ai;
import com.piggest.minecraft.bukkit.nms.nbt.No_ai_1_15;

public class NMS_manager {
	public static Ext_id ext_id_provider = null;
	public static Raid_trigger raid_provider = null;
	public static Element_type element_type_provider = null;
	public static No_ai watersheep_provider = null;
	public static Flying_time flying_time_provider = null;
	
	public NMS_manager(String version) {
		Dropper_shop_plugin.instance.getLogger().info("当前NMS:" + version);
		NMS_version nms_version = NMS_version.parse_version(version);
		switch (nms_version) {
		case v1_15:
			Dropper_shop_plugin.instance.getLogger().info("已适配NMS:" + version);
			ext_id_provider = new Ext_id_1_15();
			raid_provider = new Raid_trigger_1_15();
			element_type_provider = new Element_type_1_15();
			watersheep_provider = new No_ai_1_15();
			flying_time_provider = new Flying_time_1_15();
			break;
		default:
			Dropper_shop_plugin.instance.getLogger().warning("NMS未能适配!");
			ext_id_provider = new Ext_id_1_15();
			raid_provider = new Raid_trigger_1_15();
			element_type_provider = new Element_type_1_15();
			watersheep_provider = new No_ai_1_15();
			flying_time_provider = new Flying_time_1_15();
			break;
		}
	}
}
