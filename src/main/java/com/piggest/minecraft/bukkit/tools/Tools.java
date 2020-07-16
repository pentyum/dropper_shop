package com.piggest.minecraft.bukkit.tools;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Custom_durability;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.material_ext.Tool_material;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tools {
	private static final NamespacedKey pickaxe_recipe_namespace = Dropper_shop_plugin.get_key("pickaxe_recipe");
	private static final NamespacedKey axe_recipe_namespace = Dropper_shop_plugin.get_key("axe_recipe");
	private static final NamespacedKey shovel_recipe_namespace = Dropper_shop_plugin.get_key("shovel_recipe");
	private static final NamespacedKey hoe_recipe_namespace = Dropper_shop_plugin.get_key("hoe_recipe");
	private static final NamespacedKey sword_recipe_namespace = Dropper_shop_plugin.get_key("sword_recipe");
	static final List<NamespacedKey> tools_namespace = Arrays.asList(new NamespacedKey[]{pickaxe_recipe_namespace,
			axe_recipe_namespace, shovel_recipe_namespace, hoe_recipe_namespace, sword_recipe_namespace});

	public static final ArrayList<Tools> pickaxe_config = new ArrayList<>();
	public static final ArrayList<Tools> axe_config = new ArrayList<>();
	public static final ArrayList<Tools> shovel_config = new ArrayList<>();
	public static final ArrayList<Tools> hoe_config = new ArrayList<>();
	public static final ArrayList<Tools> sword_config = new ArrayList<>();

	public static final int tool_model_offset = 100;

	private Tool_type tool_type;
	private Tool_material.Custom_material tool_material;
	private String id_name;
	private Material raw_material;

	public Tools(Tool_material.Custom_material tool_material, Tool_type tool_type) {
		this.tool_type = tool_type;
		this.tool_material = tool_material;
		this.id_name = tool_material.name().toLowerCase() + "_" + tool_type.name().toLowerCase();
		this.raw_material = Material.valueOf(tool_material.get_raw().name() + "_" + tool_type.name());
	}

	public Tool_type get_tool_type() {
		return this.tool_type;
	}

	public Tool_material.Custom_material get_tool_material() {
		return this.tool_material;
	}

	public String get_display_name() {
		return this.tool_material.get_display_name() + this.tool_type.get_display_name();
	}

	public String get_id_name() {
		return this.id_name;
	}

	public Material get_raw_material() {
		return this.raw_material;
	}

	public static void init_recipe() {
		Dropper_shop_plugin.instance.getLogger().info("[额外工具]添加合成表");
		ShapedRecipe pickaxe_recipe = new ShapedRecipe(pickaxe_recipe_namespace, new ItemStack(Material.STONE_PICKAXE));
		pickaxe_recipe.shape("bbb", "asa", "asa");
		pickaxe_recipe.setIngredient('b', Material.BRICK);
		pickaxe_recipe.setIngredient('s', Material.STICK);
		pickaxe_recipe.setIngredient('a', Material.AIR);
		Dropper_shop_plugin.instance.add_recipe(pickaxe_recipe);

		ShapedRecipe axe_recipe = new ShapedRecipe(axe_recipe_namespace, new ItemStack(Material.STONE_AXE));
		axe_recipe.shape("bba", "bsa", "asa");
		axe_recipe.setIngredient('b', Material.BRICK);
		axe_recipe.setIngredient('s', Material.STICK);
		axe_recipe.setIngredient('a', Material.AIR);
		Dropper_shop_plugin.instance.add_recipe(axe_recipe);

		ShapedRecipe shovel_recipe = new ShapedRecipe(shovel_recipe_namespace, new ItemStack(Material.STONE_SHOVEL));
		shovel_recipe.shape("aba", "asa", "asa");
		shovel_recipe.setIngredient('b', Material.BRICK);
		shovel_recipe.setIngredient('s', Material.STICK);
		shovel_recipe.setIngredient('a', Material.AIR);
		Dropper_shop_plugin.instance.add_recipe(shovel_recipe);

		ShapedRecipe hoe_recipe = new ShapedRecipe(hoe_recipe_namespace, new ItemStack(Material.STONE_HOE));
		hoe_recipe.shape("bba", "asa", "asa");
		hoe_recipe.setIngredient('b', Material.BRICK);
		hoe_recipe.setIngredient('s', Material.STICK);
		hoe_recipe.setIngredient('a', Material.AIR);
		Dropper_shop_plugin.instance.add_recipe(hoe_recipe);

		ShapedRecipe sword_recipe = new ShapedRecipe(sword_recipe_namespace, new ItemStack(Material.STONE_SWORD));
		sword_recipe.shape("aba", "aba", "asa");
		sword_recipe.setIngredient('b', Material.BRICK);
		sword_recipe.setIngredient('s', Material.STICK);
		sword_recipe.setIngredient('a', Material.AIR);
		Dropper_shop_plugin.instance.add_recipe(sword_recipe);

	}

	public static void init_tools_config() {
		init_config(pickaxe_config, Tool_type.PICKAXE);
		init_config(axe_config, Tool_type.AXE);
		init_config(hoe_config, Tool_type.HOE);
		init_config(shovel_config, Tool_type.SHOVEL);
		init_config(sword_config, Tool_type.SWORD);
	}

	public static void init_tools() {
		init(pickaxe_config, Tool_type.PICKAXE);
		init(axe_config, Tool_type.AXE);
		init(hoe_config, Tool_type.HOE);
		init(shovel_config, Tool_type.SHOVEL);
		init(sword_config, Tool_type.SWORD);
	}

	private static void init_config(ArrayList<Tools> config_list, Tool_type tool_type) {
		for (Tool_material.Custom_material material : Tool_material.Custom_material.values()) {
			config_list.add(new Tools(material, tool_type));
		}
	}

	private static void init(ArrayList<Tools> config_list, Tool_type tool_type) {
		init_config(config_list, tool_type);
		Dropper_shop_plugin.instance.getLogger().info("[额外工具]注册" + tool_type.get_display_name());
		for (int i = 0; i < config_list.size(); i++) {
			Tools tool = config_list.get(i);
			String id_name = tool.get_id_name();
			NamespacedKey key = Dropper_shop_plugin.get_key(id_name);
			ItemStack tool_item = Material_ext.register(key, tool.get_raw_material(), tool.get_display_name(),
					Dropper_shop_plugin.custom_model_data_offset + tool_model_offset + i);
			Custom_durability.init_custom_durability(tool_item);
		}
	}

	public static ItemStack gen_tool(Tool_material tool_material, Tool_type tool_type) {
		String id_name = tool_material.name().toLowerCase() + "_" + tool_type.name().toLowerCase();
		return Material_ext.new_item(id_name, 1);
	}
}
