package com.piggest.minecraft.bukkit.resourcepacks;

public class Override {
	public Custom_model_data_tag predicate;
	public String model;

	public Override(int custom_model_data, String model) {
		this.predicate = new Custom_model_data_tag(custom_model_data);
		this.model = model;
	}
}
