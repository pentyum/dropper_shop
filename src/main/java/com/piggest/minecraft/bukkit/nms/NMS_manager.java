package com.piggest.minecraft.bukkit.nms;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.nms.nbt.Element_type;
import com.piggest.minecraft.bukkit.nms.nbt.Element_type_1_14;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id;
import com.piggest.minecraft.bukkit.nms.nbt.Ext_id_1_14;

public class NMS_manager {
	public static Ext_id ext_id_provider = null;
	public static Raid raid_provider = null;
	public static Element_type element_type_provider = null;
	public static Watersheep watersheep_provider = null;

	public NMS_manager(String version) {
		Dropper_shop_plugin.instance.getLogger().info("当前NMS:" + version);
		NMS_version nms_version = NMS_version.parse_version(version);
		switch (nms_version) {
		case v1_14:
			Dropper_shop_plugin.instance.getLogger().info("已适配NMS:" + version);
			ext_id_provider = new Ext_id_1_14();
			raid_provider = new Raid_1_14();
			element_type_provider = new Element_type_1_14();
			watersheep_provider = new Watersheep_1_14();
			break;
		default:
			Dropper_shop_plugin.instance.getLogger().warning("NMS未能适配!");
			ext_id_provider = new Ext_id_1_14();
			raid_provider = new Raid_1_14();
			element_type_provider = new Element_type_1_14();
			watersheep_provider = new Watersheep_1_14();
			break;
		}
	}
}
