package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Dropper_shop_listener implements Listener {
	private Dropper_shop_plugin plugin = null;

	public Dropper_shop_listener(Dropper_shop_plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void on_dispense(BlockDispenseEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Dropper_shop shop = this.plugin.get_shop_manager().get_dropper_shop(event.getBlock().getLocation());
		if (shop == null) {
			return;
		}
		shop.buy();
	}

	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == false && (event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getItem() == null) {

		}
	}
}
