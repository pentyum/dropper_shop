package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.nms.NMS_manager;

public class Material_ext {
	private static HashMap<NamespacedKey, ItemStack> ext_material_map = new HashMap<NamespacedKey, ItemStack>();

	/*
	 * 获得材质名称
	 */
	public static String get_name(Material material) {
		return material.name();
	}

	/*
	 * 获得显示名称
	 */
	public static String get_display_name(ItemStack itemstack) {
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				return meta.getDisplayName().substring(2, meta.getDisplayName().length());
			}
		}
		return itemstack.getType().name();
	}

	/*
	 * 获得内部名称，如stone
	 */
	public static String get_id_name(ItemStack item) {
		NamespacedKey namespacedkey = get_namespacedkey(item);
		if (namespacedkey == null) {
			return null;
		}
		return namespacedkey.getKey();
	}

	/*
	 * 获得带命名空间的全名，如minecraft:stone
	 */
	public static String get_full_name(ItemStack item) {
		String ext_id = NMS_manager.ext_id_provider.get_ext_id(item);
		if (ext_id != null) {
			return ext_id;
		}
		return item.getType().getKey().toString();
	}

	/*
	 * 获得namespacedkey
	 */
	@SuppressWarnings("deprecation")
	public static NamespacedKey get_namespacedkey(ItemStack item) {
		String full_name = get_full_name(item);
		String[] namespace_and_key = full_name.split(":");
		if (namespace_and_key.length == 2) {
			NamespacedKey namespacedkey = new NamespacedKey(namespace_and_key[0], namespace_and_key[1]);
			return namespacedkey;
		}
		return null;
	}

	/*
	 * 根据内部ID生成ItemStack，等效于new ItemStack(Material)
	 */
	private static ItemStack new_item(NamespacedKey namespacedkey, int num) {
		return new_item(namespacedkey, num, null);
	}

	/*
	 * 以名称新建物品，如果原版中没找到，则到本插件中寻找。
	 */
	public static ItemStack new_item(String id_name, int num) {
		NamespacedKey namespacedkey = null;
		if (Material.getMaterial(id_name.toUpperCase()) == null) {
			namespacedkey = Dropper_shop_plugin.instance.get_key(id_name);
		} else {
			namespacedkey = NamespacedKey.minecraft(id_name.toLowerCase());
		}
		return new_item(namespacedkey, num);
	}

	/*
	 * 新建特定namespacedkey的物品。
	 */
	private static ItemStack new_item(NamespacedKey namespacedkey, int num, Map<Enchantment, Integer> enchantments) {
		ItemStack new_item = null;
		if (namespacedkey.getNamespace().equals(NamespacedKey.MINECRAFT)) {
			new_item = new ItemStack(get_material(namespacedkey), num);
		} else {
			new_item = Material_ext.ext_material_map.get(namespacedkey).clone();
			new_item.setAmount(num);
		}
		if (enchantments != null) {
			new_item.addEnchantments(enchantments);
		}
		return new_item;
	}

	/*
	 * 以namespacedkey注册物品
	 */
	private static ItemStack register(NamespacedKey namespacedkey, ItemStack item) {
		item = NMS_manager.ext_id_provider.set_ext_id(item, namespacedkey.toString());
		return Material_ext.ext_material_map.put(namespacedkey, item.clone());
	}

	/*
	 * 注册本插件命名空间下的物品
	 */
	public static ItemStack register(String id_name, ItemStack itemstack) {
		NamespacedKey namespacedkey = Dropper_shop_plugin.instance.get_key(id_name);
		return register(namespacedkey, itemstack);
	}

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

	public static Material get_material(String id_name) {
		NamespacedKey namespacedkey = Dropper_shop_plugin.instance.get_key(id_name);
		return get_material(namespacedkey);
	}
}
