package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public enum Base_material implements Has_composition {
	water_molecule(new Has_composition[] {Element.H,Element.O},new int[] {2,1}),
	SiO2_molecule(new Has_composition[] {Element.Si,Element.O},new int[] {1,2}),
	CaO_molecule(new Has_composition[] {Element.Ca,Element.O},new int[] {1,1}),
	MgO_molecule(new Has_composition[] {Element.Mg,Element.O},new int[] {1,1}),
	CO2_molecule(new Has_composition[] {Element.C,Element.O},new int[] {1,2}),
	Al2O3_molecule(new Has_composition[] {Element.Al,Element.O},new int[] {2,3}),
	Fe2O3_molecule(new Has_composition[] {Element.Fe,Element.O},new int[] {2,3}),
	FeO_molecule(new Has_composition[] {Element.Fe,Element.O},new int[] {1,1}),
	NaCl_molecule(new Has_composition[] {Element.Na,Element.O},new int[] {1,1}),
	Na2O_molecule(new Has_composition[] {Element.Na,Element.O},new int[] {2,2}),
	K2O_molecule(new Has_composition[] {Element.K,Element.O},new int[] {2,1}),
	KCl_molecule(new Has_composition[] {Element.K,Element.Cl},new int[] {1,1}),
	KClO3_molecule(new Has_composition[] {Element.K,Element.Cl,Element.O},new int[] {1,1,3}),
	CaF2_molecule(new Has_composition[] {Element.Ca,Element.F},new int[] {1,2}),
	sucrose_molecule(new Has_composition[] {Element.C,Element.H,Element.O},new int[] {6,10,5}),//碳水化合物
	polysaccharide_molecule(new Has_composition[] {Element.C,Element.H,Element.O},new int[] {12,22,11}), //蔗糖
	
	stone(new Has_composition[] {SiO2_molecule},new int[] {1000}),
	lava(new Has_composition[] {SiO2_molecule},new int[] {1000}),
	ore(new Has_composition[] {SiO2_molecule},new int[] {1000}),
	
	granite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //花岗岩
	diorite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //闪长岩
	andesite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //安山岩
	
	snowball(new Has_composition[] {water_molecule},new int[] {100}),
	snow_block(new Has_composition[] {snowball},new int[] {4}),
	water(new Has_composition[] {water_molecule},new int[] {1000}),
	ice(new Has_composition[] {water},new int[] {1}),
	packed_ice(new Has_composition[] {ice},new int[] {9}),
	blue_ice(new Has_composition[] {packed_ice},new int[] {9}),
	suger(new Has_composition[] {polysaccharide_molecule},new int[] {20}),
	
	sapling(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {6,10}),
	leaves(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {6,10}),
	stick(new Has_composition[] {sucrose_molecule},new int[] {6}),
	wood_slab(new Has_composition[] {stick},new int[] {1}),
	planks(new Has_composition[] {stick},new int[] {2}),
	wood(new Has_composition[] {planks},new int[] {4}),
	crafting_table(new Has_composition[] {planks},new int[] {4}),
	wood_fence(new Has_composition[] {stick},new int[] {3}),
	chest(new Has_composition[] {planks},new int[] {8}),
	boat(new Has_composition[] {planks},new int[] {5}),
	
	glowstone(new Has_composition[] {},new int[] {}),
	
	charcoal(new Has_composition[] {Element.C},new int[] {1000}),
	coal_powder(new Has_composition[] {Element.C},new int[] {1000}),
	coal(new Has_composition[] {coal_powder},new int[] {1}),
	coal_ore(new Has_composition[] {coal_powder,ore},new int[] {2,1}),
	coal_block(new Has_composition[] {coal},new int[] {9}),
	
	iron_nugget(new Has_composition[] {Element.Fe},new int[] {100}),
	iron_powder(new Has_composition[] {Element.Fe},new int[] {1000}),
	iron_ingot(new Has_composition[] {iron_powder},new int[] {1}),
	iron_block(new Has_composition[] {iron_ingot},new int[] {9}),
	iron_ore(new Has_composition[] {Fe2O3_molecule,ore},new int[] {1000,1}),
	bucket(new Has_composition[] {iron_ingot},new int[] {3}),
	water_bucket(new Has_composition[] {bucket,water},new int[] {1,1}),
	lava_bucket(new Has_composition[] {bucket,lava},new int[] {1,1}),
	iron_sword(new Has_composition[] {iron_ingot,stick},new int[] {2,1}),
	iron_shovel(new Has_composition[] {iron_ingot,stick},new int[] {1,2}),
	iron_pickaxe(new Has_composition[] {iron_ingot,stick},new int[] {3,2}),
	iron_axe(new Has_composition[] {iron_ingot,stick},new int[] {3,2}),
	iron_hoe(new Has_composition[] {iron_ingot,stick},new int[] {2,2}),
	iron_helmet(new Has_composition[] {iron_ingot},new int[] {5}),
	iron_chestplate(new Has_composition[] {iron_ingot},new int[] {8}),
	iron_leggings(new Has_composition[] {iron_ingot},new int[] {7}),
	iron_boots(new Has_composition[] {iron_ingot},new int[] {4}),
	
	gold_nugget(new Has_composition[] {Element.Au},new int[] {100}),
	gold_powder(new Has_composition[] {Element.Au},new int[] {1000}),
	gold_ingot(new Has_composition[] {gold_powder},new int[] {1}),
	gold_block(new Has_composition[] {gold_ingot},new int[] {9}),
	gold_ore(new Has_composition[] {Element.Au,ore},new int[] {1000,1}),
	golden_sword(new Has_composition[] {gold_ingot,stick},new int[] {2,1}),
	golden_shovel(new Has_composition[] {gold_ingot,stick},new int[] {1,2}),
	golden_pickaxe(new Has_composition[] {gold_ingot,stick},new int[] {3,2}),
	golden_axe(new Has_composition[] {gold_ingot,stick},new int[] {3,2}),
	golden_hoe(new Has_composition[] {gold_ingot,stick},new int[] {2,2}),
	golden_helmet(new Has_composition[] {gold_ingot},new int[] {5}),
	golden_chestplate(new Has_composition[] {gold_ingot},new int[] {8}),
	golden_leggings(new Has_composition[] {gold_ingot},new int[] {7}),
	golden_boots(new Has_composition[] {gold_ingot},new int[] {4}),
	
	diamond(new Has_composition[] {Element.C},new int[] {1500}),
	diamond_block(new Has_composition[] {diamond},new int[] {9}),
	diamond_ore(new Has_composition[] {diamond,ore},new int[] {2,1}),
	diamond_sword(new Has_composition[] {diamond,stick},new int[] {2,1}),
	diamond_shovel(new Has_composition[] {diamond,stick},new int[] {1,2}),
	diamond_pickaxe(new Has_composition[] {diamond,stick},new int[] {3,2}),
	diamond_axe(new Has_composition[] {diamond,stick},new int[] {3,2}),
	diamond_hoe(new Has_composition[] {diamond,stick},new int[] {2,2}),
	diamond_helmet(new Has_composition[] {diamond},new int[] {5}),
	diamond_chestplate(new Has_composition[] {diamond},new int[] {8}),
	diamond_leggings(new Has_composition[] {diamond},new int[] {7}),
	diamond_boots(new Has_composition[] {diamond},new int[] {4});
	
	public final HashMap<Has_composition,Double> map = new HashMap<Has_composition,Double>();
	Base_material(Has_composition[] element, double[] unit){
		for(int i=0;i<element.length;i++) {
			map.put(element[i], unit[i]);
		}
	}
	
	Base_material(Has_composition[] element, int[] unit){
		for(int i=0;i<element.length;i++) {
			map.put(element[i], (double) unit[i]);
		}
	}
	
	@Override
	public Elements_composition get_elements_composition() {
		ArrayList<Elements_composition> all_composition = new ArrayList<Elements_composition>();
		for(Entry<Has_composition,Double> entry:this.map.entrySet()) {
			Elements_composition sub_composition = entry.getKey().get_elements_composition();
			sub_composition.multiply(entry.getValue());
			all_composition.add(sub_composition);
		}
		Elements_composition result = new Elements_composition();
		for(Elements_composition compostion:all_composition) {
			result.add(compostion);
		}
		return result;
	}
	
	public static Base_material get(String name) {
		Base_material material = null;
		try {
			material = valueOf(name);
		} catch (Exception e) {
		}
		return material;
	}
}
