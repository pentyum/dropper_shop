package com.piggest.minecraft.bukkit.material_ext;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Tool_material {
	public int get_max_durbility();

	public int get_level();

	public static Tool_material get_tool_material(ItemStack item) {
		return Raw_material.get_raw_material(item);
	}

	public static enum Raw_material implements Tool_material {
		WOODEN(60, 1), GOLDEN(33, 1), STONE(132, 2), IRON(251, 3), DIAMOND(1562, 4);

		private int max_durbility;
		private int level;

		Raw_material(int max_durbility, int level) {
			this.max_durbility = max_durbility;
			this.level = level;
		}

		public int get_max_durbility() {
			return this.max_durbility;
		}

		public int get_level() {
			return this.level;
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

	public static class Custom_material implements Tool_material {
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
	}
}