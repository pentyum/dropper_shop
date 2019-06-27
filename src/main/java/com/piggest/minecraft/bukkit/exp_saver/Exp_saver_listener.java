package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

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
			inventory.setRepairCost(1);
		}
	}
}
