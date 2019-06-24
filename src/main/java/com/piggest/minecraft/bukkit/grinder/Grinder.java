package com.piggest.minecraft.bukkit.grinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Grinder extends Multi_block_with_gui implements HasRunner {
	private Grinder_runner runner = new Grinder_runner(this);
	private Grinder_io_runner io_runner = new Grinder_io_runner(this);

	public Grinder() {
		/*
		ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta meta = white.getItemMeta();
		meta.setDisplayName("§e磨粉机工作进度: 0 %");
		white.setItemMeta(meta);
		for (int i = 0; i < 9; i++) {
			this.gui.setItem(i, white.clone());
		}
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

	public Inventory get_gui() {
		return this.gui;
	}

	public ItemStack get_raw() {
		return this.gui.getContents()[9];
	}

	public ItemStack get_flint() {
		return this.gui.getContents()[11];
	}

	public ItemStack get_product() {
		return this.gui.getContents()[13];
	}

	public ItemStack get_product_2() {
		return this.gui.getContents()[15];
	}

	public void set_product(ItemStack product_item) {
		this.gui.setItem(13, product_item);
	}

	public void set_raw(ItemStack raw_item) {
		this.gui.setItem(9, raw_item);
	}

	public void set_flint(ItemStack flint_item) {
		this.gui.setItem(11, flint_item);
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

	@Override
	public int completed() {
		if (this.get_block(0, 0, 0).getType() != Material.SMOOTH_STONE) {
			return 0;
		}
		if (this.get_block(0, -1, 0).getType() != Material.COBBLESTONE_WALL) {
			return 0;
		}
		if (this.get_block(1, 0, 0).getType() != Material.END_ROD) {
			return 0;
		}
		if (this.get_block(-1, 0, 0).getType() != Material.END_ROD) {
			return 0;
		}
		if (this.get_block(0, 0, 1).getType() != Material.END_ROD) {
			return 0;
		}
		if (this.get_block(0, 0, -1).getType() != Material.END_ROD) {
			return 0;
		}
		if (this.get_block(0, -2, 0).getType() != Material.IRON_BLOCK) {
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

	public synchronized Hopper get_hopper() {
		BlockState up_block = this.get_block(0, 1, 0).getState();
		if (up_block instanceof Hopper) {
			Hopper up_hopper = (Hopper) up_block;
			return up_hopper;
		}
		return null;
	}

	public synchronized Chest get_chest() {
		BlockState chest = this.get_block(1, -2, 0).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(-1, -2, 0).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(0, -2, 1).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(0, -2, -1).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		return null;
	}

	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.runner, this.io_runner };
	}

	public BukkitRunnable get_io_runner() {
		return this.io_runner;
	}

	public synchronized boolean to_product() {
		if (!Grinder.is_empty(this.get_raw())) {
			ItemStack product_item = this.get_manager().recipe.get(this.get_raw().getType());
			if (product_item != null) {
				if (Grinder.is_empty(this.get_product())) {
					this.set_product(product_item.clone());
					this.get_raw().setAmount(this.get_raw().getAmount() - 1);
					return true;
				} else if (this.get_product().isSimilar(product_item)) {
					int new_num = this.get_product().getAmount() + product_item.getAmount();
					if (new_num <= product_item.getMaxStackSize()) {
						this.get_product().setAmount(new_num);
						this.get_raw().setAmount(this.get_raw().getAmount() - 1);
						return true;
					}
				}
			}
		}
		return false;
	}

	public synchronized boolean add_a_raw(ItemStack src_item) {
		if (!Grinder.is_empty(src_item)) {
			if (Grinder.is_empty(this.get_raw())) {
				this.set_raw(src_item.clone());
				this.get_raw().setAmount(1);
				src_item.setAmount(src_item.getAmount() - 1);
				return true;
			} else if (src_item.isSimilar(this.get_raw())) {
				int new_num = 1 + this.get_raw().getAmount();
				if (new_num <= src_item.getMaxStackSize()) {
					this.get_raw().setAmount(new_num);
					src_item.setAmount(src_item.getAmount() - 1);
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean add_a_flint(ItemStack src_item) {
		if (!Grinder.is_empty(src_item)) {
			if (Grinder.is_empty(this.get_flint())) {
				this.set_flint(src_item.clone());
				this.get_flint().setAmount(1);
				src_item.setAmount(src_item.getAmount() - 1);
				return true;
			} else if (src_item.isSimilar(this.get_flint())) {
				int new_num = 1 + this.get_flint().getAmount();
				if (new_num <= src_item.getMaxStackSize()) {
					this.get_flint().setAmount(new_num);
					src_item.setAmount(src_item.getAmount() - 1);
					return true;
				}
			}
		}
		return false;
	}

	public synchronized void set_process(int process) {
		this.set_process(0, process, "§e磨粉机工作进度: %d %%", process);
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
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
	public HashMap<String, Object> get_save() {
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
		if (!player.hasPermission("grinder.make")) {
			player.sendMessage("你没有建立磨粉机的权限");
			return false;
		}
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("建立磨粉机所需的钱不够，需要" + price);
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
}