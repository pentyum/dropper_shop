package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Diamond_hoe extends Vanilla_model {
	public Diamond_hoe() {
		super(Material.DIAMOND_HOE);
		this.parent = "item/handheld";
	}
}
