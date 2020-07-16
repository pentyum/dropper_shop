package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Wooden_axe extends Vanilla_model {
	public Wooden_axe() {
		super(Material.WOODEN_AXE);
		this.parent = "item/handheld";
	}
}
