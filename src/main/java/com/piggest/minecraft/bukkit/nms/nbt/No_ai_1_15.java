package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class No_ai_1_15 implements No_ai {
	public void set_noai(Entity sheep) {
	    net.minecraft.server.v1_15_R1.Entity nmsEntity = ((CraftEntity) sheep).getHandle();
	    NBTTagCompound tag = new NBTTagCompound();
	    nmsEntity.c(tag);//获取实体的信息，并保存到NBT中
	    tag.setInt("NoAI", 1);//设置没有AI
	    nmsEntity.f(tag);//把NBT应用于实体
	}
	public void unset_noai(Entity sheep) {
	    net.minecraft.server.v1_15_R1.Entity nmsEntity = ((CraftEntity) sheep).getHandle();
	    NBTTagCompound tag = new NBTTagCompound();
	    nmsEntity.c(tag);//获取实体的信息，并保存到NBT中
	    tag.setInt("NoAI", 0);//设置有AI
	    nmsEntity.f(tag);//把NBT应用于实体
	}
}