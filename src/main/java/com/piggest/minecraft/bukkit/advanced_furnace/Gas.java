package com.piggest.minecraft.bukkit.advanced_furnace;

public enum Gas implements Chemical {
	hydrogen, oxygen, nitrogen, chlorine, CO, CO2, CH4, NH3, NO, NO2, SO2, H2S, HCl, HCN, HF, water,;

	public String get_displayname() {
		return this.name() + "(g)";
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

}
