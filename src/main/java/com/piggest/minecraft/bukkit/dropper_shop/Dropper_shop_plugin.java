package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	private Economy economy = null;
	private int price = 0;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private File shop_file = null;
	private final Dropper_shop_listener shop_listener = new Dropper_shop_listener(this);
	private Dropper_shop_manager shop_manager = null;

	public FileConfiguration get_shop_config() {
		return this.shop_config;
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
		saveDefaultConfig();
		saveResource("shops.yml", false);
		this.config = getConfig();
		this.price = config.getInt("price");
		this.shop_file = new File(this.getDataFolder(), "shops.yml");
		this.shop_config = YamlConfiguration.loadConfiguration(shop_file);
		this.shop_manager = new Dropper_shop_manager(this);

		getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}

		shop_manager.load_shops();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(shop_listener, this);
	}

	@Override
	public void onDisable() {
		shop_manager.save_shops();
		try {
			shop_config.save(this.shop_file);
		} catch (IOException e) {
			getLogger().severe("结构文件保存错误!");
		}
	}
	
	public Dropper_shop_manager get_shop_manager() {
		return this.shop_manager;
	}

	public int get_price() {
		return this.price;
	}

}
