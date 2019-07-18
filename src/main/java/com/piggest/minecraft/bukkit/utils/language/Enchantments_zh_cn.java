package com.piggest.minecraft.bukkit.utils.language;

import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;

public class Enchantments_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();
	
	public static void init() {
		name.put("minecraft:sweeping", "横扫之刃");
		name.put("minecraft:aqua_affinity", "水下速掘");
		name.put("minecraft:depth_strider", "深海探索者");
		name.put("minecraft:respiration", "水下呼吸");
		name.put("minecraft:lure", "饵钓");
		name.put("minecraft:fire_aspect", "火焰附加");
		name.put("minecraft:looting", "抢夺");
		name.put("minecraft:channeling", "引雷");
		name.put("minecraft:luck_of_the_sea", "海之眷顾");
		name.put("minecraft:smite", "亡灵杀手");
		name.put("minecraft:feather_falling", "摔落保护");
		name.put("minecraft:efficiency", "效率");
		name.put("minecraft:impaling", "穿刺");
		name.put("minecraft:thorns", "荆棘");
		name.put("minecraft:flame", "火矢");
		name.put("minecraft:bane_of_arthropods", "节肢杀手");
		name.put("minecraft:fortune", "时运");
		name.put("minecraft:punch", "冲击");
		name.put("minecraft:riptide", "激流");
		name.put("minecraft:infinity", "无限");
		name.put("minecraft:loyalty", "忠诚");
		name.put("minecraft:binding_curse", "绑定诅咒");
		name.put("minecraft:vanishing_curse", "消失诅咒");
		name.put("minecraft:protection", "保护");
		name.put("minecraft:blast_protection", "爆炸保护");
		name.put("minecraft:fire_protection", "火焰保护");
		name.put("minecraft:projectile_protection", "弹射物保护");
		name.put("minecraft:power", "力量");
		name.put("minecraft:mending", "经验修补");
		name.put("minecraft:knockback", "击退");
		name.put("minecraft:frost_walker", "冰霜行者");
		name.put("minecraft:unbreaking", "耐久");
		name.put("minecraft:silk_touch", "精准采集");
		name.put("minecraft:sharpness", "锋利");
	}
	
	public static String get_enchantment_name(Enchantment ench) {
		return name.get(ench.getKey().toString());
	}
}
