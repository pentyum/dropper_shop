package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.piggest.minecraft.bukkit.depository.Depository_command_executor;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.depository.Update_component;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	public static Dropper_shop_plugin instance = null;
	private Economy economy = null;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private File shop_file = null;
	private int make_price = 0;
	private final Dropper_shop_listener shop_listener = new Dropper_shop_listener();
	private Dropper_shop_manager shop_manager = new Dropper_shop_manager();
	private Depository_manager depository_manager = new Depository_manager();
	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	public NamespacedKey namespace = new NamespacedKey(this, "Dropper_shop");
	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();
	private Depository_listener depository_listener = new Depository_listener();

	public FileConfiguration get_shop_config() {
		return this.shop_config;
	}

	public int get_make_shop_price() {
		return this.make_price;
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
		ConfigurationSection price_section = this.config.getConfigurationSection("material");
		Set<String> price_keys = price_section.getKeys(false);
		for (String material_name : price_keys) {
			this.price_map.put(material_name, price_section.getInt(material_name));
		}
		this.shop_file = new File(this.getDataFolder(), "shops.yml");
		this.shop_config = YamlConfiguration.loadConfiguration(shop_file);
		this.getCommand("depository").setExecutor(new Depository_command_executor(this));
		this.getCommand("dropper_shop").setExecutor(new Dropper_shop_command_executor(this));

		getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}

		this.shop_manager.load_structures();
		this.depository_manager.load_structures();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(shop_listener, this);
		pm.registerEvents(depository_listener, this);
		Reader.init_reader_item();
		Reader.set_recipe();
		Update_component.init_component();
		Update_component.set_recipe();
	}

	@Override
	public void onDisable() {
		shop_manager.save_structures();
		depository_manager.save_structures();
		try {
			shop_config.save(this.shop_file);
		} catch (IOException e) {
			getLogger().severe("商店文件保存错误!");
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

	public int get_price(Material material) {
		Integer price = this.price_map.get(material.name());
		if (price == null) {
			return -1;
		} else {
			return price;
		}
	}

	public ArrayList<ShapedRecipe> get_sr() {
		return this.sr;
	}
}
