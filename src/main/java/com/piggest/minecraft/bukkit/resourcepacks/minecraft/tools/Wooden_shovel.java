package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Wooden_shovel extends Vanilla_model {
	public Wooden_shovel() {
		super(Material.WOODEN_SHOVEL);
		this.parent = "item/handheld";
	}
}
