package com.piggest.minecraft.bukkit.resourcepacks.minecraft;

import java.util.ArrayList;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.resourcepacks.Model;
import com.piggest.minecraft.bukkit.resourcepacks.Override;
import com.piggest.minecraft.bukkit.resourcepacks.Textures;

public class Sugar extends Model {
	public String parent = "item/generated";
	public Textures textures = new Textures(Material.SUGAR);
	public ArrayList<Override> overrides = new ArrayList<Override>();
}
