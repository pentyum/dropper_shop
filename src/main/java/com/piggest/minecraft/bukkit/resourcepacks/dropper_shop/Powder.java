package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Powder extends Model {
	public Powder(String material) {
		super(new Textures("dropper_shop:powder/" + material));
	}
}
