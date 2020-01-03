package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Powder;

public enum Fuel {
	coal(1.95, Status.solid, 1600), charcoal(1.15, Status.solid, 1600), coal_block(1.9, Status.solid, 16000),
	blaze_rod(2.1, Status.solid, 2400), sugar(1.05, Status.solid, 300), coal_powder(2.5, Status.solid, 1600),
	lava(1.6, Status.liquid, 20), gun_powder(12, Status.solid, 40), wool(0.9, Status.solid, 100),
	carpet(0.9, Status.solid, 67), planks(1.1, Status.solid, 300), flour_powder(9, Status.solid, 40),
	stick(1.55, Status.solid, 100), hydrogen(2.5, Status.gas, 4), CO(1.9, Status.gas, 4), CH4(2.6, Status.gas, 4),
	dried_kelp_block(1.4, Status.solid, 4000);

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

	public Chemical to_chemical() {
		if (this.status == Status.solid) {
			return Solid.get_solid(this.name());
		}
		if (this.status == Status.liquid) {
			return Liquid.get_liquid(this.name());
		}
		if (this.status == Status.gas) {
			return Gas.get_gas(this.name());
		}
		return null;
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
		case DRIED_KELP_BLOCK:
			return dried_kelp_block;
		case STICK:
			return Fuel.stick;
		case SUGAR:
			if (item.hasItemMeta()) {
				if (Powder.is_powder(item, "coal_powder")) {
					return Fuel.coal_powder;
				} else if (Powder.is_powder(item, "flour_powder")) {
					return Fuel.flour_powder;
				} else {
					return null;
				}
			} else {
				return sugar;
			}
		case GLASS_BOTTLE:
			if (Gas_bottle.is_gas_bottle(item)) {
				Set<Gas> contents_type = Gas_bottle.get_gas_map(item).keySet();
				for (Gas gas : contents_type) {
					switch (gas) {
					case hydrogen:
						return Fuel.hydrogen;
					case CO:
						return Fuel.CO;
					case CH4:
						return Fuel.CH4;
					default:
						return null;
					}
				}
				return null;
			} else {
				return null;
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
