package com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools;

import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import org.bukkit.Material;

public class Stone_shovel extends Vanilla_model {
	public Stone_shovel() {
		super(Material.STONE_SHOVEL);
		this.parent = "item/handheld";
	}
}
