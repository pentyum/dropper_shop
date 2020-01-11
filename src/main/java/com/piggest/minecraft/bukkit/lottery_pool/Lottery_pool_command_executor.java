package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.utils.language.Enchantments_zh_cn;

enum Lottery_sub_cmd {
	add, set, del, list, setprice, reload;
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
		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (args.length == 0) {
				String msg = "/lottery list 显示当前各物品概率\n";
				if (sender.hasPermission("lottery.set")) {
					msg += "/lottery add <概率> [播报:true|false] 设置抽到手上物品的概率，单位是千分比，数量以手上的为准\n/lottery del [编号] 删除该项\n/lottery set [编号] <新的概率> [播报:true|false]\n/lottery setprice <价格> 设置抽取一次的价格";
				}
				sender.sendMessage(msg);
				return true;
			} else {
				YamlConfiguration config = Dropper_shop_plugin.instance.get_lottery_config().get_config();
				@SuppressWarnings("unchecked")
				List<ItemStack> item_list = (List<ItemStack>) config.getList("pool");
				List<Integer> possibility_list = config.getIntegerList("possibility");
				List<Boolean> broadcast_list = config.getBooleanList("broadcast");
				if (args[0].equalsIgnoreCase("list")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						Lottery_pool_gui_holder gui_holder = new Lottery_pool_gui_holder();
						player.closeInventory();
						player.openInventory(gui_holder.getInventory());
					} else {
						int i = 0;
						int page = 1;
						if (args.length > 1) {
							try {
								page = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								page = 1;
							}
						}
						int total_pages = (item_list.size() - 1) / 10 + 1;
						String msg = "当前抽奖费用: " + Dropper_shop_plugin.instance.get_price_config().get_lottery_price();
						msg += "\n------------抽奖概率公示 第" + String.format("%2d /%2d", page, total_pages)
								+ " 页------------\n";
						int total = 0;
						for (i = 0; i < item_list.size(); i++) {
							ItemStack item = item_list.get(i);
							String enchantment_str = "";
							Map<Enchantment, Integer> enchantments = item.getEnchantments();
							if (enchantments != null && !enchantments.isEmpty()) {
								enchantment_str += " 附魔:§7";
								for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
									String enchantment_name = Enchantments_zh_cn.get_enchantment_name(entry.getKey());
									enchantment_str += enchantment_name + entry.getValue() + ",";
								}
								enchantment_str = enchantment_str.substring(0, enchantment_str.length() - 1);
							}
							if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
								EnchantmentStorageMeta stor = (EnchantmentStorageMeta) item.getItemMeta();
								enchantments = stor.getStoredEnchants();
								if (enchantments != null && !enchantments.isEmpty()) {
									enchantment_str += " 存储附魔:§7";
									for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
										String enchantment_name = Enchantments_zh_cn
												.get_enchantment_name(entry.getKey());
										enchantment_str += enchantment_name + entry.getValue() + ",";
									}
									enchantment_str = enchantment_str.substring(0, enchantment_str.length() - 1);
								}
							}
							int possibility = possibility_list.get(i);
							if (i >= 10 * (page - 1) && i < 10 * page) {
								msg += "[" + i + "]: " + Material_ext.get_display_name(item) + "§r 数量:"
										+ item.getAmount() + enchantment_str + "§r 概率:"
										+ String.format("%4.1f", (float) possibility / 10) + "% 播报:"
										+ broadcast_list.get(i) + "\n";
							}
							total += possibility;
						}
						msg += "----------------总中奖概率" + String.format("%5.1f", (float) total / 10)
								+ "%----------------";
						sender.sendMessage(msg);
					}
				} else if (args[0].equalsIgnoreCase("del") && args.length == 2) {
					if (!sender.hasPermission("lottery.set")) {
						sender.sendMessage("[抽奖机]你没有权限修改抽奖池");
						return true;
					}
					int del_id = 0;
					try {
						del_id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage("[抽奖机]编号不是整数");
						return true;
					}
					try {
						item_list.remove(del_id);
						possibility_list.remove(del_id);
						broadcast_list.remove(del_id);
					} catch (IndexOutOfBoundsException e) {
						sender.sendMessage("[抽奖机]编号越界");
						return true;
					}
					config.set("pool", item_list);
					config.set("possibility", possibility_list);
					config.set("broadcast", broadcast_list);
					sender.sendMessage("[抽奖机]删除成功");
				} else if (args[0].equalsIgnoreCase("add") && args.length >= 2) {
					if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
						sender.sendMessage("[抽奖机]必须由玩家执行该命令");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("lottery.set")) {
						player.sendMessage("[抽奖机]你没有权限修改抽奖池");
						return true;
					}
					ItemStack item = player.getInventory().getItemInMainHand().clone();
					if (Grinder.is_empty(item)) {
						player.sendMessage("[抽奖机]你的手上没有物品");
					}
					int possibility = 0;
					try {
						possibility = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage("[抽奖机]物品概率不是整数");
						return true;
					}
					boolean broadcast = false;
					if (args.length == 3) {
						broadcast = Boolean.parseBoolean(args[2]);
					}
					item_list.add(item);
					possibility_list.add(possibility);
					broadcast_list.add(broadcast);
					config.set("pool", item_list);
					config.set("possibility", possibility_list);
					config.set("broadcast", broadcast_list);
					player.sendMessage("[抽奖机]添加成功");
				} else if (args[0].equalsIgnoreCase("set") && args.length >= 3) {
					if (!sender.hasPermission("lottery.set")) {
						sender.sendMessage("[抽奖机]你没有权限修改抽奖池");
						return true;
					}
					int possibility = 0;
					int set_id = 0;
					try {
						set_id = Integer.parseInt(args[1]);
						possibility = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("[抽奖机]编号和物品概率必须都是整数");
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
						sender.sendMessage("[抽奖机]编号越界");
						return true;
					}
					config.set("possibility", possibility_list);
					if (args.length == 4) {
						config.set("broadcast", broadcast_list);
					}
					sender.sendMessage("[抽奖机]修改成功");
				} else if (args[0].equalsIgnoreCase("setprice")) {
					if (!sender.hasPermission("lottery.set")) {
						sender.sendMessage("[抽奖机]你没有权限修改抽奖池");
						return true;
					}
					int newprice = 0;
					try {
						newprice = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sender.sendMessage("[抽奖机]价格不是整数");
						return true;
					}
					Dropper_shop_plugin.instance.get_price_config().set_lottery_price(newprice);
					sender.sendMessage("[抽奖机]价格修改成功");
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("lottery.set")) {
						sender.sendMessage("[抽奖机]你没有权限重载抽奖池");
						return true;
					}
					Dropper_shop_plugin.instance.get_lottery_config().load();
					sender.sendMessage("[抽奖机]奖池配置已重载");
				} else {
					sender.sendMessage("[抽奖机]命令格式错误");
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> tf_list = new ArrayList<String>();
		tf_list.add("false");
		tf_list.add("true");
		if (args.length == 1) {
			return Lottery_sub_cmd.get_list(sender);
		} else if (args.length == 2) {
			YamlConfiguration config = Dropper_shop_plugin.instance.get_lottery_config().get_config();
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
			if (args[0].equalsIgnoreCase("set")) {
				return tf_list;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("add")) {
				return tf_list;
			}
		}
		return new ArrayList<String>();
	}

}
