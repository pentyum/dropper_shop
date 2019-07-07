package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.HashMap;

public class Element_composition extends HashMap<Element, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5070913379265201984L;

	public Element_composition(Element[] elements, int[] value) {
		super(elements.length);
		if (value.length != elements.length) {
			return;
		}
		int i;
		for (i = 0; i < elements.length; i++) {
			this.put(elements[i], value[i]);
		}
	}
}
