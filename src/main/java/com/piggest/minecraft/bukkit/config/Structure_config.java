package com.piggest.minecraft.bukkit.config;

import java.util.List;
import org.bukkit.World;

import com.piggest.minecraft.bukkit.structure.Structure;

public class Structure_config extends Ext_config {
	private String structure_type_name;
	
	public Structure_config(World world, String structure_type_name) {
		super("structure_" + structure_type_name + "_" + world.getName() + ".yml");
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
