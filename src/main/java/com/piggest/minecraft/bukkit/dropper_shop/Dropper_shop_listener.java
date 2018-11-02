package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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
			Dropper_shop shop = this.plugin.get_shop_manager().get_dropper_shop(event.getClickedBlock().getLocation());
			if (shop != null) {
				event.getPlayer().sendMessage("当前投掷器商店出售:" + shop.get_selling_item().name() + "\n拥有者:"
						+ shop.get_owner_name() + "\n价格:" + plugin.get_price(shop.get_selling_item()));
			}
		}
	}

	@EventHandler
	public void on_break_shop(BlockBreakEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Block break_block = event.getBlock();
		if (break_block.getType() != Material.DROPPER) {
			return;
		}
		Dropper_shop shop = this.plugin.get_shop_manager().get_dropper_shop(break_block.getLocation());
		if (shop == null) {
			return;
		}
		event.getPlayer().sendMessage("已破坏" + shop.get_owner_name() + "的" + shop.get_selling_item().name() + "投掷器商店");
		this.plugin.get_shop_manager().remove(shop);
	}
}
