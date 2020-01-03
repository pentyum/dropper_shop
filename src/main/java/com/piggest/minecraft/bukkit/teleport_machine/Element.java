package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.NamespacedKey;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public enum Element implements Has_composition {
	Magic(-1, 0, 0), H(0, 1, 1, 1), Be(1, 4, 9, 2), C(2, 6, 12, 4), N(3, 7, 14, -3, 5), O(4, 8, 16, -2),
	F(5, 9, 19, -1), Na(6, 11, 23, 1), Mg(7, 12, 24, 2), Al(8, 13, 27, 3), Si(9, 14, 28, 4), P(10, 15, 31, 5),
	S(11, 16, 32, 6, -2), Cl(12, 17, 35, -1), K(13, 19, 39, 1), Ca(14, 20, 40, 2), Fe(15, 26, 56, 3), Cu(16, 29, 64, 2),
	Zn(17, 30, 65, 2), Ag(18, 47, 108, 1), Sn(19, 50, 119, 4, 2), I(20, 53, 127, -1), Pt(21, 78, 195), Au(22, 79, 197),
	Pb(23, 82, 207), U(24, 92, 238), Pu(25, 94, 239);

	public final int atomic_number;
	public final int relative_atomic_mass;
	public final int order_id;
	public final int valence;
	public final int valence_2;
	public static final NamespacedKey namespacedkey = new NamespacedKey(Dropper_shop_plugin.instance, "element_id");

	Element(int order_id, int atomic_number, int relative_atomic_mass) {
		this.order_id = order_id;
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = 0;
		this.valence_2 = 0;
	}

	Element(int order_id, int atomic_number, int relative_atomic_mass, int valence) {
		this.order_id = order_id;
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = valence;
		this.valence_2 = 0;
	}

	Element(int order_id, int atomic_number, int relative_atomic_mass, int valence, int valence_2) {
		this.order_id = order_id;
		this.atomic_number = atomic_number;
		this.relative_atomic_mass = relative_atomic_mass;
		this.valence = valence;
		this.valence_2 = valence_2;
	}

	@Override
	public Elements_composition get_elements_composition() {
		return this.get_elements_composition(1);
	}

	public Elements_composition get_elements_composition(int value) {
		Elements_composition comp = new Elements_composition();
		comp.set_amount(this, value);
		return comp;
	}
}
