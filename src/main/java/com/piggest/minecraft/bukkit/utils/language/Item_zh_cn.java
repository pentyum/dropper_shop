package com.piggest.minecraft.bukkit.utils.language;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Item_zh_cn {
	public static final HashMap<String, String> name = new HashMap<>();

	public static void init() {
		add(Material.STONE, "石头");
		add(Material.GRANITE, "花岗岩");
		add(Material.POLISHED_GRANITE, "磨制花岗岩");
		add(Material.DIORITE, "闪长岩");
		add(Material.POLISHED_DIORITE, "磨制闪长岩");
		add(Material.ANDESITE, "安山岩");
		add(Material.POLISHED_ANDESITE, "磨制安山岩");
		add(Material.GRASS_BLOCK, "草方块");
		add(Material.DIRT, "泥土");
		add(Material.COBBLESTONE, "圆石");
		add(Material.OAK_PLANKS, "橡木木板");
		add(Material.GLASS, "玻璃");
		add(Material.STONE_BRICKS, "石砖");
		add(Material.GOLD_BLOCK, "金块");
		add(Material.IRON_BLOCK, "铁块");
		add(Material.DIAMOND_BLOCK, "钻石块");
		add(Material.LAPIS_BLOCK, "青金石块");
		add(Material.BLUE_ICE, "蓝冰");
		add(Material.PACKED_ICE, "浮冰");
		add(Material.ICE, "冰");
		add(Material.MAGMA_BLOCK, "岩浆块");
		add(Material.CLAY_BALL, "粘土");
		add(Material.WHITE_WOOL, "白色羊毛");
		add(Material.TNT, "TNT");
		add(Material.SOUL_SAND, "灵魂沙");
		add(Material.SCAFFOLDING, "脚手架");
		add(Material.SPONGE, "海绵");
		add(Material.GLOWSTONE, "萤石");
		add(Material.SEA_LANTERN, "海晶灯");
		add(Material.REDSTONE_TORCH, "红石火把");
		add(Material.REDSTONE_BLOCK, "红石块");
		add(Material.GHAST_TEAR, "恶魂之泪");
		add(Material.NETHER_STAR, "下界之星");
		add(Material.DRAGON_EGG, "龙蛋");
		add(Material.DRAGON_HEAD, "龙首");
		add(Material.CREEPER_HEAD, "苦力怕头颅");
		add(Material.ZOMBIE_HEAD, "僵尸头颅");
		add(Material.SKELETON_SKULL, "骷髅头颅");
		add(Material.WITHER_SKELETON_SKULL, "凋零骷髅头颅");
		add(Material.NAUTILUS_SHELL, "鹦鹉螺壳");
		add(Material.HEART_OF_THE_SEA, "海洋之心");
		add(Material.ENCHANTED_GOLDEN_APPLE, "附魔金苹果");
		add("dropper_shop:wrench", "扳手");
		add("dropper_shop:excalibur", "誓约胜利之剑");
		add(Material.TRIDENT, "三叉戟");
		add(Material.BOW, "弓");
		add(Material.CROSSBOW, "弩");
		add(Material.IRON_AXE, "铁斧");
		add(Material.DIAMOND_AXE, "钻石斧头");
		add(Material.IRON_PICKAXE, "铁镐");
		add(Material.DIAMOND_PICKAXE, "钻石镐");
		add(Material.DIAMOND_SWORD, "钻石剑");
		add(Material.IRON_SWORD, "铁剑");
		add(Material.IRON_LEGGINGS, "铁护腿");
		add(Material.IRON_CHESTPLATE, "铁胸甲");
		add(Material.IRON_BOOTS, "铁鞋子");
		add(Material.DIAMOND_LEGGINGS, "钻石护腿");
		add(Material.DIAMOND_CHESTPLATE, "钻石胸甲");
		add(Material.DIAMOND_BOOTS, "钻石鞋子");
		add(Material.CHAINMAIL_LEGGINGS, "锁链护腿");
		add(Material.CHAINMAIL_CHESTPLATE, "锁链胸甲");
		add(Material.CHAINMAIL_BOOTS, "锁链鞋子");
		add(Material.FISHING_ROD, "钓鱼竿");
		add(Material.FLINT_AND_STEEL, "打火石");
		add(Material.BUCKET, "铁桶");
		add(Material.CHARCOAL, "木炭");
		add(Material.COAL, "煤炭");
		add(Material.IRON_INGOT, "铁锭");
		add(Material.EMERALD, "绿宝石");
		add(Material.SLIME_BALL, "粘液球");
		add(Material.GUNPOWDER, "火药");
		add(Material.BLAZE_ROD, "烈焰棒");
		add(Material.DRAGON_BREATH, "龙息");
		add(Material.DIAMOND_HORSE_ARMOR, "钻石马铠");
		add(Material.GOLDEN_HORSE_ARMOR, "金马铠");
		add(Material.TOTEM_OF_UNDYING, "不死图腾");
		add(Material.SADDLE, "鞍");
		add(Material.INK_SAC, "墨囊");
		add(Material.END_ROD, "末地烛");
		add(Material.NAME_TAG, "命名牌");
		add(Material.EXPERIENCE_BOTTLE, "附魔之瓶");
		add(Material.BOOKSHELF, "书架");
		add(Material.SHULKER_SHELL, "潜影壳");
		add(Material.PANDA_SPAWN_EGG, "熊猫刷怪蛋");
		add(Material.TURTLE_HELMET, "海龟壳");
		add(Material.STICKY_PISTON, "粘性活塞");
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
