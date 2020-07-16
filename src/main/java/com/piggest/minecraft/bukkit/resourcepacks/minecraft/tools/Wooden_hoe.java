package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Wooden_hoe extends Vanilla_model {
	public Wooden_hoe() {
		super(Material.WOODEN_HOE);
		this.parent = "item/handheld";
	}
}
