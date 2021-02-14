package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_namespace;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Reader {
	public static String name = "§e存储读取器";
	public static String id_name = "depository_remote_reader";
	private static final NamespacedKey namespace = new NamespacedKey(Dropper_shop_plugin.instance,
			"depository_remote_reader");
	private static final NamespacedKey storage_material = new NamespacedKey(Dropper_shop_plugin.instance,
			"storage_material");
	private static final NamespacedKey storage_num = new NamespacedKey(Dropper_shop_plugin.instance,
			"storage_num");

	public static Location get_location(PersistentDataContainer reader_data_container) {
		PersistentDataContainer location_container = reader_data_container.get(Dropper_shop_namespace.location, PersistentDataType.TAG_CONTAINER);
		int x = location_container.get(Dropper_shop_namespace.x, PersistentDataType.INTEGER);
		int y = location_container.get(Dropper_shop_namespace.y, PersistentDataType.INTEGER);
		int z = location_container.get(Dropper_shop_namespace.z, PersistentDataType.INTEGER);
		String world_name = location_container.get(Dropper_shop_namespace.world, PersistentDataType.STRING);
		return new Location(Bukkit.getWorld(world_name), x, y, z);
	}

	/**
	 *
	 * @param reader_data_container
	 * @return 存储内容的全名
	 */
	public static String get_content_full_name(PersistentDataContainer reader_data_container) {
		return reader_data_container.get(storage_material, PersistentDataType.STRING);
	}

	public static String get_content_id_name(PersistentDataContainer reader_data_container) {
		String full_name = get_content_full_name(reader_data_container);
		return Material_ext.full_name_to_id_name(full_name);
	}

	public static int get_content_num(PersistentDataContainer reader_data_container) {
		return reader_data_container.get(storage_num, PersistentDataType.INTEGER);
	}

	/*
	public static Location lore_parse_loction(List<String> lore) {
		String world_name = null;
		int x = 0;
		int y = 0;
		int z = 0;
		String line = lore.get(0);
		String pattern = "§r存储器位置: (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			world_name = m.group(1);
		}
		line = lore.get(1);
		pattern = "§rx: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			x = Integer.parseInt(m.group(1));
		}
		line = lore.get(2);
		pattern = "§ry: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			y = Integer.parseInt(m.group(1));
		}
		line = lore.get(3);
		pattern = "§rz: (-?\\d+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			if (m.group(1).equals("null")) {
				return null;
			}
			z = Integer.parseInt(m.group(1));
		}
		return new Location(Bukkit.getWorld(world_name), x, y, z);
	}

	public static String lore_parse_material_str(List<String> lore) {
		String material_name;
		String line = lore.get(4);
		String pattern = "§r物品: (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			material_name = m.group(1);
			//Bukkit.getLogger().info("("+material_name+")");
			return material_name;
		}
		return null;
	}

	public static int lore_parse_num(List<String> lore) {
		int num;
		String line = lore.get(5);
		String pattern = "§r数量: ([1-9]\\d*|0)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			num = Integer.parseInt(m.group(1));
			return num;
		}
		return 0;
	}

	 */

	public static ArrayList<String> get_lore(Location loc, String material, int num) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§r存储器位置: " + loc.getWorld().getName());
		lore.add("§rx: " + loc.getBlockX());
		lore.add("§ry: " + loc.getBlockY());
		lore.add("§rz: " + loc.getBlockZ());
		lore.add("§r物品: " + material);
		lore.add("§r数量: " + num);
		return lore;
	}

	public static void lore_update(ItemStack reader) {
		ItemMeta meta = reader.getItemMeta();
		PersistentDataContainer data_container = meta.getPersistentDataContainer();
		Location loc = get_location(data_container);
		String material_name = get_content_full_name(data_container);
		List<String> lore;
		Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
		if (depository == null) {
			lore = new ArrayList<>();
			lore.add("§r存储器位置: null");
			lore.add("§rx: null");
			lore.add("§ry: null");
			lore.add("§rz: null");
			lore.add("§r物品: null");
			lore.add("§r数量: 0");
		} else {
			lore = meta.getLore();
			lore.set(5, "§r数量: " + depository.get_material_num(material_name));
		}
		// Bukkit.getLogger().info("new num" +
		// depository.get_material_num(material.name()));
		meta.setLore(lore);
		reader.setItemMeta(meta);
	}


	public static boolean is_reader(ItemStack item) {
		if (item == null) {
			return false;
		}
		String ext_id = Material_ext.get_id_name(item);
		if (ext_id == null) {
			return false;
		}
		return ext_id.equals(Reader.id_name);
	}

	public static void init_reader_item() {
		ItemStack reader_item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = reader_item.getItemMeta();
		meta.setDisplayName(Reader.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r存储器位置: null");
		lore.add("§rx: null");
		lore.add("§ry: null");
		lore.add("§rz: null");
		lore.add("§r物品: null");
		lore.add("§r数量: 0");
		meta.setLore(lore);
		reader_item.setItemMeta(meta);
		Material_ext.register(id_name, reader_item);
	}

	public static void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(Reader.namespace, Material_ext.new_item(id_name, 1));
		sr1.shape("rsr", "scs", "rsr");
		sr1.setIngredient('s', Material.NETHER_STAR);
		sr1.setIngredient('c', Material.ENDER_CHEST);
		sr1.setIngredient('r', Material.END_CRYSTAL);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("存储读取器合成表已添加");
	}
}
