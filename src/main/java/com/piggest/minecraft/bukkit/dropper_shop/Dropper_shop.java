package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop {
	private Dropper_shop_plugin plugin = null;
	private Dropper dropper_block = null;
	private Material sell_item = null;
	private String owner = null;

	public Dropper_shop(Dropper_shop_plugin plugin, Location loc) {
		this.plugin = plugin;
		this.dropper_block = (Dropper) loc.getBlock();
	}

	public Dropper_shop(Dropper_shop_plugin plugin, String world_name, int x, int y, int z) {
		this.plugin = plugin;
		World world = Bukkit.getWorld(world_name);
		Location loc = new Location(world, x, y, z);
		this.dropper_block = (Dropper) loc.getBlock();
	}

	public void set_selling_item(Material sell_item) {
		this.sell_item = sell_item;
	}

	public Material get_selling_item() {
		return this.sell_item;
	}

	public void set_owner(String player_name) {
		this.owner = player_name;
	}

	@SuppressWarnings("deprecation")
	public OfflinePlayer get_owner() {
		return Bukkit.getOfflinePlayer(this.owner);
	}

	public void add_item() {
		ItemStack itemstack = new ItemStack(this.sell_item);
		this.dropper_block.getInventory().addItem(itemstack);
	}

	public boolean buy() {
		int price = plugin.get_price(this.sell_item);
		Economy economy = this.plugin.get_economy();
		OfflinePlayer player = this.get_owner();
		if (economy.has(player, price)) {
			economy.withdrawPlayer(player, price);
			this.add_item();
			return true;
		}
		return false;
	}

	public Location get_location() {
		return this.dropper_block.getLocation();
	}
}
