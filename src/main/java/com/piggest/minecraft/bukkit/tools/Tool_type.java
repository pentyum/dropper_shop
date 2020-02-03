package com.piggest.minecraft.bukkit.tools;

public enum Tool_type {
	PICKAXE("镐"), AXE("斧"), HOE("锄"), SHOVEL("锹"), SWORD("剑");
	private String name;

	Tool_type(String name) {
		this.name = name;
	}

	public String get_display_name() {
		return this.name;
	}
}
