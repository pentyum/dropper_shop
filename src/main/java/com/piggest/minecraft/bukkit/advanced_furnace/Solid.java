package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public enum Solid implements Chemical {
	IRON_ORE(1000), iron_powder(1000), IRON_INGOT(1000), GOLD_ORE(1000), gold_powder(1000), GOLD_INGOT(1000),
	LAPIS_ORE(1000), lapis_powder(1000), LAPIS_LAZULI(1000), LOG(1000), CHARCOAL(1000), COBBLESTONE(1000), STONE(1000),
	GLASS(1000), SAND(1000), CLAY(1000), TERRACOTTA(1000), CLAY_BALL(1000), BRICK(1000), NETHERRACK(1000),
	NETHER_BRICK(1000), copper_powder(1000), sliver_powder(1000), GRAVEL(1000), FLINT(1000), QUARTZ(1000),
	OBSIDIAN(1000), REDSTONE(1000), aluminium_oxide(1000);

	private int unit;

	Solid(int unit) {
		this.unit = unit;
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
		return this.name() + "(s)";
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
