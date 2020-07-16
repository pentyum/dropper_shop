package com.piggest.minecraft.bukkit.resourcepacks;

import com.google.gson.Gson;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.resourcepacks.dropper_shop.Element;
import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Brick;
import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Sugar;
import com.piggest.minecraft.bukkit.resourcepacks.minecraft.Vanilla_model;
import com.piggest.minecraft.bukkit.resourcepacks.minecraft.tools.*;
import com.piggest.minecraft.bukkit.tools.Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Builder {
	public final static Gson gson = new Gson();
	public final static String project_dir = System.getProperty("user.dir").replace("\\", "/") + "/";
	public final static String resourcepack_target_dir = project_dir + "target/resourcepacks/dropper_shop/";
	public final static String dropper_shop_models_dir = resourcepack_target_dir + "assets/dropper_shop/models/";
	public final static String dropper_shop_textures_dir = resourcepack_target_dir + "assets/dropper_shop/textures/";
	public final static String minecraft_models_dir = resourcepack_target_dir + "assets/minecraft/models/";
	public final static String minecraft_item_models_dir = minecraft_models_dir + "item/";

	public static void write_json(String path, String json) {
		FileWriter fw;
		File json_file = new File(path);

		try {
			if (json_file.exists()) {
				json_file.delete();
			} else {
				json_file.createNewFile();
			}
			fw = new FileWriter(json_file);
			fw.write(json);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void build_elements(Sugar sugar) {
		File file = new File(dropper_shop_models_dir + "elements");
		if (!file.exists()) {
			file.mkdirs();
		}
		for (int i = 1; i < 95; i++) {
			String file_path = dropper_shop_models_dir + "elements/element_" + i + ".json";
			String js = new Element(i).to_json();
			write_json(file_path, js);
			sugar.add_custom_model_override(Dropper_shop_plugin.custom_model_data_offset + i,
					"dropper_shop:elements/element_" + i);
		}
	}

	private static void build_powder(Sugar clay_ball) {
		File file = new File(dropper_shop_models_dir + "powder");
		if (!file.exists()) {
			file.mkdirs();
		}
		for (int i = 1; i < com.piggest.minecraft.bukkit.grinder.Powder.powder_config.size(); i++) {
			String material_name = com.piggest.minecraft.bukkit.grinder.Powder.powder_config.get(i).get_material_name();
			String file_path = dropper_shop_models_dir + "powder/" + material_name + ".json";
			String js = new com.piggest.minecraft.bukkit.resourcepacks.dropper_shop.Powder(material_name).to_json();
			write_json(file_path, js);
			clay_ball.add_custom_model_override(
					Dropper_shop_plugin.custom_model_data_offset
							+ com.piggest.minecraft.bukkit.grinder.Powder.powder_model_offset + i,
					"dropper_shop:powder/" + material_name);
		}
	}

	private static void build_ingot(Brick iron_ingot) {
		File file = new File(dropper_shop_models_dir + "ingots");
		if (!file.exists()) {
			file.mkdirs();
		}
		for (int i = 1; i < com.piggest.minecraft.bukkit.grinder.Ingot.ingot_config.size(); i++) {
			String material_name = com.piggest.minecraft.bukkit.grinder.Ingot.ingot_config.get(i).get_material_name();
			String file_path = dropper_shop_models_dir + "ingots/" + material_name + ".json";
			String js = new com.piggest.minecraft.bukkit.resourcepacks.dropper_shop.Ingot(material_name).to_json();
			write_json(file_path, js);
			iron_ingot.add_custom_model_override(
					Dropper_shop_plugin.custom_model_data_offset
							+ com.piggest.minecraft.bukkit.grinder.Ingot.ingot_model_offset + i,
					"dropper_shop:ingots/" + material_name);
		}
	}

	private static void build_wrench(Iron_pickaxe iron_pickaxe) {
		File file = new File(dropper_shop_models_dir + "tools");
		if (!file.exists()) {
			file.mkdirs();
		}
		String file_path = dropper_shop_models_dir + "tools/wrench.json";
		String js = new com.piggest.minecraft.bukkit.resourcepacks.dropper_shop.Wrench().to_json();
		write_json(file_path, js);
		iron_pickaxe.add_custom_model_override(Dropper_shop_plugin.custom_model_data_offset + 1,
				"dropper_shop:tools/wrench");
	}

	private static void build_tool(List<Tools> config, Vanilla_model wooden, Vanilla_model golden, Vanilla_model stone,
								   Vanilla_model iron, Vanilla_model diamond) {
		File file = new File(dropper_shop_models_dir + "tools");
		if (!file.exists()) {
			file.mkdirs();
		}
		for (int i = 0; i < config.size(); i++) {
			Tools tool = config.get(i);
			String material_name = tool.get_id_name();
			String file_path = dropper_shop_models_dir + "tools/" + material_name + ".json";
			String js = new com.piggest.minecraft.bukkit.resourcepacks.dropper_shop.Tool(material_name).to_json();
			write_json(file_path, js);
			Vanilla_model vanilla_model;
			switch (tool.get_tool_material().get_raw()) {
				case WOODEN:
					vanilla_model = wooden;
					break;
				case GOLDEN:
					vanilla_model = golden;
					break;
				case STONE:
					vanilla_model = stone;
					break;
				case IRON:
					vanilla_model = iron;
					break;
				case DIAMOND:
					vanilla_model = diamond;
					break;
				default:
					return;
			}
			vanilla_model.add_custom_model_override(
					Dropper_shop_plugin.custom_model_data_offset + Tools.tool_model_offset + i,
					"dropper_shop:tools/" + material_name);
		}
	}

	private static void build_vanilla(Vanilla_model model) {
		File file = new File(minecraft_item_models_dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = minecraft_models_dir + model.getTextures().layer0 + ".json";
		write_json(path, model.to_json());
	}

	public static void main(String[] args) {
		System.out.println("欢迎使用Dropper shop插件");
		System.out.println("dropper_shop_models_dir:" + dropper_shop_models_dir);
		Brick brick = new Brick();
		Sugar sugar = new Sugar();

		Wooden_pickaxe wooden_pickaxe = new Wooden_pickaxe();
		Golden_pickaxe golden_pickaxe = new Golden_pickaxe();
		Stone_pickaxe stone_pickaxe = new Stone_pickaxe();
		Iron_pickaxe iron_pickaxe = new Iron_pickaxe();
		Diamond_pickaxe diamond_pickaxe = new Diamond_pickaxe();

		Wooden_axe wooden_axe = new Wooden_axe();
		Golden_axe golden_axe = new Golden_axe();
		Stone_axe stone_axe = new Stone_axe();
		Iron_axe iron_axe = new Iron_axe();
		Diamond_axe diamond_axe = new Diamond_axe();

		Wooden_hoe wooden_hoe = new Wooden_hoe();
		Golden_hoe golden_hoe = new Golden_hoe();
		Stone_hoe stone_hoe = new Stone_hoe();
		Iron_hoe iron_hoe = new Iron_hoe();
		Diamond_hoe diamond_hoe = new Diamond_hoe();

		Wooden_shovel wooden_shovel = new Wooden_shovel();
		Golden_shovel golden_shovel = new Golden_shovel();
		Stone_shovel stone_shovel = new Stone_shovel();
		Iron_shovel iron_shovel = new Iron_shovel();
		Diamond_shovel diamond_shovel = new Diamond_shovel();

		Wooden_sword wooden_sword = new Wooden_sword();
		Golden_sword golden_sword = new Golden_sword();
		Stone_sword stone_sword = new Stone_sword();
		Iron_sword iron_sword = new Iron_sword();
		Diamond_sword diamond_sword = new Diamond_sword();

		com.piggest.minecraft.bukkit.grinder.Powder.init_powder_config();
		com.piggest.minecraft.bukkit.grinder.Ingot.init_ingot_config();
		com.piggest.minecraft.bukkit.tools.Tools.init_tools_config();

		// dropper_shop文件夹
		build_elements(sugar);
		build_powder(sugar);
		build_ingot(brick);
		build_wrench(iron_pickaxe);
		build_tool(Tools.pickaxe_config, wooden_pickaxe, golden_pickaxe, stone_pickaxe, iron_pickaxe, diamond_pickaxe);
		build_tool(Tools.axe_config, wooden_axe, golden_axe, stone_axe, iron_axe, diamond_axe);
		build_tool(Tools.hoe_config, wooden_hoe, golden_hoe, stone_hoe, iron_hoe, diamond_hoe);
		build_tool(Tools.shovel_config, wooden_shovel, golden_shovel, stone_shovel, iron_shovel, diamond_shovel);
		build_tool(Tools.sword_config, wooden_sword, golden_sword, stone_sword, iron_sword, diamond_sword);

		// minecraft文件夹
		build_vanilla(sugar);
		build_vanilla(brick);
		build_vanilla(wooden_pickaxe);
		build_vanilla(golden_pickaxe);
		build_vanilla(stone_pickaxe);
		build_vanilla(iron_pickaxe);
		build_vanilla(diamond_pickaxe);

		build_vanilla(wooden_axe);
		build_vanilla(golden_axe);
		build_vanilla(stone_axe);
		build_vanilla(iron_axe);
		build_vanilla(diamond_axe);

		build_vanilla(wooden_hoe);
		build_vanilla(golden_hoe);
		build_vanilla(stone_hoe);
		build_vanilla(iron_hoe);
		build_vanilla(diamond_hoe);

		build_vanilla(wooden_shovel);
		build_vanilla(golden_shovel);
		build_vanilla(stone_shovel);
		build_vanilla(iron_shovel);
		build_vanilla(diamond_shovel);

		build_vanilla(wooden_sword);
		build_vanilla(golden_sword);
		build_vanilla(stone_sword);
		build_vanilla(iron_sword);
		build_vanilla(diamond_sword);
		/*
		 * System.out.println("Java 运行时环境版本:"+System.getProperty("java.version"));
		 * System.out.println("Java 运行时环境供应商:"+System.getProperty("java.vendor"));
		 * System.out.println("Java 供应商的URL:"+System.getProperty("java.vendor.url"));
		 * System.out.println("Java 安装目录:"+System.getProperty("java.home"));
		 * System.out.println("Java 虚拟机规范版本:"+System.getProperty(
		 * "java.vm.specification.version"));
		 * System.out.println("Java 类格式版本号:"+System.getProperty("java.class.version"));
		 * System.out.println("Java 类路径："+System.getProperty("java.class.path"));
		 * System.out.println("加载库时搜索的路径列表:"+System.getProperty("java.library.path"));
		 * System.out.println("默认的临时文件路径:"+System.getProperty("java.io.tmpdir"));
		 * System.out.println("要使用的 JIT 编译器的名称:"+System.getProperty("java.compiler"));
		 * System.out.println("一个或多个扩展目录的路径:"+System.getProperty("java.ext.dirs"));
		 * System.out.println("操作系统的名称:"+System.getProperty("os.name"));
		 * System.out.println("操作系统的架构:"+System.getProperty("os.arch"));
		 * System.out.println("操作系统的版本:"+System.getProperty("os.version"));
		 * System.out.println("文件分隔符（在 UNIX 系统中是“/”）:"+System.getProperty(
		 * "file.separator"));
		 * System.out.println("路径分隔符（在 UNIX 系统中是“:”）:"+System.getProperty(
		 * "path.separator"));
		 * System.out.println("行分隔符（在 UNIX 系统中是“/n”）:"+System.getProperty(
		 * "line.separator"));
		 * System.out.println("用户的账户名称:"+System.getProperty("user.name"));
		 * System.out.println("用户的主目录:"+System.getProperty("user.home"));
		 * System.out.println("用户的当前工作目录:"+System.getProperty("user.dir"));
		 * System.out.println("当前的classpath的绝对路径的URI表示法:" +
		 * Thread.currentThread().getContextClassLoader().getResource(""));
		 * System.out.println("得到的是当前的classpath的绝对URI路径:"+
		 * Builder.class.getResource("/"));
		 * System.out.println("得到的是当前类Builder.class文件的URI目录:"+Builder.class.getResource(
		 * ""));
		 */
	}

}
