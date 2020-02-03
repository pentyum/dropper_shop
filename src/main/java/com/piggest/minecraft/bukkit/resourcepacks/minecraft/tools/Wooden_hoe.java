package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Wooden_hoe extends Vanilla_model {
	public Wooden_hoe() {
		super(Material.WOODEN_HOE);
		this.parent = "item/handheld";
	}
}
