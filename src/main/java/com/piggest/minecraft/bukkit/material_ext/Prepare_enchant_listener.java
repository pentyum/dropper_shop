package com.piggest.minecraft.bukkit.material_ext;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.nms.NMS_manager;

public class Prepare_enchant_listener implements Listener {
	@EventHandler
	public void on_prepare_enchant(PrepareItemEnchantEvent event) {
		ItemStack item = event.getItem();
		NMS_manager.enchant_manager.enchant(item, event.getView(), event.getOffers(), event.getEnchantmentBonus());
	}
}
