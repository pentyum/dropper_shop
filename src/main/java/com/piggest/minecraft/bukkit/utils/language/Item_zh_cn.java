package com.piggest.minecraft.bukkit.utils.language;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Item_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();

	public static void init() {
		name.put("minecraft:stone", "石头");
		name.put("minecraft:granite", "花岗岩");
		name.put("minecraft:polished_granite", "磨制花岗岩");
		name.put("minecraft:diorite", "闪长岩");
		name.put("minecraft:polished_diorite", "磨制闪长岩");
		name.put("minecraft:andesite", "安山岩");
		name.put("minecraft:polished_andesite", "磨制安山岩");
		name.put("minecraft:grass_block", "草方块");
		name.put("minecraft:dirt", "泥土");
		name.put("minecraft:cobblestone", "圆石");
		name.put("minecraft:oak_planks", "橡木木板");
		name.put("minecraft:glass", "玻璃");
		name.put("minecraft:gold_block", "金块");
		name.put("minecraft:iron_block", "铁块");
		name.put("minecraft:diamond_block", "钻石块");
		name.put("minecraft:blue_ice", "蓝冰");
		name.put("minecraft:tnt", "TNT");
		name.put("minecraft:redstone_torch", "红石火把");
		name.put("minecraft:redstone_block", "红石块");
		name.put("minecraft:ghast_tear", "恶魂之泪");
		name.put("minecraft:dragon_head", "龙首");
		name.put("minecraft:creeper_head", "苦力怕头颅");
		name.put("minecraft:zombie_head", "僵尸头颅");
		name.put("minecraft:skeleton_skull", "骷髅头颅");
		name.put("minecraft:wither_skeleton_skull", "凋零骷髅头颅");
		name.put("minecraft:nautilus_shell", "鹦鹉螺壳");
		name.put("minecraft:heart_of_the_sea", "海洋之心");
		name.put("minecraft:enchanted_golden_apple", "附魔金苹果");
		name.put("dropper_shop:wrench", "扳手");
	}

	public static String get_enchantment_name(ItemStack item) {
		String full_name = Material_ext.get_full_name(item);
		String result = name.get(full_name);
		if (result == null) {
			return full_name;
		} else {
			return result;
		}
	}
}
