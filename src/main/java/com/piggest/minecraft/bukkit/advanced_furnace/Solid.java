package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public enum Solid implements Chemical {
	IRON_ORE(1000, "铁矿石"), iron_powder(1000, "铁粉"), IRON_INGOT(1000, "铁锭"), GOLD_ORE(1000, "金矿石"),
	gold_powder(1000, "金粉"), GOLD_INGOT(1000, "金锭"), LAPIS_ORE(1000, "青金石矿石"), lapis_powder(1000, "青金石粉"),
	LAPIS_LAZULI(1000, "青金石"), LOG(1000, "原木"), CHARCOAL(1000, "木炭"), COAL(1000, "煤炭"), COBBLESTONE(1000, "圆石"),
	STONE(1000, "石头"), GLASS(1000, "玻璃"), SAND(1000, "沙子"), CLAY(1000, "粘土块"), TERRACOTTA(1000, "陶瓦"),
	CLAY_BALL(1000, "粘土"), BRICK(1000, "红砖"), NETHERRACK(1000, "地狱岩"), NETHER_BRICK(1000, "地狱砖"),
	copper_powder(1000, "铜粉"), sliver_powder(1000, "银粉"), GRAVEL(1000, "砂砾"), FLINT(1000, "燧石"), QUARTZ(1000, "石英"),
	OBSIDIAN(1000, "黑曜石"), REDSTONE(1000, "红石"), aluminium_oxide(1000, "三氧化二铝"),;

	private int unit;
	private String display_name;

	Solid(int unit, String display_name) {
		this.unit = unit;
		this.display_name = display_name;
	}

	public static Solid get_solid(ItemStack itemstack) {
		String name = Material_ext.get_id_name(itemstack);
		if (name.contains("_LOG")) {
			return LOG;
		}
		return Solid.get_solid(name);
	}

	public int get_unit() {
		return this.unit;
	}

	public ItemStack get_item_stack() {
		return Material_ext.new_item(this.name(), 1);
	}

	public String get_displayname() {
		return this.display_name + "(s)";
	}

	public String get_name() {
		return this.name();
	}

	public static Solid get_solid(String name) {
		Solid solid = null;
		try {
			solid = Solid.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return solid;
	}
}
