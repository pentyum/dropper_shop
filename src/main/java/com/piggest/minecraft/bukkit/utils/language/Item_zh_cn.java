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
		name.put("minecraft:stone_bricks", "石砖");
		name.put("minecraft:gold_block", "金块");
		name.put("minecraft:iron_block", "铁块");
		name.put("minecraft:diamond_block", "钻石块");
		name.put("minecraft:lapis_block", "青金石块");
		name.put("minecraft:blue_ice", "蓝冰");
		name.put("minecraft:packed_ice", "浮冰");
		name.put("minecraft:ice", "冰");
		name.put("minecraft:magma_block", "岩浆块");
		name.put("minecraft:clay_ball", "粘土");
		name.put("minecraft:white_wool", "白色羊毛");
		name.put("minecraft:tnt", "TNT");
		name.put("minecraft:soul_sand", "灵魂沙");
		name.put("minecraft:scaffolding", "脚手架");
		name.put("minecraft:sponge", "海绵");
		name.put("minecraft:glowstone", "萤石");
		name.put("minecraft:sea_lantern", "海晶灯");
		name.put("minecraft:redstone_torch", "红石火把");
		name.put("minecraft:redstone_block", "红石块");
		name.put("minecraft:ghast_tear", "恶魂之泪");
		name.put("minecraft:nether_star", "下界之星");
		name.put("minecraft:dragon_egg", "龙蛋");
		name.put("minecraft:dragon_head", "龙首");
		name.put("minecraft:creeper_head", "苦力怕头颅");
		name.put("minecraft:zombie_head", "僵尸头颅");
		name.put("minecraft:skeleton_skull", "骷髅头颅");
		name.put("minecraft:wither_skeleton_skull", "凋零骷髅头颅");
		name.put("minecraft:nautilus_shell", "鹦鹉螺壳");
		name.put("minecraft:heart_of_the_sea", "海洋之心");
		name.put("minecraft:enchanted_golden_apple", "附魔金苹果");
		name.put("dropper_shop:wrench", "扳手");
		name.put("minecraft:trident", "三叉戟");
		name.put("minecraft:bow", "弓");
		name.put("minecraft:iron_axe", "铁斧");
		name.put("minecraft:diamond_axe", "钻斧头");
		name.put("minecraft:iron_pickaxe", "铁镐");
		name.put("minecraft:diamond_pickaxe", "钻石镐");
		name.put("minecraft:diamond_sword", "钻石剑");
		name.put("minecraft:iron_sword", "铁剑");
		name.put("minecraft:iron_leggings", "铁护腿");
		name.put("minecraft:iron_chestplate", "铁胸甲");
		name.put("minecraft:iron_boots", "铁鞋子");
		name.put("minecraft:diamond_leggings", "钻石护腿");
		name.put("minecraft:diamond_chestplate", "钻石胸甲");
		name.put("minecraft:diamond_boots", "钻石鞋子");
		name.put("minecraft:chainmail_leggings", "锁链护腿");
		name.put("minecraft:chainmail_chestplate", "锁链胸甲");
		name.put("minecraft:chainmail_boots", "锁链鞋子");
		name.put("minecraft:fishing_rod", "钓鱼竿");
		name.put("minecraft:flint_and_steel", "打火石");
		name.put("minecraft:bucket", "铁桶");
		name.put("minecraft:charcoal", "木炭");
		name.put("minecraft:coal", "煤炭");
		name.put("minecraft:iron_ingot", "铁锭");
		name.put("minecraft:emerald", "绿宝石");
		name.put("minecraft:slime_ball", "粘液球");
		name.put("minecraft:gunpowder", "火药");
		name.put("minecraft:blaze_rod", "烈焰棒");
		name.put("minecraft:dragon_breath", "龙息");
		name.put("minecraft:diamond_horse_armor", "钻石马铠");
		name.put("minecraft:golden_horse_armor", "金马铠");
		name.put("minecraft:totem_of_undying", "不死图腾");
		name.put("minecraft:saddle", "鞍");
		name.put("minecraft:ink_sac", "墨囊");
		name.put("minecraft:end_rod", "末地烛");
		name.put("minecraft:name_tag", "命名牌");
		name.put("minecraft:experience_bottle", "附魔之瓶");
		name.put("minecraft:bookshelf", "书架");
		name.put("minecraft:shulker_shell", "潜影壳");
		name.put("minecraft:panda_spawn_egg", "熊猫刷怪蛋");
		name.put("minecraft:turtle_helmet", "海龟壳");
		name.put("minecraft:sticky_piston", "粘性活塞");
		name.put("minecraft:dispenser", "发射器");
		name.put("minecraft:spider_eye", "蜘蛛眼");
		name.put("minecraft:phantom_membrane", "幻翼膜");
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
