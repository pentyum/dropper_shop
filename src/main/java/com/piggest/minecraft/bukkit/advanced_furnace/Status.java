package com.piggest.minecraft.bukkit.advanced_furnace;

public enum Status {
	gas("气态"), liquid("液体"), solid("固态"), plasma("等离子态");

	public final String display_name;

	Status(String display_name) {
		this.display_name = display_name;
	}
}
