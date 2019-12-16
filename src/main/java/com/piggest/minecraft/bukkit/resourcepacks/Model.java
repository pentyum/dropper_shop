package com.piggest.minecraft.bukkit.resourcepacks;

public abstract class Model {
	public String to_json() {
		return Builder.gson.toJson(this);
	}
}
