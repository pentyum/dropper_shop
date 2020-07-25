package com.piggest.minecraft.bukkit.structure;

public interface Has_runner {
	public <T extends Structure> Structure_runner<T>[] init_runners();
}
