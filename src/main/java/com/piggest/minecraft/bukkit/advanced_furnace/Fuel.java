package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Powder;

public enum Fuel {
	coal(2, Status.solid, 1600), charcoal(1.4, Status.solid, 1600), coal_block(1.9, Status.solid, 16000),
	blaze_rod(2.25, Status.solid, 2400), suger(1.05, Status.solid, 300), coal_powder(2.75, Status.solid, 1600),
	lava_bucket(1.7, Status.liquid, 20000), gun_powder(12, Status.solid, 40), wool(1.1, Status.solid, 100),
	carpet(1.1, Status.solid, 67), planks(1.38, Status.solid, 300), wheat_powder(9, Status.solid, 40),
	stick(2.1, Status.solid, 100);

	private Status status;
	private double power;
	private int ticks;

	private Fuel(double power, Status status, int ticks) {
		this.power = power;
		this.ticks = ticks;
		this.status = status;
	}

	public double get_power() {
		return this.power;
	}

	public Status get_status() {
		return this.status;
	}

	public int get_ticks() {
		return this.ticks;
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
			return Fuel.lava_bucket;
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
			if (type.name().contains("WOOL")) {
				return Fuel.wool;
			} else if (type.name().contains("CARPET")) {
				return Fuel.carpet;
			} else if (type.name().contains("PLANKS")) {
				return Fuel.planks;
			}
			return null;
		}
	}
}
