package com.piggest.minecraft.bukkit.resourcepacks.dropper_shop;

import java.util.HashMap;

public class Element {
	public String parent = "item/generated";
	public HashMap<String, String> textures = new HashMap<String, String>();

	public Element(int atomic_number) {
		this.textures.put("layer0", "dropper_shop:elements/element_" + atomic_number);
	}
}
