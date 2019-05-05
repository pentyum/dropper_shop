package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Dropper_shop_listener implements Listener {
	// private Dropper_shop_plugin plugin = null;

	@EventHandler
	public void on_dispense(BlockDispenseEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Dropper_shop shop = Dropper_shop_manager.instance.get(event.getBlock().getLocation());
		if (shop == null) {
			return;
		}
		shop.buy();
	}

	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.useItemInHand() == Result.ALLOW && event.useInteractedBlock() == Result.ALLOW && (event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getItem() == null) {
			Dropper_shop shop = Dropper_shop_manager.instance.get(event.getClickedBlock().getLocation());
			if (shop != null) {
				event.getPlayer()
						.sendMessage("当前投掷器商店出售:" + shop.get_selling_item().name() + "\n拥有者:" + shop.get_owner_name()
								+ "\n价格:" + Dropper_shop_plugin.instance.get_price(shop.get_selling_item()));
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
		Dropper_shop shop = Dropper_shop_manager.instance.get(break_block.getLocation());
		if (shop == null) {
			return;
		}
		event.getPlayer().sendMessage("已破坏" + shop.get_owner_name() + "的" + shop.get_selling_item().name() + "投掷器商店");
		Dropper_shop_manager.instance.remove(shop);
	}
}
