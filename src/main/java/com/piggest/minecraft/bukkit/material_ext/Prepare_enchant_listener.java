package com.piggest.minecraft.bukkit.material_ext;

import com.piggest.minecraft.bukkit.nms.NMS_manager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

public class Prepare_enchant_listener implements Listener {
	@EventHandler
	public void on_prepare_enchant(PrepareItemEnchantEvent event) {
		ItemStack item = event.getItem();
		if (Tool_material.is_custom_tool(item)) {
			NMS_manager.enchant_manager.enchant(item, event.getView(), event.getOffers(), event.getEnchantmentBonus());
		}
	}
}
