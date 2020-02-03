package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Iron_hoe extends Vanilla_model {
	public Iron_hoe() {
		super(Material.IRON_HOE);
		this.parent = "item/handheld";
	}
}
