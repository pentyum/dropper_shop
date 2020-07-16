package com.piggest.minecraft.bukkit.material_ext;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Tool_material.Custom_material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Custom_durability implements Listener {
	public static final NamespacedKey custom_durability_namespacedkey = Dropper_shop_plugin
			.get_key("custom_durability");

	@EventHandler
	public void on_item_damage(PlayerItemDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		int damage = event.getDamage();
		ItemStack item = event.getItem();
		ItemMeta meta = item.getItemMeta();
		Damageable damageable = (Damageable) meta;
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		Integer custom_durability = tag_container.get(custom_durability_namespacedkey, PersistentDataType.INTEGER);
		if (custom_durability == null) {// 一般工具
			return;
		}
		// 自定义耐久工具
		int new_custom_durbility = custom_durability + damage;
		Tool_material.Custom_material custom_material = Tool_material.Custom_material.get_custom_material(item);
		int raw_max_durbility = custom_material.get_raw().get_max_durbility();
		int new_raw_durbility = raw_max_durbility * new_custom_durbility / custom_material.get_max_durbility();
		damageable.setDamage(new_raw_durbility);
		tag_container.set(custom_durability_namespacedkey, PersistentDataType.INTEGER, new_custom_durbility);
		item.setItemMeta(meta);
		if (new_raw_durbility > raw_max_durbility) {// 耐久超出
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void on_item_mending(PlayerItemMendEvent event) {
		if (event.isCancelled()) {
			return;
		}
		int repair = 2 * event.getExperienceOrb().getExperience();
		ItemStack item = event.getItem();
		if (!has_custom_durability(item)) {
			return;
		}
		add_durability(item, -repair);
		event.setCancelled(true);
	}

	public static void init_custom_durability(ItemStack item) {
		set_custom_durability(item, 0);
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

	public static void set_custom_durability(ItemStack item, int damage) {
		ItemMeta meta = item.getItemMeta();
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		tag_container.set(custom_durability_namespacedkey, PersistentDataType.INTEGER, damage);
		item.setItemMeta(meta);
	}

	public static void add_durability(ItemStack item, int damage) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return;
		}
		if (!(meta instanceof Damageable)) {
			return;
		}
		Damageable damageable = (Damageable) meta;
		PersistentDataContainer tag_container = meta.getPersistentDataContainer();
		Integer custom_durability = tag_container.get(custom_durability_namespacedkey, PersistentDataType.INTEGER);
		if (custom_durability == null) {// 一般工具
			int new_durbility = Math.max(0, damageable.getDamage() + damage);
			damageable.setDamage(new_durbility);
		} else {// 自定义耐久工具
			int new_custom_durbility = Math.max(0, custom_durability + damage);
			Custom_material custom_material = Custom_material.get_custom_material(item);
			int raw_max_durbility = custom_material.get_raw().get_max_durbility();
			int custom_max_durbility = custom_material.get_max_durbility();
			new_custom_durbility = Math.min(custom_max_durbility, new_custom_durbility);
			int new_raw_durbility = raw_max_durbility * new_custom_durbility / custom_max_durbility;
			damageable.setDamage(new_raw_durbility);
			tag_container.set(custom_durability_namespacedkey, PersistentDataType.INTEGER, new_custom_durbility);
		}
		item.setItemMeta(meta);
	}

}
