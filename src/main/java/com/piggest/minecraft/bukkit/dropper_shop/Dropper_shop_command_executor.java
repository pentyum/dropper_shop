package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Dropper_shop_command_executor implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("dropper_shop")) {
			if (args.length == 0) {
				player.sendMessage("请使用/dropper_shop make");
				return true;
			}
			if (args[0].equalsIgnoreCase("make")) {
				if (!player.hasPermission("dropper_shop.make")) {
					player.sendMessage("你没有权限建立投掷器商店");
					return true;
				}
				Block look_block = player.getTargetBlockExact(4);
				// player.sendMessage(look_block.getType().name());
				if (look_block == null) {
					player.sendMessage("请指向投掷器方块");
					return true;
				}
				if (look_block.getType() == Material.DROPPER) {
					if (Dropper_shop_plugin.instance.get_economy().has(player,
							Dropper_shop_plugin.instance.get_make_shop_price())) {
						Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player,
								Dropper_shop_plugin.instance.get_make_shop_price());
					} else {
						player.sendMessage("钱不够");
						return true;
					}
					Material item = player.getInventory().getItemInMainHand().getType();
					if (Dropper_shop_plugin.instance.get_price(item) == -1) {
						player.sendMessage(item.name() + "不能被出售");
					} else {
						Dropper_shop shop = Dropper_shop_plugin.instance.get_shop_manager()
								.get(look_block.getLocation());
						if (shop != null) {
							shop.set_selling_item(item);
							player.sendMessage("投掷器商店已经变更为" + item.name());
						} else {
							shop = new Dropper_shop();
							shop.set_location(look_block.getLocation());
							shop.set_owner(player.getName());
							shop.set_selling_item(item);
							Dropper_shop_plugin.instance.get_shop_manager().add(shop);
							player.sendMessage(item.name() + "的投掷器商店已经被设置");
						}
					}
				} else {
					player.sendMessage("不是投掷器");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				Block look_block = player.getTargetBlockExact(4);
				Dropper_shop shop = Dropper_shop_plugin.instance.get_shop_manager().get(look_block.getLocation());
				if (shop != null) {
					if (shop.get_owner_name().equalsIgnoreCase(player.getName())
							|| player.hasPermission("dropper_shop.remove.others")) {
						player.sendMessage(
								"已移除" + shop.get_owner_name() + "的" + shop.get_selling_item().name() + "投掷器商店");
						Dropper_shop_plugin.instance.get_shop_manager().remove(shop);
					} else {
						player.sendMessage("这不是你自己的投掷器商店，而是" + shop.get_owner_name() + "的");
						return true;
					}
				} else {
					player.sendMessage("不是投掷器商店");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("setprice")) {
				if (!player.hasPermission("dropper_shop.changeprice")) {
					player.sendMessage("你没有权限修改价格");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("请输入价格");
					return true;
				}
				try {
					int set_price = Integer.parseInt(args[1]);
					ItemStack itemstack = player.getInventory().getItemInMainHand();
					if (itemstack == null || itemstack.getType() == Material.AIR) {
						Dropper_shop_plugin.instance.set_make_shop_price(set_price);
						player.sendMessage("已设置创建投掷器商店价格为" + set_price);
					} else {
						Material item = player.getInventory().getItemInMainHand().getType();
						Dropper_shop_plugin.instance.get_shop_price_map().put(item.name(), set_price);
						Dropper_shop_plugin.instance.get_config().set("material",
								Dropper_shop_plugin.instance.get_shop_price_map());
						player.sendMessage("已设置" + item.name() + "的投掷器商店价格为" + set_price);
					}
					Dropper_shop_plugin.instance.saveConfig();
				} catch (NumberFormatException e) {
					player.sendMessage("请输入整数");
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

}
