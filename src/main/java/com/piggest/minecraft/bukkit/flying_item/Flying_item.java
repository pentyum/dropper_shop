package com.piggest.minecraft.bukkit.flying_item;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.NamespacedKey;

public class Flying_item {
	public final static NamespacedKey flyting_time_namespacedkey = new NamespacedKey(Dropper_shop_plugin.instance,
			"flying_time");

	public static void init() {
		// NamespacedKey flying_item_key = new
		// NamespacedKey(Dropper_shop_plugin.instance, "flying_item");
		// Dropper_shop_plugin.instance.getLogger().info("开始初始化飞行道具");
		// Material_ext.register(flying_item_key, new
		// ItemStack(Material.FIREWORK_ROCKET));
	}
}
