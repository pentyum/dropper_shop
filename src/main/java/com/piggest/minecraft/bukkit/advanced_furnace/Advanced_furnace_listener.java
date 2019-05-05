package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Advanced_furnace_listener implements Listener {
	@EventHandler
	public void on_use_gas_bottle(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = event.getItem();
			if (Gas_bottle.is_gas_bottle(item)) {
				event.getPlayer().sendMessage("使用了气体瓶");
				Gas_bottle.clean_contents(item);
				for (Entry<Gas, Integer> entry : Dropper_shop_plugin.instance
						.get_air(event.getPlayer().getWorld().getName()).entrySet()) {
					Gas_bottle.set_contents(item, entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
