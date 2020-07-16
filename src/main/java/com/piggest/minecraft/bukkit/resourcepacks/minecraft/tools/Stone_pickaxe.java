package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Stone_pickaxe extends Vanilla_model {
	public Stone_pickaxe() {
		super(Material.STONE_PICKAXE);
		this.parent = "item/handheld";
	}
}
