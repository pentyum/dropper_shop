package com.piggest.minecraft.bukkit.exp_saver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Exp_saver extends Multi_block_structure implements InventoryHolder, HasRunner {
	private int saved_exp = 0;
	private Exp_saver_runner exp_saver_runner = new Exp_saver_runner(this);
	private Inventory gui = Bukkit.createInventory(this, 27, "经验存储器");

	@Override
	public void on_right_click(Player player) {
		player.closeInventory();
		player.openInventory(this.getInventory());
	}

	@Override
	public int completed() {
		if (this.get_block(0, 0, 0).getType() != Material.GOLD_BLOCK) {
			return 0;
		}
		HashSet<Material> block_set = new HashSet<Material>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				block_set.add(this.get_block(i, 3, j).getType());
			}
		}
		if (!block_set.contains(Material.CREEPER_HEAD)) {
			return 0;
		}
		if (!block_set.contains(Material.ZOMBIE_HEAD)) {
			return 0;
		}
		if (!block_set.contains(Material.SKELETON_SKULL)) {
			return 0;
		}
		if (!block_set.contains(Material.PLAYER_HEAD)) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean in_structure(Location loc) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	public BukkitRunnable[] get_runner() {
		return new BukkitRunnable[] { this.exp_saver_runner };
	}

	@Override
	public int[] get_runner_cycle() {
		return new int[] { 10 };
	}

	@Override
	public int[] get_runner_delay() {
		return new int[] { 10 };
	}

	@Override
	public Inventory getInventory() {
		return this.gui;
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.saved_exp = (Integer) shop_save.get("saved-exp");
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("saved-exp", this.saved_exp);
		return save;
	}
}
