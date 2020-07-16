package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.teleport_machine.Base_material;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_composition;
import org.bukkit.inventory.ItemStack;

public enum Gas implements Chemical {
	hydrogen("氢气", Base_material.hygrogen_molecule), oxygen("氧气", Base_material.oxygen_molecule),
	nitrogen("氮气", Base_material.nitrogen_molecule), chlorine("氯气", Base_material.chlorine_molecule),
	CO("一氧化碳", Base_material.CO_molecule), CO2("二氧化碳", Base_material.CO2_molecule),
	CH4("甲烷", Base_material.CH4_molecule), NH3("氨气", Base_material.NH3_molecule), NO("一氧化氮", Base_material.NO_molecule),
	NO2("二氧化氮", Base_material.NO2_molecule), SO2("二氧化硫", Base_material.SO2_molecule),
	H2S("硫化氢", Base_material.H2S_molecule), HCl("氯化氢", Base_material.HCl_molecule),
	HCN("氰化氢", Base_material.HCN_molecule), HF("氟化氢", Base_material.HF_molecule),
	water_vapor("水蒸气", Base_material.water_molecule), Ar("氩气", Base_material.hygrogen_molecule);
	private String display_name;
	private Base_material composition;

	Gas(String display_name, Base_material base_material) {
		this.display_name = display_name;
		this.composition = base_material;
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

	@Override
	public Elements_composition get_elements_composition() {
		return this.composition.get_elements_composition();
	}

}
