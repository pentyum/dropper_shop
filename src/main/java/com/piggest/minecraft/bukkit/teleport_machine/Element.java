package com.piggest.minecraft.bukkit.teleport_machine;

public enum Element implements Has_composition {
	Magic(0, 0),
	H(1, 1, 1),
	Be(4, 9, 2),
	C(6, 12, 4),
	N(7, 14, -3, 5),
	O(8, 16, -2),
	F(9, 19, -1),
	Na(11, 23, 1),
	Mg(12, 24, 2),
	Al(13, 27, 3),
	Si(14, 28, 4),
	P(15, 31, 5),
	S(16, 32, 6, -2),
	Cl(17, 35, -1),
	K(19, 39, 1),
	Ca(20, 40, 2), 
	Fe(25, 56, 3),
	Cu(29, 64, 2),
	Zn(30, 65, 2),
	Ag(47, 108, 1),
	Sn(50, 119, 4, 2),
	Pt(78, 195),
	Au(79, 197),
	Hg(80, 201),
	Pb(82, 207),
	U(92, 238),
	Pu(94, 239);

	public final int atomic_number;
	public final int relative_atomic_mass;
	public final int valence;
	public final int valence_2;

	Element(int atomic_number, int relative_atomic_mass) {
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = 0;
		this.valence_2 = 0;
	}

	Element(int atomic_number, int relative_atomic_mass, int valence) {
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = valence;
		this.valence_2 = 0;
	}

	Element(int atomic_number, int relative_atomic_mass, int valence, int valence_2) {
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = valence;
		this.valence_2 = valence_2;
	}

	@Override
	public Elements_composition get_elements_composition() {
		return new Elements_composition(this,1);
	}
}
