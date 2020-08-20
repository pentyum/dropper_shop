package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.config.Map_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class Map_init_listener implements Listener {
	@EventHandler
	public void on_map_init(MapInitializeEvent event) {
		MapView map = event.getMap();
		int id = map.getId();
		Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
		Custom_map_render edge_render = map_config.get_edge_from_config(id);
		Custom_map_render content_render = map_config.get_content_from_config(id);
		if (content_render != null) {
			//Dropper_shop_plugin.instance.getLogger().info("map_" + id + ": " + content_render.getClass().getSimpleName());
			Dropper_shop_plugin.instance.get_map_config().replace_render(map, content_render, edge_render);
		}
	}

	@EventHandler
	public void on_copy_map(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getType() == InventoryType.CRAFTING || inv.getType() == InventoryType.WORKBENCH) {
			HumanEntity player = event.getWhoClicked();
			if (event.getSlot() == 0 && event.getSlotType() == InventoryType.SlotType.RESULT) {
				ItemStack item = event.getCurrentItem();
				if (Custom_map_render.get_render_from_item(item) != null) {
					if (!player.hasPermission("custom_map.copy")) {
						player.sendMessage("你没有复制自定义地图的权限!");
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void on_map_click(EntityDamageByEntityEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Entity entity = event.getEntity();
		if (entity instanceof ItemFrame) {
			ItemFrame item_frame = (ItemFrame) entity;
			ItemStack item = item_frame.getItem();
			Custom_map_render c_render = Custom_map_render.get_render_from_item(item);
			if (c_render instanceof Screen_map_render) {
				Screen_map_render screen_map_render = (Screen_map_render) c_render;
				if (screen_map_render.get_screen().is_locked() == true) {
					Entity damager = event.getDamager();
					if (!damager.hasPermission("custom_map.damage_locked_map")) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
