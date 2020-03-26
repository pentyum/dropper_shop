package com.piggest.minecraft.bukkit.dropper_shop;

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
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
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
import com.piggest.minecraft.bukkit.biome_modify.Biome_modify;
import com.piggest.minecraft.bukkit.compressor.Compressor;
import com.piggest.minecraft.bukkit.compressor.Compressor_manager;
import com.piggest.minecraft.bukkit.config.Config_auto_saver;
import com.piggest.minecraft.bukkit.config.Lottery_config;
import com.piggest.minecraft.bukkit.config.Price_config;
import com.piggest.minecraft.bukkit.depository.Depository;
import com.piggest.minecraft.bukkit.depository.Depository_command_executor;
import com.piggest.minecraft.bukkit.depository.Depository_listener;
import com.piggest.minecraft.bukkit.depository.Depository_manager;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.depository.Upgrade_component_listener;
import com.piggest.minecraft.bukkit.electric_spawner.Electric_spawner;
import com.piggest.minecraft.bukkit.electric_spawner.Electric_spawner_command_executor;
import com.piggest.minecraft.bukkit.electric_spawner.Electric_spawner_manager;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_command_executor;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_listener;
import com.piggest.minecraft.bukkit.exp_saver.Exp_saver_manager;
import com.piggest.minecraft.bukkit.flying_item.Flying_item;
import com.piggest.minecraft.bukkit.flying_item.Flying_item_listener;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.grinder.Grinder_command_executor;
import com.piggest.minecraft.bukkit.grinder.Grinder_listener;
import com.piggest.minecraft.bukkit.grinder.Grinder_manager;
import com.piggest.minecraft.bukkit.grinder.Ingot;
import com.piggest.minecraft.bukkit.grinder.Powder;
import com.piggest.minecraft.bukkit.gui.Gui_listener;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_command_executor;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_gui_listener;
import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_manager;
import com.piggest.minecraft.bukkit.material_ext.Custom_durability;
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
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_command_executer;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;
import com.piggest.minecraft.bukkit.tools.Tool_craft_listener;
import com.piggest.minecraft.bukkit.tools.Tools;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine;
import com.piggest.minecraft.bukkit.trees_felling_machine.Trees_felling_machine_manager;
import com.piggest.minecraft.bukkit.utils.Tab_list;
import com.piggest.minecraft.bukkit.utils.language.Enchantments_zh_cn;
import com.piggest.minecraft.bukkit.utils.language.Entity_zh_cn;
import com.piggest.minecraft.bukkit.utils.language.Item_zh_cn;
import com.piggest.minecraft.bukkit.watersheep.Watersheep_command_executor;
import com.piggest.minecraft.bukkit.watersheep.Watersheep_runner;
import com.piggest.minecraft.bukkit.wrench.Wrench_command_executor;

import net.milkbowl.vault.economy.Economy;

public class Dropper_shop_plugin extends JavaPlugin {
	public final static int custom_model_data_offset = 15000;
	public static Dropper_shop_plugin instance = null;
	private Economy economy = null;
	private FileConfiguration config = null;
	private Lottery_config lottery_config = null;

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
	private Pigman_switch_manager pigman_switch_manager = null;
	private Anti_thunder_manager anti_thunder_manager = null;
	private Teleport_machine_manager teleport_machine_manager = null;
	private Compressor_manager compressor_manager = null;
	private Electric_spawner_manager electric_spawner_manager = null;

	private HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> structure_manager_map = new HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>>();

	private HashMap<String, Integer> price_map = new HashMap<String, Integer>();
	private HashMap<String, Integer> flint_unit_map = new HashMap<String, Integer>();
	private HashMap<String, Integer> piston_unit_map = new HashMap<String, Integer>();
	private HashMap<String, HashMap<Gas, Integer>> air_map = new HashMap<String, HashMap<Gas, Integer>>();
	private ArrayList<ShapedRecipe> sr = new ArrayList<ShapedRecipe>();
	private ArrayList<FurnaceRecipe> fr = new ArrayList<FurnaceRecipe>();

	private final Note_stick_listener note_listener = new Note_stick_listener();
	private final Gui_listener gui_listener = new Gui_listener();
	private final Structure_listener Structure_listener = new Structure_listener();
	private final Flying_item_listener flying_item_listener = new Flying_item_listener();
	private final Lottery_pool_gui_listener lottery_pool_gui_listener = new Lottery_pool_gui_listener();
	private final Custom_durability custom_durability_listener = new Custom_durability();
	private final Tool_craft_listener tool_craft_listener = new Tool_craft_listener();
	// private final Prepare_enchant_listener prepare_enchant_listener = new
	// Prepare_enchant_listener();

	private HashMap<String, Integer> sync_realtime_worlds = new HashMap<String, Integer>();

	private Listener[] structure_listeners = { new Depository_listener(), new Dropper_shop_listener(),
			new Upgrade_component_listener(), new Grinder_listener(), new Advanced_furnace_listener(),
			new Exp_saver_listener(), new Pigman_spawn_listener(), new Anti_thunder_listener(),
			new Elements_listener() };

	private NMS_manager nms_manager = null;
	private Config_auto_saver auto_saver = new Config_auto_saver(this);
	private Sync_realtime realtime_runner = null;
	private Biome_modify biome_modify = null;
	private Radio_manager radio_manager = null;
	private Watersheep_runner watersheep_runner = null;
	private ProtocolManager protocol_manager = null;

	public Dropper_shop_plugin() {
		Dropper_shop_plugin.instance = this;
		this.getLogger().info("加载配置中");
		saveDefaultConfig();

		saveResource("lottery_pool.yml", false);
		this.config = this.getConfig();

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
		this.watersheep_runner = new Watersheep_runner(this.getServer());

		ConfigurationSection price_section = this.config.getConfigurationSection("material");
		Set<String> price_keys = price_section.getKeys(false);
		for (String material_name : price_keys) {
			this.price_map.put(material_name, price_section.getInt(material_name));
		}
		ConfigurationSection flint_unit_section = this.config.getConfigurationSection("flint-unit");
		Set<String> flint_unit_keys = flint_unit_section.getKeys(false);
		for (String material_name : flint_unit_keys) {
			this.flint_unit_map.put(material_name, flint_unit_section.getInt(material_name));
		}
		ConfigurationSection piston_unit_section = this.config.getConfigurationSection("piston-unit");
		Set<String> piston_unit_keys = piston_unit_section.getKeys(false);
		for (String material_name : piston_unit_keys) {
			this.piston_unit_map.put(material_name, piston_unit_section.getInt(material_name));
		}
		this.lottery_config = new Lottery_config();
		this.lottery_config.load();

		this.gen_air();

		Enchantments_zh_cn.init();
		Item_zh_cn.init();
		Entity_zh_cn.init();

		this.nms_manager = new NMS_manager(Bukkit.getBukkitVersion());
	}

	public Lottery_config get_lottery_config() {
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
		this.compressor_manager = new Compressor_manager();
		this.electric_spawner_manager = new Electric_spawner_manager();

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
		this.structure_manager_map.put(Compressor.class, compressor_manager);
		this.structure_manager_map.put(Electric_spawner.class, electric_spawner_manager);

		for (Structure_manager<? extends Structure> manager : this.structure_manager_map.values()) {
			manager.load_config();
			manager.backup_config();
		}
	}

	private void init_listener() {
		this.getLogger().info("加载事件监听器");
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(this.Structure_listener, this);
		pm.registerEvents(this.gui_listener, this);
		pm.registerEvents(this.note_listener, this);
		pm.registerEvents(this.flying_item_listener, this);
		pm.registerEvents(this.lottery_pool_gui_listener, this);
		pm.registerEvents(this.custom_durability_listener, this);
		pm.registerEvents(this.tool_craft_listener, this);
		// pm.registerEvents(this.prepare_enchant_listener, this);
		for (Listener listener : this.structure_listeners) {
			pm.registerEvents(listener, this);
		}
	}

	private void init_command() {
		this.getLogger().info("加载指令");
		this.getCommand("depository").setExecutor(new Depository_command_executor());
		this.getCommand("dropper_shop").setExecutor(new Dropper_shop_command_executor());
		this.getCommand("grinder").setExecutor(new Grinder_command_executor());
		this.getCommand("adv_furnace").setExecutor(new Advanced_furnace_command_executor());
		this.getCommand("exp_saver").setExecutor(new Exp_saver_command_executor());
		this.getCommand("lottery").setExecutor(new Lottery_pool_command_executor());
		this.getCommand("sync_realtime").setExecutor(new Sync_realtime_command_executor(this.sync_realtime_worlds));
		this.getCommand("teleport_machine").setExecutor(new Teleport_machine_command_executer());
		this.getCommand("watersheep").setExecutor(new Watersheep_command_executor());
		this.getCommand("biome_modify").setExecutor(this.biome_modify);
		this.getCommand("electric_spawner").setExecutor(new Electric_spawner_command_executor());
	}

	@Override
	public void onEnable() {
		// 处理冬天模式
		this.biome_modify = new Biome_modify();
		boolean winter_mode_enabled = this.config.getBoolean("winter-mode");
		if (winter_mode_enabled) {
			this.getLogger().info("开启冬天模式");
			this.biome_modify.get_winter_mode().enable(this.biome_modify);
		}
		try {
			this.protocol_manager = ProtocolLibrary.getProtocolManager();
			protocol_manager.addPacketListener(NMS_manager.packet_map_chunk_listener);
			this.getLogger().info("生物群系伪装开启成功");
		} catch (Exception e) {

		}
		// 处理冬天模式完成

		Tab_list.init();

		// 初始化管理器
		this.init_structure_manager();
		this.radio_manager = new Radio_manager();
		// 初始化管理器完成

		// 初始化指令
		this.init_command();
		Wrench_command_executor wrench = new Wrench_command_executor();
		this.getCommand("wrench").setExecutor(wrench);
		// 初始化指令完成

		this.getLogger().info("使用Vault");
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}

		// 初始化插件特有物品
		Powder.init_powder();
		Ingot.init_ingot();
		Tools.init_tools();
		Reader.init_reader_item();
		Reader.set_recipe();
		Upgrade_component.init_component();
		Upgrade_component.set_recipe();
		Tools.init_recipe();
		grinder_manager.init_recipe();
		compressor_manager.init_recipe();
		Gas_bottle.init_gas_bottle();
		Gas_bottle.set_recipe();
		Reaction_container.init_reaction();
		wrench.init();
		Flying_item.init();
		// 初始化特有物品完成

		// 加载结构
		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : this.structure_manager_map
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
			manager.load_structures();
		}
		// 加载结构完成

		// 初始化事件监听器
		this.init_listener();
		// 初始化事件监听器完成

		// 初始化定时任务
		this.note_listener.runner.runTaskTimer(this, 10, 5);
		this.auto_saver.runTaskTimerAsynchronously(this, 10, 6000);
		this.realtime_runner.runTaskTimer(this, 5, 5);
		this.watersheep_runner.runTaskTimerAsynchronously(this, 120, 4800);
		// 初始化定时任务完成
	}

	public boolean save_structure() {
		this.getLogger().info("正在保存结构数据");
		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : this.structure_manager_map
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
			manager.save_structures();
		}
		return true;
	}

	public void remove_recipe() {
		Iterator<Recipe> i = Bukkit.recipeIterator();
		while (i.hasNext()) {
			Recipe recipe = i.next();
			if (this.sr.contains(recipe) || this.fr.contains(recipe)) {
				i.remove();
			}
		}
	}

	@Override
	public void onDisable() {
		this.stop_structure_runner();
		this.save_structure();

		this.lottery_config.save();

		this.remove_recipe();
	}

	private void stop_structure_runner() {
		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : this.structure_manager_map
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
			manager.stop_runner();
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

	public int get_flint_unit(Material material) {
		Integer unit = this.flint_unit_map.get(material.name());
		if (unit == null) {
			return 0;
		} else {
			return unit;
		}
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

	public Price_config get_price_config() {
		return this.price_config;
	}

	public int get_exp_saver_anvil_upgrade_need() {
		return this.exp_saver_anvil_upgrade_need;
	}

	public int get_exp_saver_remove_repaircost_exp() {
		return this.exp_saver_remove_repaircost_exp;
	}

	@SuppressWarnings("deprecation")
	public static NamespacedKey get_key(String key) {
		return new NamespacedKey("dropper_shop", key);
	}

	public NMS_manager get_nms_manager() {
		return this.nms_manager;
	}

	public Radio_manager get_radio_manager() {
		return this.radio_manager;
	}

	public Teleport_machine_manager get_teleport_machine_manager() {
		return this.teleport_machine_manager;
	}

	public Biome_modify get_biome_modify() {
		return this.biome_modify;
	}
	
	public Electric_spawner_manager get_electric_spawner_manager() {
		return this.electric_spawner_manager;
	}
	
	public void add_recipe(Recipe recipe) {
		this.getServer().addRecipe(recipe);
		if (recipe instanceof ShapedRecipe) {
			this.sr.add((ShapedRecipe) recipe);
		} else if (recipe instanceof FurnaceRecipe) {
			this.fr.add((FurnaceRecipe) recipe);
		}
	}

	public int get_piston_unit(Material material) {
		Integer unit = this.piston_unit_map.get(material.name());
		if (unit == null) {
			return 0;
		} else {
			return unit;
		}
	}

}
