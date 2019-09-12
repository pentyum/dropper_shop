package com.piggest.minecraft.bukkit.watersheep;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import org.bukkit.Material;

public class Watersheep_runner extends BukkitRunnable {
	private Server server = null;

	public Watersheep_runner(Server server) {
		this.server = server;
	}

	public Watersheep_process_code process_a_sheep(Entity sheep) {
		Sheep s = (Sheep) sheep;

		/* 跳过未被修剪的羊 */
		if (!s.isSheared()) {
			return Watersheep_process_code.not_sheared;
		} else {
			Location sheep_loc = s.getLocation();
			Location grass_supposed_loc = new Location(sheep_loc.getWorld(), Math.round(sheep_loc.getX()),
					Math.round(sheep_loc.getY()) - 1, Math.round(sheep_loc.getZ()));

			/* 羊的脚下必须是草方块 */
			if (grass_supposed_loc.getBlock().getType() != Material.GRASS_BLOCK) {
				return Watersheep_process_code.no_grass;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					grass_supposed_loc.getBlock().setType(Material.DIRT);
				}
			}.runTaskLater(Dropper_shop_plugin.instance, 0);

			s.setSheared(false);
			return Watersheep_process_code.succeed;
		}
	}

	@Override
	public void run() {
		if (this.server == null) {
			return;
		}

		/* 初始化一些计数器 */
		int counter_how_many_sheep_proceesed = 0;
		int counter_how_many_sheep_not_sheared = 0;
		int counter_how_many_sheep_no_grass = 0;
		int counter_how_many_sheep_succeed = 0;

		/* 取得在线玩家列表 */
		Collection<? extends Player> online_players_list = this.server.getOnlinePlayers();

		/* 取得有玩家存在的世界 */
		Set<String> worlds_with_players = new HashSet<String>();
		for (Player one_player : online_players_list) {
			if (!one_player.isOnline()) {
				continue;
			}

			worlds_with_players.add(one_player.getWorld().getName());
		}

		/* 分别处理每一个世界 */
		if (!worlds_with_players.isEmpty()) {
			for (String one_world_name : worlds_with_players) {
				World one_world = this.server.getWorld(one_world_name);

				/* 必须是白天 */
				long current_world_time = one_world.getTime();
				if (current_world_time < 6000 || current_world_time > 18000) {
					continue;
				}

				/* 取得这个世界里面加载的区块 */
				Chunk[] loaded_chunks = one_world.getLoadedChunks();
				/* 搜索这些区块里面符合条件的羊 */
				for (Chunk one_loaded_chunk : loaded_chunks) {
					if (!one_loaded_chunk.isLoaded()) {
						continue;
					}

					Entity[] entities_in_this_chunk = one_loaded_chunk.getEntities();
					for (Entity one_entity : entities_in_this_chunk) {
						if (one_entity.getType() != EntityType.SHEEP
								|| !one_entity.getScoreboardTags().contains("watersheep")) {
							continue;
						}

						/* 处理这只羊，并获得结果代码 */
						Watersheep_process_code res = process_a_sheep(one_entity);
						counter_how_many_sheep_proceesed++;

						switch (res) {
						case not_sheared:
							counter_how_many_sheep_not_sheared++;
							break;
						case no_grass:
							counter_how_many_sheep_no_grass++;
							break;
						case succeed:
							counter_how_many_sheep_succeed++;
							break;
						}
					}
				}
			}
		}
		String msg = "[植物羊]在加载的 " + counter_how_many_sheep_proceesed + " 只植物羊里面，有 " + counter_how_many_sheep_not_sheared
				+ "本身有毛，有 " + counter_how_many_sheep_no_grass + " 只脚下没有草，有 " + counter_how_many_sheep_succeed
				+ " 只成功长出了毛。";

		if (server.getPlayer("weikeng") != null && server.getPlayer("weikeng").isOnline()) {
			server.getPlayer("weikeng").sendMessage(msg);
		}

		Dropper_shop_plugin.instance.getLogger().info(msg);
	}
}
