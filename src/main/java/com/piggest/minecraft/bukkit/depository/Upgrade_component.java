package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_manager;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Upgrade_component {
	public static final String name = "§r存储升级组件";
	public static final String id_name = "depository_upgrade_component";

	public static final String overload_name = "§r高速升级组件";
	public static final String overload_id_name = "overload_upgrade_component";

	public static final String time_name = "§r长时升级组件";
	public static final String time_id_name = "time_upgrade_component";

	public static ItemStack[] component_item = new ItemStack[Depository.capacity_level.length];
	public static ItemStack[] overload_component_item = new ItemStack[6];
	public static ItemStack[] time_component_item = new ItemStack[6];

	public static Upgrade_component_type get_type(ItemStack item) {
		if (item == null) {
			return Upgrade_component_type.NOT_UPGRADE_COMPONENT;
		}
		String ext_id = Material_ext.get_id_name(item);
		if (ext_id == null) {
			return Upgrade_component_type.NOT_UPGRADE_COMPONENT;
		}
		switch (ext_id) {
			case Upgrade_component.id_name:
				return Upgrade_component_type.depository_upgrade;
			case Upgrade_component.overload_id_name:
				return Upgrade_component_type.overload_upgrade;
			case Upgrade_component.time_id_name:
				return Upgrade_component_type.time_upgrade;
			default:
				return Upgrade_component_type.NOT_UPGRADE_COMPONENT;
		}
	}

	public static void set_process(ItemStack item, int process) {
		int new_damage = process * 250 / 100;
		ItemMeta meta = item.getItemMeta();
		Damageable damageable = (Damageable) meta;
		List<String> lore = meta.getLore();
		lore.set(1, "§7升级进度: " + process + "%");
		meta.setLore(lore);
		damageable.setDamage(250 - new_damage);
		item.setItemMeta(meta);
	}

	public static void set_level(ItemStack item, int level) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7升级等级: " + level);
		if (Upgrade_component.get_type(item) == Upgrade_component_type.depository_upgrade) {
			lore.set(5, "§9+" + Depository.capacity_level[level - 1] + " 容量");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	private static ItemStack[] init_depository_component() {
		Dropper_shop_plugin.instance.getLogger().info("开始初始化存储器升级组件");
		ItemStack component_item = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = component_item.getItemMeta();
		meta.setDisplayName(Upgrade_component.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7升级等级: 1");
		lore.add("§7升级进度: 0%");
		lore.add("");
		lore.add("§7在存储器中:");
		lore.add("§9+" + 1 + " 种类");
		lore.add("§9+" + Depository.capacity_level[0] + " 容量");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		component_item.setItemMeta(meta);
		Upgrade_component.set_process(component_item, 0);
		Material_ext.register(Upgrade_component.id_name, component_item);
		component_item = Material_ext.new_item(id_name, 1);
		for (int level = 0; level < Upgrade_component.component_item.length; level++) {
			Upgrade_component.component_item[level] = component_item.clone();
			Upgrade_component.set_level(Upgrade_component.component_item[level], level + 1);
		}
		return Upgrade_component.component_item;
	}

	private static void init_adv_furnace_overload_component() {
		Dropper_shop_plugin.instance.getLogger().info("开始初始化高级熔炉高速升级组件");
		ItemStack component_item = new ItemStack(Material.IRON_AXE);
		ItemMeta meta = component_item.getItemMeta();
		meta.setDisplayName(Upgrade_component.overload_name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7升级等级: 1");
		lore.add("§7升级进度: 0%");
		lore.add("");
		lore.add("§7在高级熔炉中:");
		lore.add("§9+" + Advanced_furnace_manager.instance.get_power_add_per_overload_upgrade() + "% 燃烧功率");
		lore.add("§9-" + Advanced_furnace_manager.instance.get_time_loss_per_overload_upgrade() + "% 燃烧时间");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		component_item.setItemMeta(meta);
		Upgrade_component.set_process(component_item, 0);
		Material_ext.register(Upgrade_component.overload_id_name, component_item);
		component_item = Material_ext.new_item(overload_id_name, 1);
		for (int level = 0; level < Upgrade_component.overload_component_item.length; level++) {
			Upgrade_component.overload_component_item[level] = component_item.clone();
			Upgrade_component.set_level(Upgrade_component.overload_component_item[level], level + 1);
		}
	}

	private static void init_adv_furnace_time_component() {
		Dropper_shop_plugin.instance.getLogger().info("开始初始化高级熔炉长时升级组件");
		ItemStack component_item = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = component_item.getItemMeta();
		meta.setDisplayName(Upgrade_component.time_name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7升级等级: 1");
		lore.add("§7升级进度: 0%");
		lore.add("");
		lore.add("§7在高级熔炉中:");
		lore.add("§9+" + Advanced_furnace_manager.instance.get_time_add_per_time_upgrade() + "% 燃烧时间");
		lore.add("§9-" + Advanced_furnace_manager.instance.get_power_loss_per_time_upgrade() + "% 燃烧功率");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		component_item.setItemMeta(meta);
		Upgrade_component.set_process(component_item, 0);
		Material_ext.register(Upgrade_component.time_id_name, component_item);
		component_item = Material_ext.new_item(time_id_name, 1);
		for (int level = 0; level < Upgrade_component.time_component_item.length; level++) {
			Upgrade_component.time_component_item[level] = component_item.clone();
			Upgrade_component.set_level(Upgrade_component.time_component_item[level], level + 1);
		}
	}

	private static void set_depositoiry_recipe() {
		NamespacedKey depository_upgrade0 = new NamespacedKey(Dropper_shop_plugin.instance, "upgrade_component_0");
		NamespacedKey depository_upgrade1 = new NamespacedKey(Dropper_shop_plugin.instance, "upgrade_component_1");
		NamespacedKey depository_upgrade2 = new NamespacedKey(Dropper_shop_plugin.instance, "upgrade_component_2");
		NamespacedKey depository_upgrade3 = new NamespacedKey(Dropper_shop_plugin.instance, "upgrade_component_3");
		NamespacedKey depository_upgrade4 = new NamespacedKey(Dropper_shop_plugin.instance, "upgrade_component_4");

		ShapedRecipe sr0 = new ShapedRecipe(depository_upgrade0, Upgrade_component.component_item[0]);
		sr0.shape("ibi", "bcb", "ibi");
		sr0.setIngredient('b', Material.IRON_BARS);
		sr0.setIngredient('c', Material.CHEST);
		sr0.setIngredient('i', Material.IRON_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr0);
		Dropper_shop_plugin.instance.getLogger().info("存储器1级升级模块合成表已经添加");

		ShapedRecipe sr1 = new ShapedRecipe(depository_upgrade1, Upgrade_component.component_item[1]);
		sr1.shape("bcb", "cpc", "bcb");
		sr1.setIngredient('c', Material.CHEST);
		sr1.setIngredient('p', Material.IRON_PICKAXE);
		sr1.setIngredient('b', Material.QUARTZ_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("存储器2级升级模块合成表已经添加");

		ShapedRecipe sr2 = new ShapedRecipe(depository_upgrade2, Upgrade_component.component_item[2]);
		sr2.shape("bcb", "cpc", "bcb");
		sr2.setIngredient('c', Material.CHEST);
		sr2.setIngredient('p', Material.IRON_PICKAXE);
		sr2.setIngredient('b', Material.GOLD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr2);
		Dropper_shop_plugin.instance.getLogger().info("存储器3级升级模块合成表已经添加");

		ShapedRecipe sr3 = new ShapedRecipe(depository_upgrade3, Upgrade_component.component_item[3]);
		sr3.shape("bcb", "cpc", "bcb");
		sr3.setIngredient('c', Material.CHEST);
		sr3.setIngredient('p', Material.IRON_PICKAXE);
		sr3.setIngredient('b', Material.EMERALD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr3);
		Dropper_shop_plugin.instance.getLogger().info("存储器4级升级模块合成表已经添加");

		ShapedRecipe sr4 = new ShapedRecipe(depository_upgrade4, Upgrade_component.component_item[4]);
		sr4.shape("bcb", "cpc", "bcb");
		sr4.setIngredient('c', Material.ENDER_CHEST);
		sr4.setIngredient('p', Material.IRON_PICKAXE);
		sr4.setIngredient('b', Material.DIAMOND_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr4);
		Dropper_shop_plugin.instance.getLogger().info("存储器5级升级模块合成表已经添加");
	}

	private static void set_overload_recipe() {
		NamespacedKey overload0 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_0");
		NamespacedKey overload1 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_1");
		NamespacedKey overload2 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_2");
		NamespacedKey overload3 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_3");
		NamespacedKey overload4 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_4");
		NamespacedKey overload5 = new NamespacedKey(Dropper_shop_plugin.instance, "overload_component_5");

		ShapedRecipe sr0 = new ShapedRecipe(overload0, Upgrade_component.overload_component_item[0]);
		sr0.shape("igi", "gcg", "igi");
		sr0.setIngredient('g', Material.GLOWSTONE);
		sr0.setIngredient('c', Material.GOLDEN_HORSE_ARMOR);
		sr0.setIngredient('i', Material.IRON_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr0);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级0->1模块合成表已经添加");

		ShapedRecipe sr1 = new ShapedRecipe(overload1, Upgrade_component.overload_component_item[1]);
		sr1.shape("bgb", "gpg", "bgb");
		sr1.setIngredient('g', Material.GLOWSTONE);
		sr1.setIngredient('p', Material.IRON_AXE);
		sr1.setIngredient('b', Material.QUARTZ_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级1->2模块合成表已经添加");

		ShapedRecipe sr2 = new ShapedRecipe(overload2, Upgrade_component.overload_component_item[2]);
		sr2.shape("bgb", "gpg", "bgb");
		sr2.setIngredient('g', Material.GLOWSTONE);
		sr2.setIngredient('p', Material.IRON_AXE);
		sr2.setIngredient('b', Material.GOLD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr2);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级2->3模块合成表已经添加");

		ShapedRecipe sr3 = new ShapedRecipe(overload3, Upgrade_component.overload_component_item[3]);
		sr3.shape("bgb", "gpg", "bgb");
		sr3.setIngredient('g', Material.GLOWSTONE);
		sr3.setIngredient('p', Material.IRON_AXE);
		sr3.setIngredient('b', Material.EMERALD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr3);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级3->4模块合成表已经添加");

		ShapedRecipe sr4 = new ShapedRecipe(overload4, Upgrade_component.overload_component_item[4]);
		sr4.shape("bcb", "cpc", "bcb");
		sr4.setIngredient('c', Material.END_CRYSTAL);
		sr4.setIngredient('p', Material.IRON_AXE);
		sr4.setIngredient('b', Material.DIAMOND_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr4);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级4->5模块合成表已经添加");

		ShapedRecipe sr5 = new ShapedRecipe(overload5, Upgrade_component.overload_component_item[5]);
		sr5.shape("bcb", "cpc", "bcb");
		sr5.setIngredient('c', Material.CONDUIT);
		sr5.setIngredient('p', Material.IRON_AXE);
		sr5.setIngredient('b', Material.NETHERITE_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr5);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级5->6模块合成表已经添加");
	}

	private static void set_time_recipe() {
		NamespacedKey time0 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_0");
		NamespacedKey time1 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_1");
		NamespacedKey time2 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_2");
		NamespacedKey time3 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_3");
		NamespacedKey time4 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_4");
		NamespacedKey time5 = new NamespacedKey(Dropper_shop_plugin.instance, "time_component_5");

		ShapedRecipe sr0 = new ShapedRecipe(time0, Upgrade_component.time_component_item[0]);
		sr0.shape("iri", "rhr", "iri");
		sr0.setIngredient('r', Material.REDSTONE_BLOCK);
		sr0.setIngredient('h', Material.GOLDEN_HORSE_ARMOR);
		sr0.setIngredient('i', Material.IRON_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr0);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉长时升级0->1模块合成表已经添加");

		ShapedRecipe sr1 = new ShapedRecipe(time1, Upgrade_component.time_component_item[1]);
		sr1.shape("brb", "rhr", "brb");
		sr1.setIngredient('r', Material.REDSTONE_BLOCK);
		sr1.setIngredient('h', Material.IRON_HOE);
		sr1.setIngredient('b', Material.QUARTZ_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉长时升级1->2模块合成表已经添加");

		ShapedRecipe sr2 = new ShapedRecipe(time2, Upgrade_component.time_component_item[2]);
		sr2.shape("brb", "rhr", "brb");
		sr2.setIngredient('r', Material.REDSTONE_BLOCK);
		sr2.setIngredient('h', Material.IRON_HOE);
		sr2.setIngredient('b', Material.GOLD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr2);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉长时升级2->3模块合成表已经添加");

		ShapedRecipe sr3 = new ShapedRecipe(time3, Upgrade_component.time_component_item[3]);
		sr3.shape("brb", "rhr", "brb");
		sr3.setIngredient('r', Material.REDSTONE_BLOCK);
		sr3.setIngredient('h', Material.IRON_HOE);
		sr3.setIngredient('b', Material.EMERALD_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr3);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉长时升级3->4模块合成表已经添加");

		ShapedRecipe sr4 = new ShapedRecipe(time4, Upgrade_component.time_component_item[4]);
		sr4.shape("bcb", "cpc", "bcb");
		sr4.setIngredient('c', Material.END_CRYSTAL);
		sr4.setIngredient('p', Material.IRON_HOE);
		sr4.setIngredient('b', Material.DIAMOND_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr4);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级4->5模块合成表已经添加");

		ShapedRecipe sr5 = new ShapedRecipe(time5, Upgrade_component.time_component_item[5]);
		sr5.shape("bcb", "cpc", "bcb");
		sr5.setIngredient('c', Material.CONDUIT);
		sr5.setIngredient('p', Material.IRON_HOE);
		sr5.setIngredient('b', Material.NETHERITE_BLOCK);
		Dropper_shop_plugin.instance.add_recipe(sr5);
		Dropper_shop_plugin.instance.getLogger().info("高级熔炉高速升级5->6模块合成表已经添加");
	}

	public static void set_recipe() {
		Upgrade_component.set_depositoiry_recipe();
		Upgrade_component.set_overload_recipe();
		Upgrade_component.set_time_recipe();
	}

	public static int get_level(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		int level;
		String line = lore.get(0);
		String pattern = "§7升级等级: ([1-9]\\d*|0)";
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
		String pattern = "§7升级进度: ([1-9]\\d*|0)%";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			num = Integer.parseInt(m.group(1));
			return num;
		}
		return 0;
	}

	public static void init_component() {
		Upgrade_component.init_depository_component();
		Upgrade_component.init_adv_furnace_overload_component();
		Upgrade_component.init_adv_furnace_time_component();
	}

	public static boolean is_component(ItemStack item) {
		return Upgrade_component.get_type(item) != Upgrade_component_type.NOT_UPGRADE_COMPONENT;
	}

}
