package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Diamond_shovel extends Vanilla_model {
	public Diamond_shovel() {
		super(Material.DIAMOND_SHOVEL);
		this.parent = "item/handheld";
	}
}
