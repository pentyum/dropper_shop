package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Update_component {

	public static String name = "§r存储升级组件";
	public static ItemStack component_item;
	public static NamespacedKey namespace = new NamespacedKey(Dropper_shop_plugin.instance, "update_component");

	public static boolean is_component(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getItemMeta().hasDisplayName() == false) {
			return false;
		}
		return item.getItemMeta().getDisplayName().equals(Update_component.name);
	}

	public static void set_process(ItemStack item, int process) {
		short new_damage = (short) (process * 1561 / 100);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, "§r升级进度: " + process + "%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.setDurability((short) (1561 - new_damage));
	}

	public static ItemStack init_component() {
		ItemStack component_item = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta meta = component_item.getItemMeta();
		meta.setDisplayName(Update_component.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r升级等级: 0");
		lore.add("§r升级进度: 0%");
		meta.setLore(lore);
		component_item.setItemMeta(meta);
		Update_component.set_process(component_item, 0);
		Update_component.component_item = component_item;
		return component_item;
	}

	public static void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(Update_component.namespace, Update_component.component_item);
		sr1.shape("ibi", "bcb", "ibi");
		sr1.setIngredient('b', Material.IRON_BARS);
		sr1.setIngredient('c', Material.CHEST);
		sr1.setIngredient('i', Material.IRON_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr1);
		Dropper_shop_plugin.instance.get_sr().add(sr1);
		Dropper_shop_plugin.instance.getLogger().info("升级模块合成表已经添加");
	}

	public static int get_process(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int num;
		String line = lore.get(1);
		String pattern = "§r升级进度: ([1-9]\\d*|0)%";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			num = Integer.parseInt(m.group(1));
			return num;
		}
		return 0;
	}
}
