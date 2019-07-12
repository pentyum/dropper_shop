package com.piggest.minecraft.bukkit.config;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Price_config {
	private Dropper_shop_plugin plugin;

	private int make_price = 0;
	private int make_grinder_price = 0;
	private int make_lottery_pool_price = 0;
	private int lottery_price = 0;
	private int exp_saver_upgrade_level_price = 0;
	private int exp_saver_upgrade_base_price = 0;
	private int make_trees_felling_machine_price = 0;
	private int start_trees_felling_machine_price = 0;
	private int anti_thunder_price = 0;
	
	public Price_config(Dropper_shop_plugin dropper_shop_plugin) {
		this.plugin = dropper_shop_plugin;
	}

	public void load_price() {
		this.make_price = this.plugin.get_config().getInt("make-price");
		this.make_grinder_price = this.plugin.get_config().getInt("make-grinder-price");
		this.make_lottery_pool_price = this.plugin.get_config().getInt("make-lottery-pool-price");
		this.make_trees_felling_machine_price = this.plugin.get_config().getInt("make-trees-felling-machine-price");
		this.exp_saver_upgrade_level_price  = this.plugin.get_config().getInt("exp-saver-upgrade-level-price");
		this.exp_saver_upgrade_base_price = this.plugin.get_config().getInt("exp-saver-upgrade-base-price");
		this.lottery_price = this.plugin.get_config().getInt("lottery-price");
		this.start_trees_felling_machine_price = this.plugin.get_config().getInt("start-trees-felling-machine-price");
		this.anti_thunder_price = this.plugin.get_config().getInt("anti-thunder-price");
	}

	public int get_make_shop_price() {
		return this.make_price;
	}

	public int get_make_grinder_price() {
		return this.make_grinder_price;
	}

	public void set_make_shop_price(int price) {
		this.make_price = price;
		this.plugin.get_config().set("make-price", price);
	}

	public int get_make_lottery_pool_price() {
		return this.make_lottery_pool_price;
	}

	public int get_lottery_price() {
		return this.lottery_price;
	}

	public int get_make_trees_felling_machine_price() {
		return this.make_trees_felling_machine_price;
	}
	
	public int get_exp_saver_upgrade_level_price() {
		return this.exp_saver_upgrade_level_price;
	}
	
	public int get_exp_saver_upgrade_base_price() {
		return this.exp_saver_upgrade_base_price;
	}
	
	public int get_anti_thunder_price() {
		return this.anti_thunder_price;
	}
	
	public int get_start_trees_felling_machine_price() {
		return this.start_trees_felling_machine_price;
	}
	
	public void set_lottery_price(int newprice) {
		this.lottery_price = newprice;
		this.plugin.get_config().set("lottery-price", newprice);
		this.plugin.saveConfig();
	}
	
	public void set_start_trees_felling_machine_price(int newprice) {
		this.start_trees_felling_machine_price  = newprice;
		this.plugin.get_config().set("start-trees-felling-machine-price", newprice);
	}
	
	public void reload_price() {
		this.plugin.reloadConfig();
		this.load_price();
	}
}
