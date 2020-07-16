package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Iron_shovel extends Vanilla_model {
	public Iron_shovel() {
		super(Material.IRON_SHOVEL);
		this.parent = "item/handheld";
	}
}
