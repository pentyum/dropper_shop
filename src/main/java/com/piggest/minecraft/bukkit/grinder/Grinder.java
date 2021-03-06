package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.utils.Inventory_io;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Grinder extends Multi_block_with_gui implements Auto_io {
	int working_ticks = 0;
	private Random random = new Random();
	public static final int raw_slot = 9;
	public static final int flint_slot = 11;
	public static final int main_product_slot = 13;
	public static final int minor_product_slot = 15;
	private static final int[][] product_chest_check_list = new int[][]{{1, -2, 0}, {-1, -2, 0}, {0, -2, 1},
			{0, -2, -1}};
	private static final int[][] raw_hopper_check_list = new int[][]{{0, 1, 0}};
	private int flint_storage = 0;

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

	public int get_flint_storage() {
		return this.flint_storage;
	}

	public synchronized void set_flint_storage(int storage) {
		ItemStack flint_info = this.gui.getContents()[17];
		ItemMeta flint_info_meta = flint_info.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§r" + storage + " 单位");
		flint_info_meta.setLore(lore);
		flint_info.setItemMeta(flint_info_meta);
		this.flint_storage = storage;
	}

	public ItemStack get_raw() {
		return this.gui.getItem(raw_slot);
	}

	public ItemStack get_flint() {
		return this.gui.getItem(flint_slot);
	}

	public ItemStack get_main_product() {
		return this.gui.getItem(main_product_slot);
	}

	public ItemStack get_minor_product() {
		return this.gui.getItem(minor_product_slot);
	}

	public void set_main_product(ItemStack product_item) {
		this.gui.setItem(main_product_slot, product_item);
	}

	public void set_minor_product(ItemStack product_item) {
		this.gui.setItem(minor_product_slot, product_item);
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

	public synchronized boolean to_product() {
		ItemStack raw = this.get_raw();
		if (!Inventory_io.is_empty(raw)) {
			ItemStack main_product_item = this.get_manager().get_main_product(raw.getType());
			ItemStack minor_product_item = this.get_manager().get_minor_product(raw.getType());
			if (main_product_item != null) {
				boolean main_product_slot_available = Inventory_io.try_move_item_to_slot(main_product_item,
						main_product_item.getAmount(), this.gui, main_product_slot);
				if (minor_product_item == null) {
					if (main_product_slot_available) {
						if (Inventory_io.item_remove(raw, 1) != null) {
							Inventory_io.move_item_to_slot(main_product_item.clone(), main_product_item.getAmount(),
									this.gui, main_product_slot);// 添加主产物
							return true;
						}
					}
				} else {
					boolean minor_product_slot_available = Inventory_io.try_move_item_to_slot(minor_product_item,
							minor_product_item.getAmount(), getInventory(), minor_product_slot);
					int minor_possibility = this.get_manager().get_minor_possibility(raw.getType());
					if (main_product_slot_available && minor_product_slot_available) {
						if (Inventory_io.item_remove(raw, 1) != null) {
							Inventory_io.move_item_to_slot(main_product_item.clone(), main_product_item.getAmount(),
									this.gui, main_product_slot);// 添加主产物
							if (random.nextInt(100) < minor_possibility) {
								Inventory_io.move_item_to_slot(minor_product_item.clone(),
										minor_product_item.getAmount(), this.gui, minor_product_slot);// 添加副产物
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public synchronized boolean add_a_raw(ItemStack src_item) {
		return Inventory_io.move_item_to_slot(src_item, 1, gui, Grinder.raw_slot);
	}

	public synchronized boolean add_a_flint(ItemStack src_item) {
		return Inventory_io.move_item_to_slot(src_item, 1, gui, Grinder.flint_slot);
	}

	public synchronized void set_process(int process) {
		this.set_process(0, process, "§e磨粉机工作进度: %d %%", process);
	}

	@Override
	protected void set_from_save(Map<String, Object> shop_save) {
		super.set_from_save(shop_save);
		this.set_flint_storage((Integer) shop_save.get("flint-storge"));
		this.working_ticks = (Integer) shop_save.get("working-ticks");
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("flint-storge", this.get_flint_storage());
		save.put("working-ticks", this.working_ticks);
		return save;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_grinder_price();
		String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + format_price);
			return true;
		} else {
			this.send_message(player, "建立磨粉机所需的钱不够，需要" + format_price);
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
		return new ItemStack[]{this.get_raw(), this.get_flint(), this.get_main_product(), this.get_minor_product()};
	}

	@Nonnull
	public static Grinder deserialize(@Nonnull Map<String, Object> args) {
		Grinder structure = new Grinder();
		structure.set_from_save(args);
		return structure;
	}
}