package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.advanced_furnace.Gas;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas_bottle;
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
		if (amount > 1000000000) {
			amount = 1000000000;
		}
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
		if (meta instanceof BlockStateMeta) { // 潜影盒
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
		Map<Enchantment, Integer> ench_map = meta.getEnchants(); // 被附魔的物品
		for (int level : ench_map.values()) {
			material_composition.add(Element.Magic.get_elements_composition(level * 1000));
		}
		if (meta instanceof EnchantmentStorageMeta) { // 附魔书
			EnchantmentStorageMeta enchantmentstoragemeta = (EnchantmentStorageMeta) meta;
			Map<Enchantment, Integer> storge_ench_map = enchantmentstoragemeta.getStoredEnchants();
			for (int level : storge_ench_map.values()) {
				material_composition.add(Element.Magic.get_elements_composition(level * 1000));
			}
		}
		if(id_name.equals("gas_bottle")) {
			Map<Gas,Integer> gas_map = Gas_bottle.get_gas_map(item);
			for (Entry<Gas, Integer> entry:gas_map.entrySet()) {
				Gas gas_type = entry.getKey();
				int amount = entry.getValue();
				Elements_composition gas_composition = gas_type.get_elements_composition();
				gas_composition.multiply(amount);
				material_composition.add(gas_composition);
			}
		}
		return material_composition;
	}

	private static Elements_composition get_material_element_composition(String id_name) {
		Elements_composition compostion = null;
		if(id_name.contains("polished_")) {
			id_name = id_name.substring("polished_".length());
		}
		if(id_name.contains("smooth_")) {
			id_name = id_name.substring("smooth_".length());
		}
		if(id_name.contains("cut_")) {
			id_name = id_name.substring("cut_".length());
		}
		if(id_name.contains("chiseled_")) {
			id_name = id_name.substring("chiseled_".length());
		}
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
		} else if (id_name.contains("_slab")) {
			switch (id_name) {
			case "oak_slab":
			case "spruce_slab":
			case "birch_slab":
			case "jungle_slab":
			case "acacia_slab":
			case "dark_oak_slab":
				id_name = "planks_slab";
				break;
			default:
				break;
			}
		} else if (id_name.contains("_stairs")) {
			switch (id_name) {
			case "oak_stairs":
			case "spruce_stairs":
			case "birch_stairs":
			case "jungle_stairs":
			case "acacia_stairs":
			case "dark_oak_stairs":
				id_name = "planks_stairs";
				break;
			default:
				break;
			}
		} else if (id_name.contains("_button")) {
			switch (id_name) {
			case "oak_button":
			case "spruce_button":
			case "birch_button":
			case "jungle_button":
			case "acacia_button":
			case "dark_oak_button":
				id_name = "planks_button";
				break;
			default:
				break;
			}
		} else if (id_name.contains("_pressure_plate")) {
			switch (id_name) {
			case "oak_pressure_plate":
			case "spruce_pressure_plate":
			case "birch_pressure_plate":
			case "jungle_pressure_plate":
			case "acacia_pressure_plate":
			case "dark_oak_pressure_plate":
				id_name = "planks_pressure_plate";
				break;
			default:
				break;
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
		case "minecart":
			compostion.add(Base_material.minecart.get_elements_composition());
			break;
		case "villager":
			compostion.set_amount(Element.Magic, 1000);
			compostion.set_amount(Element.C, 475);
			compostion.set_amount(Element.H, 3150);
			compostion.set_amount(Element.O, 1275);
			compostion.set_amount(Element.N, 70);
			compostion.set_amount(Element.P, 12);
			compostion.set_amount(Element.S, 3);
			compostion.set_amount(Element.Be, 500);
			break;
		case "boat":
			compostion.add(Base_material.boat.get_elements_composition());
			break;
		default:
			compostion.set_amount(Element.Magic, 1000);
			compostion.set_amount(Element.C, 475);
			compostion.set_amount(Element.H, 3150);
			compostion.set_amount(Element.O, 1275);
			compostion.set_amount(Element.N, 70);
			compostion.set_amount(Element.P, 12);
			compostion.set_amount(Element.S, 3);
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
