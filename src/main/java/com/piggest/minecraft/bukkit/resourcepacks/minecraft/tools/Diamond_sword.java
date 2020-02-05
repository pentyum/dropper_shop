package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Diamond_sword extends Vanilla_model {
	public Diamond_sword() {
		super(Material.DIAMOND_SWORD);
		this.parent = "item/handheld";
	}
}
