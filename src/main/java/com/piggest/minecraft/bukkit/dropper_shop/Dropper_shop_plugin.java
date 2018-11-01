package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	private Economy economy = null;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private File shop_file = null;
	private int make_price = 0;
	private final Dropper_shop_listener shop_listener = new Dropper_shop_listener(this);
	private Dropper_shop_manager shop_manager = null;
	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();

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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dropper_shop")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			} else {
				Player player = (Player) sender;
				if (args.length != 1) {
					player.sendMessage("请使用/dropper_shop make");
				} else if (args[0] == "make") {
					if (economy.has(player, make_price)) {
						economy.withdrawPlayer(player, make_price);
					} else {
						player.sendMessage("钱不够");
						return true;
					}
					Block look_block = null;
					if(look_block instanceof Dropper) {
						Dropper_shop new_shop = new Dropper_shop(this,look_block.getLocation());
						Material item = player.getInventory().getItemInMainHand().getType();
						new_shop.set_selling_item(item);
						this.shop_manager.add(new_shop);
					}else {
						player.sendMessage("不是发射器");
					}
				} else if (args[0] == "setprice") {
					if (args.length != 2) {
						return true;
					}
					try {
						int set_price = Integer.parseInt(args[1]);
						ItemStack itemstack = player.getInventory().getItemInMainHand();
						if (itemstack == null) {
							this.config.set("make-price", set_price);
							player.sendMessage("已设置创建投掷器商店价格为" + set_price);
						} else {
							Material item = player.getInventory().getItemInMainHand().getType();
							this.price_map.put(item.name(), set_price);
							this.config.set("material", this.price_map);
							player.sendMessage("已设置" + item.name() + "的投掷器商店价格为" + set_price);
						}
						saveConfig();
					} catch (NumberFormatException e) {
						player.sendMessage("请输入整数");
						return true;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		saveResource("shops.yml", false);
		this.config = getConfig();
		this.make_price = this.config.getInt("make-price");
		// this.price_map
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

	public int get_price(Material material) {
		// return this.price_map.get(material.name());
		return 0;
	}

}
