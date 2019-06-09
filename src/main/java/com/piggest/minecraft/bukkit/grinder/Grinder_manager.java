package com.piggest.minecraft.bukkit.grinder;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Grinder_manager extends Gui_structure_manager {
	public HashMap<Material, ItemStack> recipe = new HashMap<Material, ItemStack>();
	public HashMap<Material, Integer> recipe_time = new HashMap<Material, Integer>();
	private String gui_name = "磨粉机";
	
	public Grinder_manager() {
		super(Grinder.class);
		//this.set_bar(0,"§e磨粉机工作进度: %d %%");
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放原料", Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放燧石或者黑曜石", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边为产品", Gui_slot_type.Indicator);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边为燧石单元储量", Gui_slot_type.Indicator);
		this.set_gui(17, Material.FLINT, "§e燧石单元", Gui_slot_type.Indicator);
	}

	@Override
	public Multi_block_structure find(String player_name, Location loc, boolean new_structure) {
		if (new_structure == false) {
			return this.get(loc);
		} else {
			Grinder grinder = new Grinder();
			grinder.set_location(loc);
			if (grinder.completed() == 0) {
				return null;
			} else {
				return grinder;
			}
		}
	}

	public Grinder get(Location location) {
		return (Grinder) super.get(location);
	}

	public String get_gui_name() {
		return this.gui_name;
	}

	private void add_recipe(Material material, Material out, int num, int time) {
		this.recipe.put(material, new ItemStack(out, num));
		this.recipe_time.put(material, time);
	}

	private void add_recipe(Material material, ItemStack item, int time) {
		this.recipe.put(material, item);
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

		this.add_recipe(Material.WHEAT, Material_ext.new_item("wheat_powder", 1), 100); // 面粉

		this.add_recipe(Material.IRON_ORE, Material_ext.new_item("iron_powder", 2), 400); // 铁粉
		this.add_recipe(Material.IRON_INGOT, Material_ext.new_item("iron_powder", 1), 200); // 铁粉

		this.add_recipe(Material.GOLD_ORE, Material_ext.new_item("gold_powder", 2), 400); // 金粉
		this.add_recipe(Material.GOLD_INGOT, Material_ext.new_item("gold_powder", 1), 200); // 金粉

		this.add_recipe(Material.COAL_ORE, Material_ext.new_item("coal_powder", 2), 400); // 煤粉
		this.add_recipe(Material.COAL, Material_ext.new_item("coal_powder", 1), 200); // 煤粉

		this.add_recipe(Material.LAPIS_ORE, Material_ext.new_item("lapis_powder", 12), 400); // 青金石粉
		this.add_recipe(Material.LAPIS_LAZULI, Material_ext.new_item("lapis_powder", 1), 100); // 青金石粉
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
}