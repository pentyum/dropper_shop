package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Iron_sword extends Vanilla_model {
	public Iron_sword() {
		super(Material.IRON_SWORD);
		this.parent = "item/handheld";
	}
}
