package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Electric_spawner_command_executor implements TabExecutor {
	public static final int max_money = 100000000;

	private static final ArrayList<String> cmd = new ArrayList<String>() {
		private static final long serialVersionUID = 4632683284690778242L;
		{
			add("charge");
		}
	};

	private static final ArrayList<String> charge_money = new ArrayList<String>() {
		private static final long serialVersionUID = -10739381756686327L;
		{
			add("100");
			add("1000");
			add("10000");
			add("100000");
		}
	};

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return cmd;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("charge")) {
				return charge_money;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("[魔力刷怪机]必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("electric_spawner")) {
			if (args.length > 0) {
				Block look_block = player.getTargetBlockExact(4);
				if (look_block == null) {
					player.sendMessage("[魔力刷怪机]请指向方块");
					return true;
				}
				if (args[0].equalsIgnoreCase("charge")) {
					Electric_spawner electric_spawner = Dropper_shop_plugin.instance.get_electric_spawner_manager()
							.find_existed(look_block.getLocation());
					if (electric_spawner == null) {
						player.sendMessage("[魔力刷怪机]没有检测到完整的刷怪机结构");
						return true;
					}
					int charge_quantity = 0;
					try {
						charge_quantity = Integer.parseInt(args[1]);
					} catch (Exception e) {
						electric_spawner.send_message(player, "数量格式错误");
						return true;
					}
					int current_money = electric_spawner.get_money();
					if (current_money + charge_quantity > max_money) {
						charge_quantity = max_money - current_money;
						electric_spawner.send_message(player, "金币充值超出上限，数量修改为" + charge_quantity);
					}
					boolean cost_result = Dropper_shop_plugin.instance.cost_player_money(charge_quantity, player);
					if (cost_result == false) {
						electric_spawner.send_message(player, "你的金币不足" + charge_quantity);
						return true;
					}
					electric_spawner.set_money(current_money + charge_quantity);
					electric_spawner.send_message(player, "成功给刷怪机充值" + charge_quantity + "金币");
				}
			} else {
				player.sendMessage("/electric_spawner charge <数量> 给刷怪机充钱");
			}
		}
		return true;
	}

}
