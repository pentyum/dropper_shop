package com.piggest.minecraft.bukkit.gui;

import org.bukkit.Material;

public class Slot_config {
	public Material material;
	public String name;
	public Gui_slot_type type;
	public String[] lore = null;
	public String full_name = null;

	public Slot_config(String full_name, String name, Gui_slot_type type) {
		this.full_name = full_name;
		this.name = name;
		this.type = type;
	}

	public Slot_config(Material material, String name, Gui_slot_type type) {
		this.material = material;
		this.name = name;
		this.type = type;
	}

	public Slot_config(Material material, String name, String[] lore, Gui_slot_type type) {
		this.material = material;
		this.name = name;
		this.type = type;
		this.lore = lore.clone();
	}

	@Override
	public Slot_config clone() {
		return new Slot_config(this.material, this.name, this.lore, this.type);
	}
}
