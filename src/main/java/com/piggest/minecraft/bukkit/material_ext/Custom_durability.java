package com.piggest.minecraft.bukkit.material_ext;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class Custom_durability implements Listener {
	@EventHandler
	public void on_item_damage(PlayerItemDamageEvent event) {
		int damage = event.getDamage();
		event.getPlayer().sendMessage("消耗量:" + damage);
	}
}
