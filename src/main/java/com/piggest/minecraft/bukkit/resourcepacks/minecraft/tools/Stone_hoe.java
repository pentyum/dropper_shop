package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Stone_hoe extends Vanilla_model {
	public Stone_hoe() {
		super(Material.STONE_HOE);
		this.parent = "item/handheld";
	}
}
