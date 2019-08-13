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
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_command_executor;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_listener;
import com.piggest.minecraft.bukkit.advanced_furnace.Advanced_furnace_manager;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas;
import com.piggest.minecraft.bukkit.advanced_furnace.Gas_bottle;
import com.piggest.minecraft.bukkit.advanced_furnace.Reaction_container;
import com.piggest.minecraft.bukkit.anti_thunder.Anti_thunder;
import com.piggest.minecraft.bukkit.anti_thunder.Anti_thunder_listener;
import com.piggest.minecraft.bukkit.anti_thunder.Anti_thunder_manager;
import com.piggest.minecraft.bukkit.config.Price_config;
import com.piggest.minecraft.bukkit.depository.Depository;
import com.piggest.minecraft.bukkit.depository.Depository_command_executor;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.depository.Upgrade_component_listener;
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
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_command_executor;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_manager;
import com.piggest.minecraft.bukkit.music_stick.Note_stick_listener;
import com.piggest.minecraft.bukkit.nms.NMS_manager;
import com.piggest.minecraft.bukkit.pigman_switch.Pigman_spawn_listener;
import com.piggest.minecraft.bukkit.pigman_switch.Pigman_switch;
import com.piggest.minecraft.bukkit.pigman_switch.Pigman_switch_manager;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_listener;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import com.piggest.minecraft.bukkit.sync_realtime.Sync_realtime;
import com.piggest.minecraft.bukkit.sync_realtime.Sync_realtime_command_executor;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_listener;
import com.piggest.minecraft.bukkit.teleport_machine.Radio_manager;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine_manager;
import com.piggest.minecraft.bukkit.utils.Tab_list;
import com.piggest.minecraft.bukkit.utils.language.Enchantments_zh_cn;
import com.piggest.minecraft.bukkit.utils.language.Item_zh_cn;
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

	private int exp_saver_max_structure_level = 0;
	private int exp_saver_anvil_upgrade_need = 0;
	private int exp_saver_remove_repaircost_exp = 0;

	private Price_config price_config = new Price_config(this);

	private Dropper_shop_manager shop_manager = null;
	private Depository_manager depository_manager = null;
	private Grinder_manager grinder_manager = null;
	private Advanced_furnace_manager adv_furnace_manager = null;
	private Exp_saver_manager exp_saver_manager = null;
	private Lottery_pool_manager lottery_pool_manager = null;
	private Trees_felling_machine_manager trees_felling_machine_manager = null;
	private Pigman_switch_manager pigman_switch_manager;
	private Anti_thunder_manager anti_thunder_manager;
	private Teleport_machine_manager teleport_machine_manager;

	private HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> structure_manager_map = new HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>>();

	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	private HashMap<String, Integer> unit_map = new HashMap<String, Integer>();
	private HashMap<String, HashMap<Gas, Integer>> air_map = new HashMap<String, HashMap<Gas, Integer>>();
	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();

	private final Note_stick_listener note_listener = new Note_stick_listener();
	private final Gui_listener gui_listener = new Gui_listener();
	private final Structure_listener Structure_listener = new Structure_listener();
	private HashMap<String, Integer> sync_realtime_worlds = new HashMap<String, Integer>();

	private Listener[] structure_listeners = { new Depository_listener(), new Dropper_shop_listener(),
			new Upgrade_component_listener(), new Grinder_listener(), new Advanced_furnace_listener(),
			new Exp_saver_listener(), new Pigman_spawn_listener(), new Anti_thunder_listener(),
			new Elements_listener() };

	private NMS_manager nms_manager = null;
	private Config_auto_saver auto_saver = new Config_auto_saver(this);
	private Sync_realtime realtime_runner = null;
	private Radio_manager radio_manager = null;

	public Dropper_shop_plugin() {
		this.getLogger().info("加载配置中");
		saveDefaultConfig();
		saveResource("shops.yml", false);
		saveResource("lottery_pool.yml", false);
		this.config = getConfig();

		this.price_config.load_price();
		this.exp_saver_max_structure_level = this.config.getInt("exp-saver-max-structure-level");
		this.exp_saver_anvil_upgrade_need = this.config.getInt("exp-saver-anvil-upgrade-need");
		this.exp_saver_remove_repaircost_exp = this.config.getInt("exp-saver-remove-repaircost-exp");
		ConfigurationSection sync_realtime_section = this.config.getConfigurationSection("sync-realtime-worlds");
		Set<String> worlds = sync_realtime_section.getKeys(false);
		for (String world_name : worlds) {
			this.sync_realtime_worlds.put(world_name, sync_realtime_section.getInt(world_name));
		}
		this.realtime_runner = new Sync_realtime(this.sync_realtime_worlds);

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
		this.lottery_config_load();

		this.gen_air();

		Enchantments_zh_cn.init();
		Item_zh_cn.init();
	}

	public FileConfiguration get_shop_config() {
		return this.shop_config;
	}

	public FileConfiguration get_lottery_config() {
		return this.lottery_config;
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

	private void init_structure_manager() {
		this.getLogger().info("加载结构管理器");
		this.shop_manager = new Dropper_shop_manager();
		this.depository_manager = new Depository_manager();
		this.grinder_manager = new Grinder_manager();
		this.adv_furnace_manager = new Advanced_furnace_manager();
		this.exp_saver_manager = new Exp_saver_manager();
		this.lottery_pool_manager = new Lottery_pool_manager();
		this.trees_felling_machine_manager = new Trees_felling_machine_manager();
		this.pigman_switch_manager = new Pigman_switch_manager();
		this.anti_thunder_manager = new Anti_thunder_manager();
		this.teleport_machine_manager = new Teleport_machine_manager();

		this.structure_manager_map.put(Dropper_shop.class, shop_manager);
		this.structure_manager_map.put(Depository.class, depository_manager);
		this.structure_manager_map.put(Grinder.class, grinder_manager);
		this.structure_manager_map.put(Advanced_furnace.class, adv_furnace_manager);
		this.structure_manager_map.put(Exp_saver.class, exp_saver_manager);
		this.structure_manager_map.put(Lottery_pool.class, lottery_pool_manager);
		this.structure_manager_map.put(Trees_felling_machine.class, trees_felling_machine_manager);
		this.structure_manager_map.put(Pigman_switch.class, pigman_switch_manager);
		this.structure_manager_map.put(Anti_thunder.class, anti_thunder_manager);
		this.structure_manager_map.put(Teleport_machine.class, teleport_machine_manager);
	}

	public boolean backup_old_shop_config_file() {
		File shop_file_backup = new File(shop_file.getAbsolutePath() + ".bak");
		try {
			if (shop_file_backup.exists()) {
				shop_file_backup.delete();
				shop_file.createNewFile();
			}
			Files.copy(shop_file, shop_file_backup);
		} catch (IOException e) {
			getLogger().severe("原结构配置备份失败");
			return false;
		}
		getLogger().info("原结构配置已保存至" + shop_file.getAbsolutePath() + ".bak");
		return true;
	}

	@Override
	public void onEnable() {
		Dropper_shop_plugin.instance = this;
		this.backup_old_shop_config_file();

		Tab_list.init();

		this.nms_manager = new NMS_manager(Bukkit.getBukkitVersion());
		this.init_structure_manager();
		this.radio_manager = new Radio_manager();

		this.getCommand("depository").setExecutor(new Depository_command_executor());
		this.getCommand("dropper_shop").setExecutor(new Dropper_shop_command_executor());
		this.getCommand("grinder").setExecutor(new Grinder_command_executor());
		this.getCommand("adv_furnace").setExecutor(new Advanced_furnace_command_executor());
		this.getCommand("exp_saver").setExecutor(new Exp_saver_command_executor());
		Wrench_command_executor wrench = new Wrench_command_executor();
		this.getCommand("wrench").setExecutor(wrench);
		this.getCommand("lottery").setExecutor(new Lottery_pool_command_executor());
		this.getCommand("sync_realtime").setExecutor(new Sync_realtime_command_executor(this.sync_realtime_worlds));

		getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}

		Powder.init_powder();
		Reader.init_reader_item();
		Reader.set_recipe();
		Upgrade_component.init_component();
		Upgrade_component.set_recipe();
		grinder_manager.init_recipe();
		Gas_bottle.init_gas_bottle();
		Gas_bottle.set_recipe();
		Reaction_container.init_reaction();
		wrench.init();

		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : this.structure_manager_map
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
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
		this.auto_saver.runTaskTimerAsynchronously(this, 10, 6000);
		this.realtime_runner.runTaskTimerAsynchronously(this, 5, 2);
	}

	public boolean save_structure() {
		this.getLogger().info("正在保存结构数据");
		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : this.structure_manager_map
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
			manager.save_structures();
		}
		try {
			shop_config.save(this.shop_file);
		} catch (IOException e) {
			this.getLogger().severe("结构文件保存错误!");
			return false;
		}
		return true;
	}

	public void remove_recipe() {
		for (ShapedRecipe sr : this.sr) {
			Iterator<Recipe> i = Bukkit.recipeIterator();
			while (i.hasNext()) {
				if (i.next().equals(sr)) {
					i.remove();
				}
			}
		}
	}

	@Override
	public void onDisable() {
		this.save_structure();

		try {
			lottery_config.save(this.lottery_file);
		} catch (IOException e) {
			this.getLogger().severe("抽奖配置文件保存错误!");
		}
		this.remove_recipe();
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

	public HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> get_structure_manager() {
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

	public int get_exp_saver_max_structure_level() {
		return this.exp_saver_max_structure_level;
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

	/*
	 * 给某玩家扣钱，返回true表示扣钱成功，返回false表示扣钱失败。
	 */
	public synchronized boolean cost_player_money(int money, OfflinePlayer player) {
		if (this.economy.has(player, money)) {
			this.economy.withdrawPlayer(player, money);
			return true;
		} else {
			return false;
		}
	}

	public void lottery_config_load(File lottery_file) {
		this.lottery_config = YamlConfiguration.loadConfiguration(lottery_file);
	}

	public void lottery_config_load() {
		this.lottery_config_load(this.lottery_file);
	}

	public Price_config get_price_config() {
		return this.price_config;
	}

	public int get_exp_saver_anvil_upgrade_need() {
		return this.exp_saver_anvil_upgrade_need;
	}

	public int get_exp_saver_remove_repaircost_exp() {
		return this.exp_saver_remove_repaircost_exp;
	}

	public NamespacedKey get_key(String key) {
		return new NamespacedKey(this, key);
	}

	public NMS_manager get_nms_manager() {
		return this.nms_manager;
	}

	public Radio_manager get_radio_manager() {
		return this.radio_manager;
	}
}
