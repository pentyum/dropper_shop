package com.piggest.minecraft.bukkit.economy;

import net.milkbowl.vault.economy.Economy;
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

	public void register_economy(Scoreboard_economy eco) {
		eco.register_service();
		eco.register_scoreboard();
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
		if (args[0].equalsIgnoreCase("get_all_economy")) {
			for (RegisteredServiceProvider<Economy> eco_provider : Bukkit.getServicesManager().getRegistrations(Economy.class)) {
				Economy eco = eco_provider.getProvider();
				sender.sendMessage(eco.getClass().getName() + ": " + eco.getName() + " " + eco.currencyNameSingular());
			}
			return true;
		} else if (args[0].equalsIgnoreCase("bal")) {
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
		return null;
	}
}
