package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.HashMap;
import java.util.Map.Entry;

public class Reaction_container {
	public static HashMap<String, Reaction> reactions = new HashMap<String, Reaction>();
	private HashMap<Chemical, Integer> unit_map = new HashMap<Chemical, Integer>();
	private double temperature;

	public static void init_reaction() {
		Reaction ammonia_synthesis = new Reaction(true, 1, 1);
		ammonia_synthesis.set_reactants(Gas.nitrogen, Gas.hydrogen);
		ammonia_synthesis.set_products(Gas.hydrogen);
		ammonia_synthesis.set_reactants_coef(1, 3);
		ammonia_synthesis.set_products_coef(2);
		reactions.put("ammonia_synthesis", ammonia_synthesis);

		Reaction get_iron = new Reaction(false, 1, 1);
		get_iron.set_reactants(Solid.iron_powder);
		get_iron.set_products(Solid.iron_ingot);
		get_iron.set_reactants_coef(1);
		get_iron.set_products_coef(1);
		reactions.put("get_iron", get_iron);
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
		for (Entry<String, Reaction> entry : reactions.entrySet()) {
			Reaction reaction = entry.getValue();
			int i;

			Chemical[] reactants = reaction.get_reactants();
			int[] reactants_coef = reaction.get_reactants_coef();
			double[] reactants_c = new double[reactants.length];
			for (i = 0; i < reactants.length; i++) { // 获取反应物浓度
				reactants_c[i] = this.get_c(reactants[i]);
			}
			int positive_rate = reaction.get_current_positive_rate(reactants_c, temperature); // 计算正反应速率

			Chemical[] products = reaction.get_products();
			int[] products_coef = reaction.get_products_coef();
			double[] products_c = new double[products.length];
			for (i = 0; i < products.length; i++) { // 获取生成物浓度
				products_c[i] = this.get_c(products[i]);
			}
			int negative_rate = reaction.get_current_negative_rate(products_c, temperature); // 计算逆反应速率

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
			
			for (i = 0; i < reactants.length; i++) {
				current_unit = this.get_unit(reactants[i]);
				d_current_unit = -rate * reactants_coef[i];
				int new_unit = current_unit + d_current_unit;
				if (new_unit > 0) {
					this.unit_map.put(reactants[i], current_unit + d_current_unit);
				} else {
					if (current_unit > 0) {
						this.unit_map.remove(reactants[i]);
					}
				}
			}
			for (i = 0; i < products.length; i++) {
				current_unit = this.get_unit(products[i]);
				d_current_unit = rate * products_coef[i];
				int new_unit = current_unit + d_current_unit;
				if (new_unit > 0) {
					this.unit_map.put(reactants[i], current_unit + d_current_unit);
				} else {
					if (current_unit > 0) {
						this.unit_map.remove(reactants[i]);
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
}
