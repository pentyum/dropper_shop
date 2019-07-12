package com.piggest.minecraft.bukkit.nms;

import java.util.Set;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;

public interface NBT_manager {
	public boolean has_tag(ItemStack item);
	public Set<String> get_keys(ItemStack item);
	public boolean has_key(ItemStack item);
	public static void test(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack nms_item = CraftItemStack.asNMSCopy(item);
		if (nms_item.hasTag()) {
			NBTTagCompound tag = nms_item.getTag();
			String id_name = tag.getString("ext_id");
			tag.getKeys();
			tag.getInt("");
			tag.getBoolean("");
			tag.getIntArray("");
			tag.getCompound("");
			tag.hasKey("");
		}
	}
}
