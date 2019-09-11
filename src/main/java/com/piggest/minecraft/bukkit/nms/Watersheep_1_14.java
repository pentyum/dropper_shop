package com.piggest.minecraft.bukkit.nms;

import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import net.minecraft.server.v1_14_R1.NBTTagCompound;

public class Watersheep_1_14 implements Watersheep {
	public void set_noai(Entity sheep) {
		net.minecraft.server.v1_14_R1.Entity nmsEntity = ((CraftEntity) sheep).getHandle();
	    NBTTagCompound tag = new NBTTagCompound();
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 1);
	    nmsEntity.f(tag);
	}
	public void unset_noai(Entity sheep) {
		net.minecraft.server.v1_14_R1.Entity nmsEntity = ((CraftEntity) sheep).getHandle();
	    NBTTagCompound tag = new NBTTagCompound();
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 0);
	    nmsEntity.f(tag);
	}
}