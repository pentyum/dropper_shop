package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Element {
	public String parent = "item/generated";
	public Textures textures = null;

	public Element(int atomic_number) {
		this.textures = new Textures("dropper_shop:elements/element_" + atomic_number);
	}
}
