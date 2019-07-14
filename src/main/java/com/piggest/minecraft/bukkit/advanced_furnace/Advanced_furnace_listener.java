package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.depository.Upgrade_component_type;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;

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

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getClickedInventory().getHolder() instanceof Advanced_furnace) {
			Advanced_furnace adv_furnace = (Advanced_furnace) event.getClickedInventory().getHolder();
			if (event.getSlot() == Advanced_furnace.upgrade_component_slot) {
				ItemStack item = event.getCurrentItem();
				int current_overload_level = adv_furnace.get_overload_upgrade();
				int current_time_level = adv_furnace.get_time_upgrade();
				ItemStack cursor_item = event.getCursor();
				Upgrade_component_type cursor_type = Upgrade_component.get_type(cursor_item);
				if (cursor_type == Upgrade_component_type.overload_upgrade) {
					if (Upgrade_component.get_level(cursor_item) != current_overload_level + 1) {
						event.getWhoClicked().sendMessage("必须放入" + (current_overload_level + 1) + "级高速升级组件");
						event.setCancelled(true);
						return;
					}
				} else if (cursor_type == Upgrade_component_type.time_upgrade) {
					if (Upgrade_component.get_level(cursor_item) != current_time_level + 1) {
						event.getWhoClicked().sendMessage("必须放入" + (current_time_level + 1) + "级长时升级组件");
						event.setCancelled(true);
						return;
					}
				} else {
					if (!Grinder.is_empty(cursor_item)) {
						event.getWhoClicked().sendMessage("必须放入" + (current_overload_level + 1) + "级高速升级组件" + "或者"
								+ (current_time_level + 1) + "级长时升级组件");
						event.setCancelled(true);
						return;
					}
				}

				if (item != null && item.getType() != Material.AIR) {
					Upgrade_component_type type = Upgrade_component.get_type(item);
					if (type == Upgrade_component_type.overload_upgrade
							|| type == Upgrade_component_type.time_upgrade) {
						event.getWhoClicked().sendMessage("已取出升级组件，升级进度归零");
						Upgrade_component.set_process(item, 0);
					}
				}
			}
		}
	}
}
