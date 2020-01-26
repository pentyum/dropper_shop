package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Ingot extends Model {
	public Ingot(String material) {
		super(new Textures("dropper_shop:ingots/" + material));
	}
}
