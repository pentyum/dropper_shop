package com.piggest.minecraft.bukkit.economy;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard_economy implements Economy, ConfigurationSerializable {
	private final String name;
	private final String display_name;
	private final int default_balance;
	private Scoreboard scoreboard = null;
	private Objective objective;
	private int id;
	private final int max_bal = 2000000000;

	public Scoreboard_economy(String currency_name, String display_name, int default_balance) {
		this.name = currency_name;
		this.display_name = display_name;
		this.default_balance = default_balance;
	}

	public void register_scoreboard() {
		this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		this.objective = scoreboard.getObjective(this.name);
		if (this.objective == null) {
			this.objective = scoreboard.registerNewObjective(this.name, "dummy", this.display_name);
		}
	}

	public void register_service() {
		Bukkit.getServicesManager().register(Economy.class, this, Dropper_shop_plugin.instance, ServicePriority.Normal);
	}

	void set_id(int id) {
		this.id = id;
	}

	public int get_id() {
		return this.id;
	}

	public String get_display_name() {
		return this.display_name;
	}

	/**
	 * Checks if economy method is enabled.
	 *
	 * @return Success or Failure
	 */
	@Override
	public boolean isEnabled() {
		return Dropper_shop_plugin.instance.isEnabled();
	}

	/**
	 * Gets name of economy method
	 *
	 * @return Name of Economy Method
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Returns true if the given implementation supports banks.
	 *
	 * @return true if the implementation supports banks
	 */
	@Override
	public boolean hasBankSupport() {
		return false;
	}

	/**
	 * Some economy plugins round off after a certain number of digits.
	 * This function returns the number of digits the plugin keeps
	 * or -1 if no rounding occurs.
	 *
	 * @return number of digits after the decimal point kept
	 */
	@Override
	public int fractionalDigits() {
		return 0;
	}

	/**
	 * Format amount into a human readable String This provides translation into
	 * economy specific formatting to improve consistency between plugins.
	 *
	 * @param amount to format
	 * @return Human readable string describing amount
	 */
	@Override
	public String format(double amount) {
		String show_quantity;
		if (amount > 1e9) {
			show_quantity = String.format("%.3g G", amount / 1e9);
		} else if (amount > 1e6) {
			show_quantity = String.format("%.3g M", amount / 1e6);
		} else if (amount > 1e3) {
			show_quantity = String.format("%.3g k", amount / 1e3);
		} else {
			show_quantity = String.format("%d ", (int) amount);
		}

		return show_quantity + this.display_name;
	}

	/**
	 * Returns the name of the currency in plural form.
	 * If the economy being used does not support currency names then an empty string will be returned.
	 *
	 * @return name of the currency (plural)
	 */
	@Override
	public String currencyNamePlural() {
		return this.display_name;
	}

	/**
	 * Returns the name of the currency in singular form.
	 * If the economy being used does not support currency names then an empty string will be returned.
	 *
	 * @return name of the currency (singular)
	 */
	@Override
	public String currencyNameSingular() {
		return this.display_name;
	}

	/**
	 * @param playerName 玩家用户名
	 * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer)} instead.
	 */
	@Override
	public boolean hasAccount(String playerName) {
		return true;
	}

	/**
	 * Checks if this player has an account on the server yet
	 * This will always return true if the player has joined the server at least once
	 * as all major economy plugins auto-generate a player account when the player joins the server
	 *
	 * @param player to check
	 * @return if the player has an account
	 */
	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return true;
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName  世界名称
	 * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer, String)} instead.
	 */
	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return true;
	}

	/**
	 * Checks if this player has an account on the server yet on the given world
	 * This will always return true if the player has joined the server at least once
	 * as all major economy plugins auto-generate a player account when the player joins the server
	 *
	 * @param player    to check in the world
	 * @param worldName world-specific account
	 * @return if the player has an account
	 */
	@Override
	public boolean hasAccount(OfflinePlayer player, String worldName) {
		return true;
	}

	/**
	 * @param playerName 玩家用户名
	 * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer)} instead.
	 */
	@Override
	public double getBalance(String playerName) {
		return this.objective.getScore(playerName).getScore();
	}

	/**
	 * Gets balance of a player
	 *
	 * @param player of the player
	 * @return Amount currently held in players account
	 */
	@Override
	public double getBalance(OfflinePlayer player) {
		if (player.getName() == null) {
			return 0;
		}
		return this.objective.getScore(player.getName()).getScore();
	}

	/**
	 * @param playerName 玩家用户名
	 * @param world      世界名称
	 * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer, String)} instead.
	 */
	@Override
	public double getBalance(String playerName, String world) {
		return this.getBalance(playerName);
	}

	/**
	 * Gets balance of a player on the specified world.
	 * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
	 *
	 * @param player to check
	 * @param world  name of the world
	 * @return Amount currently held in players account
	 */
	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return this.getBalance(player);
	}

	/**
	 * @param playerName 玩家用户名
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #has(OfflinePlayer, double)} instead.
	 */
	@Override
	public boolean has(String playerName, double amount) {
		return this.getBalance(playerName) >= amount;
	}

	/**
	 * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param player to check
	 * @param amount to check for
	 * @return True if <b>player</b> has <b>amount</b>, False else wise
	 */
	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return this.getBalance(player) >= amount;
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName  世界名称
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use @{link {@link #has(OfflinePlayer, String, double)} instead.
	 */
	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return this.has(playerName, amount);
	}

	/**
	 * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
	 * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
	 *
	 * @param player    to check
	 * @param worldName to check with
	 * @param amount    to check for
	 * @return True if <b>player</b> has <b>amount</b>, False else wise
	 */
	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return this.has(player, amount);
	}

	/**
	 * @param playerName 玩家用户名
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, double)} instead.
	 */
	@Override
	public synchronized EconomyResponse withdrawPlayer(String playerName, double amount) {
		return this.withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	/**
	 * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param player to withdraw from
	 * @param amount Amount to withdraw
	 * @return Detailed response of transaction
	 */
	@Override
	public synchronized EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		Score score = this.objective.getScore(player.getName());
		int bal = score.getScore();
		if (bal < amount) {
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.FAILURE, "余额不足，当前余额为" + this.format(bal));
		}
		score.setScore((int) (bal - amount));
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "成功");
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName  世界名称
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, String, double)} instead.
	 */
	@Override
	public synchronized EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		return this.withdrawPlayer(playerName, amount);
	}

	/**
	 * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
	 * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
	 *
	 * @param player    to withdraw from
	 * @param worldName - name of the world
	 * @param amount    Amount to withdraw
	 * @return Detailed response of transaction
	 */
	@Override
	public synchronized EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return this.withdrawPlayer(player, amount);
	}

	/**
	 * @param playerName 玩家用户名
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, double)} instead.
	 */
	@Override
	public synchronized EconomyResponse depositPlayer(String playerName, double amount) {
		return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	/**
	 * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param player to deposit to
	 * @param amount Amount to deposit
	 * @return Detailed response of transaction
	 */
	@Override
	public synchronized EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		Score score = this.objective.getScore(player.getName());
		int bal = score.getScore();
		if (bal + amount > max_bal) {
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.FAILURE, "余额超出范围(" + max_bal + ")");
		}
		score.setScore((int) (bal + amount));
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "成功");
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName  世界名称
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, String, double)} instead.
	 */
	@Override
	public synchronized EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	/**
	 * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
	 * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
	 *
	 * @param player    to deposit to
	 * @param worldName name of the world
	 * @param amount    Amount to deposit
	 * @return Detailed response of transaction
	 */
	@Override
	public synchronized EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		return this.depositPlayer(player, amount);
	}

	/**
	 * @param name
	 * @param player
	 * @deprecated As of VaultAPI 1.4 use {{@link #createBank(String, OfflinePlayer)} instead.
	 */
	@Override
	public EconomyResponse createBank(String name, String player) {
		return null;
	}

	/**
	 * Creates a bank account with the specified name and the player as the owner
	 *
	 * @param name   of account
	 * @param player the account should be linked to
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return null;
	}

	/**
	 * Deletes a bank account with the specified name.
	 *
	 * @param name of the back to delete
	 * @return if the operation completed successfully
	 */
	@Override
	public EconomyResponse deleteBank(String name) {
		return null;
	}

	/**
	 * Returns the amount the bank has
	 *
	 * @param name of the account
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse bankBalance(String name) {
		return null;
	}

	/**
	 * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param name   of the account
	 * @param amount to check for
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return null;
	}

	/**
	 * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param name   of the account
	 * @param amount to withdraw
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return null;
	}

	/**
	 * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
	 *
	 * @param name   of the account
	 * @param amount to deposit
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return null;
	}

	/**
	 * @param name
	 * @param playerName
	 * @deprecated As of VaultAPI 1.4 use {{@link #isBankOwner(String, OfflinePlayer)} instead.
	 */
	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		return null;
	}

	/**
	 * Check if a player is the owner of a bank account
	 *
	 * @param name   of the account
	 * @param player to check for ownership
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		return null;
	}

	/**
	 * @param name
	 * @param playerName
	 * @deprecated As of VaultAPI 1.4 use {{@link #isBankMember(String, OfflinePlayer)} instead.
	 */
	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return null;
	}

	/**
	 * Check if the player is a member of the bank account
	 *
	 * @param name   of the account
	 * @param player to check membership
	 * @return EconomyResponse Object
	 */
	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		return null;
	}

	/**
	 * Gets the list of banks
	 *
	 * @return the List of Banks
	 */
	@Override
	public List<String> getBanks() {
		return null;
	}

	/**
	 * @param playerName 玩家用户名
	 * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer)} instead.
	 */
	@Override
	public boolean createPlayerAccount(String playerName) {
		return true;
	}

	/**
	 * Attempts to create a player account for the given player
	 *
	 * @param player OfflinePlayer
	 * @return if the account creation was successful
	 */
	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		return true;
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName
	 * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer, String)} instead.
	 */
	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return true;
	}

	/**
	 * Attempts to create a player account for the given player on the specified world
	 * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this then false will always be returned.
	 *
	 * @param player    OfflinePlayer
	 * @param worldName String name of the world
	 * @return if the account creation was successful
	 */
	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
		return true;
	}

	/**
	 * Creates a Map representation of this class.
	 * <p>
	 * This class must provide a method to restore this class, as defined in
	 * the {@link ConfigurationSerializable} interface javadocs.
	 *
	 * @return Map containing the current state of this class
	 */
	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> config = new HashMap<>();
		config.put("name", this.name);
		config.put("display-name", this.display_name);
		config.put("default-balance", this.default_balance);
		return config;
	}

	public static Scoreboard_economy deserialize(Map<String, Object> args) {
		String name = (String) args.get("name");
		String display_name = (String) args.get("display-name");
		int default_balance = (int) args.get("default-balance");
		return new Scoreboard_economy(name, display_name, default_balance);
	}
}
