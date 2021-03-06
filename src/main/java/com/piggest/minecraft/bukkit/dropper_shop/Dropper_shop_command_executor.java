package com.piggest.minecraft.bukkit.dropper_shop;

import com.google.zxing.NotFoundException;
import com.piggest.minecraft.bukkit.config.Price_config;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;
import com.piggest.minecraft.bukkit.custom_map.Screen;
import com.piggest.minecraft.bukkit.custom_map.Screen_map_render;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.nms.NMS_manager;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_composition;
import com.piggest.minecraft.bukkit.utils.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Dropper_shop_command_executor implements TabExecutor {
	public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {

		if (cmd.getName().equalsIgnoreCase("dropper_shop")) {
			if (args.length == 0) {
				sender.sendMessage("欢迎使用Dropper_shop插件，当前版本:" + Dropper_shop_plugin.instance.getDescription().getVersion());
				return true;
			}
			if (args[0].equalsIgnoreCase("list_structure")) {
				Price_config price_config = Dropper_shop_plugin.instance.get_price_config();
				sender.sendMessage(price_config.get_info());
				return true;
			}

			if (args.length == 0) {
				sender.sendMessage("请使用/dropper_shop make");
				return true;
			}

			String sub_cmd = args[0].toLowerCase();
			List<String> need_player_cmd = Arrays.asList("make", "remove", "setprice", "show_full_name", "show_full_time",
					"show_time", "show_date", "show_elements", "show_inhabited_time", "set_inhabited_time", "show_light",
					"scan_qr_code", "get_item");

			if (need_player_cmd.contains(sub_cmd)) {
				if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
					sender.sendMessage("必须由玩家执行该命令");
					return true;
				}
				Player player = (Player) sender;
				Location loc = player.getLocation();
				Chunk chunk = loc.getChunk();
				Block look_block;
				switch (sub_cmd) {
					case "make":
						if (!player.hasPermission("dropper_shop.make")) {
							player.sendMessage("你没有权限建立投掷器商店");
							return true;
						}
						look_block = player.getTargetBlockExact(4);
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
						break;
					case "remove":
						look_block = player.getTargetBlockExact(4);
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
						break;
					case "setprice":
						if (!sender.hasPermission("dropper_shop.changeprice")) {
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
							if (itemstack.getType() == Material.AIR) {
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
						break;
					case "show_full_name":
						player.sendMessage(Material_ext.get_full_name(player.getInventory().getItemInMainHand()));
						break;
					case "show_full_time":
						player.sendMessage("Full time: " + player.getWorld().getFullTime());
						break;
					case "show_time":
						player.sendMessage("Time: " + player.getWorld().getTime());
						break;
					case "show_date":
						player.sendMessage("当前服务器日期: " + Server_date.formatDate(Server_date.get_world_date(player.getWorld())));
						break;
					case "show_elements":
						ItemStack item = player.getInventory().getItemInMainHand();
						if (!Grinder.is_empty(item)) {
							player.sendMessage(Elements_composition.get_element_composition(item).toString());
						}
						break;
					case "show_inhabited_time":
						long time = chunk.getInhabitedTime();
						float local_difficulty = NMS_manager.local_difficulty.get_local_difficulty(loc);
						player.sendMessage("当前区块inhabited_time: " + time + ", Local Difficulty: " + local_difficulty);
						break;
					case "set_inhabited_time":
						if (!player.hasPermission("dropper_shop.set_inhabited_time")) {
							player.sendMessage("你没有权限设置inhabited_time");
							return true;
						}
						long ticks = chunk.getInhabitedTime();
						try {
							ticks = Long.parseLong(args[1]);
							chunk.setInhabitedTime(ticks);
						} catch (Exception e) {
							player.sendMessage("时间参数错误");
							return true;
						}
						break;
					case "show_light":
						Block block = player.getLocation().getBlock();
						byte light = block.getLightLevel();
						byte sky_light = block.getLightFromSky();
						byte block_light = block.getLightFromBlocks();
						player.sendMessage("真实亮度" + light + ",天空亮度" + sky_light + ",方块亮度" + block_light);
						break;
					case "scan_qr_code":
						Vector direction = player.getLocation().getDirection();
						RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), direction, 5,
								e -> e.getType() != EntityType.PLAYER);
						if (result == null) {
							player.sendMessage("未检测到实体");
							return true;
						}
						Entity entity = result.getHitEntity();
						if (entity == null) {
							player.sendMessage("未检测到实体");
							return true;
						}
						if (entity instanceof ItemFrame) {
							ItemFrame item_frame = (ItemFrame) entity;
							ItemStack item_in_frame = item_frame.getItem();
							Custom_map_render render = Custom_map_render.get_render_from_item(item_in_frame);
							if (render instanceof Screen_map_render) {
								Screen_map_render image_render = (Screen_map_render) render;
								Screen screen = image_render.get_screen();
								BufferedImage image = screen.get_current_raw_img();
								String text = null;
								try {
									text = Qr_code_utils.scan(image);
								} catch (NotFoundException e) {
									player.sendMessage("没有检测到二维码");
									return true;
								}
								player.sendMessage("二维码扫描结果: " + text);
							}
						}
						break;
					case "get_item":
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
						ItemStack new_item = Material_ext.new_item_full_name(args[1], quantity);
						if (new_item == null) {
							player.sendMessage("物品ID不正确");
							return true;
						}
						ItemStack[] items = Material_ext.split_to_max_stack_size(new_item);
						// player.sendMessage("长度"+items.length);
						Inventory_io.give_item_to_player(player, items);
						break;
					default:
						return false;
				}
				return true;
			} else if (args[0].equalsIgnoreCase("show_thread")) {
				sender.sendMessage("Thread: " + Thread.currentThread().getId());
			} else if (args[0].equalsIgnoreCase("show_structures")) {
				Collection<Structure_manager<? extends Structure>> structure_managers = Dropper_shop_plugin.instance.get_structure_manager().values();
				if (args.length == 2) {
					World world = Bukkit.getWorld(args[1]);
					if (world != null) {
						for (Structure_manager<? extends Structure> structure_manager : structure_managers) {
							sender.sendMessage(world.getName() + ":" + structure_manager.get_gui_name()
									+ "总共" + structure_manager.get_all_structures_in_world(world).size() + "个"
									+ "，已加载" + structure_manager.get_loaded_structures_in_world(world).size() + "个");
						}
					} else {
						sender.sendMessage("世界名称错误");
					}
				} else {
					for (World world : Bukkit.getWorlds()) {
						for (Structure_manager<? extends Structure> structure_manager : structure_managers) {
							sender.sendMessage(world.getName() + ":" + structure_manager.get_gui_name()
									+ "总共" + structure_manager.get_all_structures_in_world(world).size() + "个"
									+ "，已加载" + structure_manager.get_loaded_structures_in_world(world).size() + "个");
						}
					}
				}
				return true;
			} else if (args[0].equalsIgnoreCase("download_img")) {
				if (args.length < 2) {
					sender.sendMessage("未填写URL，格式 download_img <URL> <保存的文件名(可选)>");
					return true;
				}
				String save_as = null;
				if (args.length >= 3) {
					save_as = args[2];
					if (save_as.contains("/") || save_as.contains("\\") || save_as.contains("<")
							|| save_as.contains(">") || save_as.contains("|") || save_as.contains(":")
							|| save_as.contains("\"") || save_as.contains("*") || save_as.contains("?")) {
						sender.sendMessage("文件名不合法");
						return true;
					}
				}
				File images_folder = new File(Dropper_shop_plugin.instance.getDataFolder(), "images");
				String url = args[1];
				Http_download downloader = new Http_download(images_folder);
				downloader.download(sender, url, save_as);
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
		if (args.length == 1) {
			return Arrays.asList("make", "remove", "setprice", "show_full_name", "show_full_time",
					"show_time", "show_date", "show_elements", "show_inhabited_time", "set_inhabited_time", "show_light",
					"scan_qr_code", "get_item", "show_thread", "show_structures", "download_img");
		}
		if (args[0].equalsIgnoreCase("get_item")) {
			if (args.length == 2) {
				ArrayList<String> ext_item_list = Material_ext.get_ext_full_name_list();
				return Tab_list.contains(ext_item_list, args[1]);
			}
		}
		return null;
	}

}
