package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

public enum Gas implements Chemical {
	hydrogen("氢气"), oxygen("氧气"), nitrogen("氮气"), chlorine("氯气"), CO("一氧化碳"), CO2("二氧化碳"), CH4("甲烷"), NH3("氨气"),
	NO("一氧化氮"), NO2("二氧化氮"), SO2("二氧化硫"), H2S("硫化氢"), HCl("氯化氢"), HCN("氰化氢"), HF("氟化氢"), water_vapor("水蒸气"), Ar("氩气");
	private String display_name;

	Gas(String display_name) {
		this.display_name = display_name;
	}

	Gas() {
		this.display_name = this.name();
	}

	public String get_displayname() {
		return this.display_name + "(g)";
	}

	public String get_name() {
		return this.name();
	}

	public static Gas get_gas(String name) {
		Gas gas = null;
		try {
			gas = Gas.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return gas;
	}

	public static int get_item_unit(ItemStack fuel_item, Gas gastype) {
		if (Gas_bottle.is_gas_bottle(fuel_item)) {
			return Gas_bottle.get_contents(fuel_item, gastype);
		}
		return 0;
	}

}
