package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.inventory.Inventory;

public interface Elements_container {
	public int get_amount(Element element);

	public void set_amount(Element element, int amount);
	
	public Inventory get_elements_gui();
}
