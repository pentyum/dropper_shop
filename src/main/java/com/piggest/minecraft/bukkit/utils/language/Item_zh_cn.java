package com.piggest.minecraft.bukkit.utils.language;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Item_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();

	public static void init() {
		add("minecraft:stone", "石头");
		add("minecraft:granite", "花岗岩");
		add("minecraft:polished_granite", "磨制花岗岩");
		add("minecraft:diorite", "闪长岩");
		add("minecraft:polished_diorite", "磨制闪长岩");
		add("minecraft:andesite", "安山岩");
		add("minecraft:polished_andesite", "磨制安山岩");
		add("minecraft:grass_block", "草方块");
		add("minecraft:dirt", "泥土");
		add("minecraft:cobblestone", "圆石");
		add("minecraft:oak_planks", "橡木木板");
		add("minecraft:glass", "玻璃");
		add("minecraft:stone_bricks", "石砖");
		add("minecraft:gold_block", "金块");
		add("minecraft:iron_block", "铁块");
		add("minecraft:diamond_block", "钻石块");
		add("minecraft:lapis_block", "青金石块");
		add("minecraft:blue_ice", "蓝冰");
		add("minecraft:packed_ice", "浮冰");
		add("minecraft:ice", "冰");
		add("minecraft:magma_block", "岩浆块");
		add("minecraft:clay_ball", "粘土");
		add("minecraft:white_wool", "白色羊毛");
		add("minecraft:tnt", "TNT");
		add("minecraft:soul_sand", "灵魂沙");
		add("minecraft:scaffolding", "脚手架");
		add("minecraft:sponge", "海绵");
		add("minecraft:glowstone", "萤石");
		add("minecraft:sea_lantern", "海晶灯");
		add("minecraft:redstone_torch", "红石火把");
		add("minecraft:redstone_block", "红石块");
		add("minecraft:ghast_tear", "恶魂之泪");
		add("minecraft:nether_star", "下界之星");
		add("minecraft:dragon_egg", "龙蛋");
		add("minecraft:dragon_head", "龙首");
		add("minecraft:creeper_head", "苦力怕头颅");
		add("minecraft:zombie_head", "僵尸头颅");
		add("minecraft:skeleton_skull", "骷髅头颅");
		add("minecraft:wither_skeleton_skull", "凋零骷髅头颅");
		add("minecraft:nautilus_shell", "鹦鹉螺壳");
		add("minecraft:heart_of_the_sea", "海洋之心");
		add("minecraft:enchanted_golden_apple", "附魔金苹果");
		add("dropper_shop:wrench", "扳手");
		add("minecraft:trident", "三叉戟");
		add("minecraft:bow", "弓");
		add("minecraft:iron_axe", "铁斧");
		add("minecraft:diamond_axe", "钻斧头");
		add("minecraft:iron_pickaxe", "铁镐");
		add("minecraft:diamond_pickaxe", "钻石镐");
		add("minecraft:diamond_sword", "钻石剑");
		add("minecraft:iron_sword", "铁剑");
		add("minecraft:iron_leggings", "铁护腿");
		add("minecraft:iron_chestplate", "铁胸甲");
		add("minecraft:iron_boots", "铁鞋子");
		add("minecraft:diamond_leggings", "钻石护腿");
		add("minecraft:diamond_chestplate", "钻石胸甲");
		add("minecraft:diamond_boots", "钻石鞋子");
		add("minecraft:chainmail_leggings", "锁链护腿");
		add("minecraft:chainmail_chestplate", "锁链胸甲");
		add("minecraft:chainmail_boots", "锁链鞋子");
		add("minecraft:fishing_rod", "钓鱼竿");
		add("minecraft:flint_and_steel", "打火石");
		add("minecraft:bucket", "铁桶");
		add("minecraft:charcoal", "木炭");
		add("minecraft:coal", "煤炭");
		add("minecraft:iron_ingot", "铁锭");
		add("minecraft:emerald", "绿宝石");
		add("minecraft:slime_ball", "粘液球");
		add("minecraft:gunpowder", "火药");
		add("minecraft:blaze_rod", "烈焰棒");
		add("minecraft:dragon_breath", "龙息");
		add("minecraft:diamond_horse_armor", "钻石马铠");
		add("minecraft:golden_horse_armor", "金马铠");
		add("minecraft:totem_of_undying", "不死图腾");
		add("minecraft:saddle", "鞍");
		add("minecraft:ink_sac", "墨囊");
		add("minecraft:end_rod", "末地烛");
		add("minecraft:name_tag", "命名牌");
		add("minecraft:experience_bottle", "附魔之瓶");
		add("minecraft:bookshelf", "书架");
		add("minecraft:shulker_shell", "潜影壳");
		add("minecraft:panda_spawn_egg", "熊猫刷怪蛋");
		add("minecraft:turtle_helmet", "海龟壳");
		add("minecraft:sticky_piston", "粘性活塞");
		add(Material.DISPENSER, "发射器");
		add(Material.SPIDER_EYE, "蜘蛛眼");
		add(Material.PHANTOM_MEMBRANE, "幻翼膜");
	}

	private static void add(Material type, String trans_name) {
		add(type.getKey().toString(), trans_name);
	}

	private static void add(String full_name, String trans_name) {
		name.put(full_name, trans_name);
	}

	public static String get_item_name(ItemStack item) {
		String full_name = Material_ext.get_full_name(item);
		String result = name.get(full_name);
		if (result == null) {
			return full_name;
		} else {
			return result;
		}
	}
}
