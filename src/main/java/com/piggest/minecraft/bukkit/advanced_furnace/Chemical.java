package com.piggest.minecraft.bukkit.advanced_furnace;

public interface Chemical {

	public String get_displayname();

	public String get_name();
		
	public static Chemical get_chemical(String name) {
		if (name.contains("_log")) {
			name = "log";
		}
		Solid solid = Solid.get_solid(name);
		if (solid != null) {
			return solid;
		}
		Gas gas = Gas.get_gas(name);
		if (gas != null) {
			return gas;
		}
		Liquid liquid = Liquid.get_liquid(name);
		if (liquid != null) {
			return liquid;
		}
		return null;
	}
}
