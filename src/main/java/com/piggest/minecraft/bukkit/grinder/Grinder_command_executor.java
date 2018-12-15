package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Grinder_command_executor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("grinder")) {
			player.sendMessage("搭建好磨粉机后使用扳手右键磨粉机中心的平滑石块即可建立磨粉机");
			return true;
		}
		return false;
	}

}
