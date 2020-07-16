package com.piggest.minecraft.bukkit.exp_saver;

import com.piggest.minecraft.bukkit.config.Price_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Custom_durability;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.Capacity_upgradable;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Exp_saver extends Multi_block_with_gui implements Capacity_upgradable, Auto_io {
	private int saved_exp = 0;
	private int max_saved_exp = 6000;
	private int structure_level = 1;
	private int anvil_count = 0;
	private int chipped_anvil_count = 0;
	private int damaged_anvil_count = 0;

	private int remove_next = 0;
	private static final int[][] hopper_check_list = { { 0, 3, 0 } };
	public static final int mending_slot = 13;

	public Exp_saver() {
		this.set_process(0);
		this.set_capacity_level(1);
		this.set_anvil_count(0, 0, 0);
		ItemStack remove_repaircost_button = this.gui.getItem(20);
		ItemMeta meta = remove_repaircost_button.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r需要" + Dropper_shop_plugin.instance.get_exp_saver_remove_repaircost_exp() + "点经验");
		meta.setLore(lore);
		remove_repaircost_button.setItemMeta(meta);
	}

	@Override
	public boolean completed() {
		if (this.get_block(0, 0, 0).getType() != Material.DIAMOND_BLOCK) {
			return false;
		}
		Material[] support = new Material[] { this.get_block(0, 1, 0).getType(), this.get_block(0, 2, 0).getType(),
				this.get_block(1, 2, 0).getType(), this.get_block(-1, 2, 0).getType(),
				this.get_block(0, 2, 1).getType(), this.get_block(0, 2, -1).getType() };
		for (Material material : support) {
			if (!Tag.WOODEN_FENCES.isTagged(material)) {
				return false;
			}
		}
		HashSet<Material> block_set = new HashSet<Material>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				block_set.add(this.get_block(i, 3, j).getType());
			}
		}
		if (!block_set.contains(Material.CREEPER_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.ZOMBIE_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.SKELETON_SKULL)) {
			return false;
		}
		if (!block_set.contains(Material.WITHER_SKELETON_SKULL)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	protected void set_from_save(Map<String, Object> shop_save) {
		super.set_from_save(shop_save);

		this.add_exp((int) shop_save.get("saved-exp"));
		int anvil = (int) shop_save.get("anvil-count");
		int chipped_anvil = (int) shop_save.get("chipped-anvil-count");
		int damaged_anvil = (int) shop_save.get("damaged-anvil-count");
		this.set_anvil_count(anvil, chipped_anvil, damaged_anvil);
	}

	public void set_mending(ItemStack item) {
		this.gui.setItem(mending_slot, item);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("saved-exp", this.saved_exp);
		save.put("anvil-count", this.anvil_count);
		save.put("chipped-anvil-count", this.chipped_anvil_count);
		save.put("damaged-anvil-count", this.damaged_anvil_count);
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
		this.send_message(player, "取出了" + need_exp + "点经验");
		player.giveExp(need_exp);
	}

	public void input_exp(int exp, Player player) {
		int remove_exp = exp;
		int rest_exp = SetExpFix.getTotalExperience(player);
		if (rest_exp < remove_exp) {
			remove_exp = rest_exp;
		}
		remove_exp = this.add_exp(remove_exp);
		this.send_message(player, "存入了" + remove_exp + "点经验");
		player.giveExp(-remove_exp);
	}

	public void set_process(int process) {
		this.set_process(0, process, "§e当前经验: %d/%d (%d级)", this.saved_exp, this.get_max_saved_exp(), this.get_level());
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
			this.capacity_upgrade_by(player);
		} else if (slot == 20) {
			this.remove_repaircost_tag(player);
		}
	}

	public void repair_mending_slot() {
		ItemStack mending = this.get_mending();
		if (mending == null) {
			return;
		}
		synchronized (mending) {
			Custom_durability.add_durability(mending, -this.remove_exp(1));
		}
	}

	public void remove_repaircost_tag(Player player) {
		if (!this.has_anvil()) {
			this.send_message(player, "没有进行铁砧升级，无法移除铁砧惩罚标签");
			return;
		}
		ItemStack item = this.get_mending();
		if (item == null) {
			this.send_message(player, "物品为空");
			return;
		}
		synchronized (item) {
			if (item.getType() == Material.AIR) {
				this.send_message(player, "物品为空");
				return;
			}
			ItemMeta meta = item.getItemMeta();
			if (!(meta instanceof Repairable)) {
				this.send_message(player, "该物品无法被修理");
				return;
			}
			Repairable repairable_meta = (Repairable) meta;
			if (!repairable_meta.hasRepairCost()) {
				this.send_message(player, "该物品没有铁砧惩罚标签");
				return;
			}
			int need_exp = Dropper_shop_plugin.instance.get_exp_saver_remove_repaircost_exp();
			if (this.get_saved_exp() < need_exp) {
				this.send_message(player, "存储器经验不足，需要" + need_exp + "点经验");
				return;
			}
			this.remove_exp(need_exp);
			repairable_meta.setRepairCost(0);
			item.setItemMeta(meta);
			this.send_message(player, "已移除铁砧惩罚标签");
		}
	}

	public ItemStack get_mending() {
		return this.gui.getItem(mending_slot);
	}

	public int get_max_saved_exp() {
		return this.max_saved_exp + 2000 * this.structure_level;
	}

	public int get_capacity_level() {
		return this.structure_level;
	}

	public void set_capacity_level(int structure_level) {
		this.structure_level = structure_level;
		ItemStack upgrade_button = this.gui.getItem(18);
		ItemMeta meta = upgrade_button.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r当前等级: " + structure_level + " / "
				+ Dropper_shop_plugin.instance.get_exp_saver_max_structure_level());
		lore.add("§7升级所需金钱: " + Exp_saver.get_upgrade_price(structure_level));
		lore.add("§7点击即可升级");
		meta.setLore(lore);
		upgrade_button.setItemMeta(meta);
		this.add_exp(0);
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	public static int get_upgrade_price(int level) {
		Price_config price_config = Dropper_shop_plugin.instance.get_price_config();
		return price_config.get_exp_saver_upgrade_level_price() * level
				+ price_config.get_exp_saver_upgrade_base_price();
	}

	public boolean capacity_upgrade_by(Player player) {
		int current_level = this.get_capacity_level();
		if (current_level >= Dropper_shop_plugin.instance.get_exp_saver_max_structure_level()) {
			this.send_message(player, "已经升级至满级");
			return false;
		}
		int need_price = Exp_saver.get_upgrade_price(current_level);
		String format_price = Dropper_shop_plugin.instance.get_economy().format(need_price);
		if (Dropper_shop_plugin.instance.cost_player_money(need_price, player)) {
			this.set_capacity_level(current_level + 1);
			this.send_message(player, "消耗了" + format_price + "把经验存储器升级至" + (current_level + 1) + "级");
			return true;
		} else {
			this.send_message(player, "你的钱不够，经验存储器由" + current_level + "升级至" + (current_level + 1) + "级需要" + format_price);
			return false;
		}
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		return true;
	}

	public boolean has_anvil() {
		return (this.anvil_count >= Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need()
				&& this.chipped_anvil_count >= Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need()
				&& this.damaged_anvil_count >= Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need());
	}

	public int get_level() {
		return SetExpFix.getLevelbyExp(this.saved_exp);
	}

	void set_remove_exp_next(int need_exp) {
		this.remove_next = need_exp;
	}

	int do_remove_exp() {
		int removed = this.remove_next;
		this.remove_exp(removed);
		this.remove_next = 0;
		return removed;
	}

	public void set_anvil_count(int anvil, int chipped_anvil, int damaged_anvil) {
		this.anvil_count = anvil;
		this.chipped_anvil_count = chipped_anvil;
		this.damaged_anvil_count = damaged_anvil;
		ItemStack anvil_icon = this.gui.getItem(19);
		ItemMeta meta = anvil_icon.getItemMeta();
		if (this.has_anvil()) {
			meta.setDisplayName("§e铁砧升级已完成");
		} else {
			meta.setDisplayName("§e铁砧升级未完成");
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7铁砧: " + anvil + "/" + Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need());
		lore.add("§7开裂的铁砧: " + chipped_anvil + "/" + Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need());
		lore.add("§7损坏的铁砧: " + damaged_anvil + "/" + Dropper_shop_plugin.instance.get_exp_saver_anvil_upgrade_need());
		lore.add("§r把铁砧投入物品修补栏即可升级");
		meta.setLore(lore);
		anvil_icon.setItemMeta(meta);
	}

	public int get_anvil_count() {
		return this.anvil_count;
	}

	public int get_chipped_anvil_count() {
		return this.chipped_anvil_count;
	}

	public int get_damaged_anvil_count() {
		return this.damaged_anvil_count;
	}

	@Override
	protected void init_after_set_location() {
		return;
	}

	public Hopper get_hopper() {
		return this.get_hopper(hopper_check_list, false);
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		return true;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return new ItemStack[] { this.get_mending() };
	}

	@Nonnull
	public static Exp_saver deserialize(@Nonnull Map<String, Object> args) {
		Exp_saver structure = new Exp_saver();
		structure.set_from_save(args);
		return structure;
	}
}
