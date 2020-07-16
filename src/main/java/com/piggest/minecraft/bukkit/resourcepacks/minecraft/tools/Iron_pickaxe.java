package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Iron_pickaxe extends Vanilla_model {
	public Iron_pickaxe() {
		super(Material.IRON_PICKAXE);
		this.parent = "item/handheld";
	}
}
