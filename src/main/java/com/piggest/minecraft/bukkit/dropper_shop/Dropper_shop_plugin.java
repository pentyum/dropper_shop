package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_command_executor;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_config;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_listener;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_manager;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas_bottle;
import com.piggest.minecraft.bukkit.advanced_furnace.Reaction_container;
import com.piggest.minecraft.bukkit.depository.Depository_command_executor;
import com.piggest.minecraft.bukkit.depository.Depository_config;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.depository.Update_component;
import com.piggest.minecraft.bukkit.depository.Update_component_listener;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_command_executor;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_config;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_listener;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_manager;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.grinder.Grinder_command_executor;
import com.piggest.minecraft.bukkit.grinder.Grinder_config;
import com.piggest.minecraft.bukkit.grinder.Grinder_listener;
import com.piggest.minecraft.bukkit.grinder.Grinder_manager;
import com.piggest.minecraft.bukkit.grinder.Powder;
import com.piggest.minecraft.bukkit.gui.Gui_config;
import com.piggest.minecraft.bukkit.gui.Gui_listener;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure_listener;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import com.piggest.minecraft.bukkit.wrench.Wrench_command_executor;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	public static Dropper_shop_plugin instance = null;
	private Economy economy = null;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private File shop_file = null;
	private int make_price = 0;
	private int make_grinder_price = 0;

	private Dropper_shop_manager shop_manager = new Dropper_shop_manager();
	private Depository_manager depository_manager = new Depository_manager();
	private Grinder_manager grinder_manager = new Grinder_manager();
	private Advanced_furnace_manager adv_furnace_manager = new Advanced_furnace_manager();
	private Exp_saver_manager exp_saver_manager = new Exp_saver_manager();
	private Structure_manager[] structure_manager = { shop_manager, depository_manager, grinder_manager,
			adv_furnace_manager, exp_saver_manager };

	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	private HashMap<String, Integer> unit_map = new HashMap<String, Integer>();

	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();
	
	private final Gui_listener gui_listener = new Gui_listener();
	private final Multi_block_structure_listener multi_block_structure_listener = new Multi_block_structure_listener();
	private Listener[] structure_listeners = { new Depository_listener(), new Dropper_shop_listener(),
			new Update_component_listener(), new Grinder_listener(), new Advanced_furnace_listener(),
			new Exp_saver_listener() };
	private HashMap<String, Gui_config> gui_config = new HashMap<String, Gui_config>();

	public FileConfiguration get_shop_config() {
		return this.shop_config;
	}

	public int get_make_shop_price() {
		return this.make_price;
	}

	public int get_make_grinder_price() {
		return this.make_grinder_price;
	}

	public void set_make_shop_price(int price) {
		this.make_price = price;
		this.get_config().set("make-price", price);
	}

	public FileConfiguration get_config() {
		return this.config;
	}

	public HashMap<String, Integer> get_shop_price_map() {
		return this.price_map;
	}

	public Economy get_economy() {
		return this.economy;
	}

	private boolean initVault() {
		boolean hasNull = false;
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			if ((economy = economyProvider.getProvider()) == null) {
				hasNull = true;
			}
		}
		return !hasNull;
	}

	@Override
	public void onEnable() {
		Dropper_shop_plugin.instance = this;

		saveDefaultConfig();
		saveResource("shops.yml", false);
		this.config = getConfig();
		this.make_price = this.config.getInt("make-price");
		this.make_grinder_price = this.config.getInt("make-grinder-price");
		ConfigurationSection price_section = this.config.getConfigurationSection("material");
		Set<String> price_keys = price_section.getKeys(false);
		for (String material_name : price_keys) {
			this.price_map.put(material_name, price_section.getInt(material_name));
		}
		ConfigurationSection unit_section = this.config.getConfigurationSection("flint-unit");
		Set<String> unit_keys = unit_section.getKeys(false);
		for (String material_name : unit_keys) {
			this.unit_map.put(material_name, unit_section.getInt(material_name));
		}
		this.shop_file = new File(this.getDataFolder(), "shops.yml");
		this.shop_config = YamlConfiguration.loadConfiguration(shop_file);

		this.getCommand("depository").setExecutor(new Depository_command_executor());
		this.getCommand("dropper_shop").setExecutor(new Dropper_shop_command_executor());
		this.getCommand("grinder").setExecutor(new Grinder_command_executor());
		this.getCommand("adv_furnace").setExecutor(new Advanced_furnace_command_executor());
		this.getCommand("exp_saver").setExecutor(new Exp_saver_command_executor());
		Wrench_command_executor wrench = new Wrench_command_executor();
		this.getCommand("wrench").setExecutor(wrench);

		getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}

		Powder.init_powder();
		Reader.init_reader_item();
		Reader.set_recipe();
		Update_component.init_component();
		Update_component.set_recipe();
		Grinder.config.init_recipe();
		Gas_bottle.init_gas_bottle();
		Gas_bottle.set_recipe();
		Reaction_container.init_reaction();
		wrench.init();
		
		Grinder_config grinder_config = new Grinder_config();
		this.gui_config.put(grinder_config.get_gui_name(), grinder_config);
		
		Advanced_furnace_config advanced_furnace_config = new Advanced_furnace_config();
		this.gui_config.put(advanced_furnace_config.get_gui_name(), advanced_furnace_config);
		
		Exp_saver_config exp_saver_config = new Exp_saver_config();
		this.gui_config.put(exp_saver_config.get_gui_name(), exp_saver_config);
		
		Depository_config depository_config = new Depository_config();
		this.gui_config.put(depository_config.get_gui_name(), depository_config);
		
		for (Structure_manager manager : this.structure_manager) {
			manager.load_structures();
		}

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(this.multi_block_structure_listener, this);
		pm.registerEvents(this.gui_listener, this);
		
		for (Listener listener : this.structure_listeners) {
			pm.registerEvents(listener, this);
		}
	}

	@Override
	public void onDisable() {
		for (Structure_manager manager : this.structure_manager) {
			manager.save_structures();
		}
		try {
			shop_config.save(this.shop_file);
		} catch (IOException e) {
			this.getLogger().severe("结构文件保存错误!");
		}
		for (ShapedRecipe sr : this.sr) {
			Iterator<Recipe> i = Bukkit.recipeIterator();
			while (i.hasNext()) {
				if (i.next().equals(sr)) {
					i.remove();
				}
			}
		}
	}

	public Dropper_shop_manager get_shop_manager() {
		return this.shop_manager;
	}

	public Depository_manager get_depository_manager() {
		return this.depository_manager;
	}

	public Grinder_manager get_grinder_manager() {
		return this.grinder_manager;
	}

	public Advanced_furnace_manager get_adv_furnace_manager() {
		return this.adv_furnace_manager;
	}

	public Structure_manager[] get_structure_manager() {
		return this.structure_manager;
	}

	public int get_price(Material material) {
		Integer price = this.price_map.get(material.name());
		if (price == null) {
			return -1;
		} else {
			return price;
		}
	}

	public int get_unit(Material material) {
		Integer unit = this.unit_map.get(material.name());
		if (unit == null) {
			return 0;
		} else {
			return unit;
		}
	}

	public ArrayList<ShapedRecipe> get_sr() {
		return this.sr;
	}

	public Exp_saver_manager get_exp_saver_manager() {
		return this.exp_saver_manager;
	}

	public Gui_config get_gui_config(String gui_name) {
		return this.gui_config.get(gui_name);
	}

}
