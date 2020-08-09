package com.piggest.minecraft.bukkit.compressor;

import com.piggest.minecraft.bukkit.structure.Structure_recipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Compressor_recipe extends Structure_recipe {
	private final String source;
	private final int need_quantity;
	private final ItemStack result;
	private final int recipe_time;

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

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<>();
		save.put("source", this.source);
		save.put("need-quantity", this.need_quantity);
		save.put("result", this.result);
		save.put("recipe-time", this.recipe_time);
		return save;
	}

	public static Compressor_recipe deserialize(@Nonnull Map<String, Object> args) {
		String source = (String) args.get("source");
		int need_quantity = (int) args.get("need-quantity");
		ItemStack result = (ItemStack) args.get("result");
		int recipe_time = (int) args.get("recipe-time");
		return new Compressor_recipe(source, need_quantity, result, recipe_time);
	}
}
