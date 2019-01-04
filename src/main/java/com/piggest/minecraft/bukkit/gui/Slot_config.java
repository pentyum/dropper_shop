package com.piggest.minecraft.bukkit.gui;

import org.bukkit.Material;

public class Slot_config {
	public Material material;
	public String name;
	public Gui_slot_type type;

	public Slot_config(Material material, String name, Gui_slot_type type) {
		this.material = material;
		this.name = name;
		this.type = type;
	}
}
