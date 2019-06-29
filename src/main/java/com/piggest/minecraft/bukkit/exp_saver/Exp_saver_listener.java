package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Exp_saver_listener implements Listener {

	@EventHandler
	public void onPrepareAnvil(PrepareAnvilEvent event) {
		AnvilInventory inventory = event.getInventory();
		int raw_repair_cost = inventory.getRepairCost();
		Bukkit.getLogger().info("修理需要等级:" + raw_repair_cost);
		Location loc = inventory.getLocation();
		Exp_saver exp_saver = Dropper_shop_plugin.instance.get_exp_saver_manager().find(loc, false);
		if (exp_saver != null) {
			Bukkit.getLogger().info("找到了经验存储器");
			if (exp_saver.has_anvil()) {
				Bukkit.getLogger().info("经验存储器已开启铁砧升级");
				int current_level = exp_saver.get_level();
				int target_level = current_level - raw_repair_cost + 1;
				int need_exp = SetExpFix.getExpToLevel(current_level) - SetExpFix.getExpToLevel(target_level);
				Bukkit.getLogger().info("需要" + need_exp + "点经验");
				Bukkit.getLogger().info("经验存储器有" + exp_saver.get_saved_exp() + "点经验");
				Bukkit.getLogger().info("经验存储器将从" + current_level + "降到" + target_level);
				if (raw_repair_cost < 255 && target_level >= 0 && exp_saver.get_saved_exp() >= need_exp) {
					Bukkit.getLogger().info("经验存储器经验足够，启动经验转移机制");
					inventory.setMaximumRepairCost(255);
					exp_saver.set_remove_exp_next(need_exp);
					inventory.setRepairCost(1);
				}
			}
		}
	}

	@EventHandler
	public void onAnvilRepair(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if (inventory.getType() == InventoryType.ANVIL) {
			if (event.getSlot() == 2) {
				if (event.getCurrentItem() != null) {
					if (event.getCurrentItem().getType() != Material.AIR) {
						Bukkit.getLogger().info("铁砧修理物品准备取出");
						Location loc = inventory.getLocation();
						Exp_saver exp_saver = Dropper_shop_plugin.instance.get_exp_saver_manager().find(loc, false);
						if (exp_saver != null) {
							if (exp_saver.has_anvil()) {
								Player player = (Player) event.getWhoClicked();
								if (player.getLevel() >= 1 && player.getGameMode() == GameMode.SURVIVAL) {
									int removed = exp_saver.do_remove_exp();
									if (removed > 0) {
										player.sendMessage(
												"由于经验存储器进行了铁砧升级，因此本次使用只消耗1级经验，同时从经验存储器中移除了" + removed + "点经验");
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
