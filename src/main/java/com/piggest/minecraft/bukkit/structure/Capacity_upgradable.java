package com.piggest.minecraft.bukkit.structure;

import org.bukkit.entity.Player;

public interface Capacity_upgradable {
	public boolean capacity_upgrade_by(Player player);

	public void set_capacity_level(int level);

	public int get_capacity_level();
}
