package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Powder;

public enum Fuel {
	coal(2, Status.solid, 1600), charcoal(1.8, Status.solid, 1600), coal_block(1.9, Status.solid, 16000),
	blaze_rod(2.25, Status.solid, 2400), suger(1.1, Status.solid, 300), coal_powder(2.75, Status.solid, 1600),
	lava_bucket(1.7, Status.liquid, 20000), gun_powder(12, Status.solid, 40), wool(1.2, Status.solid, 100),
	carpet(1.2, Status.solid, 67), planks(1.4, Status.solid, 300), wheat_powder(9, Status.solid, 40),
	stick(2.5, Status.solid, 100);

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
		if (item.getType() == Material.COAL) {
			return coal;
		} else if (item.getType() == Material.CHARCOAL) {
			return charcoal;
		} else if (item.getType() == Material.COAL_BLOCK) {
			return coal_block;
		} else if (item.getType() == Material.BLAZE_ROD) {
			return blaze_rod;
		} else if (item.getType() == Material.LAVA_BUCKET) {
			return lava_bucket;
		} else if (item.getType() == Material.GUNPOWDER) {
			return gun_powder;
		} else if (item.getType() == Material.STICK) {
			return stick;
		} else if (item.getType() == Material.SUGAR) {
			if (Powder.is_powder(item, "coal_powder")) {
				return coal_powder;
			} else if (Powder.is_powder(item, "wheat_powder")) {
				return wheat_powder;
			} else {
				return suger;
			}
		} else if (item.getType().name().contains("WOOL")) {
			return wool;
		} else if (item.getType().name().contains("CARPET")) {
			return carpet;
		} else if (item.getType().name().contains("PLANKS")) {
			return planks;
		}
		return null;
	}
}
