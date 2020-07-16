package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Radio;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

enum sub_command {
	set_online_voltage, set_working_voltage, set_freq, set_bandwidth, search,
}

public class Teleport_machine_command_executer implements TabExecutor {
	private static final ArrayList<String> sub_command_list = new ArrayList<String>();

	public Teleport_machine_command_executer() {
		for (sub_command command : sub_command.values()) {
			sub_command_list.add(command.name());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("teleport_machine")) {
			if (args.length == 1) {
				return sub_command_list;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("teleport_machine")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("[传送机]必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("[传送机]请指向方块");
				return true;
			}
			sub_command subcommand = null;
			try {
				subcommand = sub_command.valueOf(args[0]);
			} catch (Exception e) {
				player.sendMessage("[传送机]参数错误");
				return true;
			}
			Teleport_machine machine = Dropper_shop_plugin.instance.get_teleport_machine_manager()
					.find_existed(look_block.getLocation());
			if (machine == null) {
				player.sendMessage("[传送机]没有检测到完整的传送机结构");
				return true;
			}
			switch (subcommand) {
				case search:
					if (!player.hasPermission("teleport_machine.search.debug")) {
						player.sendMessage("[传送机]你没有搜台debug的权限!");
						break;
					}
					machine.known_terminal_list = machine.search(player, true);
					return true;
				default:
					if (args.length < 2) {
						player.sendMessage("[传送机]没有输入参数");
						return true;
					}
					int value = 0;
					try {
						value = Integer.parseInt(args[1]);
					} catch (Exception e) {
						player.sendMessage("[传送机]参数不合法");
						return true;
					}
					switch (subcommand) {
						case set_bandwidth:
							if (Radio.check_channel_vaild(machine.get_channel_freq(), value, machine.get_n())) {
								machine.set_channel_bandwidth(value);
								player.sendMessage("[传送机]带宽设置成功");
							} else {
								player.sendMessage("[传送机]频段超出范围");
							}
							return true;
						case set_freq:
							if (Radio.check_channel_vaild(value, machine.get_channel_bandwidth(), machine.get_n())) {
								machine.set_channel_freq(value);
								player.sendMessage("[传送机]频率设置成功");
							} else {
								player.sendMessage("[传送机]频段超出范围");
							}
							return true;
						case set_online_voltage:
							if (value <= 0) {
								player.sendMessage("[传送机]电压不能小于0");
							} else {
								machine.set_online_voltage(value);
							}
							return true;
						case set_working_voltage:
							if (value <= 0) {
								player.sendMessage("[传送机]电压不能小于0");
							} else {
								machine.set_working_voltage(value);
							}
							return true;
						default:
							return true;
					}
			}
		}
		return false;
	}

}
