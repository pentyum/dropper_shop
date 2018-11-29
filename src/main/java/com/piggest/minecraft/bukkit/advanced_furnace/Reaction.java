package com.piggest.minecraft.bukkit.advanced_furnace;

public class Reaction {
	private Chemical[] reactants_chemicals;
	private Chemical[] products_chemicals;
	private int[] reactants_coefficients;
	private int[] products_coefficients;
	private boolean reversible;
	private double positive_rate;
	private double negative_rate;

	public Reaction(boolean reversible, double positive_rate, double negative_rate) {
		this.reversible = reversible;
		this.positive_rate = positive_rate;
		this.negative_rate = negative_rate;
	}

	public void set_reactants(Chemical... chemicals) {
		this.reactants_chemicals = chemicals;
	}

	public void set_products(Chemical... chemicals) {
		this.products_chemicals = chemicals;
	}
	
	public void set_reactants_coef(int... coefs) {
		this.reactants_coefficients = coef;
	}

	public void set_products_coef(int... coefs) {
		this.products_coefficients = coef;
	}
	
	public Chemical[] get_reactants() {
		return this.reactants_chemicals;
	}

	public Chemical[] get_products() {
		return this.products_chemicals;
	}

	public double get_current_positive_rate(double[] c, double temp) {
		if (c.length != reactants_chemicals.length) {
			return 0;
		}
		double k = this.positive_rate * Math.exp(-1 / temp);
		for (int i = 0; i < this.reactants_chemicals.length; i++) {
			if (this.reactants_chemicals[i] instanceof Gas) {
				k *= Math.pow(c[i], this.reactants_coefficients[i]);
			}
			i++;
		}
		return k;
	}

	public double get_current_negative_rate(double[] c, double temp) {
		if (this.reversible == false) {
			return 0;
		} else {
			if (c.length != products_chemicals.length) {
				return 0;
			}
			double k = this.negative_rate * Math.exp(-1 / temp);
			for (int i = 0; i < this.products_chemicals.length; i++) {
				if (this.products_chemicals[i] instanceof Gas) {
					k *= Math.pow(c[i], this.products_coefficients[i]);
				}
				i++;
			}
			return k;
		}
	}
}