package com.piggest.minecraft.bukkit.structure;

import org.bukkit.OfflinePlayer;

public interface Ownable {
	public void set_owner(String owner);

	public OfflinePlayer get_owner();

	public String get_owner_name();
}
