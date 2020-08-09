package com.piggest.minecraft.bukkit.structure;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Recipe;

public abstract class Structure_recipe implements Recipe, ConfigurationSerializable {
	public abstract String get_source_full_name();
}
