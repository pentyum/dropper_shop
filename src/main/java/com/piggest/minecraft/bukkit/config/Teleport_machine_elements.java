package com.piggest.minecraft.bukkit.config;

import java.util.HashMap;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.teleport_machine.Element;
import com.piggest.minecraft.bukkit.teleport_machine.Element_composition;

public class Teleport_machine_elements {
	private HashMap<String, Element_composition> element_map = new HashMap<String, Element_composition>();

	public void init() {
		this.element_map.put(Material.STONE.name(), new Element_composition(new Element[] {}, new int[] {}));
	}
}
