package com.piggest.minecraft.bukkit.advanced_furnace;

public interface Chemical {

	public String get_displayname();

	public String get_name();
	
	public static Chemical get_chemical(String name) {
		if (name.contains("_LOG")) {
			name = "LOG";
		}
		Solid solid = Solid.get_solid(name);
		if (solid != null) {
			return solid;
		}
		Gas gas = Gas.get_gas(name);
		if (gas != null) {
			return gas;
		}
		return null;
	}
}
