package com.piggest.minecraft.bukkit.teleport_machine;

import javax.annotation.Nullable;

import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Elements_composition implements Elements_container {
	int[] composition = new int[96];

	public Elements_composition() {
		for (int i = 0; i < composition.length; i++) {
			composition[i] = 0;
		}
	}

	public Elements_composition(Element element, int unit) {
		this();
		this.composition[element.atomic_number] = unit;
	}

	public void multiply(double value) {
		for (int i = 0; i < composition.length; i++) {
			composition[i] = (int) ((double) composition[i] * value);
		}
	}

	@Override
	public int get_amount(Element element) {
		return this.composition[element.atomic_number];
	}

	@Override
	public void set_amount(Element element, int amount) {
		this.composition[element.atomic_number] = amount;
	}

	@Nullable
	@Override
	public Inventory get_elements_gui() {
		return null;
	}

	public static Elements_composition get_element_composition(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		Elements_composition material_composition = get_element_composition(id_name);
		material_composition.multiply(item.getAmount());
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof BlockStateMeta) {
			BlockStateMeta blockstatemeta = (BlockStateMeta) meta;
			BlockState blockstate = blockstatemeta.getBlockState();
			if (blockstate instanceof Container) {
				Container container = (Container) blockstate;
				Inventory inv = container.getInventory();
				for (ItemStack in_item : inv.getStorageContents()) {
					material_composition.add(Elements_composition.get_element_composition(in_item));
				}
			}
		}
		return material_composition;
	}

	private static Elements_composition get_element_composition(String id_name) {
		return new Elements_composition();
	}
}
