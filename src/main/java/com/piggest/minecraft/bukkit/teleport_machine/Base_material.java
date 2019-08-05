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
	
	stone(new Has_composition[] {Element.H},new int[] {1}),
	ore(new Has_composition[] {},new int[] {}),
	water(new Has_composition[] {},new int[] {}),
	iron(new Has_composition[] {Base_material.stone},new int[] {1}),
	carbon(new Has_composition[] {},new int[] {}),
	suger(new Has_composition[] {},new int[] {}),
	wood(new Has_composition[] {},new int[] {}),
	glowstone(new Has_composition[] {},new int[] {});
	
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
			result.merge_with(compostion);
		}
		return result;
	}

}
