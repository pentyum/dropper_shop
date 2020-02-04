package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Tool extends Model {
	public Tool(String material) {
		super(new Textures("dropper_shop:tools/" + material));
	}
}
