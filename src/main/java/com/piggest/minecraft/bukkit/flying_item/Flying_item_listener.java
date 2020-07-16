package com.piggest.minecraft.bukkit.flying_item;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.utils.Use_block;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class Flying_item_listener implements Listener {

	@EventHandler
	public void on_login(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			player.removeScoreboardTag("flying_item");
			player.setAllowFlight(false);
		}
	}

	@EventHandler
	public void on_logout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			player.removeScoreboardTag("flying_item");
			player.setAllowFlight(false);
		}
	}

	@EventHandler
	public void on_changed_dimension(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			if (player.getScoreboardTags().contains("flying_item")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						if (player != null) {
							player.setAllowFlight(true);
							player.sendMessage("[飞行道具] 检测到世界切换，现在可以继续飞行。");
						}
					}
				}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 3 * 20);

				/* retry to ensure correctness */
				new BukkitRunnable() {
					@Override
					public void run() {
						if (player != null) {
							player.setAllowFlight(true);
						}
					}
				}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 5 * 20);

				new BukkitRunnable() {
					@Override
					public void run() {
						if (player != null) {
							player.setAllowFlight(true);
						}
					}
				}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 10 * 20);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
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
		Block clicked_block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (Use_block.is_use_block(clicked_block)) {
			if (player.isSneaking() == false) {
				return;
			}
		}
		PersistentDataContainer tags = item.getItemMeta().getPersistentDataContainer();

		if (tags.has(Flying_item.flyting_time_namespacedkey, PersistentDataType.INTEGER)) {
			if (!player.hasPermission("flying_item.use")) {
				player.sendMessage("[飞行道具]你没有权限使用飞行道具");
				event.setCancelled(true);
				return;
			}
			ItemMeta meta = item.getItemMeta();
			if (meta instanceof FireworkMeta) {
				int flying_time = tags.get(Flying_item.flyting_time_namespacedkey, PersistentDataType.INTEGER);
				if (player.getAllowFlight() == true) {
					player.sendMessage("[飞行道具]你已经是飞行状态了");
					event.setCancelled(true);
					return;
				}
				player.setAllowFlight(true);
				player.addScoreboardTag("flying_item");
				player.sendMessage("[飞行道具]你的现在可以进行持续" + flying_time + "秒的飞行");
				if (flying_time >= 60) {
					new BukkitRunnable() {
						@Override
						public void run() {
							if (player != null) {
								/* remove the tag now */
								player.removeScoreboardTag("flying_item");
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
								player.removeScoreboardTag("flying_item");
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
