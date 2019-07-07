package com.piggest.minecraft.bukkit.teleport_machine;

public enum Element {
	H(1,1),
	C(6,12),
	N(7,14),
	O(8,16),
	F(9,19),
	Na(11,23),
	Mg(12,24),
	Al(13,27),
	Si(14,28),
	P(15,31),
	S(16,32),
	Cl(17,35),
	K(19,39),
	Ca(20,40),
	Fe(25,56),
	Cu(29,64),
	Zn(30,65),
	Ag(47,108),
	Sn(50,119),
	Pt(78,195),
	Au(79,197),
	Hg(80,201),
	Pb(82,207),
	U(92,238),
	Pu(94,239);
	
	private int atomic_number;
	private int relative_atomic_mass;
	
	Element(int atomic_number,int relative_atomic_mass) {
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
	}
	
	public int get_number() {
		return this.atomic_number;
	}
	
	public int get_relative_mass() {
		return this.relative_atomic_mass;
	}
}
