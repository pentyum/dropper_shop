package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Wooden_pickaxe extends Vanilla_model {
	public Wooden_pickaxe() {
		super(Material.WOODEN_PICKAXE);
		this.parent = "item/handheld";
	}
}
