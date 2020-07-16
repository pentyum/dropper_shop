package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Wooden_pickaxe extends Vanilla_model {
	public Wooden_pickaxe() {
		super(Material.WOODEN_PICKAXE);
		this.parent = "item/handheld";
	}
}
