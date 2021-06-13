package com.piggest.minecraft.bukkit.material_ext;

import com.piggest.minecraft.bukkit.advanced_furnace.Gas_bottle;
import com.piggest.minecraft.bukkit.advanced_furnace.Status;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_namespace;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.language.Item_zh_cn;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Material_ext {
	private static final HashMap<NamespacedKey, ItemStack> ext_material_map = new HashMap<>();

	protected String material_name;
	protected String chinese_name;

	public Material_ext(String material_name, String chinese_name) {
		this.material_name = material_name;
		this.chinese_name = chinese_name;
	}

	public String get_material_name() {
		return this.material_name;
	}

	public String get_chinese_name() {
		return this.chinese_name;
	}

	/*
	 * 获得材质名称
	 */
	public static String get_name(Material material) {
		return material.name();
	}

	/*
	 * 获得显示名称
	 */
	public static String get_display_name(ItemStack item) {
		if (item.hasItemMeta() == true) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName() == true) {
				return meta.getDisplayName();
			}
		}
		return Item_zh_cn.get_item_name(item);
	}

	/**
	 * 获得内部名称，如stone
	 **/
	@Nonnull
	public static String get_id_name(@Nonnull ItemStack item) {
		NamespacedKey namespacedkey = get_namespacedkey(item);
		return namespacedkey.getKey();
	}

	/**
	 * 获得带命名空间的全名，如minecraft:stone
	 **/
	@Nonnull
	public static String get_full_name(@Nonnull ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return item.getType().getKey().toString();
		}
		String ext_id = meta.getPersistentDataContainer().get(Dropper_shop_namespace.ext_id, PersistentDataType.STRING);
		if (ext_id != null) {
			return ext_id;
		}
		return item.getType().getKey().toString();
	}

	/**
	 * 根据内部ID生成ItemStack，等效于new ItemStack(Material)
	 */
	@Nullable
	public static ItemStack new_item(NamespacedKey namespacedkey, int num) {
		return new_item(namespacedkey, num, null);
	}

	/**
	 * 以名称新建物品，如果原版中没找到，则到本插件中寻找。
	 **/
	@Nullable
	public static ItemStack new_item_full_name(String full_name, int num) {
		return new_item(get_namespacedkey(full_name), num);
	}

	/**
	 * 以名称新建物品，如果原版中没找到，则到本插件中寻找。
	 **/
	@Nullable
	public static ItemStack new_item(String id_name, int num) {
		NamespacedKey namespacedkey = null;
		if (Material.getMaterial(id_name.toUpperCase()) == null) {
			namespacedkey = Dropper_shop_plugin.get_key(id_name);
		} else {
			namespacedkey = NamespacedKey.minecraft(id_name.toLowerCase());
		}
		return new_item(namespacedkey, num);
	}

	/**
	 * 新建特定namespacedkey的物品。
	 **/
	@Nullable
	private static ItemStack new_item(NamespacedKey namespacedkey, int num, Map<Enchantment, Integer> enchantments) {
		ItemStack new_item;
		if (namespacedkey.getNamespace().equals(NamespacedKey.MINECRAFT)) {
			Material material = get_material(namespacedkey);
			if (material == null) {
				return null;
			}
			new_item = new ItemStack(material, num);
		} else {
			new_item = Material_ext.ext_material_map.get(namespacedkey).clone();
			new_item.setAmount(num);
		}
		if (enchantments != null) {
			new_item.addEnchantments(enchantments);
		}
		return new_item;
	}

	/**
	 * 以namespacedkey、材质、名称、物品模型ID注册物品
	 *
	 * @return 注册出的物品.
	 **/
	@Nonnull
	public static ItemStack register(NamespacedKey namespacedkey, Material material_base, String name, int model_data) {
		ItemStack item = new ItemStack(material_base);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.RESET + name);
		itemmeta.setCustomModelData(model_data);
		item.setItemMeta(itemmeta);
		Material_ext.register(namespacedkey, item);
		return item;
	}

	/**
	 * 以namespacedkey注册物品
	 **/
	public static void register(NamespacedKey namespacedkey, ItemStack item) {
		// item = NMS_manager.ext_id_provider.set_ext_id(item,
		// namespacedkey.toString());
		ItemMeta meta = item.getItemMeta();
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(Dropper_shop_plugin.get_key("ext_id"), PersistentDataType.STRING,
				namespacedkey.toString());
		item.setItemMeta(meta);
		Material_ext.ext_material_map.put(namespacedkey, item.clone());
	}

	/**
	 * 注册本插件命名空间下的物品
	 **/
	public static void register(String id_name, ItemStack itemstack) {
		NamespacedKey namespacedkey = Dropper_shop_plugin.get_key(id_name);
		register(namespacedkey, itemstack);
	}

	public static boolean is_registered(String full_name) {
		NamespacedKey namespacedkey = Material_ext.get_namespacedkey(full_name);
		if (Material_ext.ext_material_map.get(namespacedkey) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 强制修改某物品的ext_id，需要名称已被注册，返回值为修改后的物品。
	 **/
	public static void set_full_name(ItemStack item, String full_name) {
		if (!Material_ext.is_registered(full_name)) {
			// return null;
		}
		ItemMeta meta = item.getItemMeta();
		meta.getPersistentDataContainer().set(Dropper_shop_namespace.ext_id, PersistentDataType.STRING, full_name);
		// item = NMS_manager.ext_id_provider.set_ext_id(item, full_name);
		// return item;
	}

	@Nullable
	private static Material get_material(NamespacedKey namespacedkey) { // 根据内部ID获得材质
		if (namespacedkey.getNamespace().equals(NamespacedKey.MINECRAFT)) {
			return Material.getMaterial(namespacedkey.getKey().toUpperCase());
		} else {
			ItemStack item = Material_ext.ext_material_map.get(namespacedkey);
			if (item != null) {
				return item.getType();
			} else {
				return null;
			}
		}
	}

	@Nullable
	public static Material get_material(String full_name) {
		NamespacedKey namespacedkey = Material_ext.get_namespacedkey(full_name);
		return get_material(namespacedkey);
	}

	/**
	 * 获得namespacedkey
	 **/
	@Nullable
	@SuppressWarnings("deprecation")
	public static NamespacedKey get_namespacedkey(@Nonnull String full_name) {
		String[] namespace_and_key = full_name.split(":");
		if (namespace_and_key.length == 2) {
			NamespacedKey namespacedkey = new NamespacedKey(namespace_and_key[0], namespace_and_key[1]);
			return namespacedkey;
		}
		return null;
	}

	/**
	 * 获得namespacedkey
	 **/
	@Nullable
	public static NamespacedKey get_namespacedkey(@Nonnull ItemStack item) {
		String full_name = get_full_name(item);
		return get_namespacedkey(full_name);
	}

	public static Status is_empty_container(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals(Reader.id_name)) {
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			id_name = Reader.get_content_id_name(container);
		}
		switch (id_name) {
			case "bucket":
			case "glass_bottle":
				return Status.liquid;
			case "gas_bottle":
				if (Gas_bottle.calc_capacity(item) == 0) {
					return Status.gas;
				}
			default:
				return null;
		}
	}

	public static String full_name_to_id_name(String full_name) {
		String[] namespace_and_key = full_name.split(":");
		if (namespace_and_key.length == 2) {
			return namespace_and_key[1];
		}
		return null;
	}

	public static ItemStack[] split_to_max_stack_size(ItemStack item) {
		int quantity = item.getAmount();
		int max_stack_size = item.getMaxStackSize();
		int stacks = (quantity - 1) / max_stack_size + 1;
		ItemStack[] items = new ItemStack[stacks];
		for (int i = 0; i < stacks; i++) {
			items[i] = item.clone();
			int amount = quantity - max_stack_size > 0 ? max_stack_size : quantity;
			items[i].setAmount(amount);
			quantity -= amount;
		}
		return items;
	}

	public static ItemStack get_empty_container(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals(Reader.id_name)) {
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			id_name = Reader.get_content_id_name(container);
		}
		switch (id_name) {
			case "lava_bucket":
				return new ItemStack(Material.BUCKET);
			case "water_bucket":
				return new ItemStack(Material.BUCKET);
			case "milk_bucket":
				return new ItemStack(Material.BUCKET);
			case "potion":
				return new ItemStack(Material.GLASS_BOTTLE);
			case "gas_bottle":
				return Material_ext.new_item("gas_bottle", 1);
			default:
				return null;
		}
	}

	public static ArrayList<String> get_ext_full_name_list() {
		ArrayList<String> list = new ArrayList<String>();
		ext_material_map.keySet().forEach(key -> list.add(key.toString()));
		return list;
	}

}
