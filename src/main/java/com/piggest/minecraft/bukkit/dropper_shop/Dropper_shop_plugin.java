package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.piggest.minecraft.bukkit.depository.Depository;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	private Economy economy = null;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private File shop_file = null;
	private int make_price = 0;
	private final Dropper_shop_listener shop_listener = new Dropper_shop_listener();
	private Dropper_shop_manager shop_manager = new Dropper_shop_manager(this);
	private Depository_manager depository_manager = new Depository_manager(this);
	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	private NamespacedKey namespace = new NamespacedKey(this, "Dropper_shop");
	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();
	private ItemStack reader_item = null;
	private Depository_listener depository_listener = new Depository_listener();

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
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("dropper_shop")) {
			if (args.length == 0) {
				player.sendMessage("请使用/dropper_shop make");
				return true;
			}
			if (args[0].equalsIgnoreCase("make")) {
				if (!player.hasPermission("dropper_shop.make")) {
					player.sendMessage("你没有权限建立投掷器商店");
					return true;
				}
				Block look_block = player.getTargetBlockExact(4);
				// player.sendMessage(look_block.getType().name());
				if (look_block == null) {
					player.sendMessage("请指向投掷器方块");
					return true;
				}
				if (look_block.getType() == Material.DROPPER) {
					if (economy.has(player, make_price)) {
						economy.withdrawPlayer(player, make_price);
					} else {
						player.sendMessage("钱不够");
						return true;
					}
					Material item = player.getInventory().getItemInMainHand().getType();
					if (this.get_price(item) == -1) {
						player.sendMessage(item.name() + "不能被出售");
					} else {
						Dropper_shop shop = this.shop_manager.get(look_block.getLocation());
						if (shop != null) {
							shop.set_selling_item(item);
							player.sendMessage("投掷器商店已经变更为" + item.name());
						} else {
							shop = new Dropper_shop();
							shop.set_location(look_block.getLocation());
							shop.set_owner(player.getName());
							shop.set_selling_item(item);
							this.shop_manager.add(shop);
							player.sendMessage(item.name() + "的投掷器商店已经被设置");
						}
					}
				} else {
					player.sendMessage("不是投掷器");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				Block look_block = player.getTargetBlockExact(4);
				Dropper_shop shop = this.shop_manager.get(look_block.getLocation());
				if (shop != null) {
					if (shop.get_owner_name().equalsIgnoreCase(player.getName())
							|| player.hasPermission("dropper_shop.remove.others")) {
						player.sendMessage(
								"已移除" + shop.get_owner_name() + "的" + shop.get_selling_item().name() + "投掷器商店");
						this.shop_manager.remove(shop);
					} else {
						player.sendMessage("这不是你自己的投掷器商店，而是" + shop.get_owner_name() + "的");
						return true;
					}
				} else {
					player.sendMessage("不是投掷器商店");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("setprice")) {
				if (!player.hasPermission("dropper_shop.changeprice")) {
					player.sendMessage("你没有权限修改价格");
					return true;
				}
				if (args.length < 2) {
					player.sendMessage("请输入价格");
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
					this.saveConfig();
				} catch (NumberFormatException e) {
					player.sendMessage("请输入整数");
				}
				return true;
			} else {
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("depository")) {
			if (args.length == 0) {
				player.sendMessage("请使用/depository make|info");
				return true;
			}
			if (args[0].equalsIgnoreCase("make")) {
				Block look_block = player.getTargetBlockExact(4);
				if (look_block == null) {
					player.sendMessage("请指向方块");
					return true;
				}
				Depository depository = this.depository_manager.find(player.getName(), look_block.getLocation(), true);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				this.depository_manager.add(depository);
				player.sendMessage("存储器结构建立完成");
			} else if (args[0].equalsIgnoreCase("info")) {
				Block look_block = player.getTargetBlockExact(4);
				if (look_block == null) {
					player.sendMessage("请指向方块");
					return true;
				}
				Depository depository = this.depository_manager.find(player.getName(), look_block.getLocation(), false);
				if (depository == null) {
					player.sendMessage("没有检测到完整的存储器结构");
					return true;
				}
				player.sendMessage("存储器结构信息\n");
			}
		}
		return true;
	}

	@Override
	public void onEnable() {
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
	}

	public Dropper_shop_manager get_shop_manager() {
		return this.shop_manager;
	}

	public int get_price(Material material) {
		Integer price = this.price_map.get(material.name());
		if (price == null) {
			return -1;
		} else {
			return price;
		}
	}

	public void init_reader_item() {
		this.reader_item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = reader_item.getItemMeta();
		meta.setDisplayName("§r存储读取器");
		ArrayList<String> lore = new ArrayList<String>();
		// lore.add("§r右键方块使方块");
		// lore.add("§r正面转至该面");
		// lore.add("§r(潜行状态下相反)");
		meta.setLore(lore);
		this.reader_item.setItemMeta(meta);
	}

	private void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(this.namespace, this.reader_item);
		sr1.shape("rsr", "scs", "rsr");
		sr1.setIngredient('s', Material.NETHER_STAR);
		sr1.setIngredient('c', Material.ENDER_CHEST);
		sr1.setIngredient('r', Material.END_CRYSTAL);
		getServer().addRecipe(sr1);
		this.sr.add(sr1);
		getLogger().info("扳手合成表已添加");
	}
}
