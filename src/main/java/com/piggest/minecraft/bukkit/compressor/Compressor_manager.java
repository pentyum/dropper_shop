package com.piggest.minecraft.bukkit.compressor;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Compressor_manager extends Gui_structure_manager<Compressor> {
	private HashMap<String, Compressor_recipe> recipe = new HashMap<String, Compressor_recipe>();
	// private HashMap<String, String> main_recipe = new HashMap<String, String>();
	// private HashMap<String, Integer> recipe_quantity = new HashMap<String,
	// Integer>();
	// private HashMap<String, Integer> recipe_time = new HashMap<String,
	// Integer>();

	private Material[][][] model = {
			{ { Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS },
					{ Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK },
					{ Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS } },
			{ { Material.IRON_BLOCK, Material.PISTON, Material.IRON_BLOCK }, 
					{ Material.PISTON, null, Material.PISTON },
					{ Material.IRON_BLOCK, Material.PISTON, Material.IRON_BLOCK } },
			{ { Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS },
					{ Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK },
					{ Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS } } };
	private String gui_name = "压缩机";
	private int[] center = new int[] { 1, 0, 1 };

	public Compressor_manager() {
		super(Compressor.class);
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放原料", new String[] { "§7在第3层中间放置漏斗可以自动添加" },
				Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放活塞", new String[] { "§7在第1层中间放置漏斗可以自动添加" },
				Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边为产品", new String[] { "§7在第1层石砖边上放置箱子可以自动输出" },
				Gui_slot_type.Indicator);
		this.set_gui(Compressor.raw_slot, null, "raw-slot", Gui_slot_type.Item_store);
		this.set_gui(Compressor.piston_slot, null, "piston-slot", Gui_slot_type.Item_store);
		this.set_gui(Compressor.product_slot, null, "main-product", Gui_slot_type.Item_store);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边为活塞单元储量", Gui_slot_type.Indicator);
		this.set_gui(17, Material.PISTON, "§e活塞单元", Gui_slot_type.Indicator);
	}

	@Override
	public String get_gui_name() {
		return this.gui_name;
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 18;
	}

	@Override
	public int[] get_process_bar() {
		return new int[] { 0 };
	}

	@Override
	public String get_permission_head() {
		return "compressor";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

	/*
	 * 注意：仅提供信息，要生成物品必须clone
	 */
	public ItemStack get_product(String source_full_name, int raw_quantity) {
		Compressor_recipe recipe = this.recipe.get(source_full_name);
		if (recipe == null) {
			return null;
		}
		int need_quantity = recipe.get_need_quantity();
		if (need_quantity > raw_quantity) {
			return null;
		}
		return recipe.getResult();
	}

	public int get_time(String full_name) {
		Compressor_recipe recipe = this.recipe.get(full_name);
		if (recipe == null) {
			return 0;
		}
		return recipe.get_recipe_time();
	}

	public int get_source_consume(String source_full_name) {
		Compressor_recipe recipe = this.recipe.get(source_full_name);
		if (recipe == null) {
			return 0;
		}
		return recipe.get_need_quantity();
	}

	private void add_recipe(Material material, int need_quantity, Material main_out, int time) {
		String source_full_name = material.getKey().toString();
		String product_full_name = main_out.getKey().toString();
		this.add_recipe(source_full_name, need_quantity, product_full_name, time);
	}

	private void add_recipe(String source_full_name, int need_quantity, String main_out, int time) {
		Compressor_recipe recipe = new Compressor_recipe(source_full_name, need_quantity,
				Material_ext.new_item_full_name(main_out, 1), time);
		this.recipe.put(source_full_name, recipe);

	}

	public void init_recipe() {
		Dropper_shop_plugin.instance.getLogger().info("[压缩机]开始加载合成表");
		this.add_recipe(Material.IRON_NUGGET, 9, Material.IRON_INGOT, 500);
		this.add_recipe(Material.IRON_INGOT, 9, Material.IRON_BLOCK, 500);

		this.add_recipe(Material.GOLD_NUGGET, 9, Material.GOLD_INGOT, 500);
		this.add_recipe(Material.GOLD_INGOT, 9, Material.GOLD_BLOCK, 500);

		this.add_recipe(Material.DIAMOND, 9, Material.DIAMOND_BLOCK, 500);
		this.add_recipe(Material.REDSTONE, 9, Material.REDSTONE_BLOCK, 500);
		this.add_recipe(Material.COAL, 9, Material.COAL_BLOCK, 500);
		this.add_recipe(Material.EMERALD, 9, Material.EMERALD_BLOCK, 500);
		this.add_recipe(Material.LAPIS_LAZULI, 9, Material.LAPIS_BLOCK, 500);
		this.add_recipe(Material.WHEAT, 9, Material.HAY_BLOCK, 500);
		this.add_recipe(Material.MELON_SLICE, 9, Material.MELON, 500);
		this.add_recipe(Material.DRIED_KELP, 9, Material.DRIED_KELP_BLOCK, 500);
		this.add_recipe(Material.BONE_MEAL, 9, Material.BONE_BLOCK, 500);
		this.add_recipe(Material.NETHER_WART, 9, Material.NETHER_WART_BLOCK, 500);
		this.add_recipe(Material.ICE, 9, Material.PACKED_ICE, 500);
		this.add_recipe(Material.PACKED_ICE, 9, Material.BLUE_ICE, 500);

		this.add_recipe(Material.SNOW, 4, Material.SNOW_BLOCK, 200);
		this.add_recipe(Material.GLOWSTONE_DUST, 4, Material.GLOWSTONE, 200);
		this.add_recipe(Material.QUARTZ, 4, Material.QUARTZ_BLOCK, 200);
		this.add_recipe(Material.STRING, 4, Material.WHITE_WOOL, 200);

		this.add_recipe(Material.POPPED_CHORUS_FRUIT, 1, Material.PURPUR_BLOCK, 100);
	}

}
