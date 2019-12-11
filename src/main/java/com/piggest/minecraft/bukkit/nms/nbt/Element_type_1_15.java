package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagInt;

public class Element_type_1_15 implements Element_type {
	/*
	 * 获取物品的element_id
	 */
	@Override
	public int get_element_id(ItemStack item) {
		net.minecraft.server.v1_15_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return -1;
		}
		NBTTagCompound tag = nms_item.getTag();
		int element_id = tag.getInt("element_id");
		return element_id;
	}

	/*
	 * 判断该物品是否具有ext_id
	 */
	@Override
	public boolean has_element_id(ItemStack item) {
		net.minecraft.server.v1_15_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return false;
		}
		NBTTagCompound tag = nms_item.getTag();
		return tag.hasKey("element_id");
	}

	/*
	 * 设置后添加后请使用返回值
	 */
	@Override
	public ItemStack set_element_id(ItemStack item, int element_id) {
		net.minecraft.server.v1_15_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (nms_item.hasTag()) ? nms_item.getTag() : new NBTTagCompound();
		tag.set("element_id", NBTTagInt.a(element_id));
		nms_item.setTag(tag);
		item = CraftItemStack.asBukkitCopy(nms_item);
		return item;
	}
}
