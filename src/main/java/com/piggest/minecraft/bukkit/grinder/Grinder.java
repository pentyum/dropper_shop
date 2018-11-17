package com.piggest.minecraft.bukkit.grinder;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Grinder extends Multi_block_structure implements InventoryHolder {
	public static HashMap<Material, ItemStack> recipe = new HashMap<Material, ItemStack>();
	public static HashMap<Material, Integer> recipe_time = new HashMap<Material, Integer>();
	private Inventory gui = Bukkit.createInventory(this, 18, "磨粉机");
	private Grinder_runner runner = new Grinder_runner(this);
	private Grinder_io_runner io_runner = new Grinder_io_runner(this);

	public Grinder() {
		this.gui.setItem(10, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
		this.gui.setItem(12, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
		this.gui.setItem(14, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
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

	private static void add_recipe(Material material, Material out, int num, int time) {
		Grinder.recipe.put(material, new ItemStack(out, num));
		Grinder.recipe_time.put(material, time);
	}

	private static void add_recipe(Material material, ItemStack item, int time) {
		Grinder.recipe.put(material, item);
		Grinder.recipe_time.put(material, time);
	}

	public static void init_recipe() {
		Grinder.add_recipe(Material.COBBLESTONE, Material.GRAVEL, 2, 120);
		Grinder.add_recipe(Material.GRAVEL, Material.SAND, 2, 20);
		Grinder.add_recipe(Material.SANDSTONE, Material.SAND, 4, 40);
		Grinder.add_recipe(Material.STONE, Material.COBBLESTONE, 1, 60);

		Grinder.add_recipe(Material.DIRT, Material.CLAY_BALL, 2, 40);
		Grinder.add_recipe(Material.BRICK, Material.CLAY_BALL, 1, 40);
		Grinder.add_recipe(Material.BRICKS, Material.CLAY_BALL, 4, 120);
		Grinder.add_recipe(Material.CLAY, Material.CLAY_BALL, 4, 60);
		Grinder.add_recipe(Material.TERRACOTTA, Material.CLAY_BALL, 4, 120);

		Grinder.add_recipe(Material.NETHERRACK, Material.GRAVEL, 2, 60);
		Grinder.add_recipe(Material.NETHER_BRICK, Material.NETHERRACK, 1, 40);
		Grinder.add_recipe(Material.NETHER_BRICKS, Material.NETHERRACK, 4, 120);

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

	public Inventory getInventory() {
		return this.gui;
	}

	public Hopper get_hopper() {
		BlockState up_block = this.get_block(0, 1, 0).getState();
		if (up_block instanceof Hopper) {
			Hopper up_hopper = (Hopper) up_block;
			return up_hopper;
		}
		return null;
	}

	public Chest get_chest() {
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

	public Grinder_runner get_runner() {
		return this.runner;
	}

	public BukkitRunnable get_io_runner() {
		return this.io_runner;
	}

	public boolean to_product() {
		if (!Grinder.is_empty(this.get_raw())) {
			ItemStack product_item = recipe.get(this.get_raw().getType());
			if (product_item != null) {
				if (Grinder.is_empty(this.get_product())) {
					this.set_product(product_item.clone());
					this.get_raw().setAmount(this.get_raw().getAmount() - 1);
					return true;
				} else if (this.get_product().getType() == product_item.getType()) {
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

	public void set_process(int process) {
		int n = process * 9 / 100;
		int i = 0;
		ItemStack red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		for (i = 0; i < n; i++) {
			this.gui.setItem(i, red.clone());
		}
		for (i = n; i < 9; i++) {
			this.gui.setItem(i, white.clone());
		}
	}
}