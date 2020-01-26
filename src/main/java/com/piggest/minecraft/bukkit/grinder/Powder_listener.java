package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Powder_listener implements Listener {
	@EventHandler
	public void on_smelt_powder(FurnaceSmeltEvent event) {
		ItemStack source = event.getSource();
		String full_name = Material_ext.get_full_name(source);
		Powder powder = Powder.powder_map.get(full_name);
		if (powder == null) {
			return;
		}
	}
}
