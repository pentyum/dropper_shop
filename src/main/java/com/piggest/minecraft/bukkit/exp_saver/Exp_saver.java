package com.piggest.minecraft.bukkit.exp_saver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Exp_saver extends Multi_block_with_gui implements HasRunner {
	private int saved_exp = 0;
	private int max_saved_exp = 6000;
	private int structure_level = 1;
	private Exp_saver_runner exp_saver_runner = new Exp_saver_runner(this);

	public Exp_saver() {
		this.set_process(0);
		this.set_structure_level(1);
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
		if (!player.hasPermission("exp_saver.make")) {
			player.sendMessage("你没有建立经验存储器的权限");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.exp_saver_runner };
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.add_exp((Integer) shop_save.get("saved-exp"));
		this.set_structure_level((int) shop_save.get("structure-level"));
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("saved-exp", this.saved_exp);
		save.put("structure-level", this.structure_level);
		return save;
	}

	public int get_saved_exp() {
		return this.saved_exp;
	}

	public int add_exp(int exp) {
		if (this.saved_exp + exp > this.get_max_saved_exp()) {
			exp = this.get_max_saved_exp() - this.saved_exp;
		}
		this.saved_exp += exp;
		this.set_process(this.saved_exp * 100 / this.get_max_saved_exp());
		return exp;
	}

	public int remove_exp(int exp) {
		if (exp > this.saved_exp) {
			exp = this.saved_exp;
		}
		this.saved_exp -= exp;
		this.set_process(this.saved_exp * 100 / this.get_max_saved_exp());
		return exp;
	}

	public void output_exp(int exp, Player player) {
		int need_exp = exp;
		need_exp = this.remove_exp(exp);
		player.sendMessage("取出了" + need_exp + "点经验");
		player.giveExp(need_exp);
	}

	public void input_exp(int exp, Player player) {
		int remove_exp = exp;
		int rest_exp = SetExpFix.getTotalExperience(player);
		if (rest_exp < remove_exp) {
			remove_exp = rest_exp;
		}
		remove_exp = this.add_exp(remove_exp);
		player.sendMessage("存入了" + remove_exp + "点经验");
		player.giveExp(-remove_exp);
	}
	/*
	 * public static int get_exp_to_level(int level) { if (level <= 16) { return
	 * level * level + 6 * level; } else if (level <= 31) { return (int) (2.5 *
	 * level * level + 40.5 * level + 360); } else { return (int) (4.5 * level *
	 * level + 162.5 * level + 2220); } }
	 * 
	 * public static int get_upgrade_exp(int level) { if (level <= 15) { return 2 *
	 * level + 7; } else if (level <= 30) { return 5 * level - 38; } else { return 9
	 * * level - 158; } }
	 * 
	 * 
	 * public static int get_excess_exp(Player player) { return
	 * player.getTotalExperience() - get_exp_to_level(player.getLevel()); }
	 * 
	 * public static int get_all_exp(Player player) { return
	 * Math.round(get_exp_to_level(player.getLevel()) + player.getExp() *
	 * player.getExpToLevel()); }
	 */

	public void set_process(int process) {
		this.set_process(0, process, "§e当前经验: %d/%d", this.saved_exp, this.get_max_saved_exp());
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		if (slot == 9) {
			this.output_exp(10, player);
		} else if (slot == 10) {
			this.output_exp(100, player);
		} else if (slot == 11) {
			this.output_exp(1000, player);
		} else if (slot == 15) {
			this.input_exp(10, player);
		} else if (slot == 16) {
			this.input_exp(100, player);
		} else if (slot == 17) {
			this.input_exp(1000, player);
		} else if (slot == 18) {
			this.upgrade_by(player);
		}
	}

	public ItemStack get_mending() {
		return this.gui.getItem(13);
	}

	public int get_max_saved_exp() {
		return this.max_saved_exp + 2000 * this.structure_level;
	}

	public int get_structure_level() {
		return this.structure_level;
	}

	public void set_structure_level(int structure_level) {
		this.structure_level = structure_level;
		ItemStack upgrade_button = this.gui.getItem(18);
		ItemMeta meta = upgrade_button.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r当前等级: "+structure_level);
		lore.add("§r升级所需金币: "+Exp_saver.get_upgrade_price(structure_level));
		meta.setLore(lore);
		upgrade_button.setItemMeta(meta);
		this.add_exp(0);
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	public static int get_upgrade_price(int level) {
		return Dropper_shop_plugin.instance.get_exp_saver_upgrade_base_price() * level;
	}

	public boolean upgrade_by(Player player) {
		int current_level = this.get_structure_level();
		if (current_level >= 10) {
			player.sendMessage("已经升级至满级");
			return false;
		}
		int need_price = Exp_saver.get_upgrade_price(current_level);
		if (Dropper_shop_plugin.instance.cost_player_money(need_price, player)) {
			this.set_structure_level(current_level + 1);
			player.sendMessage("消耗了" + need_price + "金币把经验存储器升级至" + (current_level + 1) + "级");
			return true;
		} else {
			player.sendMessage(
					"你的钱不够，经验存储器由" + current_level + "升级至" + (current_level + 1) + "级需要" + need_price + "金币");
			return false;
		}
	}
}
