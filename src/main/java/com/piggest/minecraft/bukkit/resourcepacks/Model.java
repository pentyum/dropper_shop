package com.piggest.minecraft.bukkit.resourcepacks;

public abstract class Model {
	protected String parent = "item/generated";
	private Textures textures = null;

	public Model(String parent, Textures textures) {
		this.parent = parent;
		this.textures = textures;
	}

	public Model(Textures textures) {
		this.textures = textures;
	}

	public String to_json() {
		return Builder.gson.toJson(this);
	}

	public String getParent() {
		return parent;
	}

	public Textures getTextures() {
		return textures;
	}
}
