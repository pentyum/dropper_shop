package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.structure.Structure_recipe;

public class Grinder_recipe extends Structure_recipe {
	private String source;
	private ItemStack main_result;
	private ItemStack minor_result;
	private int minor_possibility;
	private int recipe_time;

	public Grinder_recipe(String source_full_name, ItemStack main_result, ItemStack minor_result, int minor_possibility,
			int recipe_time) {
		this.source = source_full_name;
		this.main_result = main_result;
		this.minor_result = minor_result;
		this.minor_possibility = minor_possibility;
		this.recipe_time = recipe_time;
	}

	public Grinder_recipe(String source_full_name, ItemStack main_result, ItemStack minor_result, int recipe_time) {
		this(source_full_name, main_result, minor_result, 100, recipe_time);
	}

	public Grinder_recipe(String source_full_name, ItemStack main_result, int recipe_time) {
		this(source_full_name, main_result, null, 0, recipe_time);
	}

	@Override
	public ItemStack getResult() {
		return this.main_result;
	}

	public ItemStack get_minor_result() {
		return this.minor_result;
	}

	public int get_minor_possibility() {
		return this.minor_possibility;
	}

	public int get_recipe_time() {
		return this.recipe_time;
	}

	public String get_source_full_name() {
		return this.source;
	}
}
