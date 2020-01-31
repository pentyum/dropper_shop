package com.piggest.minecraft.bukkit.material_ext;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Tool_material {
	public int get_max_durbility();

	public int get_level();

	public int get_enchantment_ability();

	public static boolean is_raw_tool(ItemStack item) {
		return Raw_material.get_raw_material(item) != null;
	}
	
	public static boolean is_custom_tool(ItemStack item) {
		return Custom_material.get_custom_material(item) !=null;
	}
	
	public static Tool_material get_tool_material(ItemStack item) {
		Tool_material tool_material = Custom_material.get_custom_material(item);
		if (tool_material == null) {
			return Raw_material.get_raw_material(item);
		} else {
			return tool_material;
		}
	}

	public static enum Raw_material implements Tool_material {
		WOODEN(59, 1, 15), GOLDEN(32, 1, 22), STONE(131, 2, 5), IRON(250, 3, 14), DIAMOND(1561, 2, 10);

		private int max_durbility;
		private int level;
		private int enchantment_ability;

		Raw_material(int max_durbility, int level, int enchantment_ability) {
			this.max_durbility = max_durbility;
			this.level = level;
			this.enchantment_ability = enchantment_ability;
		}

		public int get_max_durbility() {
			return this.max_durbility;
		}

		public int get_level() {
			return this.level;
		}

		public int get_enchantment_ability() {
			return this.enchantment_ability;
		}

		public static Raw_material get_raw_material(ItemStack item) {
			Material material = item.getType();
			switch (material) {
			case DIAMOND_AXE:
			case DIAMOND_HOE:
			case DIAMOND_PICKAXE:
			case DIAMOND_SHOVEL:
			case DIAMOND_SWORD:
			case DIAMOND_HELMET:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_LEGGINGS:
			case DIAMOND_BOOTS:
				return Raw_material.DIAMOND;
			case IRON_AXE:
			case IRON_HOE:
			case IRON_PICKAXE:
			case IRON_SHOVEL:
			case IRON_SWORD:
			case IRON_HELMET:
			case IRON_CHESTPLATE:
			case IRON_LEGGINGS:
			case IRON_BOOTS:
				return Raw_material.IRON;
			case STONE_AXE:
			case STONE_HOE:
			case STONE_PICKAXE:
			case STONE_SHOVEL:
			case STONE_SWORD:
				return Raw_material.STONE;
			case GOLDEN_AXE:
			case GOLDEN_HOE:
			case GOLDEN_PICKAXE:
			case GOLDEN_SHOVEL:
			case GOLDEN_SWORD:
			case GOLDEN_HELMET:
			case GOLDEN_CHESTPLATE:
			case GOLDEN_LEGGINGS:
			case GOLDEN_BOOTS:
				return Raw_material.GOLDEN;
			case WOODEN_AXE:
			case WOODEN_HOE:
			case WOODEN_PICKAXE:
			case WOODEN_SHOVEL:
			case WOODEN_SWORD:
				return Raw_material.WOODEN;
			default:
				return null;
			}
		}
	}

	public static enum Custom_material implements Tool_material {
		ALUMINUM(225, Raw_material.STONE), COPPER(175, Raw_material.STONE), TIN(150, Raw_material.STONE),
		SILVER(75, Raw_material.IRON), BRONZE(325, Raw_material.IRON), NICKEL(300, Raw_material.IRON),
		LEAD(100, Raw_material.STONE);
		private int max_durbility;
		private Raw_material raw_model;

		Custom_material(int max_durbility, Raw_material raw_model) {
			this.max_durbility = max_durbility;
			this.raw_model = raw_model;
		}

		@Override
		public int get_max_durbility() {
			return this.max_durbility;
		}

		@Override
		public int get_level() {
			return this.raw_model.get_level();
		}

		public Raw_material get_raw() {
			return this.raw_model;
		}

		public static Custom_material get_custom_material(ItemStack item) {
			String id_name = Material_ext.get_id_name(item);
			String[] id_name_head = id_name.split("_");
			try {
				return Custom_material.valueOf(id_name_head[0].toUpperCase());
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		@Override
		public int get_enchantment_ability() {
			return this.raw_model.get_enchantment_ability();
		}
	}
}