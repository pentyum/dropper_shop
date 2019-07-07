package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Material_ext {
	private static HashMap<NamespacedKey, ItemStack> ext_material_map = new HashMap<NamespacedKey, ItemStack>();
	// private static HashMap<String, ItemStack> ext_material_map = new
	// HashMap<String, ItemStack>();

	public static String get_name(Material material) { // 获得材质名称
		return material.name();
	}

	public static String get_display_name(ItemStack itemstack) { // 获得显示名称
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				return meta.getDisplayName().substring(2, meta.getDisplayName().length());
			}
		}
		return itemstack.getType().name();
	}

	public static String get_id_name(ItemStack itemstack) {
		return get_namespacedkey(itemstack).getKey();
	}

	@SuppressWarnings("deprecation")
	public static NamespacedKey get_namespacedkey(ItemStack itemstack) { // 获得内部ID名称
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(itemstack);
		if (nms_item.hasTag()) {
			NBTTagCompound tag = nms_item.getTag();
			String id_name = tag.getString("ext_id");
			if (id_name != null) {
				String[] namespace_and_key = id_name.split(":");
				NamespacedKey namespacedkey = new NamespacedKey(namespace_and_key[0], namespace_and_key[1]);
				return namespacedkey;
			}
		}
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				for (Entry<NamespacedKey, ItemStack> entry : ext_material_map.entrySet()) {
					if (itemstack.isSimilar(entry.getValue())) {
						return entry.getKey();
					}
				}
			}
		}
		return itemstack.getType().getKey();
	}

	private static ItemStack new_item(NamespacedKey namespacedkey, int num) { // 根据内部ID生成ItemStack，等效于new //
																				// ItemStack(Material)
		return new_item(namespacedkey, num, null);
	}

	public static ItemStack new_item(String id_name, int num) {
		NamespacedKey namespacedkey = null;
		if (Material.getMaterial(id_name.toUpperCase()) == null) {
			namespacedkey = Dropper_shop_plugin.instance.get_key(id_name);
		} else {
			namespacedkey = NamespacedKey.minecraft(id_name.toLowerCase());
		}
		return new_item(namespacedkey, num);
	}

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

	private static ItemStack register(NamespacedKey namespacedkey, ItemStack itemstack) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(itemstack);
		NBTTagCompound tag = (nms_item.hasTag()) ? nms_item.getTag() : new NBTTagCompound();
		tag.set("ext_id", new NBTTagString(namespacedkey.toString()));
		nms_item.setTag(tag);
		itemstack = CraftItemStack.asBukkitCopy(nms_item);
		return Material_ext.ext_material_map.put(namespacedkey, itemstack.clone());
	}

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
