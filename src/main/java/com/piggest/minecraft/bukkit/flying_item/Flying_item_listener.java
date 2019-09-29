package com.piggest.minecraft.bukkit.flying_item;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.nms.NMS_manager;

public class Flying_item_listener implements Listener {

	@EventHandler
	public void on_logout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			player.setAllowFlight(false);
		}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void on_use_firework(PlayerInteractEvent event) {
		if (event.useItemInHand() == Result.DENY || event.useInteractedBlock() == Result.DENY) {
			return;
		}
		ItemStack item = event.getItem();
		if (Grinder.is_empty(item)) {
			return;
		}
		if (item.getType() != Material.FIREWORK_ROCKET) {
			return;
		}
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (NMS_manager.flying_time_provider.has_flying_time(item)) {
			ItemMeta meta = item.getItemMeta();
			if (meta instanceof FireworkMeta) {
				int flying_time = NMS_manager.flying_time_provider.get_flying_time(item);
				Player player = event.getPlayer();
				if (player.getAllowFlight() == true) {
					player.sendMessage("[飞行道具]你已经是飞行状态了");
					event.setCancelled(true);
					return;
				}
				player.setAllowFlight(true);
				player.sendMessage("[飞行道具]你的现在可以进行持续" + flying_time + "秒的飞行");
				if (flying_time >= 60) {
					new BukkitRunnable() {
						@Override
						public void run() {
							if (player != null) {
								player.sendMessage("[飞行道具]你的飞行时间还剩余30秒");
							}
						}
					}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, (flying_time - 30) * 20);
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (player != null) {
							if (player.getGameMode() == GameMode.SURVIVAL) {
								player.setAllowFlight(false);
							}
							player.sendMessage("[飞行道具]你的飞行时间已到");
						}
					}
				}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, flying_time * 20);
			}

		}
	}
}
