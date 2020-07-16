package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Iron_axe extends Vanilla_model {
	public Iron_axe() {
		super(Material.IRON_AXE);
		this.parent = "item/handheld";
	}
}
