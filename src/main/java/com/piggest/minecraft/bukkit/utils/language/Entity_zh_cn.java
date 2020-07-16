package com.piggest.minecraft.bukkit.utils.language;

import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class Entity_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();

	public static void init() {
		add(EntityType.SLIME, "史莱姆");
		add(EntityType.MAGMA_CUBE, "岩浆怪");
		add(EntityType.ZOMBIE, "僵尸");
		add(EntityType.ZOMBIE_VILLAGER, "僵尸村民");
		add(EntityType.HUSK, "尸壳");
		add(EntityType.DROWNED, "溺尸");
		add(EntityType.SKELETON, "骷髅");
		add(EntityType.STRAY, "流浪者");
		add(EntityType.WITHER_SKELETON, "凋零骷髅");
		add(EntityType.CREEPER, "苦力怕");
		add(EntityType.SPIDER, "蜘蛛");
		add(EntityType.CAVE_SPIDER, "洞穴蜘蛛");
		add(EntityType.ENDERMAN, "末影人");
		add(EntityType.ENDERMITE, "末影螨");
		add(EntityType.PHANTOM, "幻翼");
		add(EntityType.SILVERFISH, "蠹虫");
		add(EntityType.SHULKER, "潜影贝");
		add(EntityType.BLAZE, "烈焰人");
		add(EntityType.GUARDIAN, "守卫者");
		add(EntityType.VINDICATOR, "卫道士");
		add(EntityType.PILLAGER, "掠夺者");
		add(EntityType.WITCH, "女巫");
		add(EntityType.RAVAGER, "劫掠兽");
		add(EntityType.EVOKER, "唤魔者");
		add(EntityType.GHAST, "恶魂");

		add(EntityType.IRON_GOLEM, "铁傀儡");
		add(EntityType.SNOWMAN, "雪傀儡");

		add(EntityType.POLAR_BEAR, "北极熊");
		add(EntityType.PANDA, "熊猫");
		add(EntityType.BEE, "蜜蜂");
		add(EntityType.RABBIT, "兔子");
		add(EntityType.PARROT, "鹦鹉");
		add(EntityType.CAT, "猫");
		add(EntityType.COW, "牛");
		add(EntityType.MUSHROOM_COW, "哞菇");
		add(EntityType.PIG, "猪");
		add(EntityType.WOLF, "狼");
		add(EntityType.FOX, "狐狸");
		add(EntityType.SHEEP, "羊");
		add(EntityType.CHICKEN, "鸡");
		add(EntityType.BAT, "蝙蝠");
		add(EntityType.LLAMA, "羊驼");
		add(EntityType.TRADER_LLAMA, "行商羊驼");
		add(EntityType.TURTLE, "海龟");
		add(EntityType.OCELOT, "豹猫");
		add(EntityType.HORSE, "马");
		add(EntityType.SKELETON_HORSE, "骷髅马");
		add(EntityType.DONKEY, "驴");
		add(EntityType.MULE, "骡");
		add(EntityType.VILLAGER, "村民");
		add(EntityType.WANDERING_TRADER, "流浪商人");

		add(EntityType.DOLPHIN, "海豚");
		add(EntityType.SQUID, "鱿鱼");
		add(EntityType.COD, "鳕鱼");
		add(EntityType.SALMON, "鲑鱼");
		add(EntityType.PUFFERFISH, "河豚");
		add(EntityType.TROPICAL_FISH, "热带鱼");

		add(EntityType.MINECART, "矿车");
		add(EntityType.MINECART_FURNACE, "动力矿车");
		add(EntityType.MINECART_HOPPER, "漏斗矿车");
		add(EntityType.MINECART_CHEST, "箱子矿车");
		add(EntityType.MINECART_TNT, "TNT矿车");
		add(EntityType.MINECART_COMMAND, "命令方块矿车");
		add(EntityType.MINECART_MOB_SPAWNER, "刷怪箱矿车");
		add(EntityType.BOAT, "船");
		add(EntityType.ARMOR_STAND, "盔甲架");
	}

	private static void add(EntityType type, String trans_name) {
		add(type.getKey().toString(), trans_name);
	}

	private static void add(String full_name, String trans_name) {
		name.put(full_name, trans_name);
	}

	public static String get_entity_name(EntityType entity) {
		String full_name = entity.getKey().toString();
		String result = name.get(full_name);
		if (result == null) {
			return full_name;
		} else {
			return result;
		}
	}
}
