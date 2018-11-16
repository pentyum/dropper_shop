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
	private Inventory gui = Bukkit.createInventory(this, 54, "磨粉机");
	private Grinder_runner runner = new Grinder_runner(this);
	private Grinder_io_runner io_runner = new Grinder_io_runner(this);

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
}

class Grinder_contents {
	public static int get_line_start(int line) {
		return 9 * (line - 1);
	}

	private Inventory gui;
	private Inventory raw = Bukkit.createInventory(null, 9);
	private Inventory flint = Bukkit.createInventory(null, 9);
	private Inventory product = Bukkit.createInventory(null, 9);

	public Grinder_contents(Inventory gui) {
		this.gui = gui;
	}

	public void init() {
		int i = 0;
		ItemStack orange = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
		for (i = get_line_start(3); i < get_line_start(4); i++) {
			this.gui.setItem(i, orange.clone());
		}
		for (i = get_line_start(5); i < get_line_start(6); i++) {
			this.gui.setItem(i, orange.clone());
		}
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

	public void gui_to_subinv() {
		int i = 0;
		for (i = 0; i < 9; i++) {
			this.raw.setItem(i, this.gui.getItem(i + get_line_start(2)));
			this.flint.setItem(i, this.gui.getItem(i + get_line_start(4)));
			this.product.setItem(i, this.gui.getItem(i + get_line_start(6)));
		}
	}

	public void subinv_to_gui() {
		int i = 0;
		for (i = 0; i < 9; i++) {
			this.gui.setItem(i + get_line_start(2), this.raw.getItem(i));
			this.gui.setItem(i + get_line_start(4), this.flint.getItem(i));
			this.gui.setItem(i + get_line_start(6), this.product.getItem(i));
		}
	}
	
	public void add_raw(ItemStack raw_item) {
		raw_item.getAmount();
	}
}