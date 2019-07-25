package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Powder;

public enum Fuel {
	coal(2, Status.solid, 1600), charcoal(1.4, Status.solid, 1600), coal_block(1.9, Status.solid, 16000),
	blaze_rod(2.2, Status.solid, 2400), suger(1.05, Status.solid, 300), coal_powder(2.5, Status.solid, 1600),
	lava(1.6, Status.liquid, 20), gun_powder(12, Status.solid, 40), wool(1.1, Status.solid, 100),
	carpet(1.1, Status.solid, 67), planks(1.38, Status.solid, 300), wheat_powder(9, Status.solid, 40),
	stick(2.1, Status.solid, 100);

	public final Status status;
	public final double power;
	public final int ticks;

	private Fuel(double power, Status status, int ticks) {
		this.power = power;
		this.ticks = ticks;
		this.status = status;
	}

	public int get_liquid_ticks(ItemStack item, int unit) {
		return this.ticks * Liquid.get_item_unit(item);
	}

	public static Fuel get_fuel(ItemStack item) {
		Material type = item.getType();
		switch (type) {
		case COAL:
			return Fuel.coal;
		case CHARCOAL:
			return Fuel.charcoal;
		case COAL_BLOCK:
			return Fuel.coal_block;
		case BLAZE_ROD:
			return Fuel.blaze_rod;
		case LAVA_BUCKET:
			return Fuel.lava;
		case GUNPOWDER:
			return Fuel.gun_powder;
		case STICK:
			return Fuel.stick;
		case SUGAR:
			if (item.hasItemMeta()) {
				if (Powder.is_powder(item, "coal_powder")) {
					return Fuel.coal_powder;
				} else if (Powder.is_powder(item, "wheat_powder")) {
					return Fuel.wheat_powder;
				} else {
					return null;
				}
			} else {
				return suger;
			}
		default:
			if (Tag.WOOL.isTagged(type)) {
				return Fuel.wool;
			} else if (Tag.CARPETS.isTagged(type)) {
				return Fuel.carpet;
			} else if (Tag.PLANKS.isTagged(type)) {
				return Fuel.planks;
			}
			return null;
		}
	}
}
