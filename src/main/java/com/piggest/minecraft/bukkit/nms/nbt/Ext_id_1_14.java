package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Ext_id_1_14 implements Ext_id {
	/*
	 * 获取物品的ext_id
	 */
	@Override
	public String get_ext_id(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return null;
		}
		NBTTagCompound tag = nms_item.getTag();
		String id_name = tag.getString("ext_id");
		return id_name;
	}

	/*
	 * 判断该物品是否具有ext_id
	 */
	@Override
	public boolean has_ext_id(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return false;
		}
		NBTTagCompound tag = nms_item.getTag();
		return tag.hasKey("ext_id");
	}

	/*
	 * 设置后添加后请使用返回值
	 */
	@Override
	public ItemStack set_ext_id(ItemStack item, String ext_id) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (nms_item.hasTag()) ? nms_item.getTag() : new NBTTagCompound();
		tag.set("ext_id", new NBTTagString(ext_id));
		nms_item.setTag(tag);
		item = CraftItemStack.asBukkitCopy(nms_item);
		return item;
	}

}
