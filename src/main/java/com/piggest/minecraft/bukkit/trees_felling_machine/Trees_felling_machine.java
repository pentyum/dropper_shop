package com.piggest.minecraft.bukkit.trees_felling_machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine extends Multi_block_with_gui implements HasRunner {
	private int current_x;
	private int current_z;
	private int start_x;
	private int start_z;
	private int end_x;
	private int end_z;
	private int total_blocks;
	private int scanned_blocks = 0;
	private static final int axe_hopper_check_list[][] = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 1, -2 },
			{ -2, 1, 0 } }; // 注入斧头
	
	private int r = 32;
	private Trees_felling_machine_runner runner = new Trees_felling_machine_runner(this);

	@Override
	public void on_button_pressed(Player player, int slot) {
		if (slot == 10) {
			player.sendMessage("已复位");
			this.scanned_blocks = 0;
			this.init_after_set_location();
		}
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_trees_felling_machine_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("建立伐木机所需的钱不够，需要" + price);
			return false;
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.runner };
	}

	public int[] get_current_pointer_location() {
		return new int[] { this.current_x, this.current_z };
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
			if (block.getType() != Material.AIR && block.getType() != Material.VINE) { // 获得第一个非空气方块的类型
				check_block = block;
				break;
			}
		}
		if (check_block != null) {
			if (Tag.LEAVES.isTagged(check_block.getType())) {// 第一个非空气方块是树叶，则判定为树
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
					} else if (Tag.LEAVES.isTagged(block.getType())) { // 如果是树叶方块则进入树叶清楚程序
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
							Random rand = new Random();
							int num = rand.nextInt(300);
							Material type = block.getType();
							ItemStack new_item = null;
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
							if (this.get_axe() == null) {
								return;
							}
							if (new_item != null) {
								if (!this.insert_item_to_chest(new_item)) { // 箱子满了，也直接返回，指针不动
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

	public synchronized Chest get_chest() {
		BlockState chest = this.get_block(2, -1, 0).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(-2, -1, 0).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(0, -1, 2).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		chest = this.get_block(0, -1, -2).getState();
		if (chest instanceof Chest) {
			return (Chest) chest;
		}
		return null;
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

	private void set_axe(ItemStack item) {
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

	@SuppressWarnings("deprecation")
	public void use_axe() {
		ItemStack axe = this.get_axe();
		int durability_level = axe.getEnchantmentLevel(Enchantment.DURABILITY);
		Random rand = new Random();
		int num = rand.nextInt(durability_level + 1);
		if (num == 0) {
			axe.setDurability((short) (axe.getDurability() + 1));
		}
		if (axe.getType() == Material.DIAMOND_AXE) {
			if (axe.getDurability() >= 1562) {
				axe.setAmount(0);
			}
		} else if (axe.getType() == Material.STONE_AXE) {
			if (axe.getDurability() >= 132) {
				axe.setAmount(0);
			}
		} else if (axe.getType() == Material.GOLDEN_AXE) {
			if (axe.getDurability() >= 33) {
				axe.setAmount(0);
			}
		} else if (axe.getType() == Material.IRON_AXE) {
			if (axe.getDurability() >= 251) {
				axe.setAmount(0);
			}
		} else if (axe.getType() == Material.WOODEN_AXE) {
			if (axe.getDurability() >= 60) {
				axe.setAmount(0);
			}
		}
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.current_x = ((int) shop_save.get("current-x"));
		this.current_z = ((int) shop_save.get("current-z"));
		this.scanned_blocks = ((int) shop_save.get("scanned-blocks"));
		this.set_axe((ItemStack) shop_save.get("axe"));
		this.set_working((boolean) shop_save.get("working"));
		this.update_process();
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("working", this.is_working());
		save.put("current-x", this.current_x);
		save.put("current-z", this.current_z);
		save.put("scanned-blocks", this.scanned_blocks);
		save.put("axe", this.get_axe());
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
					player.sendMessage("开启成功");
					return true;
				} else {
					player.sendMessage("开启失败，你的金币不够");
					return false;
				}
			} else {
				player.sendMessage("关闭成功");
				return true;
			}
		}
		return true;
	}
	
	private synchronized Hopper get_hopper(int[][] check_list) {
		for (int[] relative_coord : check_list) {
			BlockState block = this.get_block(relative_coord[0], relative_coord[1], relative_coord[2]).getState();
			if (block instanceof Hopper) {
				org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
						.getBlockData();
				Vector vec = hopper_data.getFacing().getDirection().multiply(2)
						.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
				if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
					if (block.getBlock().isBlockPowered()) {
						continue;
					}
					return (Hopper) block;
				}
			}
		}
		return null;
	}
	
	public synchronized Hopper get_axe_hopper() {
		return this.get_hopper(axe_hopper_check_list);
	}

	@Override
	public void init_after_set_location() {
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
}
