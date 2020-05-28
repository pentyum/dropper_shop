package com.piggest.minecraft.bukkit.economy;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Tab_list;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scoreboard_economy_manager implements TabExecutor {
	private final HashMap<String, Scoreboard_economy> eco_map = new HashMap<>();
	private final ArrayList<Scoreboard_economy> eco_list = new ArrayList<>();
	private Economy default_economy = null;

	public Scoreboard_economy_manager() {
		ConfigurationSerialization.registerClass(Scoreboard_economy.class);
	}

	public static int money_parser(String str) throws NumberFormatException {
		int length = str.length();
		if (length > 1) {
			char unit = str.charAt(length - 1);
			int multi = 0;
			int amount = Integer.parseInt(str.substring(0, length - 1));
			switch (unit) {
				case 'k':
				case 'K':
					multi = 1000;
					break;
				case 'w':
				case 'W':
					multi = 10000;
					break;
				case 'm':
				case 'M':
					multi = 1000000;
					break;
				default:
					return Integer.parseInt(str);
			}
			return amount * multi;
		} else {
			return Integer.parseInt(str);
		}
	}

	public void register_economy(Scoreboard_economy eco) {
		eco.register_service();
		eco.register_scoreboard();
		eco.register_listener();
		eco.set_id(eco_list.size());
		eco_map.put(eco.getName(), eco);
		eco_list.add(eco);
	}

	@Nullable
	public Scoreboard_economy get_economy(String name) {
		return eco_map.get(name);
	}

	@Nullable
	public Scoreboard_economy get_economy(int id) {
		return eco_list.get(id);
	}

	@Nullable
	public Economy get_default_economy() {
		if (this.default_economy == null) {
			RegisteredServiceProvider<Economy> eco_provider = Bukkit.getServicesManager().getRegistration(Economy.class);
			if (eco_provider == null) {
				return null;
			}
			return eco_provider.getProvider();
		} else {
			return this.default_economy;
		}
	}

	/**
	 * Executes the given command, returning its success.
	 * <br>
	 * If false is returned, then the "usage" plugin.yml entry for this command
	 * (if defined) will be sent to the player.
	 *
	 * @param sender  Source of the command
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    Passed command arguments
	 * @return true if a valid command, otherwise false
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (command.getName().equalsIgnoreCase("scb_eco")) {
			if (args[0].equalsIgnoreCase("get_all_eco")) {
				for (RegisteredServiceProvider<Economy> eco_provider : Bukkit.getServicesManager().getRegistrations(Economy.class)) {
					Economy eco = eco_provider.getProvider();
					sender.sendMessage(eco.getClass().getName() + ": " + eco.getName() + " " + eco.currencyNameSingular());
				}
				return true;
			} else if (args[0].equalsIgnoreCase("bal")) {
				if (!sender.hasPermission("scb_eco.bal")) {
					sender.sendMessage("你没有权限查看余额");
					return true;
				}
				OfflinePlayer player;
				if (args.length < 2) {
					if (sender instanceof Player) {
						player = (Player) sender;
					} else {
						sender.sendMessage("必须指定玩家");
						return true;
					}
				} else {
					player = Bukkit.getOfflinePlayer(args[1]);
				}
				String msg = "玩家" + player.getName() + "拥有: ";
				String[] bal_info = new String[this.eco_list.size()];
				for (int i = 0; i < bal_info.length; i++) {
					Scoreboard_economy eco = this.eco_list.get(i);
					double bal = eco.getBalance(player);
					bal_info[i] = eco.format(bal);
				}
				msg += String.join(", ", bal_info);
				sender.sendMessage(msg);
				return true;
			} else if (args[0].equalsIgnoreCase("pay")) {
				if (!sender.hasPermission("scb_eco.pay")) {
					sender.sendMessage("你没有权限转账");
					return true;
				}
				if (!(sender instanceof Player)) {
					sender.sendMessage("只有玩家才能转账");
					return true;
				}
				Player player = (Player) sender;
				if (args.length < 3) {
					player.sendMessage("/scb_eco pay <玩家名称> <数量> <货币名称>");
					return true;
				}
				int amount = 0;
				try {
					amount = money_parser(args[2]);
				} catch (NumberFormatException e) {
					player.sendMessage("数量格式不对");
					return true;
				}
				Economy eco = this.get_default_economy();
				if (args.length > 3) {
					eco = this.eco_map.get(args[3]);
				}
				if (eco == null) {
					String[] names = new String[this.eco_map.size()];
					names = this.eco_map.keySet().toArray(names);
					player.sendMessage("货币类型不正确，可用的货币类型: " + String.join(", ", names));
					return true;
				}
				OfflinePlayer to_player = Bukkit.getOfflinePlayer(args[1]);
				EconomyResponse withdraw_res = eco.withdrawPlayer(player, amount);
				if (withdraw_res.type == EconomyResponse.ResponseType.SUCCESS) {
					EconomyResponse deposit_res = eco.depositPlayer(to_player, amount);
					if (deposit_res.type == EconomyResponse.ResponseType.SUCCESS) {
						player.sendMessage("成功向" + to_player.getName() + "转账" + eco.format(amount));
					} else {
						eco.depositPlayer(player, amount);
						player.sendMessage("转账失败，原因: " + deposit_res.errorMessage);
					}
				} else {
					player.sendMessage("转账失败，原因: " + withdraw_res.errorMessage);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("give")) {
				if (!sender.hasPermission("scb_eco.give")) {
					sender.sendMessage("你没有权限添加货币");
					return true;
				}
				if (args.length < 3) {
					sender.sendMessage("/scb_eco give <玩家名称> <数量> <货币名称>");
					return true;
				}
				int amount = 0;
				try {
					amount = money_parser(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("数量格式不对");
					return true;
				}
				Economy eco = this.get_default_economy();
				if (args.length > 3) {
					eco = this.eco_map.get(args[3]);
				}
				if (eco == null) {
					String[] names = new String[this.eco_map.size()];
					names = this.eco_map.keySet().toArray(names);
					sender.sendMessage("货币类型不正确，可用的货币类型: " + String.join(", ", names));
					return true;
				}
				OfflinePlayer to_player = Bukkit.getOfflinePlayer(args[1]);
				EconomyResponse deposit_res = eco.depositPlayer(to_player, amount);
				if (deposit_res.type == EconomyResponse.ResponseType.SUCCESS) {
					sender.sendMessage("成功向" + to_player.getName() + "添加" + eco.format(amount));
				} else {
					sender.sendMessage("添加失败，原因: " + deposit_res.errorMessage);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("import")) {
				if (!sender.hasPermission("scb_eco.import")) {
					sender.sendMessage("你没有权限导入经济");
					return true;
				}
				if (args.length < 3) {
					sender.sendMessage("/scb_eco import <经济系统类型名> <要导入的货币名>");
					return true;
				}
				Economy import_eco = null;
				for (RegisteredServiceProvider<Economy> eco_provider : Bukkit.getServicesManager().getRegistrations(Economy.class)) {
					if (eco_provider.getPlugin() != Dropper_shop_plugin.instance) {
						Economy eco = eco_provider.getProvider();
						if (eco.getClass().getName().equalsIgnoreCase(args[1])) {
							import_eco = eco;
							break;
						}
					}
				}
				if (import_eco == null) {
					sender.sendMessage("未找到要导入的经济系统: " + args[1]);
					return true;
				}
				Economy to_eco = null;
				to_eco = this.eco_map.get(args[2]);
				if (to_eco == null) {
					String[] names = new String[this.eco_map.size()];
					names = this.eco_map.keySet().toArray(names);
					sender.sendMessage("货币类型不正确，可用的货币类型: " + String.join(", ", names));
					return true;
				}
				OfflinePlayer[] all_players = Bukkit.getOfflinePlayers();
				int i = 0;
				for (OfflinePlayer player : all_players) {
					if (player.getName() != null) {
						double bal = import_eco.getBalance(player);
						Dropper_shop_plugin.instance.getLogger().info("导入" + player.getName() + to_eco.format(bal));
						to_eco.withdrawPlayer(player, to_eco.getBalance(player));
						to_eco.depositPlayer(player, bal);
						i++;
					}
				}
				sender.sendMessage("成功从" + args[1] + "中导入" + i + "名玩家的数据到" + args[2]);
				return true;
			}
		}
		return false;
	}

	/**
	 * Requests a list of possible completions for a command argument.
	 *
	 * @param sender  Source of the command.  For players tab-completing a
	 *                command inside of a command block, this will be the player, not
	 *                the command block.
	 * @param command Command which was executed
	 * @param alias   The alias used
	 * @param args    The arguments passed to the command, including final
	 *                partial argument to be completed and command label
	 * @return A List of possible completions for the final argument, or null
	 * to default to the command executor
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("scb_eco")) {
			if (args.length == 1) {
				ArrayList<String> sub_cmd = new ArrayList<>();
				if (sender.hasPermission("scb_eco.bal")) {
					sub_cmd.add("bal");
				}
				if (sender.hasPermission("scb_eco.pay")) {
					sub_cmd.add("pay");
				}
				if (sender.hasPermission("scb_eco.give")) {
					sub_cmd.add("give");
				}
				if (sender.hasPermission("scb_eco.import")) {
					sub_cmd.add("import");
				}
				return sub_cmd;
			}
			if (args[0].equalsIgnoreCase("bal")) {
				if (args.length == 2) {
					return Tab_list.get_online_player_name_list();
				}
			} else if (args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("give")) {
				if (args.length == 2) {
					return Tab_list.get_online_player_name_list();
				} else if (args.length == 3) {
					String amount = args[2];
					try {
						Integer.parseInt(amount);
					} catch (NumberFormatException e) {
						return null;
					}
					ArrayList<String> list = new ArrayList<>();
					list.add(amount + "k");
					list.add(amount + "w");
					list.add(amount + "M");
					return list;
				} else if (args.length == 4) {
					return new ArrayList<>(this.eco_map.keySet());
				}
			} else if (args[0].equalsIgnoreCase("import")) {
				if (args.length == 2) {
					ArrayList<String> eco_name_list = new ArrayList<>();
					for (RegisteredServiceProvider<Economy> eco : Bukkit.getServicesManager().getRegistrations(Economy.class)) {
						if (eco.getPlugin() != Dropper_shop_plugin.instance) {
							eco_name_list.add(eco.getProvider().getClass().getName());
						}
					}
					return Tab_list.contains(eco_name_list, args[1]);
				} else if (args.length == 3) {
					return new ArrayList<>(this.eco_map.keySet());
				}
			}
		}
		return null;
	}
}
