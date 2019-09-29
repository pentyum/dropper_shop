package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagInt;

public class Flying_time_1_14 implements Flying_time {
	/*
	 * 获取物品的element_id
	 */
	@Override
	public int get_flying_time(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return -1;
		}
		NBTTagCompound tag = nms_item.getTag();
		int flying_time = tag.getInt("flying_time");
		return flying_time;
	}

	/*
	 * 判断该物品是否具有ext_id
	 */
	@Override
	public boolean has_flying_time(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (!nms_item.hasTag()) {
			return false;
		}
		NBTTagCompound tag = nms_item.getTag();
		return tag.hasKey("flying_time");
	}

	/*
	 * 设置后添加后请使用返回值
	 */
	@Override
	public ItemStack set_flying_time(ItemStack item, int flying_time) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (nms_item.hasTag()) ? nms_item.getTag() : new NBTTagCompound();
		tag.set("flying_time", new NBTTagInt(flying_time));
		nms_item.setTag(tag);
		item = CraftItemStack.asBukkitCopy(nms_item);
		return item;
	}
}
