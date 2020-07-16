package com.piggest.minecraft.bukkit.trees_felling_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Tool_material;
import com.piggest.minecraft.bukkit.structure.Auto_io;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Ownable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class Trees_felling_machine extends Multi_block_with_gui implements Auto_io, Ownable {
	private int current_x;
	private int current_z;
	private int start_x;
	private int start_z;
	private int end_x;
	private int end_z;
	private int total_blocks;
	private int scanned_blocks = 0;
	private Random random = new Random();
	private ItemStack buffer_item = null;
	private static final int[][] axe_hopper_check_list = {{0, 1, 2}, {2, 1, 0}, {0, 1, -2}, {-2, 1, 0}}; // 注入斧头
	private static final int[][] product_chest_check_list = {{0, -1, 2}, {2, -1, 0}, {0, -1, -2},
			{-2, -1, 0}};
	public static final int owner_indicator = 15;

	private int r = 32;
	private String owner;

	@Override
	public void on_button_pressed(Player player, int slot) {
		if (slot == 10) {
			this.send_message(player, "已复位");
			this.scanned_blocks = 0;
			this.init_after_set_location();
		}
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_trees_felling_machine_price();
		String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + format_price);
			return true;
		} else {
			this.send_message(player, "建立伐木机所需的钱不够，需要" + format_price);
			return false;
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	public int[] get_current_pointer_location() {
		return new int[]{this.current_x, this.current_z};
	}

	private int[] pointer_move_to_next() {
		this.current_x++;
		// Bukkit.getServer().getLogger().info("开始检测" + this.current_x + "," +
		// this.current_z);
		if (this.current_x > this.end_x) {
			this.current_x = this.start_x;
			this.current_z++;
			if (this.current_z > this.end_z) {
				this.scanned_blocks++;
				this.update_process();
				this.set_working(false);
				return this.get_current_pointer_location();
			}
		}
		this.scanned_blocks++;
		this.update_process();
		return this.get_current_pointer_location();
	}

	public void do_next() {
		ItemStack axe = this.get_axe();
		Block check_block = null;
		int y;
		if (axe == null) {
			return;
		}
		if (this.get_chest() == null) {
			return;
		}
		for (y = 250; y >= 63; y--) { // 从高空开始往下检测
			Block block = this.get_location().getWorld().getBlockAt(this.current_x, y, this.current_z);
			if (block.getType() != Material.AIR && block.getType() != Material.VINE
					&& block.getType() != Material.SNOW) { // 获得第一个非空气方块的类型
				check_block = block;
				break;
			}
		}

		if (check_block != null) {
			if (Tag.LEAVES.isTagged(check_block.getType())) {// 第一个非空气方块是树叶，则准备判定为树
				BlockData check_block_data = check_block.getBlockData();
				Leaves check_block_leaves = (Leaves) check_block_data;
				if (check_block_leaves.isPersistent() == true) { // 第一个非空气方块的树叶是人为放置的，直接跳过这一格。
					this.pointer_move_to_next();
					return;
				}

				Stack<Block> tree_stack = new Stack<Block>();
				for (; y >= 50; y--) { // 继续往下检测，找到原木方块
					Block block = this.get_location().getWorld().getBlockAt(this.current_x, y, this.current_z);
					Material type = block.getType();
					if (Tag.LOGS.isTagged(type) || Tag.LEAVES.isTagged(type)) {
						tree_stack.push(block);
					} else {
						if (type != Material.AIR && type != Material.VINE) {
							break;
						}
					}
				}
				while (!tree_stack.isEmpty()) {
					Block block = tree_stack.pop();
					if (!this.get_owner().isOnline()) { // 玩家不在线砍树机不工作，指针不动
						return;
					}
					BlockBreakEvent event = new BlockBreakEvent(block, this.get_owner().getPlayer());// 新建一个方块破坏事件
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled()) { // 事件被取消了
						continue;// 直接跳过当前方块
					}
					if (Tag.LOGS.isTagged(block.getType())) { // 如果是原木方块则进入砍伐程序
						ItemStack item = new ItemStack(block.getType());
						if (this.get_axe() == null) {
							return;
						}
						if (!this.insert_item_to_chest(item)) { // 箱子满了，也直接返回，指针不动
							return;
						}
						block.setType(Material.AIR);
						this.use_axe();
					} else if (Tag.LEAVES.isTagged(block.getType())) { // 如果是树叶方块则进入树叶清除程序
						BlockData data = block.getBlockData();
						Leaves leaves = (Leaves) data;
						if (leaves.isPersistent() == true) { // 树叶为人为放置，直接跳过。
							continue;
						}
						int silk_touch_level = axe.getEnchantmentLevel(Enchantment.SILK_TOUCH);
						if (silk_touch_level > 0) { // 有精准采集附魔，直接获得树叶方块
							ItemStack item = new ItemStack(block.getType());
							if (this.get_axe() == null) {
								return;
							}
							if (!this.insert_item_to_chest(item)) { // 箱子满了，也直接返回，指针不动
								return;
							}
							block.setType(Material.AIR);
							this.use_axe();
						} else { // 没有精准采集附魔
							int num = random.nextInt(300);
							Material type = block.getType();
							ItemStack new_item = null;
							if (this.buffer_item == null) {
								if (type == Material.OAK_LEAVES || type == Material.DARK_OAK_LEAVES) {
									if (num == 0) {
										new_item = new ItemStack(Material.APPLE);
									} else if (num >= 1 && num <= 10) {
										if (type == Material.OAK_LEAVES) {
											new_item = new ItemStack(Material.OAK_SAPLING);
										} else {
											new_item = new ItemStack(Material.DARK_OAK_SAPLING);
										}
									}
								} else if (type == Material.ACACIA_LEAVES) {
									if (num < 10) {
										new_item = new ItemStack(Material.ACACIA_SAPLING);
									}
								} else if (type == Material.BIRCH_LEAVES) {
									if (num < 10) {
										new_item = new ItemStack(Material.BIRCH_SAPLING);
									}
								} else if (type == Material.SPRUCE_LEAVES) {
									if (num < 10) {
										new_item = new ItemStack(Material.SPRUCE_SAPLING);
									}
								} else if (type == Material.JUNGLE_LEAVES) {
									if (num < 5) {
										new_item = new ItemStack(Material.JUNGLE_SAPLING);
									}
								}
							} else {
								new_item = this.buffer_item;
								this.buffer_item = null;
							}
							if (this.get_axe() == null) {
								return;
							}
							if (new_item != null) {
								if (!this.insert_item_to_chest(new_item)) { // 箱子满了，也直接返回，指针不动
									this.buffer_item = new_item;// 砍伐成功存入buffer
									return;
								}
							}
							block.setType(Material.AIR);
						}
					}
				}
			}
		}
		this.pointer_move_to_next();
	}

	public Chest get_chest() {
		return this.get_chest(product_chest_check_list);
	}

	public ItemStack get_axe() {
		ItemStack item = this.gui.getItem(13);
		if (item == null) {
			return null;
		}
		Material type = item.getType();
		if (type == Material.DIAMOND_AXE || type == Material.GOLDEN_AXE || type == Material.IRON_AXE
				|| type == Material.STONE_AXE || type == Material.WOODEN_AXE) {
			return item;
		}
		return null;
	}

	public void set_axe(ItemStack item) {
		this.gui.setItem(13, item);
	}

	public boolean insert_item_to_chest(ItemStack item) {
		if (this.get_chest() == null) { // 没箱子可以输出
			return false;
		}
		HashMap<Integer, ItemStack> cannot_added = this.get_chest().getInventory().addItem(item);
		if (!cannot_added.isEmpty()) { // 箱子满了
			return false;
		}
		return true;
	}

	public void use_axe() {
		ItemStack axe = this.get_axe();
		ItemMeta meta = axe.getItemMeta();
		Damageable damageable = (Damageable) meta;
		int durability_level = axe.getEnchantmentLevel(Enchantment.DURABILITY);
		int num = random.nextInt(durability_level + 1);
		int damage = 0;
		int current_durability = damageable.getDamage();
		if (num == 0) {
			damage = 1;
		}
		PlayerItemDamageEvent event = new PlayerItemDamageEvent(this.get_owner().getPlayer(), axe, damage);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled() == false) {
			damage = event.getDamage();
			damageable.setDamage(current_durability + damage);
			axe.setItemMeta(meta);
		}
		Tool_material tool_material = Tool_material.get_tool_material(axe);
		if (current_durability > tool_material.get_max_durbility()) {
			axe.setAmount(0);
		}
	}

	@Override
	protected void set_from_save(Map<String, Object> shop_save) {
		super.set_from_save(shop_save);
		this.current_x = ((int) shop_save.get("current-x"));
		this.current_z = ((int) shop_save.get("current-z"));
		this.scanned_blocks = ((int) shop_save.get("scanned-blocks"));
		this.set_working((boolean) shop_save.get("working"));
		this.update_process();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("working", this.is_working());
		save.put("current-x", this.current_x);
		save.put("current-z", this.current_z);
		save.put("scanned-blocks", this.scanned_blocks);
		return save;
	}

	public boolean is_working() {
		return this.get_switch(9);
	}

	public void set_working(boolean working) {
		this.set_switch(9, working);
	}

	public void update_process() {
		int process = 100 * this.scanned_blocks / this.total_blocks;
		this.set_process(0, process, "§e当前进度:%d/%d (%d%%)", this.scanned_blocks, this.total_blocks, process);
	}

	public boolean add_a_axe(ItemStack src_item) {
		if (src_item != null) {
			if (src_item.getType().name().contains("_AXE")) {
				if (Grinder.is_empty(this.gui.getItem(13))) {
					this.gui.setItem(13, src_item.clone());
					src_item.setAmount(0);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		if (slot == 9) {
			if (on == true) {
				if (Dropper_shop_plugin.instance.cost_player_money(
						Dropper_shop_plugin.instance.get_price_config().get_start_trees_felling_machine_price(),
						player)) {
					this.set_owner(player.getName());
					this.send_message(player, "开启成功");
					return true;
				} else {
					this.send_message(player, "开启失败，你的金钱不够");
					return false;
				}
			} else {
				this.send_message(player, "关闭成功");
				return true;
			}
		}
		return true;
	}

	public Hopper get_axe_hopper() {
		return this.get_hopper(axe_hopper_check_list, true);
	}

	@Override
	protected void init_after_set_location() {
		this.start_x = this.get_location().getBlockX() - this.r;
		this.start_z = this.get_location().getBlockZ() - this.r;

		this.end_x = this.get_location().getBlockX() + this.r;
		this.end_z = this.get_location().getBlockZ() + this.r;

		this.current_x = this.start_x;
		this.current_z = this.start_z;
		this.total_blocks = (this.end_x - this.start_x + 1) * (this.end_z - this.start_z + 1);
		this.set_working(false);
		this.update_process();
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
	public void set_owner(String owner) {
		this.owner = owner;
		new BukkitRunnable() {
			@Override
			public void run() {
				ItemStack item = gui.getItem(owner_indicator);
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				if (owner == null) {
					meta.setDisplayName("§r当前控制者: null");
				} else {
					meta.setDisplayName("§r当前控制者: " + owner);
					meta.setOwningPlayer(get_owner());
				}
				item.setItemMeta(meta);
			}
		}.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public OfflinePlayer get_owner() {
		if (this.owner != null) {
			return Bukkit.getOfflinePlayer(this.owner);
		} else {
			return null;
		}
	}

	@Override
	public String get_owner_name() {
		return this.owner;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return new ItemStack[]{this.get_axe()};
	}

	@Nonnull
	public static Trees_felling_machine deserialize(@Nonnull Map<String, Object> args) {
		Trees_felling_machine structure = new Trees_felling_machine();
		structure.set_from_save(args);
		return structure;
	}
}
