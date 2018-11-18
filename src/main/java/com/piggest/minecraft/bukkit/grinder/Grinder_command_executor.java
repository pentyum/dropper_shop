package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Grinder_command_executor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("grinder")) {
			if (args.length == 0) {
				player.sendMessage("/grinder make");
				return true;
			}
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("请指向方块");
				return true;
			}
			if (args[0].equalsIgnoreCase("make")) {
				Grinder grinder = Dropper_shop_plugin.instance.get_grinder_manager().get(look_block.getLocation());
				if (grinder != null) {
					player.sendMessage("这里已经建立了磨粉机了");
					return true;
				}
				grinder = new Grinder();
				grinder.set_location(look_block.getLocation());
				if (grinder.completed() == 0) {
					player.sendMessage("没有检测到完整的磨粉机结构");
					return true;
				}
				if (Dropper_shop_plugin.instance.get_economy().has(player,
						Dropper_shop_plugin.instance.get_make_grinder_price())) {
					Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player,
							Dropper_shop_plugin.instance.get_make_grinder_price());
					player.sendMessage("已扣除" + Dropper_shop_plugin.instance.get_make_grinder_price());
				} else {
					player.sendMessage("钱不够");
					return true;
				}
				Dropper_shop_plugin.instance.get_grinder_manager().add(grinder);
				player.sendMessage("磨粉机建立完成");
				return true;
			}
		}
		return false;
	}

}
