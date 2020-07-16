package com.piggest.minecraft.bukkit.resourcepacks.minecraft;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Override;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;
import org.bukkit.Material;

import java.util.ArrayList;

public class Vanilla_model extends Model {

	public Vanilla_model(Material material) {
		super(new Textures(material));
	}

	public ArrayList<Override> overrides = new ArrayList<Override>();

	public void add_custom_model_override(int custom_model_data, String model) {
		this.overrides.add(new Override(custom_model_data, model));
	}
}
