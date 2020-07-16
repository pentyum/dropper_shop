package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Diamond_sword extends Vanilla_model {
	public Diamond_sword() {
		super(Material.DIAMOND_SWORD);
		this.parent = "item/handheld";
	}
}
