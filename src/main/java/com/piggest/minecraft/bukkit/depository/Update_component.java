package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Update_component {
	public static String name = "§r存储升级组件";
	public static String adv_furnace_overload_name = "高速升级组件";
	public static String adv_furnace_time_name = "长时升级组件";
	public static ItemStack[] component_item = new ItemStack[Depository.capacity_level.length];
	public static ItemStack[] overload_component_item = new ItemStack[5];
	public static ItemStack[] time_component_item = new ItemStack[5];
	
	public static NamespacedKey namespace0 = null;
	public static NamespacedKey namespace1 = null;
	public static NamespacedKey namespace2 = null;
	public static NamespacedKey namespace3 = null;
	public static NamespacedKey namespace4 = null;
	
	public static NamespacedKey overload_namespace0 = null;
	public static NamespacedKey overload_namespace1 = null;
	public static NamespacedKey overload_namespace2 = null;
	public static NamespacedKey overload_namespace3 = null;
	public static NamespacedKey overload_namespace4 = null;
	
	public static NamespacedKey time_namespace0 = null;
	public static NamespacedKey time_namespace1 = null;
	public static NamespacedKey time_namespace2 = null;
	public static NamespacedKey time_namespace3 = null;
	public static NamespacedKey time_namespace4 = null;
	
	public static boolean is_component(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getItemMeta().hasDisplayName() == false) {
			return false;
		}
		return item.getItemMeta().getDisplayName().equals(Update_component.name);
	}

	@SuppressWarnings("deprecation")
	public static void set_process(ItemStack item, int process) {
		short new_damage = (short) (process * 250 / 100);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, "§r升级进度: " + process + "%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.setDurability((short) (250 - new_damage));
	}

	public static void set_level(ItemStack item, int level) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§r升级等级: " + level);
		lore.set(5, "§9+" + Depository.capacity_level[level] + " 容量");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static ItemStack[] init_component() {
		ItemStack component_item = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = component_item.getItemMeta();
		meta.setDisplayName(Update_component.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r升级等级: 0");
		lore.add("§r升级进度: 0%");
		lore.add("");
		lore.add("§7在存储器中:");
		lore.add("§9+" + 1 + " 种类");
		lore.add("§9+" + Depository.capacity_level[0] + " 容量");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		component_item.setItemMeta(meta);
		Update_component.set_process(component_item, 0);
		for (int level = 0; level < Update_component.component_item.length; level++) {
			Update_component.component_item[level] = component_item.clone();
			Update_component.set_level(Update_component.component_item[level], level);
		}
		return Update_component.component_item;
	}

	public static void set_recipe() {
		Update_component.namespace0 = new NamespacedKey(Dropper_shop_plugin.instance, "update_component_0");
		Update_component.namespace1 = new NamespacedKey(Dropper_shop_plugin.instance, "update_component_1");
		Update_component.namespace2 = new NamespacedKey(Dropper_shop_plugin.instance, "update_component_2");
		Update_component.namespace3 = new NamespacedKey(Dropper_shop_plugin.instance, "update_component_3");
		Update_component.namespace4 = new NamespacedKey(Dropper_shop_plugin.instance, "update_component_4");
		
		Update_component.overload_namespace0 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_0");
		Update_component.overload_namespace1 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_1");
		Update_component.overload_namespace2 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_2");
		Update_component.overload_namespace3 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_3");
		Update_component.overload_namespace4 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_4");
		
		Update_component.time_namespace0 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_0");
		Update_component.time_namespace1 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_1");
		Update_component.time_namespace2 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_2");
		Update_component.time_namespace3 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_3");
		Update_component.time_namespace4 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_4");
		
		ShapedRecipe sr0 = new ShapedRecipe(namespace0, Update_component.component_item[0]);
		sr0.shape("ibi", "bcb", "ibi");
		sr0.setIngredient('b', Material.IRON_BARS);
		sr0.setIngredient('c', Material.CHEST);
		sr0.setIngredient('i', Material.IRON_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr0);
		Dropper_shop_plugin.instance.get_sr().add(sr0);
		Dropper_shop_plugin.instance.getLogger().info("存储器升级模块合成表已经添加");

		ShapedRecipe sr1 = new ShapedRecipe(namespace1, Update_component.component_item[1]);
		sr1.shape("bcb", "cpc", "bcb");
		sr1.setIngredient('c', Material.CHEST);
		sr1.setIngredient('p', Material.IRON_PICKAXE);
		sr1.setIngredient('b', Material.QUARTZ_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr1);
		Dropper_shop_plugin.instance.get_sr().add(sr1);
		Dropper_shop_plugin.instance.getLogger().info("存储器1级升级模块合成表已经添加");

		ShapedRecipe sr2 = new ShapedRecipe(namespace2, Update_component.component_item[2]);
		sr2.shape("bcb", "cpc", "bcb");
		sr2.setIngredient('c', Material.CHEST);
		sr2.setIngredient('p', Material.IRON_PICKAXE);
		sr2.setIngredient('b', Material.GOLD_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr2);
		Dropper_shop_plugin.instance.get_sr().add(sr2);
		Dropper_shop_plugin.instance.getLogger().info("存储器2级升级模块合成表已经添加");

		ShapedRecipe sr3 = new ShapedRecipe(namespace3, Update_component.component_item[3]);
		sr3.shape("bcb", "cpc", "bcb");
		sr3.setIngredient('c', Material.CHEST);
		sr3.setIngredient('p', Material.IRON_PICKAXE);
		sr3.setIngredient('b', Material.EMERALD_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr3);
		Dropper_shop_plugin.instance.get_sr().add(sr3);
		Dropper_shop_plugin.instance.getLogger().info("存储器3级升级模块合成表已经添加");

		ShapedRecipe sr4 = new ShapedRecipe(namespace4, Update_component.component_item[4]);
		sr4.shape("bcb", "cpc", "bcb");
		sr4.setIngredient('c', Material.ENDER_CHEST);
		sr4.setIngredient('p', Material.IRON_PICKAXE);
		sr4.setIngredient('b', Material.DIAMOND_BLOCK);
		Dropper_shop_plugin.instance.getServer().addRecipe(sr4);
		Dropper_shop_plugin.instance.get_sr().add(sr4);
		Dropper_shop_plugin.instance.getLogger().info("存储器4级升级模块合成表已经添加");
	}

	public static int get_level(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int level;
		String line = lore.get(0);
		String pattern = "§r升级等级: ([1-9]\\d*|0)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			level = Integer.parseInt(m.group(1));
			return level;
		}
		return 0;
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
