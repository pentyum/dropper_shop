package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Advanced_furnace_command_executor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("adv_furnace")) {
			if (args.length == 0) {
				player.sendMessage("请使用/adv_furnace make|remove|temp");
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
			if (args[0].equalsIgnoreCase("make")) {
				Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
						.find(look_block.getLocation(), false);
				if (adv_furnace != null) {
					player.sendMessage("这里已经有高级熔炉了");
					return true;
				}
				adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager().find(look_block.getLocation(),
						true);
				if (adv_furnace == null) {
					player.sendMessage("没有检测到完整的高级熔炉结构");
					return true;
				}
				Dropper_shop_plugin.instance.get_adv_furnace_manager().add(adv_furnace);
				player.sendMessage("高级熔炉结构建立完成");
				return true;
			} else if (args[0].equalsIgnoreCase("remove")) {
				Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
						.find(look_block.getLocation(), false);
				if (adv_furnace == null) {
					player.sendMessage("没有检测到完整的高级熔炉结构");
					return true;
				}
				Dropper_shop_plugin.instance.get_adv_furnace_manager().remove(adv_furnace);
				player.sendMessage("高级熔炉结构已经移除");
				return true;
			}
		}
		return false;
	}

}
