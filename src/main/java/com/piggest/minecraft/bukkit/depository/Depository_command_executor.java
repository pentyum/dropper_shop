package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

enum Depository_sub_cmd {
	info, input, output, remove, connect;
	public static ArrayList<String> get_list(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (Depository_sub_cmd cmd : Depository_sub_cmd.values()) {
			list.add(cmd.name());
		}
		return list;
	}
}

public class Depository_command_executor implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("depository")) {
			if (args.length == 0) {
				player.sendMessage(
						"/depository info 查看存储器信息\n/depository remove 移除该存储器\n/depository input 把手上的物品存入存储器\n/depository input all 把背包中和手上的物品相同的全部存入存储器\n/depository output <物品名称> <数量> 取出物品\n/depository connect <物品名称> 连接读取器和存储器");
				return true;
			}
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("请指向方块");
				return true;
			}
			if (args[0].equalsIgnoreCase(Depository_sub_cmd.info.name())) {
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
						look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				player.sendMessage(depository.get_info());
			} else if (args[0].equalsIgnoreCase(Depository_sub_cmd.input.name())) {
				if (!player.hasPermission("depository.input")) {
					player.sendMessage("你没有添加物品的权限");
					return true;
				}
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
						look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				ItemStack item = player.getInventory().getItemInMainHand();
				if (item == null || item.getType() == Material.AIR) {
					player.sendMessage("你的手中没有物品");
					return true;
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("all")) { // 输入全部
						// Material type = item.getType();
						for (ItemStack other_item : player.getInventory().getContents()) {
							if (other_item != null) {
								if (other_item.isSimilar(item)) {
									depository.add(other_item);
								}
							}
						}
					}
				} else {
					player.sendMessage("已添加物品" + item.getType().name());
					depository.add(item);
				}
			} else if (args[0].equalsIgnoreCase(Depository_sub_cmd.remove.name())) {
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
						look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				Dropper_shop_plugin.instance.get_depository_manager().remove(depository);
				player.sendMessage("存储器结构已经移除");
			} else if (args[0].equalsIgnoreCase(Depository_sub_cmd.output.name())) {
				if (!player.hasPermission("depository.output")) {
					player.sendMessage("你没有取出物品的权限");
					return true;
				}
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
						look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("/depository output <物品名称> <数量>");
					return true;
				}
				int current_num = depository.get_material_num(args[1]);
				if (current_num == 0) {
					player.sendMessage("该存储器中没有存放这种物品");
					return true;
				}
				if (args.length == 2) {
					ItemStack item = depository.remove(args[1]);
					player.getInventory().addItem(item);
				} else {
					int total_number = depository.get_material_num(args[1]);
					int remove_number = 0;
					if (args[2].equalsIgnoreCase("all")) {
						remove_number = total_number;
					} else {
						try {
							remove_number = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							player.sendMessage("输入的数字不对");
							return true;
						}
						if (remove_number > total_number) {
							remove_number = total_number;
							player.sendMessage("数量不够，只取出了" + remove_number + "个物品");
						}
					}
					ItemStack item = depository.remove(args[1], remove_number);
					HashMap<Integer, ItemStack> unaddable = player.getInventory().addItem(item);
					for (ItemStack unadd_item : unaddable.values()) {
						player.getWorld().dropItemNaturally(player.getLocation(), unadd_item).setPickupDelay(40);
					}
				}
			} else if (args[0].equalsIgnoreCase(Depository_sub_cmd.connect.name())) {
				if (!player.hasPermission("depository.connect")) {
					player.sendMessage("你没有连接存储器的权限");
					return true;
				}
				Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
						look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("/depository connect <物品名称>");
					return true;
				}
				ItemStack item = player.getInventory().getItemInMainHand();
				if (item == null || item.getType() == Material.AIR) {
					player.sendMessage("你的手中没有物品");
					return true;
				}
				if (!Reader.is_reader(item)) {
					player.sendMessage("你的手中没有连接器");
					return true;
				}
				int current_num = depository.get_material_num(args[1]);
				if (current_num == 0) {
					player.sendMessage("该存储器中没有存放这种物品");
					return true;
				}
				ItemMeta item_meta = item.getItemMeta();
				item_meta.setLore(Reader.get_lore(depository.get_location(), args[1], current_num));
				item.setItemMeta(item_meta);
				item.setType(Material_ext.get_material(args[1]));
				player.sendMessage("连接成功");
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Depository_sub_cmd.get_list(sender);
		}
		if (args.length == 2 && (args[0].equalsIgnoreCase(Depository_sub_cmd.connect.name())
				|| args[0].equalsIgnoreCase(Depository_sub_cmd.output.name()))) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Block look_block = player.getTargetBlockExact(4);
				if (look_block != null) {
					Depository depository = Dropper_shop_plugin.instance.get_depository_manager().find(player.getName(),
							look_block.getLocation(), false);
					if (depository != null) {
						ArrayList<String> list = new ArrayList<String>();
						for (Entry<String, Integer> contents : depository.get_contents_entry()) {
							list.add(contents.getKey());
						}
						return list;
					}
				}
			}
		}
		return new ArrayList<String>();
	}

}
