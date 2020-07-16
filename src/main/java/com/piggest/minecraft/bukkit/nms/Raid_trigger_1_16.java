package com.piggest.minecraft.bukkit.nms;

import net.minecraft.server.v1_16_R1.PersistentRaid;
import net.minecraft.server.v1_16_R1.WorldServer;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;

import java.lang.reflect.Field;
import java.util.Map;

public class Raid_trigger_1_16 implements Raid_trigger {
	public Raid_info trigger_raid(Location loc, int bad_omen_level) {
		CraftBlock craft_block = (CraftBlock) loc.getBlock();
		CraftWorld world = (CraftWorld) loc.getWorld();
		boolean disable_raids = world.getGameRuleValue(GameRule.DISABLE_RAIDS);
		if (disable_raids == true) {
			return null;
		}
		WorldServer world_nms = world.getHandle();
		PersistentRaid persistentraid = world_nms.getPersistentRaid();

		Field next_id_field;
		int next_id = 0;
		Map<Integer, net.minecraft.server.v1_16_R1.Raid> raid_map = persistentraid.raids;
		try {
			next_id_field = PersistentRaid.class.getDeclaredField("c");
			next_id_field.setAccessible(true);
			next_id = (int) next_id_field.get(persistentraid);
			next_id++;
			next_id_field.set(persistentraid, next_id);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		net.minecraft.server.v1_16_R1.Raid raid = new net.minecraft.server.v1_16_R1.Raid(next_id, world_nms,
				craft_block.getPosition());
		if (bad_omen_level > raid.getMaxBadOmenLevel()) {
			bad_omen_level = raid.getMaxBadOmenLevel();
		}
		raid.badOmenLevel = bad_omen_level;
		raid_map.put(next_id, raid);
		persistentraid.b();
		Raid_info info = this.get_info(raid);
		return info;
	}

	public Raid_info get_info(net.minecraft.server.v1_16_R1.Raid raid) {
		if (raid == null) {
			return null;
		}
		Raid_info info = new Raid_info();
		info.id = raid.getId();
		info.started = raid.isStarted();
		info.active = raid.v();
		info.bad_omen_level = raid.getBadOmenLevel();
		info.groups_spawned = raid.getGroupsSpawned();
		return info;
	}
}
