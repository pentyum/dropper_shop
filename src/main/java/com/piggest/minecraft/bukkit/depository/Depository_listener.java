package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Depository_listener implements Listener {
	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.useItemInHand() == Result.DENY || event.useInteractedBlock() == Result.DENY) {
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

	@EventHandler(priority = EventPriority.HIGH)
	public void on_use_reader(PlayerInteractEvent event) {
		if (event.useInteractedBlock() == Result.DENY || event.useItemInHand() == Result.DENY) {
			return;
		}
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
				if (block == null) {
					return;
				}
				if (block.getState() instanceof InventoryHolder) {
					if (!event.getPlayer().isSneaking()) {
						return;
					}
				}
				if (material.isBlock() == true) {
					if (event.useItemInHand() == Result.DENY || event.useInteractedBlock() == Result.DENY) {
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
		if (event.getClickedInventory().getHolder() instanceof Depository) {
			ItemStack item = event.getCurrentItem();
			if (item != null && item.getType() != Material.AIR) {
				// event.getWhoClicked().sendMessage("你点击了" + item.getType().name());
				if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
					Upgrade_component.set_process(item, 0);
				}
			}
		}
	}
}
