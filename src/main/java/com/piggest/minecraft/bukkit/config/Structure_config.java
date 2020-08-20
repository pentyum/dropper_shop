package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.World;

import java.util.List;

public class Structure_config extends Ext_config {
	private final String structure_type_name;

	public Structure_config(World world, String structure_type_name) {
		super("structures/" + world.getName() + "/" + structure_type_name + ".yml");
		this.structure_type_name = structure_type_name;
	}

	@SuppressWarnings("unchecked")
	public List<Structure> getList() {
		return (List<Structure>) this.config.getList(structure_type_name);
	}

	public void set(List<Structure> value) {
		this.config.set(structure_type_name, value);
	}
}
