package com.piggest.minecraft.bukkit.material_ext;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Custom_durability implements Listener {
	public static final NamespacedKey custom_durability_namespacedkey = Dropper_shop_plugin
			.get_key("custom_durability");

	@EventHandler
	public void on_item_damage(PlayerItemDamageEvent event) {
		int damage = event.getDamage();
	}

	public static boolean has_custom_durability(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		return tag_container.has(custom_durability_namespacedkey, PersistentDataType.INTEGER);
	}

	public static int get_custom_durability(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		return tag_container.get(custom_durability_namespacedkey, PersistentDataType.INTEGER);
	}

	public static int get_durability(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return 0;
		}
		if (!(meta instanceof Damageable)) {
			return 0;
		}
		Damageable damageable = (Damageable) meta;
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		Integer custom_durability = tag_container.get(custom_durability_namespacedkey, PersistentDataType.INTEGER);
		if (custom_durability == null) {
			return damageable.getDamage();
		} else {
			return custom_durability;
		}
	}
}
