package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Stone_sword extends Vanilla_model {
	public Stone_sword() {
		super(Material.STONE_SWORD);
		this.parent = "item/handheld";
	}
}
