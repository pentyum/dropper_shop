package com.piggest.minecraft.bukkit.compressor;

import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.structure.Structure_recipe;

public class Compressor_recipe extends Structure_recipe {
	private String source;
	private int need_quantity;
	private ItemStack result;
	private int recipe_time;

	public Compressor_recipe(String source_full_name, int need_quantity, ItemStack result, int recipe_time) {
		this.source = source_full_name;
		this.need_quantity = need_quantity;
		this.result = result;
		this.recipe_time = recipe_time;
	}

	@Override
	public ItemStack getResult() {
		return this.result;
	}

	public String get_source_full_name() {
		return this.source;
	}

	public int get_need_quantity() {
		return this.need_quantity;
	}

	public int get_recipe_time() {
		return this.recipe_time;
	}
}
