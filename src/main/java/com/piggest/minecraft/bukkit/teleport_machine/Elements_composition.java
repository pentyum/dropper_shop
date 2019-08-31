package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
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
		Arrays.parallelSetAll(composition, i -> (int) ((double) composition[i] * value));
	}

	public void multiply(int value) {
		Arrays.parallelSetAll(composition, i -> composition[i] * value);
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
		if (entity instanceof ExperienceOrb) {
			ExperienceOrb exp_orb = (ExperienceOrb) entity;
			int exp = exp_orb.getExperience();
			Elements_composition composition = new Elements_composition();
			composition.set_amount(Element.Magic, exp);
			material_composition.add(composition);
		} else if (entity instanceof InventoryHolder) {
			InventoryHolder holder = (InventoryHolder) entity;
			Inventory inv = holder.getInventory();
			for (ItemStack in_item : inv.getContents()) {
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
		Elements_composition compostion = null;
		if (id_name.contains("_leaves")) {
			id_name = "leaves";
		} else if (id_name.contains("_wood")) {
			id_name = "wood";
		} else if (id_name.contains("_log")) {
			id_name = "wood";
		} else if (id_name.contains("_planks")) {
			id_name = "planks";
		} else if (id_name.contains("_sapling")) {
			id_name = "sapling";
		} else if (id_name.contains("_wool")) {
			id_name = "wool";
		} else if (id_name.contains("_glass_pane")) {
			id_name = "glass_pane";
		} else if (id_name.contains("_glass")) {
			id_name = "glass";
		} else if (id_name.contains("_carpet")) {
			id_name = "carpet";
		} else if (id_name.contains("_terracotta")) {
			id_name = "terracotta";
		} else if (id_name.contains("_concrete_powder")) {
			id_name = "concrete_powder";
		} else if (id_name.contains("_concrete")) {
			id_name = "concrete";
		} else if (id_name.contains("_sign")) {
			id_name = "sign";
		} else if (id_name.contains("_bed")) {
			id_name = "bed";
		} else if (id_name.contains("_banner")) {
			id_name = "banner";
		} else if (id_name.contains("_shulker_box")) {
			id_name = "shulker_box";
		} else if (id_name.contains("_anvil")) {
			id_name = "anvil";
		} else if (id_name.contains("_door")) {
			if (!id_name.equals("iron_door")) {
				id_name = "planks_door";
			}
		} else if (id_name.contains("_trapdoor")) {
			if (!id_name.equals("iron_trapdoor")) {
				id_name = "planks_trapdoor";
			}
		} else if (id_name.contains("_button")) {
			if (!id_name.equals("stone_button")) {
				id_name = "planks_button";
			}
		}
		Base_material material = Base_material.get(id_name);
		if (material != null) {
			compostion = material.get_elements_composition();
		} else {
			compostion = new Elements_composition();
			compostion.set_amount(Element.Magic, 1000);
		}
		return compostion;
	}

	private static Elements_composition get_entity_element_composition(String entity_id_name) {
		Elements_composition compostion = new Elements_composition();
		switch (entity_id_name) {
		case "player":
			compostion.set_amount(Element.Magic, 1000);
			compostion.set_amount(Element.C, 475);
			compostion.set_amount(Element.H, 3150);
			compostion.set_amount(Element.O, 1275);
			compostion.set_amount(Element.N, 70);
			compostion.set_amount(Element.P, 12);
			compostion.set_amount(Element.S, 3);
			break;
		case "iron_golem":
			compostion.set_amount(Element.Magic, 10000);
			compostion.set_amount(Element.Fe, 36000);
			break;
		default:
			compostion = new Elements_composition();
			compostion.set_amount(Element.Magic, 1000);
			break;
		}
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

	public int get_total_byte() {
		int total = 0;
		for (Element element : Element.values()) {
			total += this.composition[element.atomic_number];
		}
		return total;
	}
}
