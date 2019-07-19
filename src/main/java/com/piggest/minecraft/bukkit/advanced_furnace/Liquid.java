package com.piggest.minecraft.bukkit.advanced_furnace;

public enum Liquid implements Chemical {
	water("水");

	private String display_name;

	Liquid(String display_name) {
		this.display_name = display_name;
	}

	@Override
	public String get_displayname() {
		return this.display_name + "(l)";
	}

	@Override
	public String get_name() {
		return this.name();
	}
	
	public static Liquid get_liquid(String name) {
		Liquid liquid = null;
		try {
			liquid = Liquid.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return liquid;
	}
}
