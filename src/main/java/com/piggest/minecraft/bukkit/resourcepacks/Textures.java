package com.piggest.minecraft.bukkit.resourcepacks;

import org.bukkit.Material;

public class Textures {
	public String layer0;

	public Textures(String layer0) {
		this.layer0 = layer0;
	}

	public Textures(Material material) {
		if (material.isItem()) {
			this.layer0 = "item/" + material.name().toLowerCase();
		}
	}
}
