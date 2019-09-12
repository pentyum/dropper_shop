package com.piggest.minecraft.bukkit.watersheep;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.Collection;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.Chunk;
import org.bukkit.Location;

import com.piggest.minecraft.bukkit.watersheep.sub_command;
import com.piggest.minecraft.bukkit.nms.NMS_manager;

enum sub_command {
	curse, bless, info
}

public class Watersheep_command_executor implements TabExecutor {
	private static final ArrayList<String> sub_command_list = new ArrayList<String>();

	public Watersheep_command_executor() {
		for (sub_command command : sub_command.values()) {
			sub_command_list.add(command.name());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("watersheep")) {
			if (args.length == 1) {
				return sub_command_list;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("watersheep")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("[植物羊]必须由玩家使用命令");
				return true;
			}
			
			Player player = (Player) sender;
			
			if (!player.hasPermission("watersheep.use")) {
				player.sendMessage("你没有权限操作植物羊");
				return true;
			}
			
			Location player_location = player.getLocation();
			
			sub_command subcommand = null;
			try {
				subcommand = sub_command.valueOf(args[0]);
			} catch (Exception e) {
				player.sendMessage("[植物羊]参数错误");
				return true;
			}
			
			switch (subcommand) {
			case curse:
				/* 把羊变成植物羊有以下步骤：
				 *    搜索距离在16个格子以内的所有实体
				 *    检查羊是否和玩家在同一区块
				 *    使用 NMS 开启羊的 NoAI 属性
				 *    把羊加入一个计分板，名称为 watersheep
				 *    设置羊为静音状态
				 */
				{
					World player_world = player_location.getWorld();
					Predicate<Entity> isSheep = p -> p.getType() == EntityType.SHEEP;
					
					int counter_how_many_sheep_cursed = 0;
	
					/* 搜索距离在16个格子以内的所有实体 */
					Collection<Entity> nearby_sheep = player_world.getNearbyEntities(player_location, 8, 8, 8, isSheep);
					
					Chunk player_chunk = player_location.getChunk();
					for (Entity one_nearby_sheep: nearby_sheep) {
						if(one_nearby_sheep != null) {
							Chunk one_nearby_sheep_chunk = one_nearby_sheep.getLocation().getChunk();
							
							/* 检查羊是否和玩家在同一区块 */
							if(player_chunk.getX() != one_nearby_sheep_chunk.getX() || player_chunk.getZ() != one_nearby_sheep_chunk.getZ()) {
								continue;
							}
							
							/* 如果羊已经有计分板项目，不参与计数 */
							if(!one_nearby_sheep.getScoreboardTags().contains("watersheep")) {
								counter_how_many_sheep_cursed ++;
								
								/* 把羊加入一个计分板，名称为 watersheep */
								one_nearby_sheep.addScoreboardTag("watersheep");
							}
							
							/* 使用 NMS 开启羊的 NoAI 属性 */
							NMS_manager.watersheep_provider.set_noai(one_nearby_sheep);	
							
							/* 设置羊为静音状态 */
							one_nearby_sheep.setSilent(true);
						}
					}
					
					player.sendMessage("[植物羊]" + counter_how_many_sheep_cursed + "只羊已经变成了植物羊");
				}
				return true;
			case bless:
				/* 把植物羊变回羊有以下步骤：
				 *    搜索距离在16个格子以内的所有实体
				 *    检查羊是否和玩家在同一区块
				 *    检查羊是否有计分板项目  watersheep
				 *    使用 NMS 关闭羊的 NoAI 属性
				 *    设置羊为非静音状态
				 */
				
				{
					World player_world = player_location.getWorld();
					Predicate<Entity> isSheep = p -> p.getType() == EntityType.SHEEP;
					
					int counter_how_many_sheep_blessed = 0;
	
					/* 搜索距离在16个格子以内的所有实体 */
					Collection<Entity> nearby_sheep = player_world.getNearbyEntities(player_location, 8, 8, 8, isSheep);
					
					Chunk player_chunk = player_location.getChunk();
					for (Entity one_nearby_sheep: nearby_sheep) {
						if(one_nearby_sheep != null) {
							Chunk one_nearby_sheep_chunk = one_nearby_sheep.getLocation().getChunk();
							
							/* 检查羊是否和玩家在同一区块 */
							if(player_chunk.getX() != one_nearby_sheep_chunk.getX() || player_chunk.getZ() != one_nearby_sheep_chunk.getZ()) {
								continue;
							}
							
							/* 如果羊没有计分板项目，不参与计数 */
							if(one_nearby_sheep.getScoreboardTags().contains("watersheep")) {
								counter_how_many_sheep_blessed ++;
								
								/* 把羊从计分板里面移除 */
								one_nearby_sheep.removeScoreboardTag("watersheep");
							}
							
							/* 使用 NMS 关闭羊的 NoAI 属性 */
							NMS_manager.watersheep_provider.unset_noai(one_nearby_sheep);	
							
							/* 设置羊为非静音状态 */
							one_nearby_sheep.setSilent(false);
						}
					}
					
					player.sendMessage("[植物羊]" + counter_how_many_sheep_blessed + "只植物羊已经变回了羊");
				}
				
				return true;
			case info:
				/*
				 * 这是一个调试和提供参考信息的 API。这个 API提供玩家附近的16 * 16 区块的所有实体羊信息
				 */
				
				{
					World player_world = player_location.getWorld();
					Predicate<Entity> isSheep = p -> p.getType() == EntityType.SHEEP;
					
					int counter_how_many_normal_sheep = 0;
					int counter_how_many_watersheep = 0;
	
					/* 搜索距离在 128 个格子以内的所有实体 */
					Collection<Entity> nearby_sheep = player_world.getNearbyEntities(player_location, 128, 128, 128, isSheep);
					
					for (Entity one_nearby_sheep: nearby_sheep) {
						if(one_nearby_sheep != null) {
							/* 如果羊没有计分板项目，不参与计数 */
							if(one_nearby_sheep.getScoreboardTags().contains("watersheep")) {
								counter_how_many_watersheep++;
							}else {
								counter_how_many_normal_sheep++;
							}
						}
					}
					
					player.sendMessage("[植物羊]附近的 16x16区块中有 " + counter_how_many_normal_sheep + " 只普通羊和 " + counter_how_many_watersheep + " 只植物羊");
				}
				
				return true;
			default:
				return true;
			}
			
		}
		
		return false;
	}

}
