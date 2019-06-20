package com.piggest.minecraft.bukkit.dropper_shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_command_executor;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_listener;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_manager;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas_bottle;
import com.piggest.minecraft.bukkit.advanced_furnace.Reaction_container;
import com.piggest.minecraft.bukkit.depository.Depository;
import com.piggest.minecraft.bukkit.depository.Depository_command_executor;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.depository.Update_component;
import com.piggest.minecraft.bukkit.depository.Update_component_listener;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_command_executor;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_listener;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_manager;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.grinder.Grinder_command_executor;
import com.piggest.minecraft.bukkit.grinder.Grinder_listener;
import com.piggest.minecraft.bukkit.grinder.Grinder_manager;
import com.piggest.minecraft.bukkit.grinder.Powder;
import com.piggest.minecraft.bukkit.gui.Gui_listener;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_command_executor;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_manager;
import com.piggest.minecraft.bukkit.music_stick.Note_stick_listener;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_listener;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine_manager;
import com.piggest.minecraft.bukkit.wrench.Wrench_command_executor;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	public static Dropper_shop_plugin instance = null;
	private Economy economy = null;
	private FileConfiguration config = null;
	private FileConfiguration shop_config = null;
	private FileConfiguration lottery_config = null;
	private File shop_file = null;
	private File lottery_file = null;
	private int make_price = 0;
	private int make_grinder_price = 0;
	private int make_lottery_pool_price = 0;
	private int lottery_price = 0;
	private int exp_saver_upgrade_base_price = 0;
	private int exp_saver_max_structure_level = 0;

	private Dropper_shop_manager shop_manager = new Dropper_shop_manager();
	private Depository_manager depository_manager = new Depository_manager();
	private Grinder_manager grinder_manager = new Grinder_manager();
	private Advanced_furnace_manager adv_furnace_manager = new Advanced_furnace_manager();
	private Exp_saver_manager exp_saver_manager = new Exp_saver_manager();
	private Lottery_pool_manager lottery_pool_manager = new Lottery_pool_manager();
	private Trees_felling_machine_manager trees_felling_machine_manager = new Trees_felling_machine_manager();
	private HashMap<Class<? extends Structure>, Structure_manager> structure_manager_map = new HashMap<Class<? extends Structure>, Structure_manager>();

	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	private HashMap<String, Integer> unit_map = new HashMap<String, Integer>();
	private HashMap<String, HashMap<Gas, Integer>> air_map = new HashMap<String, HashMap<Gas, Integer>>();
	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();

	private final Note_stick_listener note_listener = new Note_stick_listener();
	private final Gui_listener gui_listener = new Gui_listener();
	private final Structure_listener Structure_listener = new Structure_listener();

	private Listener[] structure_listeners = { new Depository_listener(), new Dropper_shop_listener(),
			new Update_component_listener(), new Grinder_listener(), new Advanced_furnace_listener(),
			new Exp_saver_listener() };
	// private HashMap<String, Gui_config> gui_config = new HashMap<String,
	// Gui_config>();
	private HashMap<String, String> enchantment_name = new HashMap<String, String>();

	public FileConfiguration get_shop_config() {
		return this.shop_config;
	}

	public FileConfiguration get_lottery_config() {
		return this.lottery_config;
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

		this.enchantment_name.put("minecraft:sweeping", "横扫之刃");
		this.enchantment_name.put("minecraft:aqua_affinity", "水下速掘");
		this.enchantment_name.put("minecraft:depth_strider", "深海探索者");
		this.enchantment_name.put("minecraft:respiration", "水下呼吸");
		this.enchantment_name.put("minecraft:lure", "饵钓");
		this.enchantment_name.put("minecraft:fire_aspect", "火焰附加");
		this.enchantment_name.put("minecraft:looting", "抢夺");
		this.enchantment_name.put("minecraft:channeling", "引雷");
		this.enchantment_name.put("minecraft:luck_of_the_Sea", "海之眷顾");
		this.enchantment_name.put("minecraft:smite", "亡灵杀手");
		this.enchantment_name.put("minecraft:feather_falling", "摔落保护");
		this.enchantment_name.put("minecraft:efficiency", "效率");
		this.enchantment_name.put("minecraft:impaling", "穿刺");
		this.enchantment_name.put("minecraft:thorns", "荆棘");
		this.enchantment_name.put("minecraft:flame", "火矢");
		this.enchantment_name.put("minecraft:bane_of_arthropods", "节肢杀手");
		this.enchantment_name.put("minecraft:fortune", "时运");
		this.enchantment_name.put("minecraft:punch", "冲击");
		this.enchantment_name.put("minecraft:riptide", "激流");
		this.enchantment_name.put("minecraft:infinity", "无限");
		this.enchantment_name.put("minecraft:loyalty", "忠诚");
		this.enchantment_name.put("minecraft:binding_curse", "绑定诅咒");
		this.enchantment_name.put("minecraft:vanishing_curse", "消失诅咒");
		this.enchantment_name.put("minecraft:protection", "保护");
		this.enchantment_name.put("minecraft:blast_protection", "爆炸保护");
		this.enchantment_name.put("minecraft:fire_protection", "火焰保护");
		this.enchantment_name.put("minecraft:projectile_protection", "弹射物保护");
		this.enchantment_name.put("minecraft:power", "力量");
		this.enchantment_name.put("minecraft:mending", "经验修补");
		this.enchantment_name.put("minecraft:knockback", "击退");
		this.enchantment_name.put("minecraft:frost_walker", "冰霜行者");
		this.enchantment_name.put("minecraft:unbreaking", "耐久");
		this.enchantment_name.put("minecraft:silk_touch", "精准采集");
		this.enchantment_name.put("minecraft:sharpness", "锋利");
		
		this.structure_manager_map.put(Dropper_shop.class, shop_manager);
		this.structure_manager_map.put(Depository.class, depository_manager);
		this.structure_manager_map.put(Grinder.class, grinder_manager);
		this.structure_manager_map.put(Advanced_furnace.class, adv_furnace_manager);
		this.structure_manager_map.put(Exp_saver.class, exp_saver_manager);
		this.structure_manager_map.put(Lottery_pool.class, lottery_pool_manager);
		this.structure_manager_map.put(Trees_felling_machine.class, trees_felling_machine_manager);

		saveDefaultConfig();
		saveResource("shops.yml", false);
		saveResource("lottery_pool.yml", false);
		this.config = getConfig();
		this.make_price = this.config.getInt("make-price");
		this.make_grinder_price = this.config.getInt("make-grinder-price");
		this.make_lottery_pool_price = this.config.getInt("make-lottery-pool-price");
		this.exp_saver_upgrade_base_price = this.config.getInt("exp-saver-upgrade-base-price");
		this.lottery_price = this.config.getInt("lottery-price");
		this.exp_saver_max_structure_level = this.config.getInt("exp-saver-max-structure-level");

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
		this.lottery_file = new File(this.getDataFolder(), "lottery_pool.yml");
		this.lottery_config = YamlConfiguration.loadConfiguration(lottery_file);
		this.gen_air();

		this.getCommand("depository").setExecutor(new Depository_command_executor());
		this.getCommand("dropper_shop").setExecutor(new Dropper_shop_command_executor());
		this.getCommand("grinder").setExecutor(new Grinder_command_executor());
		this.getCommand("adv_furnace").setExecutor(new Advanced_furnace_command_executor());
		this.getCommand("exp_saver").setExecutor(new Exp_saver_command_executor());
		Wrench_command_executor wrench = new Wrench_command_executor();
		this.getCommand("wrench").setExecutor(wrench);
		this.getCommand("lottery").setExecutor(new Lottery_pool_command_executor());

		getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}
		/*
		 * Grinder_config grinder_config = new Grinder_config();
		 * this.gui_config.put(grinder_config.get_gui_name(), grinder_config);
		 * 
		 * Advanced_furnace_config advanced_furnace_config = new
		 * Advanced_furnace_config();
		 * this.gui_config.put(advanced_furnace_config.get_gui_name(),
		 * advanced_furnace_config);
		 * 
		 * Exp_saver_config exp_saver_config = new Exp_saver_config();
		 * this.gui_config.put(exp_saver_config.get_gui_name(), exp_saver_config);
		 * 
		 * Depository_config depository_config = new Depository_config();
		 * this.gui_config.put(depository_config.get_gui_name(), depository_config);
		 */

		Powder.init_powder();
		Reader.init_reader_item();
		Reader.set_recipe();
		Update_component.init_component();
		Update_component.set_recipe();
		grinder_manager.init_recipe();
		Gas_bottle.init_gas_bottle();
		Gas_bottle.set_recipe();
		Reaction_container.init_reaction();
		wrench.init();

		for (Entry<Class<? extends Structure>, Structure_manager> entry : this.structure_manager_map.entrySet()) {
			Structure_manager manager = entry.getValue();
			manager.load_structures();
		}

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(this.Structure_listener, this);

		pm.registerEvents(this.gui_listener, this);
		pm.registerEvents(this.note_listener, this);

		for (Listener listener : this.structure_listeners) {
			pm.registerEvents(listener, this);
		}
		note_listener.runner.runTaskTimer(this, 10, 5);
	}

	@Override
	public void onDisable() {
		for (Entry<Class<? extends Structure>, Structure_manager> entry : this.structure_manager_map.entrySet()) {
			Structure_manager manager = entry.getValue();
			manager.save_structures();
		}
		try {
			shop_config.save(this.shop_file);
		} catch (IOException e) {
			this.getLogger().severe("结构文件保存错误!");
		}
		try {
			lottery_config.save(this.lottery_file);
		} catch (IOException e) {
			this.getLogger().severe("抽奖配置文件保存错误!");
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

	public Trees_felling_machine_manager get_trees_felling_machine_manager() {
		return this.trees_felling_machine_manager;
	}

	public HashMap<Class<? extends Structure>, Structure_manager> get_structure_manager() {
		return this.structure_manager_map;
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

	public Gui_structure_manager get_gui_structure_manager(String gui_name) {
		for (Entry<Class<? extends Structure>, Structure_manager> entry : this.structure_manager_map.entrySet()) {
			Structure_manager manager = entry.getValue();
			if (manager instanceof Gui_structure_manager) {
				Gui_structure_manager gui_structure_manager = (Gui_structure_manager) manager;
				if (gui_structure_manager.get_gui_name().equals(gui_name)) {
					return gui_structure_manager;
				}
			}
		}
		return null;
	}

	public int get_make_lottery_pool_price() {
		return this.make_lottery_pool_price;
	}

	public int get_lottery_price() {
		return this.lottery_price;
	}

	public int get_exp_saver_upgrade_base_price() {
		return this.exp_saver_upgrade_base_price;
	}

	public int get_exp_saver_max_structure_level() {
		return this.exp_saver_max_structure_level;
	}

	public void set_lottery_price(int newprice) {
		this.lottery_price = newprice;
		this.get_config().set("lottery-price", newprice);
		this.saveConfig();
	}

	private void gen_air() {
		ConfigurationSection air_config = this.config.getConfigurationSection("air");
		Set<String> worlds = air_config.getKeys(false);
		for (String world_name : worlds) {
			ConfigurationSection world_air_config = air_config.getConfigurationSection(world_name);
			Set<String> air_types = world_air_config.getKeys(false);
			HashMap<Gas, Integer> air = new HashMap<Gas, Integer>();
			for (String air_name : air_types) {
				int value = world_air_config.getInt(air_name);
				Gas gas = Gas.get_gas(air_name);
				air.put(gas, value);
			}
			this.air_map.put(world_name, air);
		}
	}

	public HashMap<Gas, Integer> get_air(String world_name) {
		HashMap<Gas, Integer> air = this.air_map.get(world_name);
		if (air == null) {
			air = this.air_map.get("world");
		}
		return air;
	}

	public synchronized boolean cost_player_money(int money, OfflinePlayer player) {
		if (this.economy.has(player, money)) {
			this.economy.withdrawPlayer(player, money);
			return true;
		} else {
			return false;
		}
	}

	public String get_enchantment_name(String key) {
		String name = this.enchantment_name.get(key);
		if (name != null) {
			return name;
		} else {
			return key;
		}
	}

	public String get_enchantment_name(Enchantment ench) {
		return this.get_enchantment_name(ench.getKey().toString());
	}
}
