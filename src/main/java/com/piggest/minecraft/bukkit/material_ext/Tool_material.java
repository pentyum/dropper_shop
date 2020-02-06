package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Tool_material {
	public int get_max_durbility();

	public int get_level();

	public int get_enchantment_ability();

	public String get_display_name();

	public String name();

	public Raw_material get_raw();

	public static boolean is_tool(@Nullable ItemStack item) {
		if (item == null) {
			return false;
		}
		return Raw_material.get_raw_material(item) != null;
	}

	public static boolean is_custom_tool(@Nullable ItemStack item) {
		if (item == null) {
			return false;
		}
		return Custom_material.get_custom_material(item) != null;
	}

	@Nullable
	public static Tool_material get_tool_material(ItemStack item) {
		Tool_material tool_material = Custom_material.get_custom_material(item);
		if (tool_material == null) {
			return Raw_material.get_raw_material(item);
		} else {
			return tool_material;
		}
	}

	public static enum Raw_material implements Tool_material {
		WOODEN(59, 1, 15, "木"), GOLDEN(32, 1, 22, "金"), STONE(131, 2, 5, "石"), IRON(250, 3, 14, "铁"),
		DIAMOND(1561, 2, 10, "钻石");

		public static final HashMap<Material, Raw_material> raw_material_map = new HashMap<Material, Raw_material>() {
			private static final long serialVersionUID = -3631472477554673517L;
			{
				put(Material.WOODEN_AXE, WOODEN);
				put(Material.WOODEN_HOE, WOODEN);
				put(Material.WOODEN_PICKAXE, WOODEN);
				put(Material.WOODEN_SHOVEL, WOODEN);
				put(Material.WOODEN_SWORD, WOODEN);

				put(Material.GOLDEN_AXE, GOLDEN);
				put(Material.GOLDEN_BOOTS, GOLDEN);
				put(Material.GOLDEN_CHESTPLATE, GOLDEN);
				put(Material.GOLDEN_HELMET, GOLDEN);
				put(Material.GOLDEN_HOE, GOLDEN);
				put(Material.GOLDEN_LEGGINGS, GOLDEN);
				put(Material.GOLDEN_PICKAXE, GOLDEN);
				put(Material.GOLDEN_SHOVEL, GOLDEN);
				put(Material.GOLDEN_SWORD, GOLDEN);

				put(Material.STONE_AXE, STONE);
				put(Material.STONE_HOE, STONE);
				put(Material.STONE_PICKAXE, STONE);
				put(Material.STONE_SHOVEL, STONE);
				put(Material.STONE_SWORD, STONE);

				put(Material.IRON_AXE, IRON);
				put(Material.IRON_BOOTS, IRON);
				put(Material.IRON_CHESTPLATE, IRON);
				put(Material.IRON_HELMET, IRON);
				put(Material.IRON_HOE, IRON);
				put(Material.IRON_LEGGINGS, IRON);
				put(Material.IRON_PICKAXE, IRON);
				put(Material.IRON_SHOVEL, IRON);
				put(Material.IRON_SWORD, IRON);

				put(Material.DIAMOND_AXE, DIAMOND);
				put(Material.DIAMOND_BOOTS, DIAMOND);
				put(Material.DIAMOND_CHESTPLATE, DIAMOND);
				put(Material.DIAMOND_HELMET, DIAMOND);
				put(Material.DIAMOND_HOE, DIAMOND);
				put(Material.DIAMOND_LEGGINGS, DIAMOND);
				put(Material.DIAMOND_PICKAXE, DIAMOND);
				put(Material.DIAMOND_SHOVEL, DIAMOND);
				put(Material.DIAMOND_SWORD, DIAMOND);
			}
		};

		private int max_durbility;
		private int level;
		private int enchantment_ability;
		private String name;

		Raw_material(int max_durbility, int level, int enchantment_ability, String name) {
			this.max_durbility = max_durbility;
			this.level = level;
			this.enchantment_ability = enchantment_ability;
			this.name = name;
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

		@Nullable
		public static Raw_material get_raw_material(@Nonnull ItemStack item) {
			Material material = item.getType();
			return raw_material_map.get(material);
		}

		public String get_display_name() {
			return this.name;
		}

		@Override
		public Raw_material get_raw() {
			return this;
		}
	}

	public static enum Custom_material implements Tool_material {
		ALUMINUM(250, Raw_material.STONE, 14, "铝"), COPPER(225, Raw_material.IRON, 6, "铜"),
		TIN(150, Raw_material.STONE, 7, "锡"), SILVER(100, Raw_material.IRON, 20, "银"),
		BRONZE(350, Raw_material.IRON, 15, "青铜"), NICKEL(300, Raw_material.IRON, 18, "镍"),
		LEAD(100, Raw_material.STONE, 9, "铅");

		private int max_durbility;
		private Raw_material raw_model;
		private int enchantment_ability = 0;
		private String name;

		Custom_material(int max_durbility, Raw_material raw_model, int enchantment_ability, String name) {
			this.max_durbility = max_durbility;
			this.raw_model = raw_model;
			this.name = name;
			this.enchantment_ability = enchantment_ability;
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

		@Nullable
		public static Custom_material get_custom_material(@Nonnull ItemStack item) {
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
			if (this.enchantment_ability == 0) {
				return this.raw_model.get_enchantment_ability();
			} else {
				return this.enchantment_ability;
			}
		}

		@Override
		public String get_display_name() {
			return this.name;
		}
	}
}