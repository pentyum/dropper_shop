package com.piggest.minecraft.bukkit.grinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Grinder extends Multi_block_with_gui implements HasRunner, Auto_io {
	private Grinder_runner runner = new Grinder_runner(this);
	private Grinder_io_runner io_runner = new Grinder_io_runner(this);
	public static final int raw_slot = 9;
	public static final int flint_slot = 11;
	public static final int product_slot = 13;
	private static final int[][] product_chest_check_list = new int[][] { { 1, -2, 0 }, { -1, -2, 0 }, { 0, -2, 1 },
			{ 0, -2, -1 } };
	private static final int[][] raw_hopper_check_list = new int[][] { { 0, 1, 0 } };

	public Grinder() {
		/*
		 * ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE); ItemMeta
		 * meta = white.getItemMeta(); meta.setDisplayName("§e磨粉机工作进度: 0 %");
		 * white.setItemMeta(meta); for (int i = 0; i < 9; i++) { this.gui.setItem(i,
		 * white.clone()); }
		 */
		ItemStack flint_info = this.gui.getItem(17);
		ItemMeta flint_info_meta = flint_info.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r0 单位");
		flint_info_meta.setLore(lore);
		flint_info.setItemMeta(flint_info_meta);
		this.gui.setItem(17, flint_info);
	}

	public static void set_item_name(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public synchronized int get_flint_storage() {
		int storage = 0;
		ItemStack flint_info = this.gui.getContents()[17];
		ItemMeta flint_info_meta = flint_info.getItemMeta();
		List<String> lore = flint_info_meta.getLore();
		String line = lore.get(0);
		String pattern = "§r([1-9]\\d*|0) 单位";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			storage = Integer.parseInt(m.group(1));
			return storage;
		}
		return 0;
	}

	public synchronized void set_flint_storge(int storage) {
		ItemStack flint_info = this.gui.getContents()[17];
		ItemMeta flint_info_meta = flint_info.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r" + storage + " 单位");
		flint_info_meta.setLore(lore);
		flint_info.setItemMeta(flint_info_meta);
	}

	public ItemStack get_raw() {
		return this.gui.getItem(raw_slot);
	}

	public ItemStack get_flint() {
		return this.gui.getItem(flint_slot);
	}

	public ItemStack get_product() {
		return this.gui.getItem(product_slot);
	}

	public void set_product(ItemStack product_item) {
		this.gui.setItem(product_slot, product_item);
	}

	public void set_raw(ItemStack raw_item) {
		this.gui.setItem(raw_slot, raw_item);
	}

	public void set_flint(ItemStack flint_item) {
		this.gui.setItem(flint_slot, flint_item);
	}

	public static boolean is_empty(ItemStack item) {
		if (item == null) {
			return true;
		}
		if (item.getType() == Material.AIR) {
			return true;
		}
		return false;
	}

	public Hopper get_hopper() {
		return this.get_hopper(raw_hopper_check_list, false);
	}

	public Chest get_chest() {
		return this.get_chest(product_chest_check_list);
	}

	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.runner, this.io_runner };
	}

	public BukkitRunnable get_io_runner() {
		return this.io_runner;
	}

	public synchronized boolean to_product() {
		if (!Inventory_io.is_empty(this.get_raw())) {
			ItemStack main_product_item = this.get_manager().get_main_product(this.get_raw().getType());
			ItemStack minor_product_item = this.get_manager().get_minor_product(this.get_raw().getType());
			if (main_product_item != null) {
				if (minor_product_item == null) { // 只生成一种产物
					if (Grinder.is_empty(this.get_product())) {
						this.set_product(main_product_item.clone());
						if (Inventory_io.Item_remove_one(this.get_raw()) != null) {
							return true;
						}
					} else if (this.get_product().isSimilar(main_product_item)) {
						int new_num = this.get_product().getAmount() + main_product_item.getAmount();
						if (new_num <= main_product_item.getMaxStackSize()) {
							if (Inventory_io.Item_remove_one(this.get_raw()) != null) {
								this.get_product().setAmount(new_num);
								return true;
							}
						}
					}
				} else { // 有副产物

				}
			}
		}
		return false;
	}

	public synchronized boolean add_a_raw(ItemStack src_item) {
		return Inventory_io.move_a_item_to_slot(src_item, gui, Grinder.raw_slot);
		/*
		 * if (!Grinder.is_empty(src_item)) { if (Grinder.is_empty(this.get_raw())) {
		 * this.set_raw(src_item.clone()); this.get_raw().setAmount(1);
		 * src_item.setAmount(src_item.getAmount() - 1); return true; } else if
		 * (src_item.isSimilar(this.get_raw())) { int new_num = 1 +
		 * this.get_raw().getAmount(); if (new_num <= src_item.getMaxStackSize()) {
		 * this.get_raw().setAmount(new_num); src_item.setAmount(src_item.getAmount() -
		 * 1); return true; } } } return false;
		 */
	}

	public synchronized boolean add_a_flint(ItemStack src_item) {
		return Inventory_io.move_a_item_to_slot(src_item, gui, Grinder.flint_slot);
		/*
		 * if (!Grinder.is_empty(src_item)) { if (Grinder.is_empty(this.get_flint())) {
		 * this.set_flint(src_item.clone()); this.get_flint().setAmount(1);
		 * src_item.setAmount(src_item.getAmount() - 1); return true; } else if
		 * (src_item.isSimilar(this.get_flint())) { int new_num = 1 +
		 * this.get_flint().getAmount(); if (new_num <= src_item.getMaxStackSize()) {
		 * this.get_flint().setAmount(new_num); src_item.setAmount(src_item.getAmount()
		 * - 1); return true; } } } return false;
		 */
	}

	public synchronized void set_process(int process) {
		this.set_process(0, process, "§e磨粉机工作进度: %d %%", process);
	}

	@Override
	protected void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.set_flint_storge((Integer) shop_save.get("flint-storge"));
		this.runner.working_ticks = (Integer) shop_save.get("working-ticks");
		if (shop_save.get("raw") != null) {
			ItemStack raw_item = Material_ext.new_item((String) shop_save.get("raw"), (int) shop_save.get("raw-num"));
			this.set_raw(raw_item);
		}
		if (shop_save.get("flint") != null) {
			ItemStack flint_item = Material_ext.new_item((String) shop_save.get("flint"),
					(int) shop_save.get("flint-num"));
			this.set_flint(flint_item);
		}
		if (shop_save.get("product") != null) {
			ItemStack product_item = Material_ext.new_item((String) shop_save.get("product"),
					(int) shop_save.get("product-num"));
			this.set_product(product_item);
		}
	}

	@Override
	protected HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("flint-storge", this.get_flint_storage());
		save.put("working-ticks", this.runner.working_ticks);
		if (!Grinder.is_empty(this.get_raw())) {
			save.put("raw", Material_ext.get_id_name(this.get_raw()));
			save.put("raw-num", this.get_raw().getAmount());
		}
		if (!Grinder.is_empty(this.get_flint())) {
			save.put("flint", this.get_flint().getType().name());
			save.put("flint-num", this.get_flint().getAmount());
		}
		if (!Grinder.is_empty(this.get_product())) {
			save.put("product", Material_ext.get_id_name(this.get_product()));
			save.put("product-num", this.get_product().getAmount());
		}
		return save;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_grinder_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			player.sendMessage("[磨粉机]已扣除" + price);
			return true;
		} else {
			player.sendMessage("[磨粉机]建立磨粉机所需的钱不够，需要" + price);
			return false;
		}
	}

	@Override
	public void on_button_pressed(Player player, int slot) {

	}

	@Override
	public Grinder_manager get_manager() {
		return (Grinder_manager) super.get_manager();
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		return true;
	}

	@Override
	protected void init_after_set_location() {
		return;
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
		return new ItemStack[] { this.get_raw(), this.get_flint(), this.get_product() };
	}
}