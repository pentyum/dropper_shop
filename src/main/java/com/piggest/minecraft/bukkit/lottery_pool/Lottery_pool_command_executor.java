package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

enum Lottery_sub_cmd {
	add, set, del, list, setprice;
	public static ArrayList<String> get_list(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (Lottery_sub_cmd cmd : Lottery_sub_cmd.values()) {
			list.add(cmd.name());
		}
		return list;
	}
}

public class Lottery_pool_command_executor implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (args.length == 0) {
				String msg = "/lottery list 显示当前各物品概率\n";
				if (player.hasPermission("lottery.set")) {
					msg += "/lottery <概率> 设置抽到手上物品的概率，单位是百分比，数量以手上的为准\n/lottery del [编号] 删除该项\n/lottery set [编号] <新的概率>\n/lottery setprice <价格> 设置抽取一次的价格";
				}
				player.sendMessage(msg);
				return true;
			} else {
				FileConfiguration config = Dropper_shop_plugin.instance.get_lottery_config();
				@SuppressWarnings("unchecked")
				List<ItemStack> item_list = (List<ItemStack>) config.getList("pool");
				List<Integer> possibility_list = config.getIntegerList("possibility");
				List<Boolean> broadcast_list = config.getBooleanList("broadcast");
				if (args[0].equalsIgnoreCase("list")) {
					int i = 0;
					String msg = "当前抽奖费用: "+Dropper_shop_plugin.instance.get_lottery_price();
					msg += "\n-----------抽奖概率公示-----------\n";
					int total = 0;
					for (i = 0; i < item_list.size(); i++) {
						ItemStack item = item_list.get(i);
						String enchantment_str = "";
						Map<Enchantment, Integer> enchantments = item.getEnchantments();
						if (enchantments != null && !enchantments.isEmpty()) {
							enchantment_str += " 附魔:";
							for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
								String enchantment_name = entry.getKey().getKey().toString();
								enchantment_str += enchantment_name + "-" + entry.getValue() + ",";
							}
							enchantment_str = enchantment_str.substring(0, enchantment_str.length() - 1);
						}
						if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
							EnchantmentStorageMeta stor = (EnchantmentStorageMeta) item.getItemMeta();
							enchantments = stor.getStoredEnchants();
							if (enchantments != null && !enchantments.isEmpty()) {
								enchantment_str += " 存储附魔:";
								for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
									String enchantment_name = entry.getKey().getKey().toString();
									enchantment_str += enchantment_name + "-" + entry.getValue() + ",";
								}
								enchantment_str = enchantment_str.substring(0, enchantment_str.length() - 1);
							}
						}
						int possibility = possibility_list.get(i);
						msg += "[" + i + "]: " + Material_ext.get_display_name(item) + " 数量:" + item.getAmount()
								+ enchantment_str + " 概率:" + possibility + "% 播报:" + broadcast_list.get(i) + "\n";
						total += possibility;
					}
					msg += "----------总中奖概率" + String.format("%3d", total) + "%----------";
					player.sendMessage(msg);
				} else if (args[0].equalsIgnoreCase("del") && args.length == 2) {
					if (!player.hasPermission("lottery.set")) {
						player.sendMessage("你没有权限修改抽奖池");
						return true;
					}
					int del_id = 0;
					try {
						del_id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage("编号不是整数");
						return true;
					}
					try {
						item_list.remove(del_id);
						possibility_list.remove(del_id);
						broadcast_list.remove(del_id);
					} catch (IndexOutOfBoundsException e) {
						player.sendMessage("编号越界");
						return true;
					}
					config.set("pool", item_list);
					config.set("possibility", possibility_list);
					config.set("broadcast", broadcast_list);
					player.sendMessage("删除成功");
				} else if (args[0].equalsIgnoreCase("add") && args.length == 2) {
					if (!player.hasPermission("lottery.set")) {
						player.sendMessage("你没有权限修改抽奖池");
						return true;
					}
					ItemStack item = player.getInventory().getItemInMainHand();
					if (Grinder.is_empty(item)) {
						player.sendMessage("你的手上没有物品");
					}
					int possibility = 0;
					try {
						possibility = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage("物品概率不是整数");
						return true;
					}
					item_list.add(item);
					possibility_list.add(possibility);
					broadcast_list.add(false);
					config.set("pool", item_list);
					config.set("possibility", possibility_list);
					config.set("broadcast", broadcast_list);
					player.sendMessage("添加成功");
				} else if (args[0].equalsIgnoreCase("set") && args.length >= 3) {
					if (!player.hasPermission("lottery.set")) {
						player.sendMessage("你没有权限修改抽奖池");
						return true;
					}
					int possibility = 0;
					int set_id = 0;
					try {
						set_id = Integer.parseInt(args[1]);
						possibility = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						player.sendMessage("编号和物品概率必须都是整数");
						return true;
					}
					boolean broadcast = false;
					if (args.length == 4) {
						broadcast = Boolean.parseBoolean(args[3]);
					}
					try {
						possibility_list.set(set_id, possibility);
						if (args.length == 4) {
							broadcast_list.set(set_id, broadcast);
						}
					} catch (IndexOutOfBoundsException e) {
						player.sendMessage("编号越界");
						return true;
					}
					config.set("possibility", possibility_list);
					if (args.length == 4) {
						config.set("broadcast", broadcast_list);
					}
					player.sendMessage("修改成功");
				} else if (args[0].equalsIgnoreCase("setprice") ) {
					if (!player.hasPermission("lottery.set")) {
						player.sendMessage("你没有权限修改抽奖池");
						return true;
					}
					int newprice = 0;
					try {
						newprice = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sender.sendMessage("价格不是整数");
						return true;
					}
					Dropper_shop_plugin.instance.set_lottery_price(newprice);
					player.sendMessage("价格修改成功");
				} else {
					player.sendMessage("命令格式错误");
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Lottery_sub_cmd.get_list(sender);
		} else if (args.length == 2) {
			FileConfiguration config = Dropper_shop_plugin.instance.get_lottery_config();
			List<Integer> possibility_list = config.getIntegerList("possibility");
			int size = possibility_list.size();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				list.add(String.valueOf(i));
			}
			if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("del")) {
				return list;
			}
		} else if (args.length == 4) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("false");
			list.add("true");
			if (args[0].equalsIgnoreCase("set")) {
				return list;
			}
		}
		return new ArrayList<String>();
	}

}
