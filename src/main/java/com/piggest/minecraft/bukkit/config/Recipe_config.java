package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.structure.Structure_recipe;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Recipe_config extends Ext_config {

	public Recipe_config(String file_name, Class<? extends Structure_recipe> recipe_class) {
		super(file_name);
		ConfigurationSerialization.registerClass(recipe_class);
	}

}
