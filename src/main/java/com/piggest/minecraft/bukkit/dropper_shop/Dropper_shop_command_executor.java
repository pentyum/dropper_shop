package com.piggest.minecraft.bukkit.dropper_shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.config.Price_config;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.nms.NMS_manager;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_composition;
import com.piggest.minecraft.bukkit.utils.Server_date;
import com.piggest.minecraft.bukkit.utils.Tab_list;

public class Dropper_shop_command_executor implements TabExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("dropper_shop")) {
			if(args.length==0) {
				sender.sendMessage("欢迎使用Dropper_shop插件，当前版本:");
				return true;
			}
			if (args[0].equalsIgnoreCase("list_structure")) {
				Price_config price_config = Dropper_shop_plugin.instance.get_price_config();
				sender.sendMessage(price_config.get_info());
				return true;
			}
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
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
							if (shop.create_condition(player) == true) {
								shop.set_location(look_block.getLocation());
								shop.set_owner(player.getName());
								shop.set_selling_item(item);
								Dropper_shop_plugin.instance.get_shop_manager().add(shop);
								player.sendMessage(item.name() + "的投掷器商店已经被设置");
							}
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
						shop.remove();
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
						Dropper_shop_plugin.instance.get_price_config().set_make_shop_price(set_price);
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
			} else if (args[0].equalsIgnoreCase("show_full_name")) {
				player.sendMessage(Material_ext.get_full_name(player.getInventory().getItemInMainHand()));
			} else if (args[0].equalsIgnoreCase("show_full_time")) {
				player.sendMessage("Full time: " + player.getWorld().getFullTime());
			} else if (args[0].equalsIgnoreCase("show_time")) {
				player.sendMessage("Time: " + player.getWorld().getTime());
			} else if (args[0].equalsIgnoreCase("show_thread")) {
				player.sendMessage("Thread: " + Thread.currentThread().getId());
			} else if (args[0].equalsIgnoreCase("show_date")) {
				player.sendMessage("当前服务器日期: " + Server_date.formatDate(Server_date.get_world_date(player.getWorld())));
				return true;
			} else if (args[0].equalsIgnoreCase("show_elements")) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if (!Grinder.is_empty(item)) {
					player.sendMessage(Elements_composition.get_element_composition(item).toString());
				}
				return true;
			} else if (args[0].equalsIgnoreCase("show_inhabited_time")) {
				Location loc = player.getLocation();
				Chunk chunk = loc.getChunk();
				long time = chunk.getInhabitedTime();
				float local_difficulty = NMS_manager.local_difficulty.get_local_difficulty(loc);
				player.sendMessage("当前区块inhabited_time: " + time + ", Local Difficulty: " + local_difficulty);
				return true;
			} else if (args[0].equalsIgnoreCase("set_inhabited_time")) {
				if (!player.hasPermission("dropper_shop.set_inhabited_time")) {
					player.sendMessage("你没有权限设置inhabited_time");
					return true;
				}
				Location loc = player.getLocation();
				Chunk chunk = loc.getChunk();
				long ticks = chunk.getInhabitedTime();
				try {
					ticks = Long.parseLong(args[1]);
					chunk.setInhabitedTime(ticks);
				} catch (Exception e) {
					player.sendMessage("时间参数错误");
					return true;
				}
				return true;
			} else if (args[0].equalsIgnoreCase("get_map")) {
				ItemStack item = new ItemStack(Material.MAP);
				ItemMeta meta = item.getItemMeta();
				MapMeta mapmeta = (MapMeta) meta;
				MapView mapview = Bukkit.getServer().createMap(player.getWorld());
				List<MapRenderer> renders = mapview.getRenderers();
				for (MapRenderer render : renders) {
					player.sendMessage(render.getClass().getName());
					mapview.removeRenderer(render);
				}
				mapview.addRenderer(new Custom_map_render());
				mapmeta.setMapView(mapview);
			} else if (args[0].equalsIgnoreCase("test_map")) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if (item == null) {
					player.sendMessage("空手");
					return true;
				}
				if (item.getType() == Material.MAP) {
					ItemMeta meta = item.getItemMeta();
					MapMeta mapmeta = (MapMeta) meta;
					if (mapmeta.hasMapView()) {
						MapView mapview = mapmeta.getMapView();
						List<MapRenderer> renders = mapview.getRenderers();
						for (MapRenderer render : renders) {
							player.sendMessage(render.getClass().getName());
						}
					} else {
						player.sendMessage("没有MapView");
					}
				}

			} else if (args[0].equalsIgnoreCase("get_item")) {
				if (!player.hasPermission("dropper_shop.get_item")) {
					player.sendMessage("你没有权限获得物品");
					return true;
				}
				int quantity = 1;
				if (args.length < 2) {
					player.sendMessage("未指定物品ID");
					return true;
				}
				if (args.length > 2) {
					try {
						quantity = Integer.parseInt(args[2]);
					} catch (Exception e) {
						player.sendMessage("物品数量不正确");
						return true;
					}
					if (quantity < 0) {
						player.sendMessage("物品数量不正确");
						return true;
					}
				}
				ItemStack item = Material_ext.new_item_full_name(args[1], quantity);
				if (item == null) {
					player.sendMessage("物品ID不正确");
					return true;
				}
				ItemStack[] items = Material_ext.split_to_max_stack_size(item);
				// player.sendMessage("长度"+items.length);
				HashMap<Integer, ItemStack> left = player.getInventory().addItem(items);
				for (ItemStack left_item : left.values()) {
					player.getWorld().dropItemNaturally(player.getLocation(), left_item);
				}
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args[0].equalsIgnoreCase("get_item")) {
			if (args.length == 2) {
				ArrayList<String> ext_item_list = Material_ext.get_ext_full_name_list();
				Dropper_shop_plugin.instance.getLogger().info(args[1]);
				return Tab_list.contains(ext_item_list, args[1]);
			}
		}
		return null;
	}

}
