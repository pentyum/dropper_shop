package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_composition;

public enum Liquid implements Chemical {
	water("水"), lava("熔岩"), milk("牛奶");

	private String display_name;

	Liquid(String display_name) {
		this.display_name = display_name;
	}

	@Override
	public String get_displayname() {
		return this.display_name + "(l)";
	}

	@Override
	public String get_name() {
		return this.name();
	}

	public static int get_item_unit(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals(Reader.id_name)) {
			id_name = Reader.get_content_id_name(item);
		}
		switch (id_name) {
		case "water_bucket":
			return 1000;
		case "lava_bucket":
			return 1000;
		case "milk_bucket":
			return 1000;
		case "potion":
			return 200;
		default:
			return 0;
		}
	}

	public static int get_container_max_unit(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals(Reader.id_name)) {
			id_name = Reader.get_content_id_name(item);
		}
		switch (id_name) {
		case "bucket":
			return 1000;
		case "glass_bottle":
			return 200;
		default:
			return 0;
		}
	}

	public static Liquid get_liquid(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals(Reader.id_name)) {
			id_name = Reader.get_content_id_name(item);
		}
		switch (id_name) {
		case "water_bucket":
			return water;
		case "lava_bucket":
			return lava;
		case "milk_bucket":
			return milk;
		case "potion":
			return water;
		default:
			return null;
		}
	}

	public static boolean is_empty_liquid_container(ItemStack item) {
		return Material_ext.is_empty_container(item) == Status.liquid;
	}

	public static Liquid get_liquid(String name) {
		Liquid liquid = null;
		try {
			liquid = Liquid.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return liquid;
	}

	public ItemStack get_filled_bucket() {
		return Material_ext.new_item(this.name() + "_bucket", 1);
	}

	public static ItemStack get_fill_container(Liquid liquid, ItemStack container) {
		String id_name = Material_ext.get_id_name(container);
		if (id_name.equals(Reader.id_name)) {
			id_name = Reader.get_content_id_name(container);
		}
		switch (liquid) {
		case lava:
			switch (id_name) {
			case "bucket":
				return new ItemStack(Material.LAVA_BUCKET);
			default:
				return null;
			}
		case milk:
			switch (id_name) {
			case "bucket":
				return new ItemStack(Material.MILK_BUCKET);
			default:
				return null;
			}
		case water:
			switch (id_name) {
			case "bucket":
				return new ItemStack(Material.WATER_BUCKET);
			case "glass_bottle":
				ItemStack water_bottle = new ItemStack(Material.POTION);
				PotionMeta meta = (PotionMeta) water_bottle.getItemMeta();
				PotionData data = new PotionData(PotionType.WATER);
				meta.setBasePotionData(data);
				water_bottle.setItemMeta(meta);
				return water_bottle;
			default:
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	public Elements_composition get_elements_composition() {
		return null;
	}
}
