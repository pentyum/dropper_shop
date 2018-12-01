package com.piggest.minecraft.bukkit.advanced_furnace;

public interface Chemical {

	public String get_displayname();

	public String get_name();

	public static Chemical get_chemical(String name) {
		if (name.contains("_LOG")) {
			name = "LOG";
		}
		Solid solid = Solid.valueOf(name);
		if (solid != null) {
			return solid;
		}
		Gas gas = Gas.valueOf(name);
		if (gas != null) {
			return gas;
		}
		return null;
	}
}
