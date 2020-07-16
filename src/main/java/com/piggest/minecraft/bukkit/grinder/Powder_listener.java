package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class Powder_listener implements Listener {
	@EventHandler
	public void on_smelt_powder(FurnaceSmeltEvent event) {
		ItemStack source = event.getSource();
		String full_name = Material_ext.get_full_name(source);
		Powder powder = Powder.powder_map.get(full_name);
		if (powder == null) {
			return;
		}
		ItemStack result = event.getResult();
		if (result == null) {
			return;
		}
		ItemStack new_result = Material_ext.new_item(powder.get_ingot_namespacedkey(), result.getAmount());
		if (new_result != null) {
			event.setResult(new_result);
		}
	}
}
