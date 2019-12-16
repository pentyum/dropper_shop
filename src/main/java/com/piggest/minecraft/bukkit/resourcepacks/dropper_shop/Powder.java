package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Powder extends Model {
	public String parent = "item/generated";
	public Textures textures = null;

	public Powder(String material) {
		this.textures = new Textures("dropper_shop:powder/" + material);
	}
}
