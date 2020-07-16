package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Golden_axe extends Vanilla_model {
	public Golden_axe() {
		super(Material.GOLDEN_AXE);
		this.parent = "item/handheld";
	}
}
