package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Material_composition {
	public static final HashMap<String,HashMap<Element,Integer>> map = new HashMap<String,HashMap<Element,Integer>>();
	
	public static void init() {
		HashMap<Element, Integer> compositon;
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.STONE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.COBBLESTONE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.ANDESITE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.DIORITE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.GRANITE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.OBSIDIAN.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.DIRT.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.Si, 200);
		compositon.put(Element.Al, 200);
		compositon.put(Element.Ca, 200);
		compositon.put(Element.Mg, 200);
		compositon.put(Element.O, 200);
		map.put(Material.GRASS_BLOCK.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.H, 200);
		compositon.put(Element.O, 200);
		map.put(Material.ICE.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.H, 200);
		compositon.put(Element.O, 200);
		map.put(Material.SNOW_BLOCK.getKey().getKey(), compositon);
		
		compositon = new HashMap<Element,Integer>();
		compositon.put(Element.H, 200);
		compositon.put(Element.O, 200);
		map.put(Material.WATER.getKey().getKey(), compositon);
	}
	
	public static HashMap<Element,Integer> get_elements(ItemStack item) {
		HashMap<Element, Integer> result = map.get(Material_ext.get_id_name(item));
		if(result!=null) {
			return result;
		}
		return null;
	}
}
