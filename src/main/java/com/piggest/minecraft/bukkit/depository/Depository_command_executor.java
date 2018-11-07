package com.piggest.minecraft.bukkit.depository;

import java.util.ArrayList;
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

enum Sub_cmd {
	make, info, input, output, remove, connect;
	public static ArrayList<String> get_list(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (Sub_cmd cmd : Sub_cmd.values()) {
			list.add(cmd.name());
		}
		return list;
	}
}

public class Depository_command_executor implements TabExecutor {
	private Dropper_shop_plugin plugin;

	public Depository_command_executor(Dropper_shop_plugin dropper_shop_plugin) {
		this.plugin = dropper_shop_plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("depository")) {
			if (args.length == 0) {
				player.sendMessage("请使用/depository make|info|remove|input|output|connect");
				return true;
			}
			Block look_block = player.getTargetBlockExact(4);
			if (look_block == null) {
				player.sendMessage("请指向方块");
				return true;
			}
			if (args[0].equalsIgnoreCase(Sub_cmd.make.name())) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						true);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				plugin.get_depository_manager().add(depository);
				player.sendMessage("存储器结构建立完成");
			} else if (args[0].equalsIgnoreCase("info")) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				player.sendMessage(depository.get_info());
			} else if (args[0].equalsIgnoreCase(Sub_cmd.input.name())) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						false);
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
			} else if (args[0].equalsIgnoreCase(Sub_cmd.remove.name())) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				plugin.get_depository_manager().remove(depository);
				player.sendMessage("存储器结构已经移除");
			} else if (args[0].equalsIgnoreCase(Sub_cmd.output.name())) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("/depository export <物品名称> <数量>");
					return true;
				}
				player.sendMessage("暂时不支持命令取出");
			} else if (args[0].equalsIgnoreCase(Sub_cmd.connect.name())) {
				Depository depository = plugin.get_depository_manager().find(player.getName(), look_block.getLocation(),
						false);
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
				item.setType(Material.getMaterial(args[1]));
				player.sendMessage("连接成功");
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return Sub_cmd.get_list(sender);
		}
		if (args.length == 2 && args[0].equalsIgnoreCase(Sub_cmd.connect.name())) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Block look_block = player.getTargetBlockExact(4);
				if (look_block != null) {
					Depository depository = plugin.get_depository_manager().find(player.getName(),
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
