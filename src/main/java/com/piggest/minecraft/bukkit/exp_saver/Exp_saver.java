package com.piggest.minecraft.bukkit.exp_saver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Exp_saver extends Multi_block_with_gui {
	private int saved_exp = 0;
	private int max_saved_exp = 5000;
	private Exp_saver_runner exp_saver_runner = new Exp_saver_runner(this);
	private Inventory gui = Bukkit.createInventory(this, 27, "经验存储器");
	private static int[] buttons = new int[] { 9, 10, 11, 15, 16, 17 };

	public Exp_saver() {
		this.set_gui(buttons[0], Material.FIREWORK_ROCKET, "§r取出1级");
		this.set_gui(buttons[1], Material.FIREWORK_ROCKET, "§r取出5级");
		this.set_gui(buttons[2], Material.FIREWORK_ROCKET, "§r取出10级");
		this.set_gui(buttons[3], Material.HOPPER, "§r存入1级");
		this.set_gui(buttons[4], Material.HOPPER, "§r存入5级");
		this.set_gui(buttons[5], Material.HOPPER, "§r存入10级");
		this.set_process(0);
	}

	public static int[] get_buttons() {
		return buttons;
	}

	@Override
	public int completed() {
		if (this.get_block(0, 0, 0).getType() != Material.DIAMOND_BLOCK) {
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
		if (!block_set.contains(Material.WITHER_SKELETON_SKULL)) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean in_structure(Location loc) {
		if (loc.equals(this.get_location())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.exp_saver_runner };
	}

	@Override
	public Inventory getInventory() {
		return this.gui;
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.add_exp((Integer) shop_save.get("saved-exp"));
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("saved-exp", this.saved_exp);
		return save;
	}

	public int get_saved_exp() {
		return this.saved_exp;
	}

	public int add_exp(int exp) {
		if (this.saved_exp + exp > this.max_saved_exp) {
			exp = this.max_saved_exp - this.saved_exp;
		}
		this.saved_exp += exp;
		this.set_process(this.saved_exp * 100 / this.max_saved_exp);
		return exp;
	}

	public int remove_exp(int exp) {
		if (exp > this.saved_exp) {
			exp = this.saved_exp;
		}
		this.saved_exp -= exp;
		this.set_process(this.saved_exp * 100 / this.max_saved_exp);
		return exp;
	}

	public void output_exp(int level, Player player) {
		int need_exp = get_exp_to_level(player.getLevel() + level) - get_all_exp(player);
		// player.sendMessage("当前经验"+get_all_exp(player)+"目标经验"+get_exp_to_level(player.getLevel()
		// + level)+"升级经验"+player.getExpToLevel());
		need_exp = this.remove_exp(need_exp);
		player.sendMessage("取出了" + need_exp + "点经验");
		player.giveExp(need_exp);
	}

	public void input_exp(int level, Player player) {
		int target_level = player.getLevel() - level;
		if (target_level < 0) {
			target_level = 0;
		}
		int remove_exp = get_all_exp(player) - get_exp_to_level(target_level);
		remove_exp = this.add_exp(remove_exp);
		player.sendMessage("存入了" + remove_exp + "点经验");
		player.giveExp(-remove_exp);
	}

	public static int get_exp_to_level(int level) {
		if (level <= 16) {
			return level * level + 6 * level;
		} else if (level <= 31) {
			return (int) (2.5 * level * level + 40.5 * level + 360);
		} else {
			return (int) (4.5 * level * level + 162.5 * level + 2220);
		}
	}

	public static int get_upgrade_exp(int level) {
		if (level <= 15) {
			return 2 * level + 7;
		} else if (level <= 30) {
			return 5 * level - 38;
		} else {
			return 9 * level - 158;
		}
	}

	public static int get_excess_exp(Player player) {
		return player.getTotalExperience() - get_exp_to_level(player.getLevel());
	}

	public static int get_all_exp(Player player) {
		return Math.round(get_exp_to_level(player.getLevel()) + player.getExp() * player.getExpToLevel());
	}

	public void set_process(int process) {
		int n = process * 9 / 100;
		int i = 0;
		ItemStack red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta meta = red.getItemMeta();
		meta.setDisplayName("§e当前经验: " + this.saved_exp + "/" + this.max_saved_exp);
		red.setItemMeta(meta);
		white.setItemMeta(meta);
		for (i = 0; i < n; i++) {
			this.gui.setItem(i, red.clone());
		}
		for (i = n; i < 9; i++) {
			this.gui.setItem(i, white.clone());
		}
	}
	
}
