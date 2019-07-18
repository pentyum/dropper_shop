package com.piggest.minecraft.bukkit.dropper_shop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Ownable;
import com.piggest.minecraft.bukkit.structure.Single_block_structure;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop extends Single_block_structure implements Ownable {
	private Dropper dropper_block = null;
	private Material sell_item = null;
	private String owner = null;
	// private Depository assoc = null;

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
		// plugin.getLogger().info("已添加"+itemstack.getType().name());
		this.dropper_block.getInventory().addItem(itemstack);
		// this.dropper_block.getBlock().setBlockData(this.dropper_block.getBlockData());
	}

	public boolean buy() {
		int price = Dropper_shop_plugin.instance.get_price(this.sell_item);
		Economy economy = Dropper_shop_plugin.instance.get_economy();
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

	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		// save.put("assoc-x", this.assoc.get_location().getBlockX());
		// save.put("assoc-y", this.assoc.get_location().getBlockY());
		// save.put("assoc-z", this.assoc.get_location().getBlockZ());
		save.put("item", this.sell_item.name());
		return save;
	}

	public String get_owner_name() {
		return this.owner;
	}

	public boolean completed() {
		if (this.dropper_block.getBlock().getType() == Material.DROPPER) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		String world_name = (String) shop_save.get("world");
		int x = (Integer) shop_save.get("x");
		int y = (Integer) shop_save.get("y");
		int z = (Integer) shop_save.get("z");
		// int assoc_x = (Integer) shop_save.get("assoc_x");
		// int assoc_y = (Integer) shop_save.get("assoc-y");
		// int assoc_z = (Integer) shop_save.get("assoc-z");
		String owner = (String) shop_save.get("owner");
		Material item = Material.getMaterial((String) shop_save.get("item"));
		this.set_selling_item(item);
		this.set_location(world_name, x, y, z);
		this.set_owner(owner);
	}

	@Override
	public void set_location(Location loc) {
		this.dropper_block = (Dropper) loc.getBlock().getState();
	}

	@Override
	public void set_location(String world_name, int x, int y, int z) {
		World world = Bukkit.getWorld(world_name);
		Location loc = new Location(world, x, y, z);
		this.dropper_block = (Dropper) loc.getBlock().getState();
	}

	public boolean in_structure(Location loc) {
		if (loc.equals(this.get_location())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean create_condition(Player player) {
		if (Dropper_shop_plugin.instance.get_economy().has(player,
				Dropper_shop_plugin.instance.get_price_config().get_make_shop_price())) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player,
					Dropper_shop_plugin.instance.get_price_config().get_make_shop_price());
			return true;
		} else {
			player.sendMessage("钱不够");
			return false;
		}
	}

	@Override
	protected boolean on_break(Player player) {
		if (this.get_owner_name().equalsIgnoreCase(player.getName())) {
			return true;
		} else {
			if (player.hasPermission("dropper_shop.remove.others")) {
				return true;
			} else {
				player.sendMessage("你没有破坏其他人的投掷器商店的权限");
				return false;
			}
		}
	}

	@Override
	public void init_after_set_location() {
		return;
	}
}
