package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.HashMap;
import java.util.Map.Entry;

public class Reaction_container {
	public static HashMap<String, Reaction> reactions = new HashMap<String, Reaction>();
	private HashMap<Chemical, Integer> unit_map = new HashMap<Chemical, Integer>();
	private double temperature;
	private HashMap<Chemical, Integer> rate_map = new HashMap<Chemical, Integer>();

	public static void init_reaction() {
		double ntop = 1;
		double pton = 1;

		Reaction ammonia_synthesis = new Reaction(true, 100, 1200, 130, 1400);
		ammonia_synthesis.set_reactants(Gas.nitrogen, Gas.hydrogen);
		ammonia_synthesis.set_products(Gas.NH3);
		ammonia_synthesis.set_reactants_coef(1, 3);
		ammonia_synthesis.set_products_coef(2);
		reactions.put("ammonia_synthesis", ammonia_synthesis);

		pton = 1e-16 * Math.exp(131500 / 8.314 / 293);
		Reaction get_hydrogen = new Reaction(true, 80*pton, 221500, 80, 90000);
		get_hydrogen.set_reactants(Solid.coal, Gas.water_vapor);
		get_hydrogen.set_products(Gas.hydrogen, Gas.CO);
		get_hydrogen.set_reactants_coef(1, 1);
		get_hydrogen.set_products_coef(1, 1);
		reactions.put("get_hydrogen", get_hydrogen);

		ntop = Math.exp(-44000 / 8.314 / 373);
		Reaction get_water_vapor = new Reaction(true, 8000000, 44000, ntop * 8000000, 0);
		get_water_vapor.set_reactants(Liquid.water);
		get_water_vapor.set_products(Gas.water_vapor);
		get_water_vapor.set_reactants_coef(1);
		get_water_vapor.set_products_coef(1);
		reactions.put("get_water_vapor", get_water_vapor);

		ntop = Math.exp(-6000 / 8.314 / 273);
		Reaction get_water = new Reaction(true, 1000, 6000, ntop * 1000, 0);
		get_water.set_reactants(Solid.ice);
		get_water.set_products(Liquid.water);
		get_water.set_reactants_coef(1);
		get_water.set_products_coef(1);
		reactions.put("get_water", get_water);

		Reaction burn_coal = new Reaction(false, 100000, 80000, 100, 800);
		burn_coal.set_reactants(Solid.coal, Gas.oxygen);
		burn_coal.set_products(Gas.CO2);
		burn_coal.set_reactants_coef(1, 1);
		burn_coal.set_products_coef(1);
		reactions.put("burn_coal", burn_coal);

		Reaction burn_stone = new Reaction(false, 100000, 80000, 100, 800);
		burn_stone.set_reactants(Solid.granite);
		burn_stone.set_products(Gas.CO2, Solid.stone);
		burn_stone.set_reactants_coef(2);
		burn_stone.set_products_coef(1, 1);
		reactions.put("burn_stone", burn_stone);

		Reaction get_iron = new Reaction(false, 100000, 80000, 1, 1000);
		get_iron.set_reactants(Solid.iron_powder);
		get_iron.set_products(Solid.iron_ingot);
		get_iron.set_reactants_coef(1);
		get_iron.set_products_coef(1);
		reactions.put("get_iron", get_iron);

		Reaction get_gold = new Reaction(false, 100000, 80000, 1, 1000);
		get_gold.set_reactants(Solid.gold_powder);
		get_gold.set_products(Solid.gold_ingot);
		get_gold.set_reactants_coef(1);
		get_gold.set_products_coef(1);
		reactions.put("get_gold", get_gold);
		
		Reaction get_silver = new Reaction(false, 100000, 80000, 1, 1000);
		get_silver.set_reactants(Solid.silver_powder);
		get_silver.set_products(Solid.silver_ingot);
		get_silver.set_reactants_coef(1);
		get_silver.set_products_coef(1);
		reactions.put("get_silver", get_silver);
		
		Reaction get_copper = new Reaction(false, 100000, 80000, 1, 1000);
		get_copper.set_reactants(Solid.copper_powder);
		get_copper.set_products(Solid.copper_ingot);
		get_copper.set_reactants_coef(1);
		get_copper.set_products_coef(1);
		reactions.put("get_copper", get_copper);
		
		Reaction get_lapis = new Reaction(false, 100000, 80000, 1, 1000);
		get_lapis.set_reactants(Solid.lapis_powder);
		get_lapis.set_products(Solid.lapis_lazuli);
		get_lapis.set_reactants_coef(1);
		get_lapis.set_products_coef(1);
		reactions.put("get_lapis", get_lapis);

		Reaction get_glass = new Reaction(false, 120000, 60000, 1, 1000);
		get_glass.set_reactants(Solid.sand);
		get_glass.set_products(Solid.glass);
		get_glass.set_reactants_coef(1);
		get_glass.set_products_coef(1);
		reactions.put("get_glass", get_glass);

		Reaction get_stone = new Reaction(false, 30000, 40000, 1, 1000);
		get_stone.set_reactants(Solid.cobblestone);
		get_stone.set_products(Solid.stone);
		get_stone.set_reactants_coef(1);
		get_stone.set_products_coef(1);
		reactions.put("get_stone", get_stone);

		Reaction get_brick = new Reaction(false, 400000, 100000, 1, 1000);
		get_brick.set_reactants(Solid.clay_ball);
		get_brick.set_products(Solid.brick);
		get_brick.set_reactants_coef(1);
		get_brick.set_products_coef(1);
		reactions.put("get_brick", get_brick);

		Reaction get_terracotta = new Reaction(false, 400000, 100000, 1, 1000);
		get_terracotta.set_reactants(Solid.clay);
		get_terracotta.set_products(Solid.terracotta);
		get_terracotta.set_reactants_coef(1);
		get_terracotta.set_products_coef(1);
		reactions.put("get_terracotta", get_terracotta);

		Reaction get_nether_brick = new Reaction(false, 400000, 100000, 1, 1000);
		get_nether_brick.set_reactants(Solid.netherrack);
		get_nether_brick.set_products(Solid.nether_brick);
		get_nether_brick.set_reactants_coef(1);
		get_nether_brick.set_products_coef(1);
		reactions.put("get_nether_brick", get_nether_brick);

		Reaction get_charcoal = new Reaction(false, 420000, 100000, 1, 1000);
		get_charcoal.set_reactants(Solid.log);
		get_charcoal.set_products(Solid.charcoal);
		get_charcoal.set_reactants_coef(1);
		get_charcoal.set_products_coef(1);
		reactions.put("get_charcoal", get_charcoal);

		Reaction get_flint = new Reaction(false, 400000, 100000, 1, 1000);
		get_flint.set_reactants(Solid.gravel);
		get_flint.set_products(Solid.flint);
		get_flint.set_reactants_coef(2);
		get_flint.set_products_coef(1);
		reactions.put("get_flint", get_flint);

		Reaction get_obsidian = new Reaction(false, 400000, 100000, 1, 1000);
		get_obsidian.set_reactants(Solid.stone, Solid.quartz);
		get_obsidian.set_products(Solid.obsidian);
		get_obsidian.set_reactants_coef(1, 4);
		get_obsidian.set_products_coef(1);
		reactions.put("get_obsidian", get_obsidian);

		Reaction get_smooth_quartz = new Reaction(false, 400000, 100000, 1, 1000);
		get_smooth_quartz.set_reactants(Solid.quartz_block);
		get_smooth_quartz.set_products(Solid.smooth_quartz);
		get_smooth_quartz.set_reactants_coef(1);
		get_smooth_quartz.set_products_coef(1);
		reactions.put("get_smooth_quartz", get_smooth_quartz);

		Reaction get_smooth_stone = new Reaction(false, 400000, 120000, 1, 1000);
		get_smooth_stone.set_reactants(Solid.stone);
		get_smooth_stone.set_products(Solid.smooth_stone);
		get_smooth_stone.set_reactants_coef(1);
		get_smooth_stone.set_products_coef(1);
		reactions.put("get_smooth_stone", get_smooth_stone);
	}

	public int get_unit(Chemical chemical) {
		Integer unit = this.unit_map.get(chemical);
		if (unit == null) { // 没有物质，浓度为0
			return 0;
		}
		return unit;
	}

	public double get_c(Chemical chemical) {
		if (chemical instanceof Gas) { // 气体单位1000为一个标准大气压
			return (double) this.get_unit(chemical) / 1000;
		} else { // 其余物质浓度为1
			return 1;
		}
	}

	public void run_all_reactions() {
		this.rate_map = new HashMap<Chemical, Integer>();
		for (Entry<String, Reaction> entry : reactions.entrySet()) {
			Reaction reaction = entry.getValue();
			int i;

			Chemical[] reactants = reaction.get_reactants();
			int[] reactants_coef = reaction.get_reactants_coef();
			double[] reactants_c = new double[reactants.length];
			for (i = 0; i < reactants.length; i++) { // 获取反应物浓度
				// Bukkit.getLogger().info(reactants[i].get_displayname() + "浓度=" +
				// this.get_c(reactants[i]));
				reactants_c[i] = this.get_c(reactants[i]);
			}
			int positive_rate = reaction.get_current_positive_rate(reactants_c, temperature); // 计算正反应速率
			// Bukkit.getLogger().info(entry.getKey() + "正反应速率=" + positive_rate);

			Chemical[] products = reaction.get_products();
			int[] products_coef = reaction.get_products_coef();
			double[] products_c = new double[products.length];
			for (i = 0; i < products.length; i++) { // 获取生成物浓度
				products_c[i] = this.get_c(products[i]);
			}
			int negative_rate = reaction.get_current_negative_rate(products_c, temperature); // 计算逆反应速率
			// Bukkit.getLogger().info(entry.getKey() + "逆反应速率=" + negative_rate);

			int rate = positive_rate - negative_rate; // 实际反应速率
			int current_unit = 0;
			int d_current_unit = 0;

			for (i = 0; i < reactants.length; i++) {
				current_unit = this.get_unit(reactants[i]);
				d_current_unit = -rate * reactants_coef[i];
				if (current_unit + d_current_unit < 0) { // 反应物耗尽，重新定义反应速率
					rate = current_unit / reactants_coef[i];
				}
			}
			for (i = 0; i < products.length; i++) {
				current_unit = this.get_unit(products[i]);
				d_current_unit = rate * products_coef[i];
				if (current_unit + d_current_unit < 0) { // 生成物耗尽，重新定义反应速率
					rate = -current_unit / products_coef[i];
				}
			}

			// Bukkit.getLogger().info(entry.getKey() + "实际反应速率=" + rate);

			for (i = 0; i < reactants.length; i++) { // 计算反应物变化量
				current_unit = this.get_unit(reactants[i]);
				d_current_unit = -rate * reactants_coef[i];
				int new_unit = current_unit + d_current_unit;
				if (new_unit > 0) {
					int old_rate = this.get_rate(reactants[i]);
					this.rate_map.put(reactants[i], old_rate + d_current_unit);
					this.unit_map.put(reactants[i], current_unit + d_current_unit);
				} else {
					if (current_unit > 0) {
						this.rate_map.remove(reactants[i]);
						this.unit_map.remove(reactants[i]);
					}
				}
			}
			for (i = 0; i < products.length; i++) { // 计算生成物变化量
				current_unit = this.get_unit(products[i]);
				d_current_unit = rate * products_coef[i];
				int new_unit = current_unit + d_current_unit;
				if (new_unit > 0) {
					int old_rate = this.get_rate(products[i]);
					this.rate_map.put(products[i], old_rate + d_current_unit);
					this.unit_map.put(products[i], current_unit + d_current_unit);
				} else {
					if (current_unit > 0) {
						this.rate_map.remove(products[i]);
						this.unit_map.remove(products[i]);
					}
				}
			}
		}
	}

	public void set_temperature(double temperature) {
		this.temperature = temperature;
	}

	public double get_temperature() {
		return this.temperature;
	}

	public void clear_gas() {
		for (Chemical chemical : this.unit_map.keySet()) {
			if (chemical instanceof Gas) {
				this.unit_map.remove(chemical);
			}
		}
	}

	public void clear_solid() {
		for (Chemical chemical : this.unit_map.keySet()) {
			if (chemical instanceof Solid) {
				this.unit_map.remove(chemical);
			}
		}
	}

	public HashMap<Solid, Integer> get_all_solid() {
		HashMap<Solid, Integer> solid_map = new HashMap<Solid, Integer>();
		for (Entry<Chemical, Integer> entry : this.unit_map.entrySet()) {
			Chemical chemical = entry.getKey();
			int unit = entry.getValue();
			if (chemical instanceof Solid) {
				solid_map.put((Solid) chemical, unit);
			}
		}
		return solid_map;
	}

	public HashMap<Chemical, Integer> get_all_chemical() {
		return this.unit_map;
	}

	public int get_rate(Chemical chemical) {
		Integer rate = this.rate_map.get(chemical);
		if (rate != null) {
			return this.rate_map.get(chemical);
		} else {
			return 0;
		}
	}

	public void set_unit(Chemical chemical, int unit) {
		if (unit <= 0) {
			this.unit_map.remove(chemical);
		} else {
			this.unit_map.put(chemical, unit);
		}
	}
}
