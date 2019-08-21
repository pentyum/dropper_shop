package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.Arrays;
import javax.annotation.Nullable;

import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Elements_composition implements Elements_container {
	private int[] composition = new int[96];

	public void multiply(double value) {
		Arrays.parallelPrefix(composition, (i, j) -> (int) ((double) j * value));
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

	public static Elements_composition get_element_composition(Entity entity) {
		if (entity instanceof Item) {
			Item item_entity = (Item) entity;
			return get_element_composition(item_entity.getItemStack());
		}
		String entity_id_name = entity.getType().getKey().toString();
		Elements_composition material_composition = get_entity_element_composition(entity_id_name);
		if (entity instanceof InventoryHolder) {
			InventoryHolder holder = (InventoryHolder) entity;
			Inventory inv = holder.getInventory();
			for (ItemStack in_item : inv.getStorageContents()) {
				if (in_item == null) {
					continue;
				}
				material_composition.add(Elements_composition.get_element_composition(in_item));
			}
		}
		return material_composition;

	}

	public static Elements_composition get_element_composition(ItemStack item) {
		if (Grinder.is_empty(item)) {
			return new Elements_composition();
		}
		String id_name = Material_ext.get_id_name(item);
		Elements_composition material_composition = get_material_element_composition(id_name);
		material_composition.multiply(item.getAmount());
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof BlockStateMeta) {
			BlockStateMeta blockstatemeta = (BlockStateMeta) meta;
			BlockState blockstate = blockstatemeta.getBlockState();
			if (blockstate instanceof Container) {
				Container container = (Container) blockstate;
				Inventory inv = container.getInventory();
				for (ItemStack in_item : inv.getStorageContents()) {
					if (in_item == null) {
						continue;
					}
					material_composition.add(Elements_composition.get_element_composition(in_item));
				}
			}
		}
		return material_composition;
	}

	private static Elements_composition get_material_element_composition(String id_name) {
		Elements_composition compostion = new Elements_composition();
		compostion.set_amount(Element.Magic, 1000);
		return compostion;
	}

	private static Elements_composition get_entity_element_composition(String entity_id_name) {
		Elements_composition compostion = new Elements_composition();
		compostion.set_amount(Element.Magic, 1000);
		return compostion;
	}

	@Override
	public String toString() {
		String result = "";
		for (Element element : Element.values()) {
			if (this.composition[element.atomic_number] != 0) {
				result += element.name() + ": " + this.composition[element.atomic_number] + ", ";
			}
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

}
