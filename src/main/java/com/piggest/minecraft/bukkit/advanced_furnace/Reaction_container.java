package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;

public class Reaction_container {
	public static HashMap<String, Reaction> reactions = new HashMap<String, Reaction>();
	private HashMap<Chemical, Integer> unit_map = new HashMap<Chemical, Integer>();
	private double temperature;
	private HashMap<Chemical, Integer> rate_map = new HashMap<Chemical, Integer>();

	public static void init_reaction() {
		Reaction ammonia_synthesis = new Reaction(true, 100, 1200, 130, 1400);
		ammonia_synthesis.set_reactants(Gas.nitrogen, Gas.hydrogen);
		ammonia_synthesis.set_products(Gas.NH3);
		ammonia_synthesis.set_reactants_coef(1, 3);
		ammonia_synthesis.set_products_coef(2);
		reactions.put("ammonia_synthesis", ammonia_synthesis);

		Reaction get_hydrogen = new Reaction(false, 100, 2100, 100, 800);
		get_hydrogen.set_reactants(Solid.COAL, Gas.water);
		get_hydrogen.set_products(Gas.hydrogen, Gas.CO);
		get_hydrogen.set_reactants_coef(1, 1);
		get_hydrogen.set_products_coef(1, 1);
		reactions.put("get_hydrogen", get_hydrogen);
		
		Reaction get_water_vapor = new Reaction(true, 93973313, 6130, 100, 1000);
		get_water_vapor.set_reactants(Solid.WATER);
		get_water_vapor.set_products(Gas.water);
		get_water_vapor.set_reactants_coef(1);
		get_water_vapor.set_products_coef(1);
		reactions.put("get_water_vapor", get_water_vapor);
		
		Reaction burn_coal = new Reaction(false, 500, 1600, 100, 800);
		burn_coal.set_reactants(Solid.COAL, Gas.oxygen);
		burn_coal.set_products(Gas.CO2);
		burn_coal.set_reactants_coef(1, 1);
		burn_coal.set_products_coef(1);
		reactions.put("burn_coal", burn_coal);

		Reaction burn_stone = new Reaction(false, 150, 2300, 100, 800);
		burn_stone.set_reactants(Solid.GRANITE);
		burn_stone.set_products(Gas.CO2, Solid.STONE);
		burn_stone.set_reactants_coef(2);
		burn_stone.set_products_coef(1, 1);
		reactions.put("burn_stone", burn_stone);

		Reaction get_iron = new Reaction(false, 150, 1800, 1, 1000);
		get_iron.set_reactants(Solid.iron_powder);
		get_iron.set_products(Solid.IRON_INGOT);
		get_iron.set_reactants_coef(1);
		get_iron.set_products_coef(1);
		reactions.put("get_iron", get_iron);

		Reaction get_gold = new Reaction(false, 120, 1400, 1, 1000);
		get_gold.set_reactants(Solid.gold_powder);
		get_gold.set_products(Solid.GOLD_INGOT);
		get_gold.set_reactants_coef(1);
		get_gold.set_products_coef(1);
		reactions.put("get_gold", get_gold);

		Reaction get_lapis = new Reaction(false, 120, 1500, 1, 1000);
		get_lapis.set_reactants(Solid.lapis_powder);
		get_lapis.set_products(Solid.LAPIS_LAZULI);
		get_lapis.set_reactants_coef(1);
		get_lapis.set_products_coef(1);
		reactions.put("get_lapis", get_lapis);

		Reaction get_glass = new Reaction(false, 200, 1900, 1, 1000);
		get_glass.set_reactants(Solid.SAND);
		get_glass.set_products(Solid.GLASS);
		get_glass.set_reactants_coef(1);
		get_glass.set_products_coef(1);
		reactions.put("get_glass", get_glass);

		Reaction get_stone = new Reaction(false, 120, 1400, 1, 1000);
		get_stone.set_reactants(Solid.COBBLESTONE);
		get_stone.set_products(Solid.STONE);
		get_stone.set_reactants_coef(1);
		get_stone.set_products_coef(1);
		reactions.put("get_stone", get_stone);

		Reaction get_brick = new Reaction(false, 150, 1700, 1, 1000);
		get_brick.set_reactants(Solid.CLAY_BALL);
		get_brick.set_products(Solid.BRICK);
		get_brick.set_reactants_coef(1);
		get_brick.set_products_coef(1);
		reactions.put("get_brick", get_brick);

		Reaction get_terracotta = new Reaction(false, 150, 1500, 1, 1000);
		get_terracotta.set_reactants(Solid.CLAY);
		get_terracotta.set_products(Solid.TERRACOTTA);
		get_terracotta.set_reactants_coef(1);
		get_terracotta.set_products_coef(1);
		reactions.put("get_terracotta", get_terracotta);

		Reaction get_nether_brick = new Reaction(false, 180, 2000, 1, 1000);
		get_nether_brick.set_reactants(Solid.NETHERRACK);
		get_nether_brick.set_products(Solid.NETHER_BRICK);
		get_nether_brick.set_reactants_coef(1);
		get_nether_brick.set_products_coef(1);
		reactions.put("get_nether_brick", get_nether_brick);

		Reaction get_charcoal = new Reaction(false, 80, 1300, 1, 1000);
		get_charcoal.set_reactants(Solid.LOG);
		get_charcoal.set_products(Solid.CHARCOAL);
		get_charcoal.set_reactants_coef(1);
		get_charcoal.set_products_coef(1);
		reactions.put("get_charcoal", get_charcoal);

		Reaction get_flint = new Reaction(false, 130, 1600, 1, 1000);
		get_flint.set_reactants(Solid.GRAVEL);
		get_flint.set_products(Solid.FLINT);
		get_flint.set_reactants_coef(2);
		get_flint.set_products_coef(1);
		reactions.put("get_flint", get_flint);

		Reaction get_obsidian = new Reaction(false, 130, 2500, 1, 1000);
		get_obsidian.set_reactants(Solid.STONE, Solid.QUARTZ);
		get_obsidian.set_products(Solid.OBSIDIAN);
		get_obsidian.set_reactants_coef(1, 4);
		get_obsidian.set_products_coef(1);
		reactions.put("get_obsidian", get_obsidian);

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
					this.rate_map.put(products[i], d_current_unit);
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
