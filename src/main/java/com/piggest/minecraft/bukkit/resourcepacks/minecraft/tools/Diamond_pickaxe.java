package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Diamond_pickaxe extends Vanilla_model {
	public Diamond_pickaxe() {
		super(Material.DIAMOND_PICKAXE);
		this.parent = "item/handheld";
	}
}
