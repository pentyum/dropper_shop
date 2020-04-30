package com.piggest.minecraft.bukkit.ecomomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class Scoreboard_economy implements Economy {
	private String name;
	private String display_name;
	private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	private Objective objective;
	private int max_bal = 1000000000;

	public Scoreboard_economy(String currency_name, String display_name) {
		this.name = currency_name;
		this.display_name = display_name;
		this.objective = scoreboard.getObjective(this.name);
		if (this.objective == null) {
			this.objective = scoreboard.registerNewObjective(this.name, "dummy", this.display_name);
		}
	}

	/**
	 * Checks if economy method is enabled.
	 *
	 * @return Success or Failure
	 */
	@Override
	public boolean isEnabled() {
		return true;
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
		return String.valueOf((int) amount);
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
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
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
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		Score score = this.objective.getScore(player.getName());
		int bal = score.getScore();
		if (bal < amount) {
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.FAILURE, "货币不够");
		}
		score.setScore(bal - amount);
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "成功");
	}

	/**
	 * @param playerName 玩家用户名
	 * @param worldName  世界名称
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, String, double)} instead.
	 */
	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
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
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return this.withdrawPlayer(player, amount);
	}

	/**
	 * @param playerName 玩家用户名
	 * @param amount     数量
	 * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, double)} instead.
	 */
	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
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
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		Score score = this.objective.getScore(player.getName());
		int bal = score.getScore();
		if (bal + amount > max_bal) {
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.FAILURE, "货币超出范围");
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
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
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
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
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
}
