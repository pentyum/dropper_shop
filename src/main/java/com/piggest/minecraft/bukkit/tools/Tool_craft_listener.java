package com.piggest.minecraft.bukkit.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.grinder.Ingot;
import com.piggest.minecraft.bukkit.material_ext.Custom_durability;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.material_ext.Tool_material;
import com.piggest.minecraft.bukkit.material_ext.Tool_material.Custom_material;

public class Tool_craft_listener implements Listener {
	public static final List<String> raw_tool_material = Arrays
			.asList(new String[] { Material.GOLD_INGOT.getKey().toString(), Material.COBBLESTONE.getKey().toString(),
					Material.IRON_INGOT.getKey().toString(), Material.DIAMOND.getKey().toString() });

	@EventHandler
	public void on_prepare_craft(PrepareItemCraftEvent event) {
		Recipe recipe = event.getRecipe();
		if (recipe == null) {
			return;
		}
		if (!(recipe instanceof ShapedRecipe)) {
			return;
		}
		ShapedRecipe sr = (ShapedRecipe) recipe;
		if (Tools.tools_namespace.contains(sr.getKey())) {
			CraftingInventory inventory = event.getInventory();
			ArrayList<ItemStack> brick_list = new ArrayList<ItemStack>();
			for (int i = 1; i <= 6; i++) {
				ItemStack item = inventory.getItem(i);
				if (!Grinder.is_empty(item)) {
					if (item.getType() == Material.BRICK) {
						brick_list.add(item);
					}
				}
			}
			String first_full_name = Material_ext.get_full_name(brick_list.get(0));
			if (first_full_name.equals(Material.BRICK.getKey().toString())) { // 真红砖
				inventory.setResult(null);
				return;
			}
			for (ItemStack item : brick_list) {
				String full_name = Material_ext.get_full_name(item);
				if (!full_name.equals(first_full_name)) { // 不匹配
					inventory.setResult(null);
					return;
				}
			}
			ItemStack result = inventory.getResult();
			Tool_type tool_type = Tool_type.get_tool_type(result);
			Ingot ingot = Ingot.ingot_map.get(first_full_name);
			Tool_material.Custom_material tool_material = ingot.get_tool_material();
			inventory.setResult(Tools.gen_tool(tool_material, tool_type));
		}
	}

	@EventHandler
	public void on_repair_tool(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		ArrayList<ItemStack> tool_list = new ArrayList<ItemStack>();
		for (int i = 1; i <= 9; i++) {
			ItemStack item = inventory.getItem(i);
			if (!Grinder.is_empty(item)) {
				if (Tool_material.is_tool(item)) {
					tool_list.add(item);
				}
			}
		}
		if (tool_list.size() != 2) {
			return;
		}
		ItemStack tool1 = tool_list.get(0);
		ItemStack tool2 = tool_list.get(1);
		String tool1_id_name = Material_ext.get_id_name(tool1);
		if (!tool1_id_name.equals(Material_ext.get_id_name(tool2))) {
			inventory.setResult(null);
			return;
		}
		Custom_material custom_material = Custom_material.get_custom_material(tool1);
		if (custom_material != null) {
			int max_durbility = custom_material.get_max_durbility();
			int tool1_left = max_durbility - Custom_durability.get_custom_durability(tool1);
			int tool2_left = max_durbility - Custom_durability.get_custom_durability(tool2);
			int left = tool1_left + tool2_left + max_durbility / 20;
			if (left > max_durbility) {
				left = max_durbility;
			}
			ItemStack result = Material_ext.new_item(tool1_id_name, 1);
			int new_damage = max_durbility - left;
			Custom_durability.set_custom_durability(result, new_damage);
			inventory.setResult(result);
		}
	}

	@EventHandler
	public void on_anvil(PrepareAnvilEvent event) {
		AnvilInventory inventory = event.getInventory();
		ItemStack item1 = inventory.getItem(0);
		ItemStack item2 = inventory.getItem(1);
		String rename_text = inventory.getRenameText();
		ItemStack result = event.getResult();
		if (item1 != null) {
			ItemMeta meta1 = item1.getItemMeta();
			if (meta1.hasDisplayName()) {
				if (meta1.getDisplayName().substring(1).equals(rename_text)) {
					if (Grinder.is_empty(item2)) {
						event.setResult(null);
						return;
					} else {
						if (!Grinder.is_empty(result)) {
							ItemMeta result_meta = result.getItemMeta();
							result_meta.setDisplayName(meta1.getDisplayName());
							result.setItemMeta(result_meta);
						}
					}
				}
			}
		}
		if (Tool_material.is_custom_tool(item1)) {// 铁砧处理自定义工具
			Custom_material custom_material = Custom_material.get_custom_material(item1);
			if (!Grinder.is_empty(item2)) {
				String full_name = Material_ext.get_full_name(item2);
				if (raw_tool_material.contains(full_name)) {
					event.setResult(null);
					return;
				}
				Ingot ingot = Ingot.ingot_map.get(full_name);
				if (ingot != null) {
					if (ingot.get_tool_material() == custom_material) {
						event.setResult(item1.clone());
						inventory.setRepairCost(20);
						Dropper_shop_plugin.instance.getLogger().info("暂不支持金属锭修复");
						return;
					}
				}
				Tool_material material2 = Tool_material.get_tool_material(item2);
				if (material2 != null && custom_material != material2) {
					event.setResult(null);
					return;
				} else if (custom_material == material2) {// 物品合并
					int max_durbility = custom_material.get_max_durbility();
					int tool1_left = max_durbility - Custom_durability.get_custom_durability(item1);
					int tool2_left = max_durbility - Custom_durability.get_custom_durability(item2);
					int left = tool1_left + tool2_left + (int) (max_durbility * 0.12);
					if (left > max_durbility) {
						left = max_durbility;
					}
					int new_damage = max_durbility - left;
					Custom_durability.set_custom_durability(result, new_damage);
				}
			}
		}
	}

	@EventHandler
	public void on_anvil_result_click(InventoryClickEvent event) {
		Inventory inventory = event.getClickedInventory();
		if (inventory instanceof AnvilInventory) {
			AnvilInventory anvil_inv = (AnvilInventory) inventory;
			int repair_cost = anvil_inv.getRepairCost();
			// Dropper_shop_plugin.instance.getLogger().info("你点击了铁砧,repair_cost:" +
			// repair_cost);

			if (event.getSlot() == 2) {
				// Dropper_shop_plugin.instance.getLogger().info("取出结果");
			}
		}
	}
}
