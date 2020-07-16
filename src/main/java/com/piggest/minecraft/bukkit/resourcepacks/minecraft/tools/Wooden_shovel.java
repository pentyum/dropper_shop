package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Wooden_shovel extends Vanilla_model {
	public Wooden_shovel() {
		super(Material.WOODEN_SHOVEL);
		this.parent = "item/handheld";
	}
}
