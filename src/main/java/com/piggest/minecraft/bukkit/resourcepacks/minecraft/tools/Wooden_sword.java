package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;

public class Wooden_sword extends Vanilla_model {
	public Wooden_sword() {
		super(Material.WOODEN_SWORD);
		this.parent = "item/handheld";
	}
}
