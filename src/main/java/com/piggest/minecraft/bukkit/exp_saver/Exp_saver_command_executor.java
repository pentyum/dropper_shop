package com.piggest.minecraft.bukkit.exp_saver;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

enum Exp_saver_sub_cmd {
	remove,;
	public static ArrayList<String> get_list(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (Exp_saver_sub_cmd cmd : Exp_saver_sub_cmd.values()) {
			list.add(cmd.name());
		}
		return list;
	}
}

public class Exp_saver_command_executor implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("exp_saver")) {
			if (args.length == 0) {
				player.sendMessage("/exp_saver remove 移除该经验存储器");
				return true;
			}
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("请指向方块");
				return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				Exp_saver exp_saver = Dropper_shop_plugin.instance.get_exp_saver_manager()
						.find(look_block.getLocation(), false);
				if (exp_saver == null) {
					player.sendMessage("没有检测到完整的经验存储器结构");
					return true;
				}
				Dropper_shop_plugin.instance.get_exp_saver_manager().remove(exp_saver);
				player.sendMessage("经验存储器结构已经移除");
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Exp_saver_sub_cmd.get_list(sender);
		}
		return null;
	}

}
