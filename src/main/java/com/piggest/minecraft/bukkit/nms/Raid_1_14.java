package com.piggest.minecraft.bukkit.nms;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.block.CraftBlock;
import net.minecraft.server.v1_14_R1.PersistentRaid;
import net.minecraft.server.v1_14_R1.WorldServer;

public class Raid_1_14 implements Raid {
	@SuppressWarnings("unchecked")
	public Raid_info trigger_raid(Location loc, int bad_omen_level) {
		CraftBlock craft_block = (CraftBlock) loc.getBlock();
		CraftWorld world = (CraftWorld) loc.getWorld();
		boolean disable_raids = world.getGameRuleValue(GameRule.DISABLE_RAIDS);
		if (disable_raids == true) {
			return null;
		}
		WorldServer world_nms = world.getHandle();
		PersistentRaid persistentraid = world_nms.C();

		Field map_field;
		Field next_id_field;
		int next_id = 0;
		Map<Integer, net.minecraft.server.v1_14_R1.Raid> raid_map = null;
		try {
			map_field = PersistentRaid.class.getDeclaredField("a");
			next_id_field = PersistentRaid.class.getDeclaredField("c");
			map_field.setAccessible(true);// 允许访问私有字段
			next_id_field.setAccessible(true);
			raid_map = (Map<Integer, net.minecraft.server.v1_14_R1.Raid>) map_field.get(persistentraid);
			next_id = (int) next_id_field.get(persistentraid);
			next_id++;
			next_id_field.set(persistentraid, next_id);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		net.minecraft.server.v1_14_R1.Raid raid = new net.minecraft.server.v1_14_R1.Raid(next_id, world_nms,
				craft_block.getPosition());
		if (bad_omen_level > raid.l()) {
			bad_omen_level = raid.l();
		}
		Field bad_omen_field;
		try {
			bad_omen_field = net.minecraft.server.v1_14_R1.Raid.class.getDeclaredField("o");
			bad_omen_field.setAccessible(true);
			bad_omen_field.setInt(raid, bad_omen_level);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		raid_map.put(next_id, raid);
		persistentraid.b();
		Raid_info info = this.get_info(raid);
		return info;
	}

	public Raid_info get_info(net.minecraft.server.v1_14_R1.Raid raid) {
		if (raid == null) {
			return null;
		}
		Raid_info info = new Raid_info();
		info.id = raid.u();
		info.started = raid.j();
		info.active = raid.v();
		info.bad_omen_level = raid.m();
		info.groups_spawned = raid.k();
		return info;
	}
}
