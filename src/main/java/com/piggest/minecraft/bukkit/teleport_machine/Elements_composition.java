package com.piggest.minecraft.bukkit.teleport_machine;

public class Elements_composition {
	int[] composition = new int[100];
	
	public Elements_composition(){
		for (int i = 0; i < composition.length; i++) {
			composition[i] = 0;
		}
	}
	public Elements_composition(Element element,int unit){
		this();
		this.composition[element.atomic_number] = unit;
	}
	
	public void multiply(double value) {
		for (int i = 0; i < composition.length; i++) {
			composition[i] = (int) ((double)composition[i]*value);
		}
	}

	public void merge_with(Elements_composition other) {
		for (int i = 0; i < composition.length; i++) {
			composition[i] += other.composition[i];
		}
	}
	
	public int get(Element element) {
		return this.composition[element.atomic_number];
	}
}
