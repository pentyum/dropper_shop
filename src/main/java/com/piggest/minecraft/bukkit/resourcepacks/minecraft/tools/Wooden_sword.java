package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Wooden_sword extends Vanilla_model {
	public Wooden_sword() {
		super(Material.WOODEN_SWORD);
		this.parent = "item/handheld";
	}
}
