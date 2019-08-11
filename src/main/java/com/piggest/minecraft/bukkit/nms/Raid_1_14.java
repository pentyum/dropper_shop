package com.piggest.minecraft.bukkit.nms;

import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_14_R1.PersistentRaid;
import net.minecraft.server.v1_14_R1.WorldServer;

public class Raid_1_14 implements Raid {
	public boolean trigger_raid(Player player) {
		CraftPlayer craftplayer = (CraftPlayer) player;
		CraftWorld world = (CraftWorld) player.getWorld();
		WorldServer world_nms = world.getHandle();
		PersistentRaid persistentraid = world_nms.C();
		net.minecraft.server.v1_14_R1.Raid raid = persistentraid.a(craftplayer.getHandle());
		return raid != null;
	}
}
