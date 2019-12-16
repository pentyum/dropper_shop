package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Element extends Model {
	public Element(int atomic_number) {
		super(new Textures("dropper_shop:elements/element_" + atomic_number));
	}
}
