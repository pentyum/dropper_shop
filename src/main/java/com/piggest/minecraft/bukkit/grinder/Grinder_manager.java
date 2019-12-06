package com.piggest.minecraft.bukkit.grinder;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Grinder_manager extends Gui_structure_manager<Grinder> {
	private HashMap<Material, ItemStack> main_recipe = new HashMap<Material, ItemStack>();
	private HashMap<Material, ItemStack> minor_recipe = new HashMap<Material, ItemStack>();
	private HashMap<Material, Integer> recipe_time = new HashMap<Material, Integer>();
	private String gui_name = "磨粉机";

	private Material[][][] model = {
			{ { null, null, null }, { null, Material.IRON_BLOCK, null }, { null, null, null } },
			{ { null, null, null }, { null, Material.COBBLESTONE_WALL, null }, { null, null, null } },
			{ { null, Material.END_ROD, null }, { Material.END_ROD, Material.SMOOTH_STONE, Material.END_ROD },
					{ null, Material.END_ROD, null } } };
	private int center_x = 1;
	private int center_y = 2;
	private int center_z = 1;

	public Grinder_manager() {
		super(Grinder.class);
		// this.set_bar(0,"§e磨粉机工作进度: %d %%");
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放原料", Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放燧石或者黑曜石", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边为产品，右边为副产品", Gui_slot_type.Indicator);
		this.set_gui(Grinder.minor_product_slot, null, "minor-product", Gui_slot_type.Item_store);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边为燧石单元储量", Gui_slot_type.Indicator);
		this.set_gui(17, Material.FLINT, "§e燧石单元", Gui_slot_type.Indicator);
	}
	/*
	 * @Override public Grinder find(String player_name, Location loc, boolean
	 * new_structure) { if (new_structure == false) { return this.get(loc); } else {
	 * Grinder grinder = new Grinder(); grinder.set_location(loc); if
	 * (grinder.completed() == false) { return null; } else { return grinder; } } }
	 */

	public String get_gui_name() {
		return this.gui_name;
	}

	private void add_recipe(Material material, Material main_out, int main_num, int time) {
		this.main_recipe.put(material, new ItemStack(main_out, main_num));
		this.recipe_time.put(material, time);
	}

	private void add_recipe(Material material, String main_out, int main_num, int time) {
		this.main_recipe.put(material, Material_ext.new_item(main_out, main_num));
		this.recipe_time.put(material, time);
	}

	private void add_recipe(Material material, String main_out, int main_num, String minor_out, int minor_num,
			int time) {
		this.main_recipe.put(material, Material_ext.new_item(main_out, main_num));
		this.minor_recipe.put(material, Material_ext.new_item(minor_out, minor_num));
		this.recipe_time.put(material, time);
	}

	public void init_recipe() {
		this.add_recipe(Material.COBBLESTONE, Material.GRAVEL, 2, 600); // 圆石
		this.add_recipe(Material.GRAVEL, Material.SAND, 2, 200); // 砂砾
		this.add_recipe(Material.SANDSTONE, Material.SAND, 4, 400); // 砂石
		this.add_recipe(Material.RED_SANDSTONE, Material.RED_SAND, 4, 400); // 红砂石
		this.add_recipe(Material.STONE, Material.COBBLESTONE, 1, 300); // 石块
		this.add_recipe(Material.STONE_BRICKS, Material.COBBLESTONE, 1, 300); // 石砖
		this.add_recipe(Material.PRISMARINE, Material.PRISMARINE_SHARD, 4, 400); // 海晶石
		this.add_recipe(Material.PRISMARINE_BRICKS, Material.PRISMARINE_SHARD, 8, 800); // 海晶石砖
		this.add_recipe(Material.DARK_PRISMARINE, Material.PRISMARINE_SHARD, 8, 800); // 暗海晶石砖
		this.add_recipe(Material.GLOWSTONE, Material.GLOWSTONE_DUST, 4, 400); // 荧石
		this.add_recipe(Material.QUARTZ_BLOCK, Material.QUARTZ, 4, 800); // 石英
		this.add_recipe(Material.REDSTONE_ORE, Material.REDSTONE, 8, 500); // 红石矿

		this.add_recipe(Material.DIRT, Material.CLAY_BALL, 2, 300); // 泥土
		this.add_recipe(Material.BRICK, Material.CLAY_BALL, 1, 100); // 红砖
		this.add_recipe(Material.BRICKS, Material.CLAY_BALL, 4, 400); // 砖块
		this.add_recipe(Material.CLAY, Material.CLAY_BALL, 4, 400); // 粘土块
		this.add_recipe(Material.TERRACOTTA, Material.CLAY_BALL, 4, 500); // 陶瓦

		this.add_recipe(Material.NETHERRACK, Material.GRAVEL, 2, 400); // 地狱岩
		this.add_recipe(Material.NETHER_BRICK, Material.NETHERRACK, 1, 100); // 地狱砖
		this.add_recipe(Material.NETHER_BRICKS, Material.NETHERRACK, 4, 400); // 地狱砖块
		this.add_recipe(Material.MAGMA_BLOCK, Material.SOUL_SAND, 2, 400); // 岩浆块
		this.add_recipe(Material.NETHER_WART_BLOCK, Material.NETHER_WART, 9, 400); // 地狱疣块

		this.add_recipe(Material.COBBLESTONE_SLAB, Material.GRAVEL, 1, 300); // 圆石半砖
		this.add_recipe(Material.STONE_BRICK_SLAB, Material.COBBLESTONE_SLAB, 1, 150); // 石砖半砖
		this.add_recipe(Material.STONE_SLAB, Material.COBBLESTONE_SLAB, 1, 150); // 石头半砖
		this.add_recipe(Material.BRICK_SLAB, Material.CLAY_BALL, 2, 200); // 红砖半砖
		this.add_recipe(Material.NETHER_BRICK_SLAB, Material.NETHERRACK, 2, 200); // 地狱砖半砖
		this.add_recipe(Material.SANDSTONE_SLAB, Material.SAND, 2, 200); // 砂石半砖
		this.add_recipe(Material.RED_SANDSTONE_SLAB, Material.RED_SAND, 2, 200); // 红砂石半砖
		this.add_recipe(Material.PRISMARINE_SLAB, Material.PRISMARINE_SHARD, 2, 200); // 海晶石半砖
		this.add_recipe(Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_SHARD, 4, 400); // 海晶石砖半砖
		this.add_recipe(Material.DARK_PRISMARINE_SLAB, Material.PRISMARINE_SHARD, 4, 400); // 暗海晶石砖半砖

		this.add_recipe(Material.BONE, Material.BONE_MEAL, 4, 200); // 骨头
		this.add_recipe(Material.BLAZE_ROD, Material.BLAZE_POWDER, 3, 200); // 烈焰粉
		this.add_recipe(Material.MELON, Material.MELON_SEEDS, 9, 400); // 西瓜
		this.add_recipe(Material.PUMPKIN, Material.PUMPKIN_SEEDS, 4, 200); // 南瓜
		this.add_recipe(Material.CARVED_PUMPKIN, Material.PUMPKIN_SEEDS, 4, 200); // 被雕刻的南瓜

		this.add_recipe(Material.ROSE_BUSH, Material.RED_DYE, 3, 200); // 玫瑰红染料
		this.add_recipe(Material.POPPY, Material.RED_DYE, 2, 200); // 玫瑰红染料
		this.add_recipe(Material.RED_TULIP, Material.RED_DYE, 2, 200); // 玫瑰红染料
		this.add_recipe(Material.BEETROOT, Material.RED_DYE, 2, 200); // 玫瑰红染料

		this.add_recipe(Material.AZURE_BLUET, Material.LIGHT_GRAY_DYE, 2, 200); // 淡灰色染料
		this.add_recipe(Material.OXEYE_DAISY, Material.LIGHT_GRAY_DYE, 2, 200); // 淡灰色染料
		this.add_recipe(Material.WHITE_TULIP, Material.LIGHT_GRAY_DYE, 2, 200); // 淡灰色染料

		this.add_recipe(Material.PINK_TULIP, Material.PINK_DYE, 2, 200); // 粉红色染料
		this.add_recipe(Material.PEONY, Material.PINK_DYE, 3, 200); // 粉红色染料

		this.add_recipe(Material.BLUE_ORCHID, Material.LIGHT_BLUE_DYE, 2, 200); // 淡蓝色染料

		this.add_recipe(Material.ALLIUM, Material.MAGENTA_DYE, 2, 200); // 品红色染料
		this.add_recipe(Material.LILAC, Material.MAGENTA_DYE, 3, 200); // 品红色染料

		this.add_recipe(Material.DANDELION, Material.YELLOW_DYE, 2, 200); // 蒲公英黄染料
		this.add_recipe(Material.SUNFLOWER, Material.YELLOW_DYE, 3, 200); // 蒲公英黄染料

		this.add_recipe(Material.ORANGE_TULIP, Material.ORANGE_DYE, 2, 200); // 橙色染料

		this.add_recipe(Material.WHEAT, "wheat_powder", 1, 100); // 面粉

		this.add_recipe(Material.IRON_ORE, "iron_powder", 2, 400); // 铁粉
		this.add_recipe(Material.IRON_INGOT, "iron_powder", 1, 200); // 铁粉

		this.add_recipe(Material.GOLD_ORE, "gold_powder", 1, "sliver_powder", 1, 400); // 金粉和银粉
		this.add_recipe(Material.GOLD_INGOT, "gold_powder", 1, 200); // 金粉

		this.add_recipe(Material.COAL_ORE, "coal_powder", 2, 400); // 煤粉
		this.add_recipe(Material.COAL, "coal_powder", 1, 200); // 煤粉

		this.add_recipe(Material.LAPIS_ORE, "lapis_powder", 12, 400); // 青金石粉
		this.add_recipe(Material.LAPIS_LAZULI, "lapis_powder", 1, 100); // 青金石粉
	}

	@Override
	public int get_slot_num() {
		return 18;
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int[] get_process_bar() {
		return new int[] { 0 };
	}

	@Override
	public String get_permission_head() {
		return "grinder";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return new int[] { this.center_x, this.center_y, this.center_z };
	}
	/*
	 * 注意：仅提供信息，要生成物品必须clone
	 */
	public ItemStack get_main_product(Material material) {
		return this.main_recipe.get(material);
	}
	/*
	 * 注意：仅提供信息，要生成物品必须clone
	 */
	public ItemStack get_minor_product(Material material) {
		return this.minor_recipe.get(material);
	}
	
	public int get_time(Material material) {
		return this.recipe_time.get(material);
	}
}