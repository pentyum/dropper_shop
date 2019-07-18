package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

enum Advanced_furnace_sub_cmd {
	remove, getmoney, temp;
	public static ArrayList<String> get_list(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (Advanced_furnace_sub_cmd cmd : Advanced_furnace_sub_cmd.values()) {
			list.add(cmd.name());
		}
		return list;
	}
}

public class Advanced_furnace_command_executor implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("adv_furnace")) {
			if (args.length == 0) {
				player.sendMessage("请使用/adv_furnace remove|temp|getmoney");
				return true;
			}
			if (args[0].equalsIgnoreCase("temp")) {
				player.sendMessage("温度:"
						+ String.format("%.1f", Advanced_furnace.get_block_temperature(player.getLocation().getBlock()))
						+ "K");
				return true;
			}
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("请指向方块");
				return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
						.find_existed(look_block.getLocation());
				if (adv_furnace == null) {
					player.sendMessage("没有检测到完整的高级熔炉结构");
					return true;
				}
				adv_furnace.remove();
				player.sendMessage("高级熔炉结构已经移除");
				return true;
			} else if (args[0].equalsIgnoreCase("getmoney")) {
				Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
						.find_existed(look_block.getLocation());
				if (adv_furnace == null) {
					player.sendMessage("没有检测到完整的高级熔炉结构");
					return true;
				}
				int money = adv_furnace.get_money();
				Dropper_shop_plugin.instance.get_economy().depositPlayer(player, money);
				player.sendMessage("已获得" + money);
				adv_furnace.set_money(0);
				return true;
			} else if (args[0].equalsIgnoreCase("set_overload") || args[0].equalsIgnoreCase("set_time")) {
				if (!player.hasPermission("adv_furnace.admin")) {
					player.sendMessage("你没有权限直接设置等级");
					return true;
				}
				Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
						.find_existed(look_block.getLocation());
				if (adv_furnace == null) {
					player.sendMessage("没有检测到完整的高级熔炉结构");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("没有输入等级");
					return true;
				}
				int level = 0;
				try {
					level = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					player.sendMessage("等级不是数字");
					return true;
				}
				if (args[0].equalsIgnoreCase("set_overload")) {
					adv_furnace.set_overload_upgrade(level);
				} else {
					adv_furnace.set_time_upgrade(level);
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Advanced_furnace_sub_cmd.get_list(sender);
		}
		return null;
	}

}
