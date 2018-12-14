package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Depository_listener implements Listener {
	@EventHandler
	public void on_break_depository(BlockBreakEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Block break_block = event.getBlock();
		Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(null,
				break_block.getLocation(), false);
		if (depository != null) {
			event.getPlayer().sendMessage("使用/depository remove移除存储器后才能破坏存储器方块(所有物品都会消失!)");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block break_block = event.getClickedBlock();
			Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(null,
					break_block.getLocation(), false);
			if (depository != null) {
				player.sendMessage(depository.get_info());
			}
		}
	}

	@EventHandler
	public void on_use_reader(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item == null) {
			return;
		}
		ItemMeta item_meta = item.getItemMeta();
		if (Reader.is_reader(item)) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				String material_str = Reader.lore_parse_material_str(item_meta.getLore());
				Material material = Material_ext.get_material(material_str);
				Location loc = Reader.lore_parse_loction(item_meta.getLore());
				// Bukkit.getLogger().info(loc.toString());
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
				if (material == null) {
					event.setCancelled(true);
					return;
				}
				if (depository == null) {
					event.getPlayer().sendMessage("原存储器已经被移除");
					event.setCancelled(true);
					return;
				}
				if (depository.is_accessible() == false) {
					event.getPlayer().sendMessage("原存储器目前处于不可访问的状态");
					event.setCancelled(true);
					return;
				}
				Block block = event.getClickedBlock();
				if (block.getState() instanceof InventoryHolder) {
					if (!event.getPlayer().isSneaking()) {
						return;
					}
				}
				if (material.isBlock() == true) {
					if (event.isCancelled() == true) {
						return;
					}
					BlockFace face = event.getBlockFace();
					if (block.getType() == Material.GRASS || block.getType() == Material.TALL_GRASS) {
						if (depository.remove(material_str) != null) {
							block.setType(material);
						}
					} else {
						Location place_loc = block.getLocation();
						place_loc.add(face.getModX(), face.getModY(), face.getModZ());
						Block replaced_block = place_loc.getBlock();
						Material replaced = replaced_block.getType();
						// event.getPlayer().sendMessage(replaced.name());
						if (replaced == Material.AIR || replaced == Material.CAVE_AIR || replaced == Material.WATER
								|| replaced == Material.GRASS || replaced == Material.LAVA) {
							if (depository.remove(material_str) != null) {
								replaced_block.setType(material);
							}
						}
					}
				} else {
					ItemStack new_item = depository.remove(material_str);
					if (new_item != null) {
						event.getPlayer().getInventory().addItem(new_item);
					}
				}
				Reader.item_lore_update(item);
				event.setCancelled(true);
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
		if (event.getClickedInventory().getName().equals("存储器")) {
			ItemStack item = event.getCurrentItem();
			if (item != null && item.getType() != Material.AIR) {
				// event.getWhoClicked().sendMessage("你点击了" + item.getType().name());
				if (Update_component.is_component(item)) {
					Update_component.set_process(item, 0);
				}
			}
		}
	}
}
