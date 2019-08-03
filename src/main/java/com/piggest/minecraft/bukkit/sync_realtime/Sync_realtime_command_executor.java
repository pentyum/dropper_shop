package com.piggest.minecraft.bukkit.sync_realtime;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.piggest.minecraft.bukkit.utils.Server_date;
import com.piggest.minecraft.bukkit.utils.Tab_list;

public class Sync_realtime_command_executor implements TabExecutor {
	private HashMap<String, Integer> sync_realtime_worlds;

	public Sync_realtime_command_executor(HashMap<String, Integer> sync_realtime_worlds2) {
		this.sync_realtime_worlds = sync_realtime_worlds2;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Tab_list.world_name_list;
		}
		if (args.length == 2) {
			return Tab_list.true_false_list;
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("sync_realtime")) {
			if (args.length == 0) {
				if (!sender.hasPermission("sync_realtime.list")) {
					sender.sendMessage("你没有权限查看同步情况");
					return true;
				}
				String message = "世界与现实时间同步\n";
				for (World world : Bukkit.getWorlds()) {
					String world_name = world.getName();
					message += world_name + ": ";
					message += this.sync_realtime_worlds.containsKey(world_name)
							? "同步(" + this.sync_realtime_worlds.get(world_name) + ")"
							: (world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE) ? "非同步" : "锁定");
					message += " " + Server_date.get_format_world_date(world);
					message += "\n";
				}
				sender.sendMessage(message);
				return true;
			} else if (args.length >= 2) {
				if (!sender.hasPermission("sync_realtime.set")) {
					sender.sendMessage("你没有权限进行设置");
					return true;
				}
				if (args[1].equalsIgnoreCase("true")) {
					int offset = 0;
					if (args.length >= 3) {
						offset = Integer.parseInt(args[2]);
					}
					this.sync_realtime_worlds.put(args[0], offset);
				} else {
					this.sync_realtime_worlds.remove(args[0]);
				}
				return true;
			}
		}
		return false;
	}

}
