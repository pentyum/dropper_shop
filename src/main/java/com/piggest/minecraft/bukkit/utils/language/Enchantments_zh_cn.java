package com.piggest.minecraft.bukkit.utils.language;

import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;

public class Enchantments_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();

	public static void init() {
		add("minecraft:sweeping", "横扫之刃");
		add("minecraft:aqua_affinity", "水下速掘");
		add("minecraft:depth_strider", "深海探索者");
		add("minecraft:respiration", "水下呼吸");
		add("minecraft:lure", "饵钓");
		add("minecraft:fire_aspect", "火焰附加");
		add("minecraft:looting", "抢夺");
		add("minecraft:channeling", "引雷");
		add("minecraft:luck_of_the_sea", "海之眷顾");
		add("minecraft:smite", "亡灵杀手");
		add("minecraft:feather_falling", "摔落保护");
		add("minecraft:efficiency", "效率");
		add("minecraft:impaling", "穿刺");
		add("minecraft:thorns", "荆棘");
		add("minecraft:flame", "火矢");
		add("minecraft:bane_of_arthropods", "节肢杀手");
		add("minecraft:fortune", "时运");
		add("minecraft:punch", "冲击");
		add("minecraft:riptide", "激流");
		add("minecraft:infinity", "无限");
		add("minecraft:loyalty", "忠诚");
		add("minecraft:binding_curse", "绑定诅咒");
		add("minecraft:vanishing_curse", "消失诅咒");
		add("minecraft:protection", "保护");
		add("minecraft:blast_protection", "爆炸保护");
		add("minecraft:fire_protection", "火焰保护");
		add("minecraft:projectile_protection", "弹射物保护");
		add("minecraft:power", "力量");
		add("minecraft:mending", "经验修补");
		add("minecraft:knockback", "击退");
		add("minecraft:frost_walker", "冰霜行者");
		add(Enchantment.DURABILITY, "耐久");
		add(Enchantment.SILK_TOUCH, "精准采集");
		add(Enchantment.DAMAGE_ALL, "锋利");
	}

	private static void add(Enchantment type, String trans_name) {
		add(type.getKey().toString(), trans_name);
	}

	private static void add(String full_name, String trans_name) {
		name.put(full_name, trans_name);
	}

	public static String get_enchantment_name(Enchantment ench) {
		return name.get(ench.getKey().toString());
	}
}
